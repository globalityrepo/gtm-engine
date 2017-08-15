package br.com.globality.gtm.engine.web.api.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcao;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcaoTodo;
import br.com.globality.gtm.engine.common.exception.ValidationException;
import br.com.globality.gtm.engine.common.util.CommonUtils;
import br.com.globality.gtm.engine.web.api.dao.GenericDAO;
import br.com.globality.gtm.engine.web.api.dao.ResubDAO;

public class ResubServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1872659091672249989L;
	
	@Inject
	ResubDAO resubDAO;
	
	@Inject 
	GenericDAO genericDAO;
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
              throws ServletException, IOException {
		
		MQQueueManager queueManager = null;
		MQQueue queue = null;	
		String queueName = "";
		
		try {
			// Recupera e valida os parâmetros informados na url.
			String stepIdStr = req.getParameter("stepId");
			String typeEvtCode = req.getParameter("evtType");
			String evtInstId = req.getParameter("evtInstId");
			String stepInstId = req.getParameter("stepInstId");
			String trxInstId = req.getParameter("trxInstId");
			
			if (StringUtils.isBlank(stepIdStr)) 
				throw new ValidationException("O atributo stepId não foi informado.");
			else if (StringUtils.isBlank(typeEvtCode)) 
				throw new ValidationException("O atributo evtType não foi informado.");
			else if (StringUtils.isBlank(evtInstId)) 
				throw new ValidationException("O atributo evtInstId não foi informado.");
			else if (StringUtils.isBlank(stepInstId)) 
				throw new ValidationException("O atributo stepInstId não foi informado.");
			else if (StringUtils.isBlank(trxInstId)) 
				throw new ValidationException("O atributo trxInstId não foi informado.");
			
			Long stepId = 0l;
			try {
				stepId = Long.valueOf(req.getParameter("stepId"));
			}
			catch (NumberFormatException e) {
				throw new ValidationException("O atributo stepId deve receber valor numérico.");
			}
			
			// Verificando se resub já foi executado anteriormente.
			TransacaoPassoAcaoTodo voAcaoTodo = resubDAO.findTransacaoPassoAcaoTodo(stepId, typeEvtCode, evtInstId);
			
			if (voAcaoTodo==null) 
				throw new ValidationException("Esta requisição não pode ser processada.");
			
			// Recupera a acao matriz configurada.
			TransacaoPassoAcao voAcao = resubDAO.findTransacaoPassoAcao(stepId, typeEvtCode);
			
			if (voAcao==null)
				throw new ValidationException("Configuração de ação não localizada.");
			else if (StringUtils.isBlank(voAcao.getFilaDestino()))
				throw new ValidationException("Fila de destino não informada.");
			
			// Publica o conteúdo da instância do evento na fila configurada para retry.
			queueManager = CommonUtils.initQueueManager();
			CommonUtils.writeQueueMQ(queueManager, queueName, voAcaoTodo.getEventoInstancia().getConteudo());
			
			// Atualiza o registro da acao para EXECUTADA.O
			voAcaoTodo.setStatus("X");
			genericDAO.update(voAcaoTodo);
			
			RequestDispatcher requestDispatcher; 
			requestDispatcher = req.getRequestDispatcher("/resub-success.jsp");
			requestDispatcher.forward(req, resp);
		}
		catch (ValidationException e) {
			throw new ServletException(e.getMessage());
		}
		catch (MQException e) {
			throw new ServletException("Erro ao acessar a fila MQ: " + queueName);
		}
		catch (Exception e) {
			throw new ServletException("Ocorreu um erro inesperado. Entre em contato com o administrador do sistema.");
		}
		finally {
			try {
				// Fechando objetos auxiliares.
				if (queue!=null)
					queue.close();
				if (queueManager!=null)
					queueManager.disconnect();
			}
			catch (Exception e) {
				
			}
		}
		
    }
		
}

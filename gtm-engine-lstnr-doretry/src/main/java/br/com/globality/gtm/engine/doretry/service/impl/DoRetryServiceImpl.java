package br.com.globality.gtm.engine.doretry.service.impl;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

import br.com.globality.gtm.engine.common.domain.ConfiguracaoSistema;
import br.com.globality.gtm.engine.common.domain.EventoInstanciaConteudo;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcao;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcaoTodo;
import br.com.globality.gtm.engine.common.exception.BusinessException;
import br.com.globality.gtm.engine.common.exception.ServiceException;
import br.com.globality.gtm.engine.common.exception.ValidationException;
import br.com.globality.gtm.engine.common.util.CommonConstants;
import br.com.globality.gtm.engine.common.util.CommonUtils;
import br.com.globality.gtm.engine.common.util.MessageResource;
import br.com.globality.gtm.engine.doretry.dao.DoRetryDAO;
import br.com.globality.gtm.engine.doretry.dao.GenericDAO;
import br.com.globality.gtm.engine.doretry.service.DoRetryService;

/**
 * Classe de regras de negócio da tarefa de retry do engine
 * do GTM.
 * 
 * @author Leonardo Andrade
 * @version 1.0
 * @since 25/04/2017O
 */
@Service("doRetryService")
@Scope("prototype")
@Transactional
public class DoRetryServiceImpl implements DoRetryService {

	private static final Log logger = LogFactory.getLog(DoRetryServiceImpl.class);

	@Autowired
	private MessageResource messageResource;

	@Autowired
	private DoRetryDAO doRetryDAO;
	
	@Autowired
	private GenericDAO genericDAO;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {ServiceException.class, BusinessException.class})
	public void execute(TransacaoPassoAcaoTodo transacaoPassoAcaoTodo) throws ServiceException, BusinessException {		
		
		MQQueueManager queueManager = null;
		MQQueue queue = null;		
		try {				
			// Atualiza o registro da acao para EM EXECUÇÃO.
			transacaoPassoAcaoTodo.setStatus(CommonConstants.STATUS_ACAO_EXECUTANDO);
			genericDAO.update(transacaoPassoAcaoTodo);
						
			// Recupera o conteúdo da instância do evento cadastrado.
			EventoInstanciaConteudo evtInstanciaConteudo = doRetryDAO.findEventoInstanciaConteudo(
					transacaoPassoAcaoTodo.getEventoInstancia().getTransacaoPassoInstancia().getTransacaoInstancia().getId(), 
					transacaoPassoAcaoTodo.getEventoInstancia().getTransacaoPassoInstancia().getId());
			
			// Recupera a configuração da acao associada.
			TransacaoPassoAcao transacaoPassoAcao = doRetryDAO.findTransacaoPassoAcaoById(
					transacaoPassoAcaoTodo.getEventoInstancia().getTransacaoPassoInstancia().getTransacaoPasso().getId(), 
					transacaoPassoAcaoTodo.getEventoInstancia().getEventoTipo().getId());
			
			// Verifica se o nome da fila foi informado.
			if (StringUtils.isBlank(transacaoPassoAcao.getFilaDestino()))
				throw new ValidationException(messageResource.getMessage("message.queue.name.empty.error", null, Locale.getDefault()));
			
			// Publica o conteúdo da instância do evento na fila configurada para retry.
			queueManager = CommonUtils.initQueueManager();
			CommonUtils.writeQueueMQ(queueManager, transacaoPassoAcao.getFilaDestino(), evtInstanciaConteudo.getConteudo());
		}
		catch (ValidationException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage(), e);
		} 
		catch (Exception e1) {
			try {
				// Rollback no status da ação.
				transacaoPassoAcaoTodo.setStatus(CommonConstants.STATUS_ACAO_PARADA);
				genericDAO.update(transacaoPassoAcaoTodo);
			}
			catch (Exception e2) {
				logger.error(e2.getMessage(), e2);
				throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e2);
			}
			logger.error(e1.getMessage(), e1);
			throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e1);
		}
		finally {
			try {
				// Fechando objetos de conexão ao MQ server.
				if (queue!=null)
					queue.close();
				if (queueManager!=null)
					queueManager.disconnect();
			}
			catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<TransacaoPassoAcaoTodo> findRetriesDisponiveis() throws ServiceException {
		try {
			return doRetryDAO.findRetriesDisponiveis();
		} 
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS, readOnly = true)
	public ConfiguracaoSistema findConfiguracaoSistema() throws ServiceException {		
		try {
			List<ConfiguracaoSistema> lstConfig = (List<ConfiguracaoSistema>) genericDAO.findAll(ConfiguracaoSistema.class);
			if (lstConfig==null || lstConfig.size()==0 || StringUtils.isBlank(lstConfig.get(0).getPathStorage())) {
				throw new ValidationException(
						messageResource.getMessage("message.config.pathstorage.notfound.error", null, Locale.getDefault()));
			}
 			return lstConfig.get(0);
		}
		catch (Exception e) {
			logger.error(e);
			throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}		
	}
	
}

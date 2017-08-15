package br.com.globality.gtm.engine.dbmanager.service.impl;

import java.util.Calendar;
import java.util.Date;
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

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

import br.com.globality.gtm.engine.common.domain.Aplicacao;
import br.com.globality.gtm.engine.common.domain.ConfiguracaoSistema;
import br.com.globality.gtm.engine.common.domain.EventoInstancia;
import br.com.globality.gtm.engine.common.domain.EventoInstanciaConteudo;
import br.com.globality.gtm.engine.common.domain.EventoTipo;
import br.com.globality.gtm.engine.common.domain.TransacaoGrupo;
import br.com.globality.gtm.engine.common.domain.TransacaoInstancia;
import br.com.globality.gtm.engine.common.domain.TransacaoParametro;
import br.com.globality.gtm.engine.common.domain.TransacaoParametroValor;
import br.com.globality.gtm.engine.common.domain.TransacaoPasso;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcao;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcaoTodo;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoEventoNivel;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoInstancia;
import br.com.globality.gtm.engine.common.domain.compositeId.TransacaoParametroValorCompositeId;
import br.com.globality.gtm.engine.common.domain.compositeId.TransacaoPassoAcaoTodoCompositeId;
import br.com.globality.gtm.engine.common.exception.BusinessException;
import br.com.globality.gtm.engine.common.exception.DAOException;
import br.com.globality.gtm.engine.common.exception.ServiceException;
import br.com.globality.gtm.engine.common.exception.ValidationException;
import br.com.globality.gtm.engine.common.type.EmailEnvelopeType;
import br.com.globality.gtm.engine.common.type.GTMEnvelopeType;
import br.com.globality.gtm.engine.common.type.ParameterType;
import br.com.globality.gtm.engine.common.util.CommonConstants;
import br.com.globality.gtm.engine.common.util.CommonUtils;
import br.com.globality.gtm.engine.common.util.MessageResource;
import br.com.globality.gtm.engine.dbmanager.dao.DBManagerDAO;
import br.com.globality.gtm.engine.dbmanager.dao.GenericDAO;
import br.com.globality.gtm.engine.dbmanager.service.DBManagerService;

/**
 * Classe de regras de negócio da tarefa de gravação em banco de dados do engine
 * do GTM.
 * 
 * @author Leonardo Andrade
 * @version 1.0
 * @since 13/03/2017
 */
@Service("dbManagerService")
@Scope("prototype")
public class DBManagerServiceImpl implements DBManagerService {

	private static final Log logger = LogFactory.getLog(DBManagerServiceImpl.class);

	@Autowired
	private MessageResource messageResource;
	
	@Autowired
	private DBManagerDAO dbManagerDAO;

	@Autowired
	private GenericDAO genericDAO;
		
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {ServiceException.class, BusinessException.class})
	public void execute(String envXml) throws ServiceException, BusinessException {		
		
		try {			
			// Converte o XML do envelope para o objeto POJO de Log.
			GTMEnvelopeType envObj = (GTMEnvelopeType) CommonUtils.convertXmlToObj(envXml, GTMEnvelopeType.class);
			
			// Verifica se código do grupo é válido.
			if (!dbManagerDAO.hasGrupoByTrxGroupCode(envObj.getTrxGroupCode())) {
				throw new ValidationException(messageResource.getMessage("message.group.code.invalid.error",
						new String[] { envObj.getTrxCode(), envObj.getTrxGroupCode() }, Locale.getDefault()));
			}
			// Verifica se código de transação é válido.
			if (!dbManagerDAO.hasTransacaoByTrxCode(envObj.getTrxCode())) {
				throw new ValidationException(messageResource.getMessage("message.trasaction.code.invalid.error",
						new String[] { envObj.getTrxCode(), envObj.getTrxGroupCode() }, Locale.getDefault()));
			}
			// Verifica se relação entre transação e grupo é válida.
			TransacaoGrupo transacaoGrupo = dbManagerDAO.findIdTransacaoGrupoByTrxCodeAndTrxGroupCode(envObj.getTrxCode(), envObj.getTrxGroupCode());
			if (transacaoGrupo == null) {
				throw new ValidationException(messageResource.getMessage("message.group.transaction.association.invalid.error",
						new String[] { envObj.getTrxCode(), envObj.getTrxGroupCode() }, Locale.getDefault()));
			}
			
			// Registra o evento.
			String resultAddEvent = addEvent(envObj, transacaoGrupo);
			
			// Recuperando parâmetros retornados pelo método addEvent.
			boolean isFiltered		 = "Y".equalsIgnoreCase(resultAddEvent.split(",")[0]) ? true : false;
			boolean hasConteudo 	 = "Y".equalsIgnoreCase(resultAddEvent.split(",")[1]) ? true : false;
			long 	idTransacaoPasso = Long.valueOf(resultAddEvent.split(",")[2]);
			
			// Se evento não filtrado.
			if (!isFiltered) {
				// Adicionando conteúdo do evento, se houver.
				if (hasConteudo) {
					addEventContent(envObj);
				}
				// Adiciona os parâmetros informados.
				if (envObj.getTrxInstParameters() != null && 
						envObj.getTrxInstParameters().getParameter().length > 0) {
					addEventParameters(envObj, transacaoGrupo);
				}
				// Tratando as ações configuradas.
				TransacaoPasso transacaoPasso = (TransacaoPasso) genericDAO.findById(TransacaoPasso.class, idTransacaoPasso);
				checkActionToDo(envObj, transacaoPasso, hasConteudo);				
			}
		}
		catch (ValidationException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage(), e);
		} 
		catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		catch (DAOException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(messageResource.getMessage("message.erro.database", null, Locale.getDefault()), e);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}
	}
	
	@Transactional(propagation = Propagation.MANDATORY, rollbackFor = {ServiceException.class, ValidationException.class})
	private String addEvent(GTMEnvelopeType envObj, TransacaoGrupo transacaoGrupo) throws ServiceException, ValidationException {
		
		try {
			// Variáveis de controle de filtro e conteudo de evento.
			String  idcEventInstanceCont = "N";
			
			// Se a instância da transação não estiver cadastrada...
			TransacaoInstancia transacaoInstancia = (TransacaoInstancia) genericDAO.findById(TransacaoInstancia.class, envObj.getTrxInstIdentifier());
			
			if (transacaoInstancia==null) {
				// Efetua o cadastro da instância da transação.
				transacaoInstancia = new TransacaoInstancia();
				transacaoInstancia.setId(envObj.getTrxInstIdentifier());
				transacaoInstancia.setTransacaoGrupo(transacaoGrupo);
				transacaoInstancia.setTransacaoInstanciaPai(null);
				transacaoInstancia.setData(new Date());
				genericDAO.add(transacaoInstancia);
			}
				
			// Tratando referência se parent existir.
			if (envObj.getTrxEventInstParentIdentifier()!=null
					&& envObj.getTrxEventInstParentIdentifier().trim().length()>0) {				
				// Verifica se a instância parent existe.
				TransacaoInstancia transacaoInstanciaParent = (TransacaoInstancia) genericDAO.findById(TransacaoInstancia.class, envObj.getTrxEventInstParentIdentifier());
				if (transacaoInstanciaParent==null) {
					throw new ValidationException(messageResource.getMessage("message.transaction.parent.invalid.error", null, Locale.getDefault()));
				}				
				// Definindo a referência ao parent na instância da transação.
				transacaoInstancia.setTransacaoInstanciaPai(transacaoInstanciaParent);
				genericDAO.update(transacaoInstancia);
			}
						
			// Recupera a instancia da transação.
			TransacaoPassoInstancia transacaoPassoInstancia = dbManagerDAO.findTransacaoPassoInstanciaByFiltro(envObj.getTrxStpInstIdentifier(), envObj.getTrxInstIdentifier());
			
			TransacaoPasso transacaoPasso = null;
			
			if (transacaoPassoInstancia!=null) {
				transacaoPasso = transacaoPassoInstancia.getTransacaoPasso();
			}
			else { // Se não existir, cadastra a nova instância de transação.
				
				// Recuperando o passo da transação.				
				transacaoPasso = dbManagerDAO.findTransacaoPassoByTrxCodeAndTrxStepCode(envObj.getTrxCode(), envObj.getTrxStpCode());
				
				// Se referência do passo da transação não encontrada.
				if (transacaoPasso == null) {
					throw new ValidationException(messageResource.getMessage("message.step.inalid.error", 
							new String[] {envObj.getTrxInstIdentifier(), envObj.getTrxStpCode()}, Locale.getDefault()));
				}
				
				// Inclui a nova a instância do passo da transação.
				transacaoPassoInstancia = new TransacaoPassoInstancia();
				transacaoPassoInstancia.setId(envObj.getTrxStpInstIdentifier());
				transacaoPassoInstancia.setTransacaoInstancia(transacaoInstancia);
				transacaoPassoInstancia.setTransacaoPasso(transacaoPasso);
				transacaoPassoInstancia.setData(new Date());
				genericDAO.add(transacaoPassoInstancia);
			}
		
			// Tratamento de eventos.
			if (transacaoPasso!=null) {
				
				TransacaoPassoEventoNivel transacaoPassoEventoNivel = dbManagerDAO.findTransacaoPassoEventoNivelByIdTransacaoPassoAndTrxEventLevelCode(
						transacaoPasso.getId(), envObj.getTrxEventLevelCode());
				
				// Resolvendo o filtro do evento.
				if (transacaoPassoEventoNivel==null) {
					return "Y,N" + "," + transacaoPasso.getId(); // Filtered, Content
				}
				
				// Validação o código do tipo de evento informado.
				EventoTipo eventoTipo = (EventoTipo) genericDAO.findById(EventoTipo.class, envObj.getTrxEventTypeCode());
				if (eventoTipo==null) {
					throw new ValidationException(messageResource.getMessage("message.event.type.invalid.error", null, Locale.getDefault()));
				}
				
				// Validando o código da aplicação informado.
				Aplicacao aplicacao = dbManagerDAO.findAplicacaoByTrxInstAppCode(envObj.getTrxInstAppCode());
				if (aplicacao==null) {
					throw new ValidationException(messageResource.getMessage("message.application.invalid.error", null, Locale.getDefault()));
				}
				
				// Verificando necessidade de gravação do conteúdo.
				if (StringUtils.isNotBlank(envObj.getTrxEventInstContent())) {
					if ("Y".equalsIgnoreCase(transacaoPasso.getGravarNaBase())) {
						if ("Y".equalsIgnoreCase(transacaoPassoEventoNivel.getConteudo())) { 
							idcEventInstanceCont = "Y";
						}
					}	
				}
									
				// Gravando a instância do evento.
				EventoInstancia vo = new EventoInstancia();
				vo.setId(envObj.getTrxEventInstIdentifier());
				vo.setAplicacao(aplicacao);
				vo.setDescricao((envObj.getTrxEventInstDesc()!=null) ? envObj.getTrxEventInstDesc().trim() : null);
				vo.setConteudo(idcEventInstanceCont);
				vo.setTransacaoPassoInstancia(transacaoPassoInstancia);
				vo.setEventoInstanciaPai(null);
				vo.setData(new Date());
				vo.setEventoNivel(transacaoPassoEventoNivel.getEventoNivel());
				vo.setEventoTipo(eventoTipo);
				genericDAO.add(vo);
				
				// Tratando parent da instância do evento.
				if (envObj.getTrxEventInstParentIdentifier()!=null && envObj.getTrxEventInstParentIdentifier().trim().length()>0) {
					
					EventoInstancia evtIntanciaParent = (EventoInstancia) genericDAO.findById(EventoInstancia.class, envObj.getTrxEventInstParentIdentifier());
					
					// Se parent não localizado lançar erro de validação.
					if (evtIntanciaParent==null) {
						throw new ValidationException(messageResource.getMessage("message.event.instance.parent.invalid.error", null, Locale.getDefault()));
					}
					
					// Definindo a referencia para o parent.
					vo.setEventoInstanciaPai(evtIntanciaParent);
					genericDAO.update(vo);
					
				}				
			}			
			return "N," + idcEventInstanceCont + "," + transacaoPasso.getId();			
		}
		catch (ValidationException e) {
			throw e;
		}
		catch (DAOException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(messageResource.getMessage("message.erro.database", null, Locale.getDefault()), e);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}
	}
	
	@Transactional(propagation = Propagation.MANDATORY, rollbackFor = {ServiceException.class, ValidationException.class})
	private void addEventContent(GTMEnvelopeType envObj) throws ServiceException, ValidationException {
	
		try {
			// Efetuando o cadastro da instância do evento.
			EventoInstancia evtInstancia = (EventoInstancia) genericDAO.findById(EventoInstancia.class, envObj.getTrxEventInstIdentifier());
			if (evtInstancia==null) {
				throw new ValidationException(
						messageResource.getMessage("message.event.notfound.for.content.error", null, Locale.getDefault()));
			}			
			// Verificando se o conteúdo da instância do evento já está gravado.
			EventoInstanciaConteudo evtInstanciaConteudo = (EventoInstanciaConteudo) genericDAO.findById(EventoInstanciaConteudo.class, envObj.getTrxEventInstIdentifier());
			if (evtInstanciaConteudo!=null) {
				throw new ValidationException(
						messageResource.getMessage("message.duplicate.content.event.instance.error", null, Locale.getDefault()));
			}						
			// Incluindo o registro do conteúdo da instância do evento.
			EventoInstanciaConteudo vo = new EventoInstanciaConteudo();
			vo.setId(evtInstancia.getId());
			vo.setData(new Date());
			vo.setConteudo(envObj.getTrxEventInstContent());
			vo.setAplicacao(evtInstancia.getAplicacao());
			genericDAO.add(vo);
		}
		catch (ValidationException e) {
			throw e;
		}
		catch (DAOException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(messageResource.getMessage("message.erro.database", null, Locale.getDefault()), e);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}
	}
	
	@Transactional(propagation = Propagation.MANDATORY, rollbackFor = {ServiceException.class})
	private void addEventParameters(GTMEnvelopeType envObj, TransacaoGrupo transacaoGrupo) throws ServiceException {
		try {
			// Recuperando parâmetros cadastrados para a transação.
			List<TransacaoParametro> lstTransacaoParametro = dbManagerDAO.findTransacaoParametroByIdTransacao(transacaoGrupo.getTransacao().getId());
			// Incluindo parâmetros e valores.
			for (TransacaoParametro dto : lstTransacaoParametro) {
				for (int i = 0; i < envObj.getTrxInstParameters().getParameter().length; i++) {
					ParameterType parameter = envObj.getTrxInstParameters().getParameter()[i];
					if (parameter.getName().equals(dto.getNome())) {
						// Incluindo o registro para o valor do parâmetro informado.
						TransacaoParametroValor vo = new TransacaoParametroValor();
						vo.setValor(parameter.getValue());
						TransacaoParametroValorCompositeId voId = new TransacaoParametroValorCompositeId();
						voId.setIdParametro(dto.getId());
						voId.setIdEventoInstancia(envObj.getTrxEventInstIdentifier());
						vo.setId(voId);
						genericDAO.add(vo);
						break;
					}
				}
			}
		}
		catch (DAOException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(messageResource.getMessage("message.erro.database", null, Locale.getDefault()), e);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}		
	}
	
	@Transactional(propagation = Propagation.MANDATORY, rollbackFor = {ServiceException.class, ValidationException.class})
	private void checkActionToDo(GTMEnvelopeType envObj, TransacaoPasso transacaoPasso, boolean hasConteudo) throws ServiceException, ValidationException {		
		
		try {
			List<TransacaoPassoAcao> acoes = dbManagerDAO.findTransacaoPassoAcaoByIdTransacaoPassoAndTrxEventTypeCode(
						transacaoPasso.getId(), envObj.getTrxEventTypeCode());
							
			if (acoes!=null && !acoes.isEmpty()) {
				
				TransacaoPassoAcao acaoPlan = acoes.get(0);
				
				if (!hasConteudo) {
					// Verificando se o passo da transação possui conteúdo cadastrado anteriormente.
					hasConteudo = dbManagerDAO.hasConteudoByIdTransacaoInstanciaAndIdPassoInstancia(
							envObj.getTrxInstIdentifier(), envObj.getTrxStpInstIdentifier());
				}
				
				// Verificação de necessidade do retry.
				if (acaoPlan.getIntervalo() > 0 && acaoPlan.getQtdeTentativas() > 0 
						&& StringUtils.isNotBlank(acaoPlan.getFilaDestino())
						&& hasConteudo) {
					
					Calendar dataAtual = Calendar.getInstance();
					
					// Calculando o tempo previsto para a execução considerando o intervalo configurado.
					long t = dataAtual.getTimeInMillis();
					Date dataExecucaoRetry = new Date(t + (acaoPlan.getIntervalo() * 60000));
					
					// Recupera o total de retries já exetutados.
					Long qtdeResubReal = dbManagerDAO.findQtdeRetriesExecutados(transacaoPasso.getId(), envObj.getTrxStpInstIdentifier());
					qtdeResubReal++;
					
					// Se total já executado for inferior ou igual ao total previsto.
					if (qtdeResubReal <= acaoPlan.getQtdeTentativas()) {
						// Grava o registro da acao a ser executada pelo mecanismo de retry.
						TransacaoPassoAcaoTodo vo = new TransacaoPassoAcaoTodo();
						vo.setQtdeTentativas(Long.valueOf(qtdeResubReal));
						vo.setData(dataExecucaoRetry);
						vo.setStatus("N");
						TransacaoPassoAcaoTodoCompositeId voId = new TransacaoPassoAcaoTodoCompositeId();
						voId.setIdTransacaoPasso(transacaoPasso.getId());
						voId.setIdEventoTipo(envObj.getTrxEventTypeCode());
						voId.setIdEventoInstancia(envObj.getTrxEventInstIdentifier());
						vo.setId(voId);
						genericDAO.add(vo);
					} 
					else {
						// Removendo histórico de ações executadas.
						List<TransacaoPassoAcaoTodo> acoesExecutadas = dbManagerDAO.findTransacaoPassoAcaoTodoExecutadas(envObj.getTrxEventTypeCode(), transacaoPasso.getId(), envObj.getTrxInstIdentifier());
						for (TransacaoPassoAcaoTodo acao : acoesExecutadas) {
							genericDAO.delete(TransacaoPassoAcaoTodo.class, acao);
						}
						// Verificando necessidade de Ressub.
						if (StringUtils.isNotBlank(acaoPlan.getDestinatario()) && StringUtils.isNotBlank(acaoPlan.getFilaDestino())) {
							// Grava o registro da acao a ser executada pelo mecanismo de resub.
							TransacaoPassoAcaoTodo vo = new TransacaoPassoAcaoTodo();
							vo.setQtdeTentativas(0l);
							vo.setData(dataAtual.getTime());
							vo.setStatus("N");
							TransacaoPassoAcaoTodoCompositeId voId = new TransacaoPassoAcaoTodoCompositeId();
							voId.setIdTransacaoPasso(transacaoPasso.getId());
							voId.setIdEventoTipo(envObj.getTrxEventTypeCode());
							voId.setIdEventoInstancia(envObj.getTrxEventInstIdentifier());
							vo.setId(voId);
							genericDAO.add(vo);
							// Envio de email.
							sendMailAction(envObj, acaoPlan, CommonConstants.ACAO_RESSUB);
						}
					}
				
				}
				// Verificando necessidade de Ressub ou Notificação.
				else if (StringUtils.isNotBlank(acaoPlan.getDestinatario())) {
					if (StringUtils.isNotBlank(acaoPlan.getFilaDestino())) {
						if (hasConteudo) { // Execução de Ressub.
							// Grava o registro da acao a ser executada pelo mecanismo de resub.
							TransacaoPassoAcaoTodo vo = new TransacaoPassoAcaoTodo();
							vo.setQtdeTentativas(0l);
							vo.setData(Calendar.getInstance().getTime());
							vo.setStatus("N");
							TransacaoPassoAcaoTodoCompositeId voId = new TransacaoPassoAcaoTodoCompositeId();
							voId.setIdTransacaoPasso(transacaoPasso.getId());
							voId.setIdEventoTipo(envObj.getTrxEventTypeCode());
							voId.setIdEventoInstancia(envObj.getTrxEventInstIdentifier());
							vo.setId(voId);
							genericDAO.add(vo);
							// Envio de e-mail.
							sendMailAction(envObj, acaoPlan, CommonConstants.ACAO_RESSUB);
						}
					}
					else {
						// Envio de Notificação.
						sendMailAction(envObj, acaoPlan, CommonConstants.ACAO_NOTIFY);
					}
				}
			}
		}
		catch (DAOException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(messageResource.getMessage("message.erro.database", null, Locale.getDefault()), e);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}
	}
	
	@Transactional(propagation = Propagation.MANDATORY, rollbackFor = {ServiceException.class})
	private void sendMailAction(GTMEnvelopeType envObj, TransacaoPassoAcao acaoPlan, int acao) throws ServiceException {
		
		MQQueueManager queueManager = null;
		MQQueue queue = null;		
		try {
			EmailEnvelopeType envelope = new EmailEnvelopeType();
			envelope.setIdTansacaoPasso(acaoPlan.getTransacaoPasso().getId());
			envelope.setDestinatario(acaoPlan.getDestinatario());
			envelope.setGtmEnvelope(envObj);
			envelope.setAcao(acao);
			
			// Convertendo o envelope para o formato XML.
			String xml = CommonUtils.convertObjToXml(envelope, EmailEnvelopeType.class);
			
			// Publicando o XML na fila de envio de e-mails.
			queueManager = CommonUtils.initQueueManager();
			CommonUtils.writeQueueMQ(queueManager, CommonConstants.QUEUE_EMAIL, xml);
		} 
		catch (MQException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(messageResource.getMessage("message.queue.access.error", new String[] {CommonConstants.QUEUE_EMAIL}, Locale.getDefault()), e);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
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
				throw new ServiceException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
			}
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

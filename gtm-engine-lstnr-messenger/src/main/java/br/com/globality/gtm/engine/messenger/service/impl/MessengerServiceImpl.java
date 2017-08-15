package br.com.globality.gtm.engine.messenger.service.impl;

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

import br.com.globality.gtm.engine.common.domain.ConfiguracaoSistema;
import br.com.globality.gtm.engine.common.exception.BusinessException;
import br.com.globality.gtm.engine.common.exception.ServiceException;
import br.com.globality.gtm.engine.common.exception.ValidationException;
import br.com.globality.gtm.engine.common.type.EmailEnvelopeType;
import br.com.globality.gtm.engine.common.util.AppConfigBundle;
import br.com.globality.gtm.engine.common.util.CommonConstants;
import br.com.globality.gtm.engine.common.util.CommonUtils;
import br.com.globality.gtm.engine.common.util.MessageResource;
import br.com.globality.gtm.engine.messenger.dao.GenericDAO;
import br.com.globality.gtm.engine.messenger.service.MessengerService;
import br.com.globality.gtm.engine.messenger.util.EmailManager;

/**
 * Classe de regras de negócio da tarefa de envio de e-mails do engine do GTM.
 * 
 * @author Leonardo Andrade
 * @version 1.0
 * @since 25/04/2017
 */
@Service("messengerService")
@Scope("prototype")
public class MessengerServiceImpl implements MessengerService {

	private static final Log logger = LogFactory.getLog(MessengerServiceImpl.class);

	@Autowired
	private MessageResource messageResource;
		
	@Autowired
    private EmailManager emailManager;
	
	@Autowired
    private GenericDAO genericDAO;
	
	@Override
	public void execute(String envXml) throws ServiceException, BusinessException {		
		
		try {	
			EmailEnvelopeType envObj = (EmailEnvelopeType) CommonUtils.convertXmlToObj(envXml, EmailEnvelopeType.class);
			
			String subject = null;
			
			if (envObj.getAcao()==CommonConstants.ACAO_NOTIFY) 
				subject = messageResource.getMessage("message.email.subject.notify", null, Locale.getDefault());
			else 
				subject = messageResource.getMessage("message.email.subject.resub", null, Locale.getDefault());
			
			StringBuffer sb = new StringBuffer();
			
			sb.append("<table width='100%'>");
			sb.append("<tr><td align='center'>");
			
			sb.append("<p style='margin-top:10px;'/>");
			
			if (envObj.getAcao()==CommonConstants.ACAO_RESSUB) { 
				
				String baseUrl = AppConfigBundle.getProperty("env.engine.baseurl") ;
				if (StringUtils.isNotBlank(baseUrl))  {
					if (baseUrl.lastIndexOf('/')!=baseUrl.length()-1) 
						baseUrl += '/';
				}
				else 
					throw new ValidationException(messageResource.getMessage("message.email.baseurl.empty.error", null, Locale.getDefault()));
				
				sb.append("<table border='1' width='98%' bgcolor='#FFFFE0' cellpadding='6'>");
				sb.append("<tr valign='middle'><td align='center'><font size='2'>");
				sb.append("<a href='" + baseUrl + "doresub?stepId="+envObj.getIdTansacaoPasso()+"&evtType="+envObj.getGtmEnvelope().getTrxEventTypeCode()+"&evtInstId="+envObj.getGtmEnvelope().getTrxEventInstIdentifier()+"&stepInstId="+envObj.getGtmEnvelope().getTrxStpInstIdentifier()+"&trxInstId="+envObj.getGtmEnvelope().getTrxInstIdentifier()+"'>" + messageResource.getMessage("message.email.resub.link", null, Locale.getDefault()) + "</a>");
				sb.append("</font></td></tr></table><p style='margin-top:25px;'/>");
			}
						
			sb.append("<table border='1' width='98%' bgcolor='#F0F8FF' cellpadding='6'>");
			sb.append("<tr valign='middle'><th><b>" + messageResource.getMessage("message.email.subtitle.error.description", null, Locale.getDefault()) + "</b></th></tr>");
			sb.append("<tr valign='middle'><td align='center'><font size='2'>");
			
			if (envObj.getGtmEnvelope()!=null && 
					StringUtils.isNotBlank(envObj.getGtmEnvelope().getTrxEventInstDesc())) 
				sb.append(envObj.getGtmEnvelope().getTrxEventInstDesc());
			else 
				sb.append(messageResource.getMessage("message.email.msg.error.empty", null, Locale.getDefault()));
			
			sb.append("</font></td></tr></table><p style='margin-top:25px;'/>");
			
			sb.append("<table border='1' width='98%' bgcolor='#F0F8FF' cellpadding='6'>");
			sb.append("<tr valign='middle'><th><b>" + messageResource.getMessage("message.email.subtitle.technical.information", null, Locale.getDefault()) + "</b></th></tr>");
			sb.append("<tr valign='middle'><td align='center'>");
			
			sb.append("<table width='100%' height='100%' cellspacing='4'>");
			sb.append("<tr><td width='150'><font size='2'>" + messageResource.getMessage("message.email.information.label.appcode", null, Locale.getDefault()) + ":</font></td><td><font size='2'>" +envObj.getGtmEnvelope().getTrxInstAppCode() + "</font></td></tr>");
			sb.append("<tr><td><font size='2'>" + messageResource.getMessage("message.email.information.label.datetime", null, Locale.getDefault()) + ":</font></td><td><font size='2'>" + envObj.getGtmEnvelope().getTrxEventInstDt().getTime() + "</font></td></tr>");
			sb.append("<tr><td><font size='2'>" + messageResource.getMessage("message.email.information.label.msgid", null, Locale.getDefault()) + ":</font></td><td><font size='2'>" + envObj.getGtmEnvelope().getTrxInstIdentifier() + "</font></td></tr>");
			sb.append("<tr><td><font size='2'>" + messageResource.getMessage("message.email.information.label.transaction", null, Locale.getDefault()) + ":</font></font></td><td><font size='2'>" + envObj.getGtmEnvelope().getTrxCode() + "</font></font></td></tr>");
			sb.append("<tr><td><font size='2'>" + messageResource.getMessage("message.email.information.label.environment", null, Locale.getDefault()) + ":</font></td><td><font size='2'>" + envObj.getGtmEnvelope().getTrxInstIdentifier() + "</font></td></tr>");
			sb.append("<tr><td><font size='2'>" + messageResource.getMessage("message.email.information.label.errorcode", null, Locale.getDefault()) + ":</font></td><td><font size='2'>" + envObj.getGtmEnvelope().getTrxEventTypeCode() + "</font></td></tr>");
			sb.append("<tr><td><font size='2'>" + messageResource.getMessage("message.email.information.label.loglevel", null, Locale.getDefault()) + ":</font></td><td><font size='2'>" + envObj.getGtmEnvelope().getTrxEventLevelCode() + "</font></td></tr>");
			
			sb.append("</table>");
			
			sb.append("</td></tr></table><p style='margin-top:15px;'/>");
			
			sb.append("<table width='98%' cellpadding='6'>");
			sb.append("<tr valign='middle'><td align='center'><font size='2'>");
			sb.append("<b>GTM - GLOBAL TRANSACTION MANAGER</b><p style='margin-top:6px;'>");
			sb.append("<i>" + messageResource.getMessage("message.email.footer.noreply", null, Locale.getDefault()) + "</i>");
			sb.append("</font></td></tr></table>");
			
			sb.append("</td></tr>");
			sb.append("</table>");
			
			emailManager.sendMail(envObj.getDestinatario(), subject, sb.toString());
	    } 
		catch (ValidationException e) {
			logger.error(e);
			throw new BusinessException(e.getMessage(), e);
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
			@SuppressWarnings("unchecked")
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

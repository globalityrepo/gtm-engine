package br.com.globality.gtm.engine.messenger.task;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ibm.mq.MQQueueManager;

import br.com.globality.gtm.engine.common.exception.BusinessException;
import br.com.globality.gtm.engine.messenger.service.MessengerService;

/**
 * Classe que executa a tarefa de envio de e-mails.
 * 
 * @author Leonardo Andrade
 * @version 1.0
 * @since 25/04/2017
 */
@Component("messengerTask")
@Scope("prototype")
public class MessengerTask implements Runnable {

	final static Logger logger = Logger.getLogger(MessengerTask.class);
	
	private MessengerService messengerService;
	
	private String envXml;
	
	private MQQueueManager queueManager;
	
	public MessengerTask(String envXml, MessengerService messengerService, MQQueueManager queueManager) {
		super();
		this.envXml = envXml;
		this.messengerService = messengerService;
		this.queueManager = queueManager;
	}
	
	@Override
	public void run() {
		boolean commitQueueManager = true;
		try {
			messengerService.execute(envXml);
		}
		catch (BusinessException e) {
			logger.error(e.getMessage(), e);			
		}
		catch (Exception e) {
			commitQueueManager = false;
			logger.error(e.getMessage(), e);		
		}
		finally {
			if (commitQueueManager && queueManager!=null) {
				try {
					queueManager.commit();
					queueManager.disconnect();
				}
				catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

}

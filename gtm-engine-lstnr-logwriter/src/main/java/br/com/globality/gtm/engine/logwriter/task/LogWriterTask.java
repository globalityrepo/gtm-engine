package br.com.globality.gtm.engine.logwriter.task;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ibm.mq.MQQueueManager;

import br.com.globality.gtm.engine.common.exception.BusinessException;
import br.com.globality.gtm.engine.logwriter.service.LogWriterService;

/**
 * Classe que executa a tarefa de escrita de arquivo texto da engine do GTM.
 * 
 * @author Leonardo Andrade
 * @version 1.0
 * @since 13/03/2017
 */
@Component("logWriterTask")
@Scope("prototype")
public class LogWriterTask implements Runnable {

	final static Logger logger = Logger.getLogger(LogWriterTask.class);

	private LogWriterService logWriterService;
	
	private String envXml;
	
	private MQQueueManager queueManager;
		
	public LogWriterTask(String envXml, LogWriterService logWriterService, MQQueueManager queueManager) {
		super();
		this.envXml = envXml;
		this.logWriterService = logWriterService;
		this.queueManager = queueManager;
	}

	@Override
	public void run() {		
		boolean commitQueueManager = true;
		try {
			logWriterService.execute(envXml);
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

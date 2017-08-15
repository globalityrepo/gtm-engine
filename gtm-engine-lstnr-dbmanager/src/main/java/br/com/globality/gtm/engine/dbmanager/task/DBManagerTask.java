package br.com.globality.gtm.engine.dbmanager.task;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ibm.mq.MQQueueManager;

import br.com.globality.gtm.engine.common.exception.BusinessException;
import br.com.globality.gtm.engine.dbmanager.service.DBManagerService;

/**
 * Classe que possui executa executa a tarefa de gravação em banco de dados do listener.
 * 
 * @author Leonardo Andrade
 * @version 1.0
 * @since 13/03/2017
 */
@Component("dbManagerTask")
@Scope("prototype")
public class DBManagerTask implements Runnable {

	final static Logger logger = Logger.getLogger(DBManagerTask.class);
	
	private DBManagerService dbManagerService;
	
	private String envXml;
	
	private MQQueueManager queueManager;
	
	public DBManagerTask(String envXml, DBManagerService dbManagerService, MQQueueManager queueManager) {
		super();
		this.envXml = envXml;
		this.dbManagerService = dbManagerService;
		this.queueManager = queueManager;
	}
	
	@Override
	public void run() {		
		boolean commitQueueManager = true;
		try {
			dbManagerService.execute(envXml);
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

package br.com.globality.gtm.engine.dbmanager.executor;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;

import br.com.globality.gtm.engine.common.domain.ConfiguracaoSistema;
import br.com.globality.gtm.engine.common.exception.JobException;
import br.com.globality.gtm.engine.common.util.CommonConstants;
import br.com.globality.gtm.engine.common.util.CommonUtils;
import br.com.globality.gtm.engine.common.util.MessageResource;
import br.com.globality.gtm.engine.dbmanager.service.DBManagerService;
import br.com.globality.gtm.engine.dbmanager.task.DBManagerTask;

/**Listenner para a fila de mensagens para gravação em banco de dados da Engine do GTM.
* @author Leonardo Andrade
* @version 1.0
* @since 22/02/2017
*/
@Component("dbManagerExecutor")
public class DBManagerExecutor {

	final static Logger logger = Logger.getLogger(DBManagerExecutor.class);
	
	@Autowired
	@Qualifier("runDBManagerExecutor")
	ThreadPoolTaskExecutor poolTaskExecutor;
	
	@Autowired
	private DBManagerService dbManagerService;
	
	@Autowired
	private MessageResource messageResource;
	
	@Scheduled(fixedDelay=5000)
	public void listener() throws Exception {
		MQQueueManager queueManager = null;
		try {
			// Definindo o locale da aplicação.
			ConfiguracaoSistema configApp = dbManagerService.findConfiguracaoSistema();
			CommonUtils.changeAppLocale(configApp.getLocale());
			// Estabelecendo comunicação com o MQ Server.
			queueManager = CommonUtils.initQueueManager();
			String envXml = CommonUtils.readMQMessageBySyncPoint(queueManager, CommonConstants.QUEUE_DB);
			if (envXml!=null) {
				poolTaskExecutor.execute(new DBManagerTask(envXml, dbManagerService, queueManager));
			}
			else if (queueManager!=null) {
				try {
					queueManager.disconnect();
				}
				catch (Exception e) {
					logger.error(e);
				}
			}
		}
		catch (MQException e) {
			throw new JobException(messageResource.getMessage("message.queue.access.error", new String[] {CommonConstants.QUEUE_DB}, Locale.getDefault()), e);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new JobException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}
	}
	
}

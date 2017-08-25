package br.com.globality.gtm.engine.messenger.executor;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;

import br.com.globality.gtm.engine.common.domain.ConfiguracaoSistema;
import br.com.globality.gtm.engine.common.util.AppConfigBundle;
import br.com.globality.gtm.engine.common.util.CommonConstants;
import br.com.globality.gtm.engine.common.util.CommonUtils;
import br.com.globality.gtm.engine.common.util.MessageResource;
import br.com.globality.gtm.engine.messenger.service.MessengerService;
import br.com.globality.gtm.engine.messenger.task.MessengerTask;
import br.com.globality.gtm.engine.messenger.task.PoolSizeLoggerTask;

/**
 * Listener para a fila de mensagens para envio de e-mails da Engine do GTM.
 * 
 * @author Leonardo Andrade
 * @version 1.0
 * @since 25/04/2017
 */
@Component("messengerExecutor")
public class MessengerExecutor {

	final static Logger logger = Logger.getLogger(MessengerExecutor.class);
	
	@Autowired
	private MessengerService messengerService;
	
	@Autowired
	private MessageResource messageResource;
		
	public static AtomicInteger threadCounter = new AtomicInteger(0);
		
	public void init() {
		ExecutorService executor = null;
		try {
			// Define o locale da aplicação.
			localeSetup();
			
			// Recupera a tamanho do pool do arquivo de configurações.
			final Integer POOL_SIZE = Integer.valueOf(AppConfigBundle.getProperty("messenger.threadpool.size"));
			
			// Agenda a execução da task de verificação do tamanho do pool.
			schedulePoolSizeLoggerTask();
			
			// Criação de pool de tasks.
			executor = Executors.newCachedThreadPool();	
			
			// Loop para instanciar tasks.
			while (true) {
				if (POOL_SIZE>threadCounter.get()) {
					// Estabelecendo comunicação com o MQ Server.
					MQQueueManager queueManager = null;
					try {
						queueManager = CommonUtils.initQueueManager();
						String envXml = CommonUtils.readMQMessageBySyncPoint(queueManager, CommonConstants.QUEUE_EMAIL);
						if (envXml!=null) {
							executor.submit(new MessengerTask(envXml, messengerService, queueManager));
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
						e.printStackTrace();
						logger.error(messageResource.getMessage("message.queue.access.error", new String[] {CommonConstants.QUEUE_EMAIL}, Locale.getDefault()), e);
						Thread.sleep(5000);
					}
					catch (Exception e) {
						e.printStackTrace();
						logger.error(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
						Thread.sleep(5000);
					}
				}
			}
		}
		catch (MissingResourceException | NumberFormatException e) {
			logger.error(messageResource.getMessage("message.erro.threadpoolsize.bad", null, Locale.getDefault()));
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}
		finally {
			if (executor!=null) {
				executor.shutdown();
			}
			this.init(); 
		}
	}
	
	private void localeSetup() throws Exception {
		ConfiguracaoSistema configApp = messengerService.findConfiguracaoSistema();
		CommonUtils.changeAppLocale(configApp.getLocale());
	}
	
	private void schedulePoolSizeLoggerTask() {
		ScheduledExecutorService executor = null;
		try {
			executor = Executors.newSingleThreadScheduledExecutor();
			executor.scheduleAtFixedRate(new PoolSizeLoggerTask(), 1, 60, TimeUnit.SECONDS);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}
	}
	
}

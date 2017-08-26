package br.com.globality.gtm.engine.doretry.executor;

import java.util.List;
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

import br.com.globality.gtm.engine.common.domain.ConfiguracaoSistema;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcaoTodo;
import br.com.globality.gtm.engine.common.util.AppConfigBundle;
import br.com.globality.gtm.engine.common.util.CommonUtils;
import br.com.globality.gtm.engine.common.util.MessageResource;
import br.com.globality.gtm.engine.doretry.service.DoRetryService;
import br.com.globality.gtm.engine.doretry.task.DoRetryTask;
import br.com.globality.gtm.engine.doretry.task.PoolSizeLoggerTask;

/**Listenner para execução de retries da engine do GTM.
* @author Leonardo Andrade
* @version 1.0
* @since 25/04/2017
*/
@Component("doRetryExecutor")
public class DoRetryExecutor {

	final static Logger logger = Logger.getLogger(DoRetryExecutor.class);
	
	@Autowired
	private DoRetryService doRetryService;
	
	@Autowired
	private MessageResource messageResource;
	
	public static AtomicInteger threadCounter = new AtomicInteger(0);
		
	public void init() {
		ExecutorService executor = null;
		try {
			// Define o locale da aplicação.
			localeSetup();
			
			// Recupera a tamanho do pool do arquivo de configurações.
			final Integer POOL_SIZE = Integer.valueOf(AppConfigBundle.getProperty("doretry.threadpool.size"));
			
			// Agenda a execução da task de verificação do tamanho do pool.
			schedulePoolSizeLoggerTask();
			
			// Criação de pool de tasks.
			executor = Executors.newCachedThreadPool();	
			
			// Loop para instanciar tasks.
			while (true) {
				if (POOL_SIZE>threadCounter.get()) {
					try {
						// Verifica se há retries disponíveis.
						List<TransacaoPassoAcaoTodo> retries = doRetryService.findRetriesDisponiveis();
						for (TransacaoPassoAcaoTodo retry : retries) {
							executor.submit(new DoRetryTask(retry, doRetryService));
						}
					}
					catch (Exception e) {
						e.printStackTrace();
						logger.error(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
					}
				}
				// Intervalo de execução.
				Thread.sleep(5000); // 5s
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
		ConfiguracaoSistema configApp = doRetryService.findConfiguracaoSistema();
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

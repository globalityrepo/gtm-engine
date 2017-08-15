package br.com.globality.gtm.engine.doretry.executor;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import br.com.globality.gtm.engine.common.domain.ConfiguracaoSistema;
import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcaoTodo;
import br.com.globality.gtm.engine.common.exception.JobException;
import br.com.globality.gtm.engine.common.util.CommonUtils;
import br.com.globality.gtm.engine.common.util.MessageResource;
import br.com.globality.gtm.engine.doretry.service.DoRetryService;
import br.com.globality.gtm.engine.doretry.task.DoRetryTask;

/**Listenner para execução de retries da engine do GTM.
* @author Leonardo Andrade
* @version 1.0
* @since 25/04/2017
*/
@Component("doRetryExecutor")
public class DoRetryExecutor {

	final static Logger logger = Logger.getLogger(DoRetryExecutor.class);
	
	@Autowired
	@Qualifier("runDoRetryExecutor")
	ThreadPoolTaskExecutor poolTaskExecutor;
	
	@Autowired
	private DoRetryService doRetryService;
	
	@Autowired
	private MessageResource messageResource;
	
	@Scheduled(fixedDelay=10000)
	public void listener() throws Exception {
		try {
			// Definindo o locale da aplicação.
			ConfiguracaoSistema configApp = doRetryService.findConfiguracaoSistema();
			CommonUtils.changeAppLocale(configApp.getLocale());
			// Verifica se há retries disponíveis.
			List<TransacaoPassoAcaoTodo> retries = doRetryService.findRetriesDisponiveis();
			for (TransacaoPassoAcaoTodo retry : retries) {
				poolTaskExecutor.execute(new DoRetryTask(retry, doRetryService));
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new JobException(messageResource.getMessage("message.erro.ineperado", null, Locale.getDefault()), e);
		}
	}
	
}

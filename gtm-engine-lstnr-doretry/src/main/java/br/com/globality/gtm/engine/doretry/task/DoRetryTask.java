package br.com.globality.gtm.engine.doretry.task;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.globality.gtm.engine.common.domain.TransacaoPassoAcaoTodo;
import br.com.globality.gtm.engine.common.exception.BusinessException;
import br.com.globality.gtm.engine.doretry.service.DoRetryService;

/**
 * Classe que executa a tarefa de retry.
 * 
 * @author Leonardo Andrade
 * @version 1.0
 * @since 25/04/2017
 */
@Component("doRetryTask")
@Scope("prototype")
public class DoRetryTask implements Runnable {

	final static Logger logger = Logger.getLogger(DoRetryTask.class);
	
	private DoRetryService doRetryService;
	
	private TransacaoPassoAcaoTodo transacaoPassoAcaoTodo;
	
	public DoRetryTask(TransacaoPassoAcaoTodo transacaoPassoAcaoTodo, DoRetryService doRetryService) {
		super();
		this.transacaoPassoAcaoTodo = transacaoPassoAcaoTodo;
		this.doRetryService = doRetryService;
	}
	
	@Override
	public void run() {
		try {
			doRetryService.execute(transacaoPassoAcaoTodo);
		}
		catch (BusinessException e) {
			logger.error(e.getMessage(), e);				
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);			
		}
	}

}

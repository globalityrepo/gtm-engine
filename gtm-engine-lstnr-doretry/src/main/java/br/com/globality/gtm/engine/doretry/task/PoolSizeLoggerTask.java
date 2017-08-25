package br.com.globality.gtm.engine.doretry.task;

import org.apache.log4j.Logger;

import br.com.globality.gtm.engine.doretry.executor.DoRetryExecutor;

/**
 * Classe verifica e loga a quantidade de tasks em execução.
 * 
 * @author Leonardo Andrade
 * @version 1.0
 * @since 25/08/2017
 */
public class PoolSizeLoggerTask implements Runnable {

	final static Logger logger = Logger.getLogger(PoolSizeLoggerTask.class);

	@Override
	public void run() {	
		logger.warn("== DoRetryExecutor - Running Tasks: " + DoRetryExecutor.threadCounter.get() + " ==");
	}
	
}

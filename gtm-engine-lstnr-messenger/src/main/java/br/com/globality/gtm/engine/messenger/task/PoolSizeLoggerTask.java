package br.com.globality.gtm.engine.messenger.task;

import org.apache.log4j.Logger;

import br.com.globality.gtm.engine.messenger.executor.MessengerExecutor;

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
		logger.warn("== MessengerExecutor - Running Tasks: " + MessengerExecutor.threadCounter.get() + " ==");
	}
	
}

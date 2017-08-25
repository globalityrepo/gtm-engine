package br.com.globality.gtm.engine.logwriter.task;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ibm.mq.MQQueueManager;

import br.com.globality.gtm.engine.common.exception.BusinessException;
import br.com.globality.gtm.engine.logwriter.executor.LogWriterExecutor;
import br.com.globality.gtm.engine.logwriter.service.LogWriterService;

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
		logger.warn("== LogWriterExecutor - Running Tasks: " + LogWriterExecutor.threadCounter.get() + " ==");
	}
	
}

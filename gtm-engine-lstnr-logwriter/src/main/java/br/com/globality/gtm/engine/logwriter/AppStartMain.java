package br.com.globality.gtm.engine.logwriter;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**Inicializador dos listeners para escuta das filas do GTM.
* @author Leonardo Andrade
* @version 1.0
* @since 22/02/2017
*/
@Component
public class AppStartMain {
	
	final static Logger logger = Logger.getLogger(AppStartMain.class);
		
	@SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) {
		
		// Inicializa o contexto do Spring.
		AnnotationConfigApplicationContext ctx  = new AnnotationConfigApplicationContext(ThreadPoolContextConfig.class);
		
		// Inicializa as threads dos listeners.
		ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) ctx.getBean("runLogWriterExecutor");
	
	}
	
}

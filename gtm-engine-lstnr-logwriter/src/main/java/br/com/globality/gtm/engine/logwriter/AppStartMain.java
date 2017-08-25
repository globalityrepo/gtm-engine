package br.com.globality.gtm.engine.logwriter;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import br.com.globality.gtm.engine.logwriter.executor.LogWriterExecutor;

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
		AnnotationConfigApplicationContext ctx  = new AnnotationConfigApplicationContext(SpringContextInitializer.class);
		
		// Inicializa o job.
		LogWriterExecutor executor = (LogWriterExecutor) ctx.getBean("logWriterExecutor");
		executor.init();
		
	}
	
}

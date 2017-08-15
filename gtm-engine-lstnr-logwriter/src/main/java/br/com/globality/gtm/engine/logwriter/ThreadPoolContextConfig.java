package br.com.globality.gtm.engine.logwriter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = "br.com.globality.gtm.engine")
public class ThreadPoolContextConfig {
	
	@Bean(name="runLogWriterExecutor")
	public ThreadPoolTaskExecutor taskExecutorListener() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(300);
        executor.initialize();
		return executor;
	}
		
}
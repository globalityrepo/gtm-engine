package br.com.globality.gtm.engine.messenger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = "br.com.globality.gtm.engine")
public class ThreadPoolContextConfig {
	
	@Bean(name="runMessengerExecutor")
	public ThreadPoolTaskExecutor taskExecutorListener() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(300);
        executor.initialize();
		return executor;
	}
	
}
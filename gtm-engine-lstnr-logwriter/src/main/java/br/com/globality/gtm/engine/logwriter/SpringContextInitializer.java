package br.com.globality.gtm.engine.logwriter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = "br.com.globality.gtm.engine")
public class SpringContextInitializer {
		
}
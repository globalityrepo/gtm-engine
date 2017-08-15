package br.com.globality.gtm.engine.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 * gtm-engine-listener
 * @author Leonardo Andrade
 * @since 13/03/2017
 */
@Component("messageResource")
public class MessageResource extends ResourceBundleMessageSource {
	
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(MessageResource.class);
	
	public MessageResource() {
		super.setBasename("messages");
		super.setDefaultEncoding("UTF-8");
	}
	
}

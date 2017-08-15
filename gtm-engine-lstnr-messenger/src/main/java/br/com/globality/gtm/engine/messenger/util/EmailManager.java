package br.com.globality.gtm.engine.messenger.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service("emailManager")
@Scope("prototype")
@PropertySource("file:/gtm/config/application.properties")
public class EmailManager {
	 
	final static Logger logger = Logger.getLogger(EmailManager.class);
	
	@Autowired
	private Environment env;
	
	public void sendMail(String emailTo, String subject, String message) throws Exception {

		try {			
			
			Properties props = new Properties();
			props.put("mail.smtp.host", env.getProperty("email.host"));
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", env.getProperty("email.port"));
			props.put("mail.debug", "false");
			props.put("mail.smtp.ssl.enable", "false");
			
			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(env.getProperty("email.username"), env.getProperty("email.password"));
						}
					});
						
			Message msg = new MimeMessage(session);
			
			InternetAddress from = new InternetAddress("GTM - Global Transaction Manager <" + env.getProperty("email.username") + ">");
			msg.setFrom(from);

			InternetAddress[] toAddresses = new InternetAddress[1];
			toAddresses[0] = new InternetAddress(emailTo);
			msg.setRecipients(Message.RecipientType.TO, toAddresses);

			msg.setSubject(MimeUtility.encodeText(subject, "UTF-8", "Q"));
			msg.setContent(message, "text/html; charset=utf-8");
			Transport.send(msg);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		
	}

}

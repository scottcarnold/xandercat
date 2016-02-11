package org.xandercat.ofe.searchutility.result;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.xandercat.ofe.Candidate;

/**
 * ResultDestination that sends search results as an email.
 * 
 * The mail configuration uses password authentication along with the following javax.mail related properties:
 * 
 * <ul><li>mail.smtp.auth</li>
 *     <li>mail.smtp.starttls.enable</li>
 *     <li>mail.smtp.host</li>
 *     <li>mail.smtp.port</li>
 *     <li>mail.to</li>
 *     <li>mail.from</li>
 *     <li>mail.username</li>
 *     <li>mail.password</li></ul>
 * 
 * @author Scott Arnold
 *
 * @param <T>
 */
public class MailResultDestination<T extends Candidate> extends TextMessageResultDestination<T> {

	private static final Logger LOGGER = Logger.getLogger(MailResultDestination.class);
	
	private Properties properties;
	
	@Override
	public void initialize(Properties properties) {
		super.initialize(properties);
		this.properties = properties;	
	}

	@Override
	protected void handleTextMessage(String textMessage) {
		final String username = properties.getProperty("mail.username");
		final String password = properties.getProperty("mail.password");
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(properties.getProperty("mail.from")));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(properties.getProperty("mail.to")));
			message.setSubject(properties.getProperty("mail.subject", getReportTitle()));
			message.setText(textMessage);
			Transport.send(message);
		} catch (MessagingException me) {
			LOGGER.error("Unable to send alert message.", me);
		}
		
	}
}

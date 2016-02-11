package org.xandercat.ofe.searchutility.result;

import org.xandercat.ofe.Candidate;

/**
 * ResultDestination that sends search results to the console as a text message.
 * 
 * @author Scott Arnold
 *
 * @param <T>
 */
public class ConsoleResultDestination<T extends Candidate> extends TextMessageResultDestination<T> {

	@Override
	protected void handleTextMessage(String textMessage) {
		System.out.println(textMessage);		
	}
}

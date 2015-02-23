package com.rulefinancial.jms;

/**
 * Simple interface used to send a message to queue/topic
 * 
 * @author Piotr Gabara
 *
 */
public interface JmsBatchProcessor {
	
	/**
	 * Method used to send a String message to a jms queue/topic
	 * @param message	Message that You want to send to a jms queue/topic
	 */
	void sendMessage(String message);

}

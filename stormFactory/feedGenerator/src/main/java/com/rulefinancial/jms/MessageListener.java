package com.rulefinancial.jms;

import java.util.List;

/**
 * Interface used to consume messages from jms queue/topic 
 * 
 * @author Piotr Gabara
 *
 */
public interface MessageListener {
	
	/**
	 * Method used to consume message
	 * @param message	Message that You want to consume
	 */
	void consumeMessage(String message);
	
	/**
	 * Method used when You want to get all consumed messages
	 * @return	List of messages consumed from a jms queue/topic
	 */
	List<String> getFeeds();

}

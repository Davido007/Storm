package com.rulefinancial.storm.jms.sender;

/**
 * Inteface used to send messages into a jms queue
 * 
 * @author Piotr Gabara
 *
 * @param <T> Type of message
 */
public interface MessageSender<T> {
	
	/**
	 * Method sends message into a jms queue
	 * @param message Message value
	 */
	void sendMessage(T message);
	
}

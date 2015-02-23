package com.rulefinancial.jms;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of MessageListener interface.
 * It is used to consume messages from a jms queue
 * 
 * @author Piotr Gabara
 *
 */
public class MessageListenerImpl implements MessageListener {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static List<String> feeds = new LinkedList<String>();
	
	
	@Override
	public void consumeMessage(String message) {
		logger.info("Message received: " + message);
		feeds.add(message);
	}
	
	public List<String> getFeeds() {
		return feeds;
	}

}

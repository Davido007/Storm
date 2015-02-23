package com.rulefinancial.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

/**
 * Implementation of JmsBatchProcessor interface. 
 * It is used to send text messages into jms queue by using spring jmsTemaple
 * 
 * @author Piotr Gabara
 *
 */
public class JmsBatchProcessorImpl implements JmsBatchProcessor {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	
	@Override
	public void sendMessage(String message) {
		try {
			jmsTemplate.convertAndSend(message);
		} catch(JmsException e) {
			logger.error("context", e);
			logger.error(e.getMessage());
		}
	}

}

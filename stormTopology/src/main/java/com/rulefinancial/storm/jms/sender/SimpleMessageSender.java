package com.rulefinancial.storm.jms.sender;

import java.io.Serializable;

import org.springframework.jms.core.JmsTemplate;

/**
 * Class implements MessageSender interface for String generic type
 * 
 * @author Piotr Gabara
 *
 */
public class SimpleMessageSender implements MessageSender<String>, Serializable {

	private static final long serialVersionUID = -7689719224193849378L;
	
	private JmsTemplate jmsTemplate;
	
	/**
	 * Constructor injects JmsTemplate instance into class
	 * @param jmsTemplate	an instance of JmsTemplate 
	 */
	public SimpleMessageSender(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	
	@Override
	public void sendMessage(String message) {
		jmsTemplate.convertAndSend(message);
	}

}

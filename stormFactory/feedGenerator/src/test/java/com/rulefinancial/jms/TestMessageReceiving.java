package com.rulefinancial.jms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations={"/test-spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestMessageReceiving {

	@Autowired private JmsTemplate jmsTemplate;
	@Autowired private JmsBatchProcessor jmsBatchProcessor;
	@Autowired private MessageListener messageListener;
	
	@Before
	public void init() {
		Assert.assertNotNull(jmsBatchProcessor);
		Assert.assertNotNull(messageListener);
		Assert.assertNotNull(jmsTemplate);
	}
	
	@Test
	public void testSynchronizedReceiving() throws InterruptedException {
		String messageToSend = "com.rulefinancial.message";
		jmsBatchProcessor.sendMessage(messageToSend);
		
		Thread.sleep(200);
		
		String receivedMessage = messageListener.getFeeds().get(0); 
		Assert.assertEquals(messageToSend, receivedMessage);
	}
}

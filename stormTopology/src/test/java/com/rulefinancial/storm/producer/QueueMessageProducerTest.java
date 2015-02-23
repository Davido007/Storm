package com.rulefinancial.storm.producer;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class QueueMessageProducerTest {
  
	private QueueMessageProducer producer;
  
	@Before
	public void init() {
		this.producer = new QueueMessageProducer();
	}
  
	
	@Test
	public void producerShouldConvertJmsMessageToValuesObject() throws JMSException {
		TextMessage message = Mockito.mock(TextMessage.class);
		Mockito.when(message.getText()).thenReturn("Test message text");
    
		Values actual = this.producer.toTuple(message);
		String txtMessage = (String)actual.get(0);
    
		Assert.assertEquals("Test message text", txtMessage);
	}
  
	@Test
	public void producerShouldReturnEmptyStringWhenTheTypeOfMessageIsNotATextMessage() throws JMSException {
		ObjectMessage message = Mockito.mock(ObjectMessage.class);
    
		Values actual = this.producer.toTuple(message);
		String txtMessage = (String)actual.get(0);
    
		Assert.assertEquals("", txtMessage);
	}
  
	@Test
	public void producerShouldDeclareFieldsInDeclareOutputFieldsMethod() {
		OutputFieldsDeclarer declarer = Mockito.mock(OutputFieldsDeclarer.class);
		this.producer.declareOutputFields(declarer);
		Mockito.verify(declarer, Mockito.times(1)).declare(Mockito.any(Fields.class));
	}
	
}

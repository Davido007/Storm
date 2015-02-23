package com.rulefinancial.jms.receiver;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.rulefinancial.entity.Indicator;


public class MessageReceiverTest {
	
	private MessageReceiver messageReceiver;
	
	@Before
	public void init() {
		messageReceiver = new MessageReceiver();
	}
	
	
	@Test
	public void onMessageMethodShouldInvokegetTextMethodOfMessageObjectIfMessageIsCorrect() throws JMSException {
		TextMessage textMessage = Mockito.mock(TextMessage.class);
		Mockito.when(textMessage.getText()).thenReturn("{\"symbol\":\"TEST.JU\",\"value\":1.00,\"indicatorType\":\"SIMPLE_MOVING_AVERAGE\",\"published\":null}");
		
		messageReceiver.onMessage(textMessage);
		Mockito.verify(textMessage, Mockito.times(1)).getText();
	}
	
	@Test
	public void onMessageMethodShouldStoreConvertedJsonObjectsInSMAMapLasValues() throws JMSException {
		TextMessage textMessage = Mockito.mock(TextMessage.class);
		Mockito.when(textMessage.getText()).thenReturn("{\"symbol\":\"TEST.JU\",\"value\":1.00,\"indicatorType\":\"SIMPLE_MOVING_AVERAGE\",\"published\":null}");
		messageReceiver.onMessage(textMessage);
		Indicator result = messageReceiver.getLastSMAValue("TEST.JU");
		
		Assert.assertEquals(1.00, result.getValue(), 0.01);
	}
	
	@Test
	public void onMessageMethodShouldStoreConvertedJsonObjectsInEMAMapLasValues() throws JMSException {
		TextMessage textMessage = Mockito.mock(TextMessage.class);
		Mockito.when(textMessage.getText()).thenReturn("{\"symbol\":\"TEST.JU\",\"value\":1.00,\"indicatorType\":\"EXPONENTIAL_MOVING_AVERAGE\",\"published\":null}");
		messageReceiver.onMessage(textMessage);
		Indicator result = messageReceiver.getLastEMAValue("TEST.JU");
		
		Assert.assertEquals(1.00, result.getValue(), 0.01);
	}
	
	@Test
	public void getLastValueMethodShouldReturnNullIfSymboldoesntExistInMap() {
		Indicator resultSMA = messageReceiver.getLastSMAValue("NULL");
		Indicator resultEMA = messageReceiver.getLastEMAValue("NULL");
		
		Assert.assertNull(resultSMA);
		Assert.assertNull(resultEMA);
	}
	 
}

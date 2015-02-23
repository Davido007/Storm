package com.rulefinancial.storm.bolt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.rulefinancial.storm.entity.Indicator;
import com.rulefinancial.storm.entity.IndicatorType;

import backtype.storm.task.OutputCollector;
import backtype.storm.tuple.Tuple;


@ContextConfiguration(locations={"/test-app-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ConvertAndSendIndicatorBoltTest {

	@Autowired JmsTemplate jmsTemplate;
	
	private ConvertAndSendIndicatorBolt convertAndSendIndicatorBolt;
	private Indicator testIndicator;
	private OutputCollector collector;
	private Tuple tuple;
	
	private Gson gsonConverter;
	
	@Before
	public void init() {
		convertAndSendIndicatorBolt = new ConvertAndSendIndicatorBolt("test-app-context.xml");
		
		testIndicator = new Indicator();
		testIndicator.setIndicatorType(IndicatorType.SIMPLE_MOVING_AVERAGE);
		testIndicator.setSymbol("TEST.JU");
		testIndicator.setValue(1.00);
		
		collector = Mockito.mock(OutputCollector.class);
		
		tuple = Mockito.mock(Tuple.class);
		
		gsonConverter = new Gson();
	}
	
	
	@Test
	public void executeMethodShouldAcksAfterAllProcessIsDone() {
		Mockito.when(tuple.getValueByField("indicator")).thenReturn(testIndicator);
		
		convertAndSendIndicatorBolt.prepare(null, null, collector);
		convertAndSendIndicatorBolt.execute(tuple);
		
		Mockito.verify(collector, Mockito.times(1)).ack(tuple);
	}
	
	@Test
	public void executeMethodShouldSendsCorrectMessageIntoQueue() {
		Mockito.when(tuple.getValueByField("indicator")).thenReturn(testIndicator);
		
		convertAndSendIndicatorBolt.prepare(null, null, collector);
		convertAndSendIndicatorBolt.execute(tuple);
		
		String json =  (String) jmsTemplate.receiveAndConvert();
		String pattern = gsonConverter.toJson(testIndicator);
		
		Assert.assertEquals(pattern, json);
	}
	
}

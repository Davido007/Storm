package com.rulefinancial.storm.bolt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import backtype.storm.task.OutputCollector;
import backtype.storm.tuple.Tuple;

import com.rulefinancial.storm.entity.Feed;

public class CalaculateEMAIndicatorBoltTest {
	private CalculateEMAIndicatorBolt calculateEMAIndicatorBolt;
	  
	@Before
	public void init() {
		this.calculateEMAIndicatorBolt = new CalculateEMAIndicatorBolt();
	}
	@Test
	public void inExecuteMethodTheCollectorMethodAckShouldBeInvoked() {
		OutputCollector collector = Mockito.mock(OutputCollector.class);
		Tuple tuple = Mockito.mock(Tuple.class);
    
		Mockito.when(tuple.getValueByField("feed")).thenReturn(new Feed());
    
		this.calculateEMAIndicatorBolt.prepare(null, null, collector);
		this.calculateEMAIndicatorBolt.execute(tuple);
    
		Mockito.verify(collector, Mockito.times(1)).ack(tuple);
	}
	@Test
	public void executeMethodShouldCreateInstrumentInInstruments() {
		OutputCollector collector = Mockito.mock(OutputCollector.class);
		Tuple tuple = Mockito.mock(Tuple.class);
		Assert.assertNull(calculateEMAIndicatorBolt.instruments.get("TEST.JU"));
		Feed feed = new Feed();
		feed.setStockClose(10.0);
		feed.setSymbol("TEST.JU");
		Mockito.when(tuple.getValueByField("feed")).thenReturn(feed);
		this.calculateEMAIndicatorBolt.prepare(null, null, collector);
		this.calculateEMAIndicatorBolt.execute(tuple);
		Assert.assertNotNull(calculateEMAIndicatorBolt.instruments.get("TEST.JU"));
	}
	@Test
	public void executeMethodShouldAddValueToInstrument() {
		OutputCollector collector = Mockito.mock(OutputCollector.class);
		Tuple tuple = Mockito.mock(Tuple.class);
		Feed feed = new Feed();
		feed.setStockClose(10.0);
		feed.setSymbol("TEST.JU");
		Mockito.when(tuple.getValueByField("feed")).thenReturn(feed);
		this.calculateEMAIndicatorBolt.prepare(null, null, collector);
		this.calculateEMAIndicatorBolt.execute(tuple);
		Assert.assertEquals(2, calculateEMAIndicatorBolt.instruments.get("TEST.JU").size());
		Feed feed1 = new Feed();
		feed1.setStockClose(10.0);
		feed1.setSymbol("TEST.JU");
		Mockito.when(tuple.getValueByField("feed")).thenReturn(feed1);
		this.calculateEMAIndicatorBolt.prepare(null, null, collector);
		this.calculateEMAIndicatorBolt.execute(tuple);
		Assert.assertEquals(3, calculateEMAIndicatorBolt.instruments.get("TEST.JU").size());
	}
	@Test
	public void executeMethodShouldCalculateFirstValueOfIndicator() {
		OutputCollector collector = Mockito.mock(OutputCollector.class);
		Tuple tuple = Mockito.mock(Tuple.class);
		for(int i=0;i<5;i++){
			Feed feed = new Feed();
			feed.setStockClose(Double.valueOf(i*10));
			feed.setSymbol("TEST.JU");
			Mockito.when(tuple.getValueByField("feed")).thenReturn(feed);
			this.calculateEMAIndicatorBolt.prepare(null, null, collector);
			this.calculateEMAIndicatorBolt.execute(tuple);			
		} 
		double temp = 0;
		for(int i=0;i<5;i++){
			temp+=Double.valueOf(i*10);			
		}
		double expected=temp/5;
		Assert.assertNotNull(calculateEMAIndicatorBolt.ema);
		Assert.assertEquals(expected, calculateEMAIndicatorBolt.ema.getValue(),0.01);
	}
	@Test
	public void executeMethodShouldCalculateSecondValueOfIndicator() {
		OutputCollector collector = Mockito.mock(OutputCollector.class);
		Tuple tuple = Mockito.mock(Tuple.class);
		for(int i=0;i<6;i++){
			Feed feed = new Feed();
			feed.setStockClose(Double.valueOf(i*10));
			feed.setSymbol("TEST.JU");
			Mockito.when(tuple.getValueByField("feed")).thenReturn(feed);
			this.calculateEMAIndicatorBolt.prepare(null, null, collector);
			this.calculateEMAIndicatorBolt.execute(tuple);
		}
		double temp = 0;
		for(int i=0;i<6;i++){
			temp+=Double.valueOf(i*10);			
		}
		double expected=temp/5;
		Assert.assertNotNull(calculateEMAIndicatorBolt.ema);
		Assert.assertEquals(expected, calculateEMAIndicatorBolt.ema.getValue(),0.01);
	}
}

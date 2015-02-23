package com.rulefinancial.storm.bolt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import backtype.storm.task.OutputCollector;
import backtype.storm.tuple.Tuple;

import com.rulefinancial.storm.entity.Feed;

public class CalaculateIndicatorBoltTest {
	private CalculateIndicatorBolt calculateIndicatorBolt;
	  
	@Before
	public void init() {
		this.calculateIndicatorBolt = new CalculateIndicatorBolt();
	}
	@Test
	public void inExecuteMethodTheCollectorMethodAckShouldBeInvoked() {
		OutputCollector collector = Mockito.mock(OutputCollector.class);
		Tuple tuple = Mockito.mock(Tuple.class);
    
		Mockito.when(tuple.getValueByField("feed")).thenReturn(new Feed());
    
		this.calculateIndicatorBolt.prepare(null, null, collector);
		this.calculateIndicatorBolt.execute(tuple);
    
		Mockito.verify(collector, Mockito.times(1)).ack(tuple);
	}
	@Test
	public void executeMethodShouldCreateInstrumentInInstruments() {
		OutputCollector collector = Mockito.mock(OutputCollector.class);
		Tuple tuple = Mockito.mock(Tuple.class);
		Assert.assertNull(calculateIndicatorBolt.instruments.get("TEST.JU"));
		Feed feed = new Feed();
		feed.setStockClose(10.0);
		feed.setSymbol("TEST.JU");
		Mockito.when(tuple.getValueByField("feed")).thenReturn(feed);
		this.calculateIndicatorBolt.prepare(null, null, collector);
		this.calculateIndicatorBolt.execute(tuple);
		Assert.assertNotNull(calculateIndicatorBolt.instruments.get("TEST.JU"));
	}
	@Test
	public void executeMethodShouldAddValueToInstrument() {
		OutputCollector collector = Mockito.mock(OutputCollector.class);
		Tuple tuple = Mockito.mock(Tuple.class);
		Feed feed = new Feed();
		feed.setStockClose(10.0);
		feed.setSymbol("TEST.JU");
		Mockito.when(tuple.getValueByField("feed")).thenReturn(feed);
		this.calculateIndicatorBolt.prepare(null, null, collector);
		this.calculateIndicatorBolt.execute(tuple);
		Assert.assertEquals(1, calculateIndicatorBolt.instruments.get("TEST.JU").size());
		Feed feed1 = new Feed();
		feed1.setStockClose(10.0);
		feed1.setSymbol("TEST.JU");
		Mockito.when(tuple.getValueByField("feed")).thenReturn(feed1);
		this.calculateIndicatorBolt.prepare(null, null, collector);
		this.calculateIndicatorBolt.execute(tuple);
		Assert.assertEquals(2, calculateIndicatorBolt.instruments.get("TEST.JU").size());
	}
	@Test
	public void executeMethodShouldCalculateIndicator() {
		OutputCollector collector = Mockito.mock(OutputCollector.class);
		Tuple tuple = Mockito.mock(Tuple.class);
		for(int i=0;i<5;i++){
			Feed feed = new Feed();
			feed.setStockClose(Double.valueOf(i*10));
			feed.setSymbol("TEST.JU");
			Mockito.when(tuple.getValueByField("feed")).thenReturn(feed);
			this.calculateIndicatorBolt.prepare(null, null, collector);
			this.calculateIndicatorBolt.execute(tuple);			
		}
		double temp = 0;
		for(int i=0;i<5;i++){
			temp+=Double.valueOf(i*10);			
		}
		double expected=temp/5;
		Assert.assertNotNull(calculateIndicatorBolt.sma);
		Assert.assertEquals(expected, calculateIndicatorBolt.sma.getValue(),0.01);
	}
}

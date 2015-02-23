package com.rulefinancial.storm.bolt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import backtype.storm.task.OutputCollector;
import backtype.storm.tuple.Tuple;

import com.rulefinancial.storm.entity.Feed;

public class FeedPrinterBoltTest {
  
	private FeedPrinterBolt printerBolt;
  
	@Before
	public void init() {
		this.printerBolt = new FeedPrinterBolt();
	}
  
	@Test
	public void inExecuteMethodTheCollectorMethodAckShouldBeInvoked() {
		OutputCollector collector = Mockito.mock(OutputCollector.class);
		Tuple tuple = Mockito.mock(Tuple.class);
    
		Mockito.when(tuple.getValueByField("feed")).thenReturn(new Feed());
    
		this.printerBolt.prepare(null, null, collector);
		this.printerBolt.execute(tuple);
    
		Mockito.verify(collector, Mockito.times(1)).ack(tuple);
	}
  
	@Test
	public void executeMethodShouldPrepareCorrectFeedObject() {
		OutputCollector collector = Mockito.mock(OutputCollector.class);
		Tuple tuple = Mockito.mock(Tuple.class);
    
		Feed feed = new Feed();
		feed.setKeyColumn(1);
		feed.setSymbol("TEST.JU");
		Mockito.when(tuple.getValueByField("feed")).thenReturn(feed);
    
		this.printerBolt.prepare(null, null, collector);
		this.printerBolt.execute(tuple);
    
		Feed fromTuple = this.printerBolt.getLastFeedFromTuple();
    
		Assert.assertNotNull(fromTuple);
		Assert.assertEquals(1L, fromTuple.getKeyColumn());
		Assert.assertEquals("TEST.JU", fromTuple.getSymbol());
	}
	
}

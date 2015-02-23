package com.rulefinancial.storm.bolt;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import backtype.storm.task.OutputCollector;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.rulefinancial.storm.converter.Converter;
import com.rulefinancial.storm.entity.Feed;

public class FeedConverterBoltTest {
  
	@Test
	public void boltShouldInvokeEmitAndThenAckMethodInsideTheExecuteMethod() {
    
		@SuppressWarnings("unchecked")
		Converter<Feed> converter = Mockito.mock(Converter.class);
		Mockito.when(converter.convertJsonTo(Mockito.anyString())).thenReturn(new Feed());
    
		OutputCollector collector = (OutputCollector)Mockito.mock(OutputCollector.class);
		Tuple tuple = (Tuple)Mockito.mock(Tuple.class);
    
		FeedConverterBolt converterBolt = new FeedConverterBolt(converter);
		converterBolt.prepare(null, null, collector);
    
		converterBolt.execute(tuple);
    
		Mockito.verify(collector, Mockito.times(1)).ack(tuple);
		Mockito.verify(collector, Mockito.times(1)).emit(Mockito.any(Values.class));
		
	}
  
	@Test
	public void boltShouldReadStringFromTupleCorrectly() {
		
		@SuppressWarnings("unchecked")
		Converter<Feed> converter = Mockito.mock(Converter.class);
		Mockito.when(converter.convertJsonTo(Mockito.anyString())).thenReturn(new Feed());
    
		OutputCollector collector = (OutputCollector)Mockito.mock(OutputCollector.class);
    
		Tuple tuple = (Tuple)Mockito.mock(Tuple.class);
		Mockito.when(tuple.getStringByField("message")).thenReturn("test text message");
    
		FeedConverterBolt converterBolt = new FeedConverterBolt(converter);
		converterBolt.prepare(null, null, collector);
    
		converterBolt.execute(tuple);
		Assert.assertEquals("test text message", converterBolt.getLastJsonMessage());
	}
	
}

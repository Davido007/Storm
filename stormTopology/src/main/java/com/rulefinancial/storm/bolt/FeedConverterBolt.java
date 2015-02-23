package com.rulefinancial.storm.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.rulefinancial.storm.converter.Converter;
import com.rulefinancial.storm.entity.Feed;

/**
 * Storm bolt gets jsons from a jms queue, converts to Feed objects and emit forward 
 * 
 * @author Piotr Gabara
 *
 */
public class FeedConverterBolt extends BaseRichBolt {
	
	private static final long serialVersionUID = 32902497284427221L;
	
	private OutputCollector collector;
	private Converter<Feed> converter;
	private String lastJsonMessage;
  
	/**
	 * Constructor injects into the bolt Converter interface implementation
	 * @param converter	Converter interface implementation
	 */
	public FeedConverterBolt(Converter<Feed> converter) {
		this.converter = converter;
	}
	
	/**
	 * Method injects OutputCollector instance into the bolt 
	 */
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}
  
	
	/**
	 * Method gets json frome the tuple, converts it and emit forward
	 */
	public void execute(Tuple input) {
		this.lastJsonMessage = input.getStringByField("message");
    
		Feed current = (Feed)this.converter.convertJsonTo(this.lastJsonMessage);
		this.collector.emit(new Values(current.getSymbol(), current));
    
		this.collector.ack(input);
	}
  
	/**
	 * Method delares two fields: symbol and feed
	 */
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("symbol", "feed"));
	}
  
	/**
	 * Method returns last received json message
	 * @return	Last json message
	 */
	public String getLastJsonMessage() {
		return this.lastJsonMessage;
	}
	
}
package com.rulefinancial.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import com.rulefinancial.storm.entity.Feed;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Storm bolt used to log some information about Feeds
 * 
 * @author Piotr Gabara
 *
 */
public class FeedPrinterBolt extends BaseRichBolt {
	
	private static final long serialVersionUID = -2746926539255468944L;
	private static final Logger LOG = LoggerFactory.getLogger(FeedPrinterBolt.class);
  
	private OutputCollector collector;
	private Feed lastFeed;
  
	
	/**
	 * Method injects OutputCollector into the bolt instance
	 */
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}
  
	/**
	 * Method gets feed from a tuple and logs information about this feed 
	 */
	public void execute(Tuple input) {
		this.lastFeed = ((Feed)input.getValueByField("feed"));
    
		LOG.info("JMS MESSAGE CONVERTED TO FEED OBJECT: " + this.lastFeed.toString());
    
		this.collector.ack(input);
	}
  
	/**
	 * Method is useless, nothing more is emitted
	 */
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// method is useless for this topology
	}
  
	/**
	 * Method gets last received feed and returns it
	 * @return	Last received feed
	 */
	public Feed getLastFeedFromTuple() {
		return this.lastFeed;
	}
	
}

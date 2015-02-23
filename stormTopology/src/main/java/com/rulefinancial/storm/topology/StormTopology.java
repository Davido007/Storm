package com.rulefinancial.storm.topology;
  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.contrib.jms.JmsProvider;
import backtype.storm.contrib.jms.JmsTupleProducer;
import backtype.storm.contrib.jms.spout.JmsSpout;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

import com.rulefinancial.storm.bolt.CalculateEMAIndicatorBolt;
import com.rulefinancial.storm.bolt.CalculateIndicatorBolt;
import com.rulefinancial.storm.bolt.ConvertAndSendIndicatorBolt;
import com.rulefinancial.storm.bolt.FeedConverterBolt;
import com.rulefinancial.storm.bolt.FeedPrinterBolt;
import com.rulefinancial.storm.converter.Converter;
import com.rulefinancial.storm.converter.JsonToFeedConverter;
import com.rulefinancial.storm.entity.Feed;
import com.rulefinancial.storm.jms.provider.SpringJmsProvider;
import com.rulefinancial.storm.producer.QueueMessageProducer;
   

/**
 * Class contains whole storm topology configuration
 * 
 * @author Dawid Plichta, Piotr Gabara
 *
 */
public class StormTopology {
  
	private static final Logger LOG = LoggerFactory.getLogger(StormTopology.class);
	  
	private static final String REMOVE_TOPOLOGY = "REMOVE_DATA";
	private static final String TOPOLOGY_NAME = "STORM_TOPOLOGY";
	private static final String SPOUT_NAME = "JMS_QUEUE_SPOUT";
	private static final String CONVERTER_BOLT_NAME = "CONVERTER_BOLT";
	private static final String FEED_PRINTER_BOLT_NAME = "FEED_PRINTER_BOLT";
	private static final String CALCULATE_SMA_INDICATOR_BOLT = "CALCULATE_SMA_INDICATOR_BOLT";
	private static final String CALCULATE_EMA_INDICATOR_BOLT = "CALCULATE_EMA_INDICATOR_BOLT";
	private static final String CONVERT_AND_SEND_INDICATOR_BOLT_NAME = "CONVERT_AND_SEND_INDICATOR";
	private static final String CONVERT_AND_SEND_EMA_INDICATOR_BOLT_NAME = "CONVERT_AND_SEND_EMA_INDICATOR";
	
	private static final String SPRING_CONTEXT_PATH = "app-context.xml";
      
	private StormTopology() {
	}
	 
	/**
	 * Method contains storm topology configuration
	 * 
	 * @param args	Typical attribute for java main static method
	 */
	public static void main(String[] args) {
		
		TopologyBuilder builder = new TopologyBuilder();
		TopologyBuilder builder1 = new TopologyBuilder();
		JmsProvider provider = new SpringJmsProvider(SPRING_CONTEXT_PATH, "connectionFactory", "destination");
		JmsTupleProducer producer = new QueueMessageProducer();
        
              
       
		JmsSpout spout = new JmsSpout();
		spout.setJmsProvider(provider);
		spout.setJmsTupleProducer(producer);
		spout.setJmsAcknowledgeMode(1);
		spout.setDistributed(true);
		
		builder.setSpout(SPOUT_NAME, spout, 1);
		builder1.setSpout(SPOUT_NAME, spout, 1);
		Converter<Feed> feedConverter = new JsonToFeedConverter();
		BaseRichBolt converterBolt = new FeedConverterBolt(feedConverter);
		BaseRichBolt convertAndSendIndicatorBolt = new ConvertAndSendIndicatorBolt(SPRING_CONTEXT_PATH);
		                 
		builder1.setBolt(CONVERTER_BOLT_NAME, converterBolt, 1).shuffleGrouping(SPOUT_NAME);
		builder.setBolt(CONVERTER_BOLT_NAME, converterBolt, 1).shuffleGrouping(SPOUT_NAME);
		builder.setBolt(FEED_PRINTER_BOLT_NAME, new FeedPrinterBolt(), 1).shuffleGrouping(CONVERTER_BOLT_NAME);
	 	builder.setBolt(CALCULATE_SMA_INDICATOR_BOLT, new CalculateIndicatorBolt(), 1).fieldsGrouping(CONVERTER_BOLT_NAME, new Fields("symbol"));
		builder.setBolt(CALCULATE_EMA_INDICATOR_BOLT, new CalculateEMAIndicatorBolt(), 1).fieldsGrouping(CONVERTER_BOLT_NAME, new Fields("symbol"));
		builder.setBolt(CONVERT_AND_SEND_INDICATOR_BOLT_NAME, convertAndSendIndicatorBolt, 1).shuffleGrouping(CALCULATE_SMA_INDICATOR_BOLT);
		builder.setBolt(CONVERT_AND_SEND_EMA_INDICATOR_BOLT_NAME, convertAndSendIndicatorBolt, 1).shuffleGrouping(CALCULATE_EMA_INDICATOR_BOLT);
		Config config = new Config();
		LocalCluster cluster = new LocalCluster();
		
		config.setDebug(true);
		config.setMaxTaskParallelism(3);		 
    	cluster.submitTopology(REMOVE_TOPOLOGY , config, builder1.createTopology());
    	Utils.sleep(10000L);
    	cluster.killTopology(REMOVE_TOPOLOGY ); 
                 
	        
		if(args.length > 0) { 
			   
			config.setDebug(false);
			config.setNumWorkers(3);
			try {
				StormSubmitter.submitTopology(args[0], config, builder.createTopology());
			} catch (AlreadyAliveException | InvalidTopologyException e) {
				LOG.error("The same topology: '" + args[0] + "' already run or there are other technical problems", e);
			}
		 
		} else {		
			config.setDebug(true);
			config.setMaxTaskParallelism(3);
			
	    	cluster.submitTopology(TOPOLOGY_NAME, config, builder.createTopology());
	    	while(true){}
		}
		
	}
	
}

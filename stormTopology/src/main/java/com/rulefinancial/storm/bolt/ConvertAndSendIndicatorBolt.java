package com.rulefinancial.storm.bolt;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;
import com.rulefinancial.storm.entity.Indicator;
import com.rulefinancial.storm.jms.sender.MessageSender;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;


/**
 * Storm bold, which convert Indicator into a json representation and sends it to a jms queue
 * 
 * @author Piotr Gabara
 *
 */
public class ConvertAndSendIndicatorBolt extends BaseRichBolt {

	private static final Logger LOG = LoggerFactory.getLogger(ConvertAndSendIndicatorBolt.class);
	private static final long serialVersionUID = -4683299231914523946L;

	private OutputCollector collector;
	private String springContextPath;
	
	
	/**
	 * Constructor injects path to xml spring configuration file 
	 * @param springContextPath Path to spring xml context file
	 */
	public ConvertAndSendIndicatorBolt(String springContextPath) {
		this.springContextPath = springContextPath;
	}
	
	/**
	 * Method injects to the bolt OutputCollector instance 
	 */
	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	/**
	 * Methods gets indicator object from a tuple, converts it to a json and sends it into a jms queue
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Tuple input) {
		Indicator indicator = (Indicator) input.getValueByField("indicator");
		
		Gson gson = new Gson();
		String json = gson.toJson(indicator);
		
		ApplicationContext context = new ClassPathXmlApplicationContext(springContextPath);
		MessageSender<String> messageSender = (MessageSender<String>) context.getBean("messageSender");
		((ConfigurableApplicationContext)context).close();
		
		LOG.info("SEND JSON INDICsATOR TO QUEUE  |  JSON: " + json);
		messageSender.sendMessage(json);
		collector.ack(input);
	}

	/**
	 * Method is useless, nothing more is emitted 
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// method is useless for this topology
	}

}

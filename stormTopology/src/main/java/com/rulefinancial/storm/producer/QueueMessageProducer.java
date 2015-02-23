package com.rulefinancial.storm.producer;

import backtype.storm.contrib.jms.JmsTupleProducer;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;


/**
 * Simple Implementation of JmsTupleProducer.
 * Class produces Values from jms messages
 * 
 * @author Piotr Gabara
 *
 */
public class QueueMessageProducer implements JmsTupleProducer {
	
	private static final long serialVersionUID = -8652907334725709059L;
  
	
	/**
	 * Method declares new field "message"
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("message"));
	}
  
	/**
	 * Method gets message from jms queue and returns it via a Values object
	 */
	@Override
	public Values toTuple(Message message) throws JMSException {
		String value = "";
		
		if (message instanceof TextMessage) {
			value = ((TextMessage)message).getText();
		}
		
		return new Values(value);
	}
	
}

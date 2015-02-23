package com.rulefinancial.jms.receiver;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.rulefinancial.entity.Indicator;
import com.rulefinancial.entity.IndicatorType;


/**
 * Simple implementation of MessageListener interface.
 * Class gets messages from a queue, converts them and saves into specific map
 * 
 * @author Piotr Gabara, Dawid Plichta
 *
 */
public class MessageReceiver implements MessageListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiver.class);
	
	private Map<String, Indicator> lastSMAValues;
	private Map<String, Indicator> lastEMAValues;
	
	/**
	 * Simple constructor, which set hash maps for lastSMAValues and lastEMAValues maps
	 */
	public MessageReceiver() {
		lastSMAValues = new HashMap<String, Indicator>();
		lastEMAValues = new HashMap<String, Indicator>();
	}
	
	@Override
	public void onMessage(Message message) {
		
		String text = "";
		Indicator i = null;
		
		try {
			text = ((TextMessage) message).getText();
			Gson gson = new Gson();
			i = gson.fromJson(text, Indicator.class);
			
			i.setValue( Math.round( i.getValue() * 100 ) / 100.0d );
			
			if(i.getIndicatorType()==IndicatorType.SIMPLE_MOVING_AVERAGE){
				lastSMAValues.put(i.getSymbol(), i);
			}
			
			if(i.getIndicatorType()==IndicatorType.EXPONENTIAL_MOVING_AVERAGE){
				lastEMAValues.put(i.getSymbol(), i);
			}
			
		} catch (JMSException e) {
			LOGGER.error("Cannot read body message from jms message", e);
		}
		
		LOGGER.info("New message received:  " + i.getSymbol());
		
	}
	
	
	/**
	 * Method gets last SMA indicator for the specific instrument
	 * 
	 * @param symbol	Instrument name
	 * @return	SMA Indicator object or null if map does not contain indicator for specific instrument
	 */
	public Indicator getLastSMAValue(String symbol) {
		if(lastSMAValues.containsKey(symbol)) {
			return lastSMAValues.get(symbol);
		}
		
		return null;
	}
	
	/**
	 * Method gets last EMA indicator for the specific instrument
	 * 
	 * @param symbol	Instrument name
	 * @return	EMA Indicator object or null if map does not contain indicator for specific instrument
	 */
	public Indicator getLastEMAValue(String symbol) {
		if(lastEMAValues.containsKey(symbol)) {
			return lastEMAValues.get(symbol);
		}
		
		return null;
	}

}

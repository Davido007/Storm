package com.rulefinancial.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rulefinancial.entity.Indicator;
import com.rulefinancial.jms.receiver.MessageReceiver;

/**
 * Class represents RESTful web service, allows to get last indicators objects for specific instruments
 * 
 * @author Dawid Plichta, Piotr Gabara
 *
 */
@Controller
public class StormController {
	
	@Autowired
	private MessageReceiver messageReceiver;
	
	@Value("${instruments.list}")
	private String[] instruments;
	
	
	/**
	 * Method returns to a client last calculated sma indicator
	 * Request method: GET
	 * Relative path: /simple-moving-average
	 * 
	 * @param symbol	Request param, which represent instrument symbol
	 * @return	Last calculated ema indicator
	 */
	@RequestMapping(value="simple-moving-average", method=RequestMethod.GET)
	@ResponseBody
	public Indicator getSimpleMovingAverageIndicator(@RequestParam(value = "symbol") String symbol){
		return messageReceiver.getLastSMAValue(symbol);	  
	}
	
	
	/**
	 * Method returns to a client last calculated ema indicator
	 * Request method: GET
	 * Relative path: /exponential-moving-average
	 * 
	 * @param symbol	Request param, which represent instrument symbol
	 * @return	Last calculated ema indicator  
	 */
	@RequestMapping(value="exponential-moving-average", method=RequestMethod.GET)
	@ResponseBody
	public Indicator getExponentialMovingAverageIndicator(@RequestParam(value = "symbol") String symbol){
		return messageReceiver.getLastEMAValue(symbol);	  
	}
	
	
	/**
	 * Method returns to a client list of available instruments
	 * Request method: GET
	 * Relative path: /instruments
	 * 
	 * @return	List of available instruments
	 */
	@RequestMapping(value="instruments", method=RequestMethod.GET)
	@ResponseBody
	public List<String> getSupportedInstrumentsList() {
		return Arrays.asList(instruments);
	}
	
	
	
	public void setBox(MessageReceiver ms) {	 
		this.messageReceiver = ms;
	}
	
	public void setInstruments(String... instruments) {
		this.instruments = instruments;
	}
}

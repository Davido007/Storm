package com.rulefinancial.storm.bolt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.rulefinancial.storm.entity.Feed;
import com.rulefinancial.storm.entity.Indicator;
import com.rulefinancial.storm.entity.IndicatorType;

public class CalculateEMAIndicatorBolt extends BaseRichBolt {
	double k=2d/6d;
	private static final long serialVersionUID = -2746926539255468944L;
	Map<String, ArrayList<Double>> instruments=new HashMap<String,ArrayList<Double>>();
	private OutputCollector collector;
	private Feed lastFeed;
	Indicator ema = null;
	 
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}
	
	public void execute(Tuple input) {
		// get lastFeed from queue and put its symbol to instrumentSymbol value and its stock close to stockClose value 
		this.lastFeed = ((Feed)input.getValueByField("feed"));
		String instrumentSymbol=this.lastFeed.getSymbol();
		double stockClose=this.lastFeed.getStockClose();
		// check if exists array with key (instrumentSymbol) in instruments map
		// if not create array, add to this array first ema value (0.0) and stock close of lastFeed and put array with key to instruments map
		if(instruments.get(instrumentSymbol)==null){
			ArrayList<Double> instrument = new ArrayList<Double>();
			instrument.add(0.0);
			instrument.add(stockClose);
			instruments.put(instrumentSymbol, instrument);
		}
		// if array exist put its object from index 0 to emaValue value and add stock close of lastFeed to array with key (Symbol) 
		else {
			double emaValue=instruments.get(instrumentSymbol).get(0);
			instruments.get(instrumentSymbol).add(stockClose);
			//if array size > 5 calculate EMA 
			if(instruments.get(instrumentSymbol).size()>5){
				// if ema = 0.0 it means that class should calculate first ema value ema=avg of values 
				if(emaValue==0.0){
					double temp=0.0;
					for(int i=1;i<6;i++){
						temp+=instruments.get(instrumentSymbol).get(i);				
					}					
					instruments.get(instrumentSymbol).set(0, temp/5);
					emaValue=instruments.get(instrumentSymbol).get(0);
				} 
				// if ema != 0.0 calculate ema from formula ema = (actualPrice*k)+(emaValue*(1-k))
				else {
					instruments.get(instrumentSymbol).set(0,(
					instruments.get(instrumentSymbol).get(5)*k)+(emaValue*(1-k)));
					emaValue=instruments.get(instrumentSymbol).get(0);
					
				}
				ema = new Indicator();
				ema.setSymbol(lastFeed.getSymbol());
				ema.setValue(emaValue);
				ema.setIndicatorType(IndicatorType.EXPONENTIAL_MOVING_AVERAGE);
				ema.setPublished(lastFeed.getDate());
				ema.setTime(lastFeed.getTime());
				this.collector.emit(new Values(ema));
				
				
				for(int i=2;i<instruments.get(instrumentSymbol).size();i++){
					instruments.get(instrumentSymbol).set(i-1, instruments.get(instrumentSymbol).get(i));			
				}
				//delete actual price
				instruments.get(instrumentSymbol).remove(5);
				}
			
			}
		
		
		this.collector.ack(input);
		
	}
  
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("indicator"));
	}
  
	public Feed getLastFeedFromTuple() {
		return this.lastFeed;
	}
	
}

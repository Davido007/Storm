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

public class CalculateIndicatorBolt extends BaseRichBolt {
	double simpleMovingAverageResult=0;
	private static final long serialVersionUID = -2746926539255468944L;
	Map<String, ArrayList<Double>> instruments=new HashMap<String,ArrayList<Double>>();
	private OutputCollector collector;
	private Feed lastFeed;
	Indicator sma = null;
	 
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}
  
	public void execute(Tuple input) {
		
		this.lastFeed = ((Feed)input.getValueByField("feed"));
		if(instruments.get(this.lastFeed.getSymbol())==null){
			ArrayList<Double> instrument = new ArrayList<Double>();
			instrument.add(this.lastFeed.getStockClose());
			instruments.put(this.lastFeed.getSymbol(), instrument);
		}else{
			instruments.get(this.lastFeed.getSymbol()).add(this.lastFeed.getStockClose());
			if(instruments.get(this.lastFeed.getSymbol()).size()==5){
				double temp=0;
				for (int i=0;i<instruments.get(this.lastFeed.getSymbol()).size();i++){
					temp+=instruments.get(this.lastFeed.getSymbol()).get(i);					
				}
			
				simpleMovingAverageResult=temp/5;
				
				sma = new Indicator();
				sma.setSymbol(lastFeed.getSymbol());
				sma.setValue(simpleMovingAverageResult);
				sma.setIndicatorType(IndicatorType.SIMPLE_MOVING_AVERAGE);
				sma.setPublished(lastFeed.getDate());
				sma.setTime(lastFeed.getTime());
				
				
				this.collector.emit(new Values(sma));
				
				for(int i=1;i<instruments.get(this.lastFeed.getSymbol()).size();i++){
					instruments.get(this.lastFeed.getSymbol()).set(i-1, instruments.get(this.lastFeed.getSymbol()).get(i));			
				}
				instruments.get(this.lastFeed.getSymbol()).remove(4);
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

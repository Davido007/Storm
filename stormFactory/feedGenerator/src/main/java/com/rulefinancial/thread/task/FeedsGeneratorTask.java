package com.rulefinancial.thread.task;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.rulefinancial.Feeds;
import com.rulefinancial.dao.FeedDao;
import com.rulefinancial.jms.JmsBatchProcessor;


/**
 * Class implements Runnable interface.
 * It is used to send feeds into a jms queue 
 * 
 * @author Piotr Gabara
 *
 */
public class FeedsGeneratorTask implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final Gson gson = new Gson(); 
	
	private String symbol;
	private int sleepTime;
	private JmsBatchProcessor processor;
	private FeedDao feedDao;
	
	private long endTime;
	private boolean isRun;
	
	private List<Feeds> feeds;
	
	
	/**
	 * Class constructor with paramters:
	 * @param symbol	Indicator symbol
	 * @param sleepTime	Sleep time value
	 * @param duration	Duration time value
	 * @param processor	An instance of JmsBatchProcessor class
	 * @param feedDao	An instance of FeedDao class
	 */
	public FeedsGeneratorTask(String symbol, int sleepTime, int duration, JmsBatchProcessor processor, FeedDao feedDao) {
		this.sleepTime = sleepTime;
		this.symbol = symbol;
		this.processor = processor;
		this.feedDao = feedDao;
		
		isRun = true;
		if(duration == -1) {
			endTime = -1;
		} else {
			endTime = System.currentTimeMillis() + duration;
		}
	}
	
	
	@Override
	public void run() {
		
		feeds = feedDao.getAllFeeds(symbol);
		Iterator<Feeds> iterator = feeds.iterator();
		
		while(isRun) {
			try {
				if(iterator.hasNext()) {
					Feeds current = iterator.next();
					String json = gson.toJson(current);
					processor.sendMessage(json);
				} else {
					isRun = false;
				}
				
				Thread.sleep(sleepTime);
				
				if(endTime != -1 && System.currentTimeMillis() >= endTime) {
					isRun = false;
				}
			} catch (InterruptedException e) {
				logger.error("Interrupted exception", e);
				isRun = false;
			}
		}
		
		logger.info("Thread - " + Thread.currentThread().getName() + ":" + Thread.currentThread().getId() + " end");
		
	}


	public long getEndTime() {
		return endTime;
	}

	public List<Feeds> getFeeds() {
		return feeds;
	}
}

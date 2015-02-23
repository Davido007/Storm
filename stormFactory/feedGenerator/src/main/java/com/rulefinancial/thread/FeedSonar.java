package com.rulefinancial.thread;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import com.rulefinancial.dao.FeedDao;
import com.rulefinancial.jms.JmsBatchProcessor;


/**
 * Class divides specified symbols into groups and executes tasks in separate threads
 * 
 * @author Piotr Gabara
 *
 */
public class FeedSonar {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JmsBatchProcessor processor;
	
	private TaskExecutor taskExecutor;
	private Class<Runnable> runClass;
	private FeedDao feedDao;
	
	private Integer sleepTime;
	private Integer duration;
	private String[] symbols;
	
	/**
	 * Class constructor
	 * @param taskExecutor	The TaskExecutor instance
	 * @param runClass	The name of task class
	 * @param feedDao	The FeedDao instance
	 */
	public FeedSonar(TaskExecutor taskExecutor, Class<Runnable> runClass, FeedDao feedDao) {
		this.taskExecutor = taskExecutor;
		this.runClass = runClass;
		this.feedDao = feedDao;
	}
	
	
	/**
	 * Method starts every task in new thread
	 */
	public void start() {
		for(String symbol : symbols) {
			Runnable task = createNewTask(runClass, symbol, sleepTime, duration, processor, feedDao);
			taskExecutor.execute(task);
		}
	}
	
	
	/**
	 * Method creates Runnable instance using parameters
	 * @param runClass	Class that implements Runnable interface
	 * @param symbol	Indicator sybmol
	 * @param sleepTime	Sleep time value
	 * @param duration	Duration time value
	 * @param processor	Instance of JmsBatchProcessor
	 * @param feedDao	Instance of FeedDao
	 * @return
	 */
	public Runnable createNewTask(Class<Runnable> runClass, String symbol, int sleepTime, int duration, JmsBatchProcessor processor, FeedDao feedDao) {
		
		try {
			Constructor<Runnable> constructor = runClass.getConstructor(String.class, int.class, int.class, JmsBatchProcessor.class, FeedDao.class);
			return constructor.newInstance(symbol, sleepTime, duration, processor, feedDao);
		} catch (Exception e) {
			logger.error("Cannot create new Runnable object", e);
			throw new TaskCreationRuntimeException(e);
		}
		
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String[] getSymbols() {
		return symbols;
	}

	public void setSymbols(String[] symbols) {
		this.symbols = Arrays.copyOf(symbols, symbols.length);
	}

	public Class<Runnable> getRunClass() {
		return runClass;
	}

	public void setRunClass(Class<Runnable> runClass) {
		this.runClass = runClass;
	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

}

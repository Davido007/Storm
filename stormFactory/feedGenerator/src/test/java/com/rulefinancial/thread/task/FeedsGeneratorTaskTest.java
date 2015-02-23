package com.rulefinancial.thread.task;


import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rulefinancial.Feeds;
import com.rulefinancial.dao.FeedDao;
import com.rulefinancial.jms.JmsBatchProcessor;
import com.rulefinancial.jms.MessageListener;


@ContextConfiguration(locations={"/test-spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class FeedsGeneratorTaskTest {
	
	@Autowired
	private FeedsGeneratorTask task;
	
	@Autowired
	private MessageListener messageListener;
	
	@Test
	public void ifDurationIsMinusOneEndTimeShouldBeAlsoEqualMinusOne() {
		long endTimeValue = task.getEndTime();
		Assert.assertEquals(-1, endTimeValue);
	}
	
	@Test
	public void feedGeneratorTaskShouldSendMessage2TimesWhenItFetch2FeedsFromDatabase() {
		JmsBatchProcessor processor = Mockito.mock(JmsBatchProcessor.class);
		FeedDao feedDao = Mockito.mock(FeedDao.class);
		
		Mockito.when(feedDao.getAllFeeds("TEST.JU")).thenReturn(Arrays.asList(new Feeds(), new Feeds()));
		
		FeedsGeneratorTask generatorTask = new FeedsGeneratorTask("TEST.JU", 100, 1000, processor, feedDao);
		generatorTask.run();
		
		Mockito.verify(processor, Mockito.times(2)).sendMessage(Mockito.anyString());
	}

}

package com.rulefinancial.thread;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rulefinancial.dao.FeedDao;
import com.rulefinancial.jms.JmsBatchProcessor;


@ContextConfiguration(locations={"/test-spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class FeedSonarTest {
	
	@Autowired
	private FeedSonar feedSonar;
	
	@Test
	public void symbolsShouldBeTheSame() {
		String[] symbols = new String[]{ "AAN.US","MYD.US","STM.US" };
		String[] symbolsFromPropertiesFile = feedSonar.getSymbols();
		Assert.assertEquals(symbols.length, symbolsFromPropertiesFile.length);
	}
	
	@Test
	public void methodCreateNewTaskShouldReturnNewTask() throws ClassNotFoundException {
		FeedDao feedDao = Mockito.mock(FeedDao.class);
		JmsBatchProcessor processor = Mockito.mock(JmsBatchProcessor.class);
		
		@SuppressWarnings("unchecked")
		Class<Runnable> clazz = (Class<Runnable>) Class.forName("com.rulefinancial.thread.task.FeedsGeneratorTask");
		
		Runnable returnTask = feedSonar.createNewTask(clazz, "", 0, 0, processor, feedDao);
		Assert.assertNotNull(returnTask);
	}
	
	@Test(expected = TaskCreationRuntimeException.class)
	public void methodCreateNewTaskShouldThrowExceptionWhenParametersAreNotCorrect() throws ClassNotFoundException {
		FeedDao feedDao = Mockito.mock(FeedDao.class);
		JmsBatchProcessor processor = Mockito.mock(JmsBatchProcessor.class);
		
		feedSonar.createNewTask(null, "", 0, 0, processor, feedDao);
	}
	
	@Test
	public void taskExecutorInLoopShouldExecutes3Times() {
		TaskExecutor executor = Mockito.mock(TaskExecutor.class);
		String[] symbols = new String[]{ "AAN.US","MYD.US","STM.US" };
		
		feedSonar.setSymbols(symbols);
		feedSonar.setTaskExecutor(executor);
		feedSonar.start();
		
		Mockito.verify(executor, Mockito.times(3)).execute(Mockito.any(Runnable.class));
	}
	
}

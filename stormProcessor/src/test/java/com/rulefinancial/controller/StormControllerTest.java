package com.rulefinancial.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rulefinancial.entity.Indicator;
import com.rulefinancial.entity.IndicatorType;
import com.rulefinancial.jms.receiver.MessageReceiver;


@ContextConfiguration(locations={"/test-app-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class StormControllerTest {
	
	private MessageReceiver messageReceiver;
	private StormController stormController;

	
	@Before
	public void init() {
		stormController = new StormController();
		messageReceiver = Mockito.mock(MessageReceiver.class);
	}
	
	
	@Test
	public void getSimpleMovingAverage() {
		Indicator indi = new Indicator();
		indi.setIndicatorType(IndicatorType.SIMPLE_MOVING_AVERAGE);
		indi.setSymbol("AAN.US");
		indi.setValue(29.89);
		
		Mockito.when(messageReceiver.getLastSMAValue("AAN.US")).thenReturn(indi);
		
		stormController.setBox(messageReceiver);
		Indicator results = stormController.getSimpleMovingAverageIndicator("AAN.US");
		
		Assert.assertNotNull(results);
		Assert.assertEquals(29.89, results.getValue(), 0.01);
	}
	
	
	@Test
	public void getSupportedInstrumentsListMethodShouldReturnPropertiesList() {
		stormController.setInstruments("AA1.JU", "AA2.JU", "AA3.JU");
		List<String> results = stormController.getSupportedInstrumentsList();
		
		Assert.assertTrue( Arrays.equals(new String[]{"AA1.JU", "AA2.JU", "AA3.JU"} , results.toArray()) );
	}

}
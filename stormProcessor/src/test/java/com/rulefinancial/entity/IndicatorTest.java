package com.rulefinancial.entity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class IndicatorTest {
	private Indicator indicator;
	
	@Before
	public void init() {
		indicator = new Indicator();
	}	
	
	
	@Test
	public void getValueTest() {
		indicator.setValue(22.22);
		double value=indicator.getValue();
		Assert.assertNotNull(value);
		Assert.assertEquals(22.22, value, 0.01);
	}
	
	
}

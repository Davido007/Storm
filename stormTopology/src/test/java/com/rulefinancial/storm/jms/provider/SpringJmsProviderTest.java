package com.rulefinancial.storm.jms.provider;

import com.rulefinancial.storm.jms.provider.SpringJmsProvider;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations={"/test-app-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringJmsProviderTest {
  
	@Test
	public void providerShouldReturnRightDestinationAndConnectionFactoryObjects() throws Exception {
		SpringJmsProvider provider = new SpringJmsProvider("test-app-context.xml", "connectionFactory", "destination");
		Assert.assertNotNull(provider.connectionFactory());
		Assert.assertNotNull(provider.destination());
	}
  
	@Test(expected=Exception.class)
	public void providerShouldThrowExceptionWhenTheConnectionFactoryBeanNameIsIncorrect() {
		@SuppressWarnings("unused")
		SpringJmsProvider provider = new SpringJmsProvider("test-app-context.xml", "badConnectionFactoryName", "destination");
	}
  
	@Test(expected=Exception.class)
	public void providerShouldThrowExceptionWhenTheDestinationBeanNameIsIncorrect() {
		@SuppressWarnings("unused")
		SpringJmsProvider provider = new SpringJmsProvider("test-app-context.xml", "connectionFactory", "badDestinationName");
	}
}

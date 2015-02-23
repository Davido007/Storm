package com.rulefinancial.storm.converter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rulefinancial.storm.entity.Feed;

public class JsonToFeedConverterTest {
  
	private JsonToFeedConverter converter;
  
	@Before
	public void init() {
		this.converter = new JsonToFeedConverter();
	}
  
	
	@Test
	public void converterShouldConvertJsonStringToFeedObject() {
    
		String json = "{\"keyColumn\":1,\"symbol\":\"AAN.US\",\"date\":\"lis 12, 2013\",\"time\":\"03:35:00 PM\",\"stockOpen\":29.47,\"stockHigh\":29.48,\"stockLow\":29.47,\"stockClose\":29.48,\"stockVolume\":500.0,\"stockOpenInt\":0.0}";
		Feed feed = this.converter.convertJsonTo(json);
    
		Assert.assertNotNull(feed);
		Assert.assertEquals(1L, feed.getKeyColumn());
		Assert.assertEquals("AAN.US", feed.getSymbol());
		Assert.assertEquals("2013-11-12", feed.getDate().toString());
		Assert.assertEquals("15:35:00", feed.getTime().toString());
		Assert.assertEquals(29.469999999999999D, feed.getStockOpen(), 0.01D);
		Assert.assertEquals(29.48D, feed.getStockHigh(), 0.01D);
		Assert.assertEquals(29.469999999999999D, feed.getStockLow(), 0.01D);
		Assert.assertEquals(29.48D, feed.getStockClose(), 0.01D);
		Assert.assertEquals(500.0D, feed.getStockVolume(), 0.01D);
		Assert.assertEquals(0.0D, feed.getStockOpenInt(), 0.01D);
	}
  
	@Test
	public void converterShouldReturnNullValueWhenTheJsonStringIsEmpty() {
		String json = "";
		Feed feed = this.converter.convertJsonTo(json);
    
		Assert.assertNull(feed);
	}
	
}

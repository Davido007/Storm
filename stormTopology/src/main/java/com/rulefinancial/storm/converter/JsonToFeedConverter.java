package com.rulefinancial.storm.converter;

import com.google.gson.Gson;
import com.rulefinancial.storm.entity.Feed;
import java.io.Serializable;


/**
 * Simple implementation of Converter interface, converts JSONs to Feeds
 * 
 * @author Piotr Gabara
 *
 */
public class JsonToFeedConverter implements Converter<Feed>, Serializable {
  
	private static final long serialVersionUID = 3146475396420158849L;
  
	/**
	 * Method converts JSON to Feed object and returns it
	 */
	public Feed convertJsonTo(String json) {
		Gson gson = new Gson();
		return (Feed)gson.fromJson(json, Feed.class);
	}
	
}

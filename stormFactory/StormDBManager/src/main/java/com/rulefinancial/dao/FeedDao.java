package com.rulefinancial.dao;

import java.util.List;

import com.rulefinancial.Feeds;

/**
 * This interface is using by feedGedenetor module
 * @author dkmiecik
 * @version 1.0
 */
public interface FeedDao {

	/**
	 * This method is implemented in DAOfeeds.class
	 * Is responsible for getting all feeds from DB
	 * @param symbol
	 */
	List<Feeds> getAllFeeds(String symbol);
	
}

package com.rulefinancial.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.rulefinancial.Feeds;

/**
 * This interface is using by feedGenerator module
 * @author dkmiecik
 * @version 1.0
 */
public interface DAOfeedsI {
	
	/**
	 * This method is implemented in DAOfeeds.class
	 * Is responsible for adding new feed to DB
	 * @param session
	 * @param feed
	 */
	Session addFeed(Session session,Feeds feed);
	
	/**
	 * This method is implemented in DAOfeeds.class
	 * Is responsible for getting all feeds by Symbol from DB
	 * @param symbol
	 * @param sessionFactory
	 * @param session
	 */
	List<Feeds> getAllFeedsBySymbol(String symbol, SessionFactory sessionFactory, Session session);
}

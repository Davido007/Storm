package com.rulefinancial.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.rulefinancial.Feeds;

/**
 * This class implements DAOfeedsI interface and is responsible for feeds from/to DB
 * @author dkmiecik
 * @version 1.0
 */
public class DAOfeeds implements DAOfeedsI{

	/**
	 * This method adds feeds to DB
	 * @param session
	 * @param feed
	 * @return session
	 */
	public Session addFeed(Session session,Feeds feed) {
		session.persist(feed);
		return session;
	}
	
	/**
	 * This method get all feeds from DB
	 * @param sessionFactory
	 * @param session
	 * @return feeds list
	 */
	public List<Feeds> getAll(SessionFactory sessionFactory, Session session) {
		
		session = sessionFactory.openSession();
		
		Query query = session.createQuery("from Feeds");
		
		@SuppressWarnings("unchecked")
		List<Feeds> feeds = query.list(); 
		
		return feeds;
	}
	
	/**
	 * This method gets all feeds by Symbol from DB
	 * @param symbol
	 * @param sessionFactory
	 * @param session
	 * @return feeds list
	 */
	public List<Feeds> getAllFeedsBySymbol(String symbol, SessionFactory sessionFactory, Session session) {
	
		session = sessionFactory.openSession();
		
		Query query = session.createQuery("from Feeds where symbol = :symbol");
		query.setString("symbol", symbol);
		
		@SuppressWarnings("unchecked")
		List<Feeds> feeds = query.list(); 
		
		return feeds;
	}
	
}

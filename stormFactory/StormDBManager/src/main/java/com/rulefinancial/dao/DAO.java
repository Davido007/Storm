package com.rulefinancial.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.rulefinancial.Feeds;
import com.rulefinancial.utils.HibernateUtils;

/**
 * This class implements FeedDao interface and is responsible for starts
 * functions in another parts of project
 * @author dkmiecik
 * @version 1.0
 */
public class DAO implements FeedDao {
	
	private SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
	private Session session;
	
	DAOfeeds a = new DAOfeeds();
	
	/**
	 * This method start method which get all feeds by symbol from DB
	 * @param symbol
	 * @return feeds 
	 */
	public List<Feeds> getAllFeeds(String symbol){
		return a.getAllFeedsBySymbol(symbol, sessionFactory, session);
	}
	
	/**
	 * This method start method which get all feeds by symbol from DB
	 * @param symbol
	 * @return size list
	 */
	public int getSize(String symbol){
		return a.getAllFeedsBySymbol(symbol, sessionFactory, session).size();
	}
	
	/**
	 * This method start method which get all feeds from DB
	 * @return feeds 
	 */
	public List<Feeds> getAll(){
		return a.getAll(sessionFactory, session);
	}
	

}

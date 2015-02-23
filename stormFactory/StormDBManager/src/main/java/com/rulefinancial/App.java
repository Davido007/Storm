package com.rulefinancial;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.rulefinancial.dao.DAOfeeds;
import com.rulefinancial.utils.HibernateUtils;

/**
 * This class is responsible get config file and start methods from DAOfeeds
 * @author dkmiecik
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class App {

	/**
	 * This method create session, get Transaction and get file to config
	 */
	public static void main(String[] args){
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		session.beginTransaction();
		session.getTransaction().commit();
		session.close();
		AnnotationConfiguration config = new AnnotationConfiguration();
		config.configure("hibernate.cfg.xml");

	}
	
	/**
	 * This method create object of DAOfeeds and start method for adding feed to DB
	 * @param session
	 * @param table
	 * @return session
	 */
	public static Session addValue(Session session,Feeds table){	
		DAOfeeds dao=new DAOfeeds();
		dao.addFeed(session,table);
		return session;
	}
	
	/**
	 * This method create object of DAOfeeds and start method for getting all feeds 
	 * from DB
	 * @param sessionFactory
	 * @param session
	 * @return list
	 */
	public static List<Feeds> getAll(SessionFactory sessionFactory, Session session) {
		DAOfeeds dao=new DAOfeeds();
		return dao.getAll(sessionFactory, session);
	}
	
	/**
	 * This method create object of DAOfeeds and start method for getting all feeds 
	 * by symbol from DB
	 * @param sessionFactory
	 * @param session
	 * @return list
	 */
	public static List<Feeds> getAllBySymbol(String symbol, SessionFactory sessionFactory, Session session){
		DAOfeeds dao=new DAOfeeds();
		return dao.getAllFeedsBySymbol(symbol, sessionFactory, session);
	}
}

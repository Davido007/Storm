package com.rulefinancial.utils;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;



/**
 * This class is responsible for connection with db.
 * @author dkmiecik
 * @version 1.0
 */
public class HibernateUtils {
	
	private static final SessionFactory SESSION_FACTORY = buildSessionFactory();
	
	/**
	 * This method build SessionFactory
	 * @return SessionFactory
	 */
	private static SessionFactory buildSessionFactory() {
		
		try {
			
			Configuration configuration = new Configuration();
			configuration.configure();
			
			ServiceRegistry serviceRegistry  = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
			
			return configuration.buildSessionFactory(serviceRegistry);
			
		} catch(Exception e) {
			throw new ExceptionInInitializerError(e);
		}
		
	}
	
	/**
	 * This method returns builded SessionFactory
	 * @return SessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		return SESSION_FACTORY;
	}
	
	/**
	 * This method creates hibernateSession and returns it
	 * @return hibernateSession
	 */
	public static Session beginTransaction(){
		Session hibernateSession = HibernateUtils.getSession();
		hibernateSession.beginTransaction();
		return hibernateSession;
	}
	
	/**
	 * This method commits transaction
	 */
	public static void commitTransaction(){
		HibernateUtils.getSession().getTransaction().commit();
		
	}
	
	/**
	 * This method creates session and returns it
	 * @return session
	 */
	public static Session getSession(){
		return SESSION_FACTORY.getCurrentSession();
		
	}
	
	/**
	 * This method creates transaction and returns it
	 * @param session
	 * @return transaction
	 */
	public Transaction getTransaction(Session session){
		if(session!=null){
			return session.getTransaction();
			}else{
				return null;
				}
	}
	

}

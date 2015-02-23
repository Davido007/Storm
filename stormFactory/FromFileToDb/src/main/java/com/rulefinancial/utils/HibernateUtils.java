package com.rulefinancial.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * This class is responsible for connection with db.
 * @author dplichta
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

}

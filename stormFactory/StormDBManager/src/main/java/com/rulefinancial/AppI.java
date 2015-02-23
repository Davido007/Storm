package com.rulefinancial;

import org.hibernate.Session;

/**
 * This interface is using by FromFileToDb module
 * @author dkmiecik
 * @version 1.0
 */
public interface AppI {
	
	/**
	 * This method is implemented in App.class
	 * @param session
	 * @param table
	 */
	Session addValue(Session session,Feeds table);
}

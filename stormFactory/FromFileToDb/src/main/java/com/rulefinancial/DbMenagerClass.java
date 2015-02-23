package com.rulefinancial;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.rulefinancial.utils.HibernateUtils;
/**
 * This class create SessionFactory
 * @author dplichta
 * @version 1.0
 */
public class DbMenagerClass {
private SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
/**
 * This method creates session and returns it
 * @return session
 */
public Session getSession(){
	return sessionFactory.openSession();
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

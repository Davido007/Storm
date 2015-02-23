package com.rulefinancial.utils;

import java.io.IOException;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class HbUtilsTest {
	HibernateUtils HbUtils;

	
	Session session;
	@Before
	public void init() {
		HbUtils=new HibernateUtils();
		
		Assert.assertNotNull(HbUtils);
	
	}
	@Test
	public void testGetingSession(){
		Assert.assertNotNull(HibernateUtils.getSession());}
	
	@Test
	public void testGetingTransaction(){
		Assert.assertNull( HbUtils.getTransaction(session));
		session= HibernateUtils.getSession();
		Assert.assertNotNull(HbUtils.getTransaction(session));
	}
	@SuppressWarnings("static-access")
	@Test
	public void testBeginTransaction()throws IOException{
		
		HbUtils.beginTransaction();
		Assert.assertNotNull(HbUtils);
	}
	@SuppressWarnings("static-access")
	@Test
	public void testCommitTransaction(){
		HbUtils = Mockito.mock(HibernateUtils.class);
		HbUtils.getSessionFactory().openSession();
		HbUtils.beginTransaction();
		HbUtils.commitTransaction();
		Assert.assertNotNull(HbUtils);
	}

}

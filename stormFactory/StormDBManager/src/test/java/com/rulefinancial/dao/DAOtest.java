package com.rulefinancial.dao;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;

import com.rulefinancial.Feeds;

@SuppressWarnings("deprecation")
public class DAOtest {
	private static Configuration config;
	private static SessionFactory factory;
	private static Session hibernateSession;
	static Transaction tr;
	static BufferedReader br;
	List<Feeds> emp = new ArrayList<Feeds>();
	DAO dao = new DAO();
	@SuppressWarnings("unused")
	@Before
	public void init(){
		DAO dao = Mockito.mock(DAO.class);
		config = new AnnotationConfiguration();
	    config.configure(new File("HB.cfg.xml"));
	    factory = config.buildSessionFactory();
	    hibernateSession = factory.openSession();
	    tr=hibernateSession.getTransaction();
	    
	}
	@Test
	public void testGetSize(){
		DAO dao = Mockito.mock(DAO.class);
		int i = dao.getSize(" ");
		//Assert.assertNotNull(i);
		Assert.assertEquals(0, i);
		System.out.println(i);
	}
	
	@Test
	public void testGetAll(){
		DAO dao = Mockito.mock(DAO.class);
		DAOfeeds DaoFeeds = Mockito.mock(DAOfeeds.class);
		dao.getAll();
		DaoFeeds.getAll(factory, hibernateSession);
		Mockito.verify(dao).getAll();
		Mockito.verify(DaoFeeds, atLeastOnce()).getAll(factory, hibernateSession);
		Mockito.when(dao.getAll()).thenReturn(emp);
		Assert.assertNotNull(emp);
		Assert.assertNotNull(tr);
		Assert.assertNotEquals(emp, null);

	}
	@Test
	public void testGetAllFeeds(){
		DAO dao = new DAO();
		emp = dao.getAllFeeds(" ");
		Assert.assertNotNull(emp);
	}

}

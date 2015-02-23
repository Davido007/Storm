package com.rulefinancial.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.rulefinancial.App;
import com.rulefinancial.Feeds;



@SuppressWarnings("deprecation")
public class AppTest {
	private static Configuration config;
	private static SessionFactory factory;
	private static Session hibernateSession;
	static Transaction tr;
	static BufferedReader br;
	Feeds feed;
	App app;
	DAOfeeds daoFeeds;
	DAO dao;
	
	public static Feeds addFeed(Feeds feed)
	{
		Feeds feed2 = new Feeds();
		feed2.setKeyColumn(1);
	    feed2.setStockClose(3.5);
	    feed2.setStockHigh(3.5);
	    feed2.setStockLow(4.4);
	    feed2.setStockOpen(3.1);
	    feed2.setStockOpenInt(44.1);
	    feed2.setStockVolume(4.1);
	    feed2.setSymbol("4");
		return feed2;
		
	}
	
	
	@Before
	public void init(){

	
	
		app = Mockito.mock(App.class);
		config = new AnnotationConfiguration();
	    config.configure(new File("HB.cfg.xml"));
	    factory = config.buildSessionFactory();
	    hibernateSession = factory.openSession();
	    tr=hibernateSession.getTransaction();

	}
	
	@Test
	public void TestApp() throws IOException {
		Assert.assertNotNull(app);
		App.main(null);
	}
	@Test
	public void GetValueTest(){
		List<Feeds> emp = App.getAll(factory,hibernateSession);
		Assert.assertNotNull(emp);
	}
	@Test
	public void GetAllFeedsBySybmolTest(){
		List<Feeds> emp = App.getAllBySymbol(" ", factory,hibernateSession);
		Assert.assertNotNull(emp);
	}
	@Test
	public void testAddValue(){
		Feeds emp2 = new Feeds();
		addFeed(emp2);
		App.addValue(hibernateSession,emp2);
		Assert.assertNotNull(emp2);
		Assert.assertNotNull(hibernateSession);
	}
	
	
}

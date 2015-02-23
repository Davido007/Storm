package com.rulefinancial;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.rulefinancial.DbMenagerClass;

public class DbManagerTest {
	DbMenagerClass dbManager;
	Session session;
	@Before
	public void init() {
		dbManager=new DbMenagerClass();
		Assert.assertNotNull(dbManager);
	}
	@Test
	public void testGetingSession(){
		Assert.assertNotNull(dbManager.getSession());
	}
	@Test
	public void testGetingTransaction(){
		Assert.assertNull(dbManager.getTransaction(session));
		session=dbManager.getSession();
		Assert.assertNotNull(dbManager.getTransaction(session));
	}
}

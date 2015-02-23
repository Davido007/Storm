package com.rulefinancial;
 
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MainTest {
	MainClass mainClass;
	DbMenagerClass dbManager;
	@Before
	public void init() {
		mainClass=new MainClass();
		dbManager=new DbMenagerClass();
		Assert.assertNotNull(mainClass);
		Assert.assertNotNull(dbManager);
	}

	@Test
	public void ValidPropertiesFormatTest() throws IOException{
		mainClass.parseAndWriteToDb(dbManager.getSession(),"folder3.path","src/main/resources/FolderSource.xml");
	}
	@Test(expected=InvalidPropertiesFormatException.class)
	public void InvalidPropertiesFormatExceptionTest() throws IOException{
		mainClass.parseAndWriteToDb(dbManager.getSession(),"something","src/main/resources/FolderSourceTest.xml");
	}
	@Test(expected=IOException.class)
	public void PropertiesFileNotFoundExceptionTest() throws IOException{
		mainClass.parseAndWriteToDb(dbManager.getSession(),"something","src/main/resources/FolderSource1.xml");
	}
	@Test(expected=NullPointerException.class)
	public void EmptyOrNotExistingDataFolder() throws NullPointerException, IOException{
		mainClass.parseAndWriteToDb(dbManager.getSession(),"folder2.path","src/main/resources/FolderSource.xml");
	}
	
}

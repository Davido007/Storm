package com.rulefinancial;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.BeforeClass;

 
public class IntegrationTest {  
   private static Configuration config;
   private static SessionFactory factory;
   private static Session hibernateSession;
   static BufferedReader br;
   static FileParsingClass fileParsing;
   
   @SuppressWarnings("deprecation")
@BeforeClass  
   public static void init() {
	   fileParsing=new FileParsingClass();
	   Assert.assertNotNull(fileParsing);;
       config = new Configuration();
       config.configure(new File("src/main/resources/hibernateTest.cfg.xml"));
       factory = config.buildSessionFactory();
       hibernateSession = factory.openSession();
       }
    public static int linesOfFile(String fileName) throws IOException{
    	int counter = 0;
    	br = new BufferedReader(new FileReader(fileName.toString()));
    	while(br.readLine()!=null){
    		counter++;
    	}
    	return counter-1;
    }
    public static int rowsOfDatabase(Session session,String fileName) throws IOException{
    	fileParsing.getFileAndWriteToDB(session,new StringBuilder(fileName));
    	br = new BufferedReader(new FileReader(fileName.toString()));
    	List<Feeds> feedsList = App.getAll(factory,hibernateSession);
    	return feedsList.size();
    }
   @Test
   public void checkIfNumberOfFileLinesEqualsNumberOfRowsInDb() throws IOException{
	   int fileCounter=linesOfFile("nyse/1/AAN_US5.TXT");
	   int DbCounter=rowsOfDatabase(hibernateSession,"nyse/1/AAN_US5.TXT");
       Assert.assertEquals(fileCounter, DbCounter);
   }
}

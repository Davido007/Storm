package com.rulefinancial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

import org.hibernate.Session;
import org.hibernate.Transaction;


 
/**
 * 
 * @author dplichta
 * @version 1.0
 */
public class FileParsingClass {
	/**
	 * This method parses one file and:
	 * - uses DbMenagerClass() to create session and transaction
	 * - uses App() to write whole file to db
	 * @param fileName
	 */
	public void getFileAndWriteToDB (Session session,StringBuilder fileName) throws IOException{
		DbMenagerClass dbManager=new DbMenagerClass();
		try {
			Transaction transaction=dbManager.getTransaction(session);
			String line;
			String splitBy = ","; 
			StringBuilder date=new StringBuilder();
			StringBuilder time=new StringBuilder();
			BufferedReader br = new BufferedReader(new FileReader(fileName.toString()));
			transaction.begin();
			br.readLine();
			int batch=0;
			line = br.readLine();
			while (line != null) {
				//parsing
				String[] record = line.split(splitBy);
				date.replace(0, 12, record[2]);
				date.insert(4, "-");
				date.insert(7, "-");
				time.replace(0, 12, record[3]);
				time.insert(2, ":");
				time.insert(5, ":");
				//writing to DB
				session=App.addValue(session,new Feeds(record[0],Date.valueOf(date.toString()),Double.valueOf(record[7]),Double.valueOf(record[5]),Double.valueOf(record[6]),
					Double.valueOf(record[4]),Double.valueOf(record[9]),Double.valueOf(record[8]),Time.valueOf(time.toString())));
			   if ( batch % 30 == 0 ) { 
			        session.flush();
			        session.clear();
			   }
			    batch++;
			    line = br.readLine();
			} 		
			transaction.commit();
			session.close();
			br.close();
		} catch(IllegalArgumentException e){
			throw new IllegalArgumentException("context", e);
		}
		
	}
}

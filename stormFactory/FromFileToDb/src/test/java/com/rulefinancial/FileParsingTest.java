package com.rulefinancial;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class FileParsingTest {
	FileParsingClass fileParsing;
	DbMenagerClass dbManager;
	@Before
	public void init() {
		fileParsing=new FileParsingClass();
		dbManager=new DbMenagerClass();
		Assert.assertNotNull(fileParsing);
		Assert.assertNotNull(dbManager);
	}

	@Test(expected=IOException.class)
	public void GetFileExceptionTest() throws IOException, IllegalArgumentException{
			fileParsing.getFileAndWriteToDB(dbManager.getSession(),new StringBuilder("wrong path"));

	}
	@Test(expected=IllegalArgumentException.class)
	public void ParseFileExceptionTest() throws IllegalArgumentException, IOException{
			fileParsing.getFileAndWriteToDB(dbManager.getSession(),new StringBuilder("nyse/3/AAN_US5.TXT"));
	}
	@Test
	public void ParsingSuccess() throws IllegalArgumentException, IOException{
			fileParsing.getFileAndWriteToDB(dbManager.getSession(),new StringBuilder("nyse/1/AAN_US5.TXT"));
	}
}

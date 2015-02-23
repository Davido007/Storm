package com.rulefinancial;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FileMenagerTest {
	FileMenagerClass fileManager;
	@Before
	public void init() {
		fileManager=new FileMenagerClass();
		Assert.assertNotNull(fileManager);
	}
	@Test
	public void testGetingFolderSize(){
		int size;
		Assert.assertNotNull(size=fileManager.fileFolderSize("nyse/2"));
		int expected=12;
		Assert.assertEquals(expected, size);
	}
	@Test
	public void testGetingFilePath(){
		String path;
		Assert.assertNotNull(path=(fileManager.fileIterator(1, "nyse/1")).toString());
		String expected="nyse\\1\\AAP_US5.TXT";
		Assert.assertEquals(expected, path);
	}

}

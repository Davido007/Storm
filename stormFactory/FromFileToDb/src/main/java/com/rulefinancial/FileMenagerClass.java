package com.rulefinancial;

import java.io.File;

/**
 * 
 * @author dplichta
 *
 */
public class FileMenagerClass{
/**
 * This method opens folder and returns file path of particular file.
 * @param Iterator
 * @param folderPath
 * @return filePatch as a StringBuilder
 */
	public StringBuilder fileIterator(int iterator,String folderPath){
		File folder = new File(folderPath);	
		File[] listOfFiles = folder.listFiles();
		if(listOfFiles!=null){
		File file = listOfFiles[iterator];
		StringBuilder filePath = new StringBuilder();
		filePath.replace(0,50,file.getPath());
		return filePath;
		}else{
			return null;
		}
	}
	/**
	 * This method opens folder and returns number of its files.
	 * @param folderPath
	 * @return size of folder as an int
	 */
	public int fileFolderSize(String folderPath){
		File folder = new File(folderPath);
		if(folder.listFiles()!=null){
		return folder.listFiles().length;
		}else{
			return 0;
		}
	}
}

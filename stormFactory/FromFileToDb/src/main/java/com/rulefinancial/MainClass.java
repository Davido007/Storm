package com.rulefinancial;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.Session;

    
/** 
 * This class parse all files and write Feeds objects to Db.
 * @author dplichta
 * @version 1.0
 */ 
public class MainClass {
    StringBuilder filePath=new StringBuilder();
    static double start;
    static double end;
    static DbMenagerClass dbManager=new DbMenagerClass();
    FileParsingClass fileParsing=new FileParsingClass();
    FileMenagerClass fileManager=new FileMenagerClass();
    static Logger logger = Logger.getLogger("ErrorLogging");
    /**
     * This method uses ParseAndWriteToDB() and measure its execution time
     * @param args Unused.
     * @return Nothing.
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws InvalidPropertiesFormatException 
     */
    public static void main(String[] args) throws IOException{
        java.util.logging.Logger logger=java.util.logging.Logger.getLogger("InfoLogging");
        String sourceProperty="folder.path";
        String propertiesPath="src/main/resources/FolderSource.xml";
        MainClass main=new MainClass();
        start=System.currentTimeMillis();
        main.parseAndWriteToDb(dbManager.getSession(),sourceProperty,propertiesPath);
        end=System.currentTimeMillis();
        logger.info(((end-start)/1000)+" seconds");
    }  
    /**
     * This method uses FileMenagerClass() and FileParsingClass() to parse all files and write it to db.
     * This method uses FolderSource.xml file to get property of folder path.
     * @param Nothing.
     * @return Nothing.
     * @throws IOException 
     * @throws InvalidPropertiesFormatException 
     */
    public void parseAndWriteToDb(Session session,String property,String propertyFilePath) throws IOException{
        int listOfDoneFilesCounter=0;
        Properties properties=new Properties();
        try {
            properties.loadFromXML(new FileInputStream(propertyFilePath));
        } catch (InvalidPropertiesFormatException e) {
            String error="Problem with properties format";
            logger.error(error,e);
            throw new InvalidPropertiesFormatException(error);
        } catch (IOException e) {
            String error="Problem with properties file";
            logger.error(error,e);
            throw new IOException(error,e);
        } 
        int folderSize=fileManager.fileFolderSize(properties.getProperty(property));
            do{
            session=dbManager.getSession();
            StringBuilder fileName = fileManager.fileIterator(listOfDoneFilesCounter,properties.getProperty(property));
            fileParsing.getFileAndWriteToDB(session,new StringBuilder(fileName));			
            listOfDoneFilesCounter++;		
            }while(listOfDoneFilesCounter!=folderSize);
}}

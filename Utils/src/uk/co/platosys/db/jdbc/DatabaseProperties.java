/*
 * DatabaseProperties.java
 *
 * Created on 03 October 2007, 11:27
 *
 *  Copyright (C) 2008  Edward Barrow

    This class is free software; you can redistribute it and/or
    modify it under the terms of the GNU Library General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Library General Public License for more details.

    You should have received a copy of the GNU Library General Public
    License along with this library; if not, write to the Free
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * 
 * for more information contact edward.barrow@platosys.co.uk
 *
 */

package uk.co.platosys.db.jdbc;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;

import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.PlatosysProperties;

/**
 * To make things work, you must have a working Postgresql installation with the matching jdbc driver in your classpath.
 * 
 * You need to create a master  database,  a corresponding  user/role and the initial databases.xml file, which must be in your 
 * /usr/local/platosys/conf directory and to which /etc/platosys is a symlink. 
 *   
 * 
 *
 *
 * @author edward
 */
public class DatabaseProperties {
   
    /*private static String DEFAULT_DB_URL=PlatosysProperties.readProperty("platosyx", "master_db");
    private static String DEFAULT_DB_USER=PlatosysProperties.readProperty("platosyx", "master_db_user");
    private static String DEFAULT_DB_PASSWORD=PlatosysProperties.readProperty("platosyx", "master_db_pwd");
    private static String DEFAULT_DB_DRIVER=PlatosysProperties.readProperty("platosyx", "master_db_driver");*/
    private static String UNIX_CONFIG_READ_DIR = "/etc/platosys/";//this should be a symlink to the write dir
    private static String UNIX_CONFIG_WRITE_DIR = "/usr/local/platosys/conf";
    private static String WIN_CONFIGDIR="";
    private static String CONFIG_FILE="databases.xml";
    protected static Logger DATABASE_LOGGER= new Logger("dbplat");
    private DatabaseCredentials masterCredentials=null;
    private static Logger logger=DATABASE_LOGGER;
    private List<DatabaseCredentials> credentials = new ArrayList<DatabaseCredentials>();
    private List<String>names=new ArrayList<String>();
    /** Creates a new instance of DatabaseProperties */
    public DatabaseProperties() {
    	
      try {
          File configDir = new File(UNIX_CONFIG_READ_DIR);
          File configFile = new File(configDir, CONFIG_FILE);
          SAXBuilder builder = new org.jdom2.input.SAXBuilder();
          Document configDoc = builder.build(configFile);
          Element rootElement = configDoc.getRootElement();
          List databases = rootElement.getChildren("database");
          Iterator it = databases.iterator();
          while(it.hasNext()){
              Element database = (Element) it.next();
              String name = database.getAttributeValue("name");
             
              String databaseDriver = database.getChildText("databaseDriver");
              String databaseUrl = database.getChildText("databaseUrl");
              String databaseUser = database.getChildText("databaseUser");
              String databasePassword = database.getChildText("databasePassword");
              DatabaseCredentials databaseCredentials = new DatabaseCredentials(
                      name,
                      databaseDriver,
                      databaseUrl,
                      databaseUser,
                      databasePassword
                      ); 
              String status = database.getAttributeValue("status");
              if (status.equals("master")){
            	masterCredentials=databaseCredentials; 
            	logger.log(databaseCredentials.getName() + " is the master database");
              }
              credentials.add(databaseCredentials);
              names.add(name);
              logger.log("DatabaseProperties has loaded details for "+status+ " database "+name);
          }
      }catch(Exception e){
          logger.log("Problem loading database properties", e);
      }
    }   
    protected List<DatabaseCredentials> getDatabases(){
        return credentials;
    }
    public List<String> getDatabaseNames(){
        return names;
    }
    /**
     * creates a new database with the given name and the master database credentials
     * @param name
     * @return
     */
    public String createDatabase(String masterDatabase, String newDatabaseName) throws PlatosysDBException {
    	logger.log("creating new database: "+newDatabaseName);
    	DatabaseCredentials newCredentials = DatabaseCredentials.getNewCredentials(getMasterCredentials(), newDatabaseName);
    	logger.log("creating new database/credentials: "+newDatabaseName);
    	return createDatabase(masterDatabase, newCredentials);
    }
    
    /**
     * creates a new database and returns a String with its name
     * @param masterDatabase
     * @param newCredentials
     * @return
     * @throws PlatosysDBException 
     */
    public static String createDatabase(String masterDatabase, DatabaseCredentials newCredentials) throws PlatosysDBException {
       logger.log("checking database credentials etc");
    	//some checks:
          String databaseName = newCredentials.getName();
        String databaseUser = newCredentials.getUsername();
        String databaseURL = newCredentials.getUrl();
        logger.log("into the next method");
        Document configDoc=null;
        Element rootElement=null;
        try {
          File configDir = new File(UNIX_CONFIG_READ_DIR);
          File configFile = new File(configDir, CONFIG_FILE);
          SAXBuilder builder = new org.jdom2.input.SAXBuilder();
          configDoc = builder.build(configFile);
          rootElement = configDoc.getRootElement();
          List<Element> databases = rootElement.getChildren("database");
          Iterator<Element> it = databases.iterator();
          while(it.hasNext()){
              Element databaseElement=(Element) it.next();
              if (databaseElement.getAttributeValue("name").equals(masterDatabase)){
                  //check the database drivers match
                  if (!(databaseElement.getChildText("databaseDriver").equals(newCredentials.getDriver()))){
                      throw new PlatosysDBException("Database driver class names do not  match");
                  }
                  //TODO: fix location checking
                  /*
                   * not doing database location checks. Not working, too much time to fix
                   * for now.
                  //check the URL matches; strip off the name suffix of the URL
                  String masterURL = databaseElement.getChildText("databaseUrl");
                  logger.log("full masterURL is "+masterURL);
                  int b = masterDatabase.length();
                  int a = masterURL.length();
                  logger.log("masterDatabase is "+masterDatabase);
                  masterURL = masterURL.substring(0, a-b);
                  logger.log("stripped masterURL is "+masterURL);
                  logger.log("new dburl is "+databaseURL);
                  a = databaseURL.length();
                  b = databaseName.length();
                  String newURL = databaseURL.substring(0, a-b);
                  logger.log("newURL is"+newURL);
                  if (!masterURL.equals(newURL)){
                      throw new PlatosysDBException("New database must match the master database location");
                  }*/
              }
              if (databaseElement.getAttributeValue("name").equals(databaseName)){
                  throw new PlatosysDBException("Database name "+databaseName+ " already exists");
              }
              /*
              if (databaseElement.getChildText("databaseUser").equals(databaseUser)){
                  throw new PlatosysDBException("Database user "+databaseUser+" already exists");
              }*/
          }
        }catch(Exception x){
           logger.log("problem doing createDB checks", x);	
           throw new PlatosysDBException("Error doing create DB checks", x); 
        }  
        
        //connect to the master database and create new database and role
        Connection connection = ConnectionSource.getConnection(masterDatabase);
        try{
            Statement statement = connection.createStatement();
            //not creating new role because it is the same one (see how we get the credentials).
            //arguably we should create a new role. So just creating database.
            //statement.execute("CREATE ROLE "+databaseUser+" WITH LOGIN PASSWORD "+newCredentials.getPassword());
            statement.execute("CREATE DATABASE "+databaseName);
        }catch(Exception x){
        	logger.log("problem creating database", x);
            throw new PlatosysDBException("Failed to create new database: ", x);
        }
        try{
            connection.close();
        }catch(Exception x){}
        try {
        //write this to a file:
        	logger.log(2, "writing info to a file");
         File configDir = new File(UNIX_CONFIG_WRITE_DIR);
         File configFile = new File(configDir, CONFIG_FILE);
         Element databaseElement = new Element("database");
         databaseElement.setAttribute("name", databaseName);
         databaseElement.setAttribute("status", "slave");
         Element driverElement = new Element("databaseDriver");
         driverElement.setText(newCredentials.getDriver());
         databaseElement.addContent(driverElement);
         Element urlElement = new Element("databaseUrl");
         urlElement.setText(newCredentials.getUrl());
         databaseElement.addContent(urlElement);
         Element userElement = new Element("databaseUser");
         userElement.setText(newCredentials.getUsername());
         databaseElement.addContent(userElement);
         Element passwordElement = new Element("databasePassword");
         passwordElement.setText(newCredentials.getPassword());
         databaseElement.addContent(passwordElement);
         rootElement.addContent(databaseElement);
         
         XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
         FileWriter writer = new FileWriter(configFile);
         outputter.output(configDoc, writer);
         writer.close();
        }catch(Exception x){
        	logger.log("problem writing database info to xml", x);
            throw new PlatosysDBException ("problem writing new database configuration file ", x);
        }
         
        return newCredentials.getName();
    }
    private  DatabaseCredentials getMasterCredentials(){
    	 return masterCredentials;
    }
}
/*
 * PlatosysProperties.java
 *
 * Created on 28 April 2007, 22:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.co.platosys.util;
import java.io.*;
import java.security.AccessControlException;

/**
 * Generic implementation of java.util.Properties for Platosys software.
 * 
 * Tested only for *nix systems. 
 * 
 * Property files are java xml properties files read from /etc/platosys, which should 
 * normally be a symlink to /usr/local/platosys/conf 
 * 
 * String Properties are read using the static method call PlatosysProperties.readProperty(propertyFile, propertyName)
 *
 *
 *
 * @author edward
 */
public abstract class PlatosysProperties extends java.util.Properties implements Serializable{
    
   public static File SYSTEM_CONFIG_DIR = setSystemConfigDir();
   public static File USER_CONFIG_DIR=setUserConfigDir();
   public static Logger PROPERTIES_LOGGER = Logger.getLogger("psprops");
   public static Logger DEBUG_LOGGER=Logger.getLogger("debug");//TODO make it read the right one for the app
    /** Creates a new instance of PlatosysProperties */
   
 
    
  /**
   * This method tries to find a readable configuration directory in the
   * system-wide config location (/etc/ on *nix), vaguely attempting to make
   * it if it isn't there. Otherwise it returns a user config directory.
   * @return
   */
   
   private static File setSystemConfigDir(){
       Logger logger = Logger.getLogger("psprops");
        if(System.getProperty("os.name").startsWith("Windows")){
            //TODO: make this work properly on Windows
            return new File("C:\\Program Files\\platosys\\c");
        }else {
           File systemRootConfigDir=new File("/etc/");
           boolean confDirIsEtc=false;
           try{
               if(systemRootConfigDir.canRead()){confDirIsEtc=true;}
           }catch(AccessControlException ace){

           }
           if(confDirIsEtc){
               File systemConfigDir=new File(systemRootConfigDir, "platosys");
               logger.log(5, "scd is /etc/platosys");
               if(systemConfigDir.exists()){
                   logger.log(5, "scd exists");

                   if (systemConfigDir.canRead()){
                       logger.log(5, "scd exists and is readable, OK");

                       return systemConfigDir;
                   }else{
                       logger.log(5, "scd exists but is unreadable, using ucd");
                       return setUserConfigDir();
                   }

               }else if (systemConfigDir.mkdir()){
                   if (systemConfigDir.canRead()){
                       return systemConfigDir;

                   }else{
                       logger.log(5, "scd is made but unreadable, using ucd");
                       return setUserConfigDir();
                   }
               }else{
                   logger.log(5, "scd is unmakable, using ucd");
                   return setUserConfigDir();
               }

           }
        }
        return setUserConfigDir();
    }


 private static File setUserConfigDir(){
      
         File userHomeDirectory = new File(System.getProperty("user.home"));
         if(userHomeDirectory.exists()&&userHomeDirectory.canRead()){
             File userConfigDirectory=new File(userHomeDirectory, ".platosys");
             if(userConfigDirectory.exists()&&userConfigDirectory.canRead()){
                 return userConfigDirectory;
             }else if(!userConfigDirectory.exists()){
                 if (userConfigDirectory.mkdir()){
                     return userConfigDirectory;
                 }
             }else{
                 userConfigDirectory.setReadable(true);
                 return userConfigDirectory;
             }

         }
             return null;
         

    }
    public static String readProperty (String propFile, String propertyName){
        File propertiesFile = null;
        String propertyValue = null;
        java.util.Properties properties=new java.util.Properties();
        try{
            if(! propFile.endsWith(".xml")){
                propFile=propFile+".xml";
            }
            propertiesFile = new File(USER_CONFIG_DIR, propFile);
            if (propertiesFile.exists()&&propertiesFile.canRead()){

                properties.loadFromXML(new FileInputStream(propertiesFile));
                propertyValue=properties.getProperty(propertyName);
            }
            if (propertyValue==null){

                propertiesFile=new File(SYSTEM_CONFIG_DIR, propFile);
                properties.loadFromXML(new FileInputStream(propertiesFile));
                propertyValue=properties.getProperty(propertyName);
            }
           PROPERTIES_LOGGER.log(propertyName +" in "+ propertiesFile.getAbsolutePath()+ " is "+propertyValue);
             return propertyValue;
        }catch(Exception e){
            PROPERTIES_LOGGER.log("problem loading properties file "+propertiesFile.getAbsolutePath(), e);
            return null;
        }
    }
    public static boolean readBooleanProperty(String propFile, String propertyName){
        //PROPERTIES_LOGGER.log("reading boolean property "+propertyName+" from file "+propFile);
        return (readProperty(propFile, propertyName).equalsIgnoreCase("true"));
    }
    public static void setProperty(String propFile, String propertyName, String value){
        File propertiesFile = null;
        try{
            if(! propFile.endsWith(".xml")){
                propFile=propFile+".xml";
            }
            propertiesFile = new File(USER_CONFIG_DIR, propFile);
            java.util.Properties properties = new java.util.Properties();
            properties.loadFromXML(new FileInputStream(propertiesFile));
            properties.setProperty(propertyName, value);
            properties.storeToXML(new FileOutputStream(propertiesFile),null);
        }catch(Exception e){
            PROPERTIES_LOGGER.log("problem saving properties file "+propertiesFile.getAbsolutePath(), e);
            
        }
    }
}

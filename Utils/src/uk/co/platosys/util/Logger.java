/*
 * Logger.java
 *
 * Created on 29 October 2004, 10:43
 */

package uk.co.platosys.util;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;



/**
 * This is a really simple logger. The single-arg constructor - which takes a string argument "name" creates or opens a logfile called name_YYYY_MM_DD.log in /var/log/domuse (TODO: make the logfile location configurable)
 * so there is one for each day. Alternatively, you can specify an additional boolean "dated" argument, in which
 * if true, it behaves exactly as above; if false, it creates an undated logfile, which
 * with the date specified at entry level rather than file level. 
 * 
 * It lets you specify the loglevel.
 * 
 * It does the logging in a separate thread, so logging doesn't disrupt the main flow of behaviour
 *
 * @author  edward
 */
public class Logger implements Serializable {
    File logFile;
    File logDirectory = new File("/var/log/platosys");//TODO get this from a config file 
    Calendar calendar;
    BufferedInputStream streamBuffer;
    boolean dated;
    private int logLevel=5;
    private String logerror;
    public static final int DEBUG_LEVEL=5;
    public static final int TRACE_LEVEL=4;
    public static final int STATUS_LEVEL=3;
    public static final int WARN_LEVEL=2;
    public static final int ERROR_LEVEL=1;
    public static final int LOG_LEVEL=0;
    public static  int MESSAGE_LIST_SIZE=50;
    public static final String DEFAULT_LOGGER_NAME="platosys";
    private String[] messageList;
    /** Creates a new instance of Logger */
    public Logger (){}
    
    public Logger (String name){
       makeList();
    	makeDatedLogger(name);
    }
    
    public Logger(String name, boolean dated){
      makeList();
      if(dated){makeDatedLogger(name);}
    	else makeUndatedLogger(name);
    }
     private  Logger(String name, String directoryPath, boolean dated){
      this.logDirectory=new File(directoryPath);
      makeList();
      if(dated){makeDatedLogger(name);}
    	else makeUndatedLogger(name);
    }
    public static Logger getLogger(String name, String directoryPath, boolean dated){
        return new Logger(name, directoryPath, dated);
    }
    public static Logger getDefaultLogger(){
        return new Logger(DEFAULT_LOGGER_NAME, true);
    }
    public static Logger getLogger(String name){
        Logger logger = new Logger(name, true);
        //logger.log(1, "logger instance "+name+" created");
        return logger;
    }
    public static Logger getSecurityLogger(String name){
        return getLogger(name);
    }
    private void makeUndatedLogger(String name){
    	dated=false;
    	logFile = new File(logDirectory, name+".log");
      try {
          if (!(logFile.exists())){
              logFile.createNewFile();
          }
          if (!logFile.canWrite()){
              
          }
      }catch(Exception e){
      list("Exception creating logger: "+e.toString()+":"+e.getStackTrace().toString());
      }
    }
    private void makeDatedLogger(String name)  {
    	dated=true;
    	String date = new ISODate().dateString();
      logFile = new File(logDirectory, name+"_"+date+".log");
      try {
          if (!(logFile.exists())){
              logFile.createNewFile();
          }
          if (!logFile.canWrite()){
              throw new Exception("can't write to log file");
          }
      }catch(Exception e){
          list("Exception creating logger: "+e.toString()+":"+e.getStackTrace().toString());
      }
 }
   public void log(String message) {
   	 try {
   	 	String date ="";
   	 	ISODate isoDate = new ISODate();
       	 if(dated){date = isoDate.timeMsString();}
       	 else{date=isoDate.dateTimeMs();}
       	  String user="unknown";
       	  try{
             user = System.getProperty("user.name");
       	  }catch(Exception e){
             list("Exception while logging: "+e.toString()+":"+e.getStackTrace().toString());
          }
            Writer writer = new FileWriter(logFile, true);
            String logString = (date+"\t"+user+"\t"+message+"\n");
            list(logString);
            writer.write(logString);
            writer.close();
       }catch(Exception e){
    	   list("Exception logging entry "+e.toString()+":"+e.getStackTrace().toString());
       }
   }

    public void log(int level, String message){
	   	if (!(level > logLevel)){
	   		switch (level){
	   		case 0:  log (message, "LOG    "); break;
	   		case 1:  log (message, "*ERROR*"); break;
	   		case 2:  log (message, "WARNING"); break;
	   		case 3:  log (message, "STATUS "); break;
	   		case 4:  log (message, "TRACE  "); break;
	   		case 5:  log (message, "DEBUG  "); break;
	   		default: log (message, "DEBUG!!"); break;
	   		}
	   	}
	   }
   public void log(String message, String level) {
        String user="unknown";
	   	 try {
	   	 	String date ="";
	   	 	ISODate isoDate = new ISODate();
	       	 if(dated){date = isoDate.timeMsString();}
	       	 else{date=isoDate.dateTimeMs();}
	       	
	       	 try{
	            user = System.getProperty("user.name");
	       	 }catch(Exception e){
                     list("Exception getting user"+e.toString()+":"+e.getStackTrace().toString());
                 }
	            Writer writer = new FileWriter(logFile, true);
                    String logString=(date+"\t"+user+"\t"+level+"\t"+message+"\n");
                    list(logString);
                    writer.write(logString);
	            writer.close();
	       }catch(Exception e){
	    	   list("Exception while logging - user: "+user+":"+e.toString()+":"+e.getStackTrace().toString());
	       }
	   }
   public void log(String message, Exception e){
   	log(1, message + "- cause: " + e.getClass().toString());
        log( e.getMessage());
        StackTraceElement[] stel=e.getStackTrace();
        for(int i=0;i<stel.length;i++){
            log(stel[i].toString());
        }
   }
   public void list(Collection collection){
       log (5, "Listing Collection: "+collection.toString());
       Iterator it =collection.iterator();
       while(it.hasNext()){
           String string;
            try{
                string = (String)it.next();
            }catch(ClassCastException cce){
                string=it.toString();
            }
           log(5, string);
       }
   }
   public PrintWriter getWriter(){
	   try{
		   return new PrintWriter(new FileWriter(logFile, true));
	   }catch(Exception e){
		   log ("Logging Exception", e);
		   return null;
	   }
	   
   }
   public String getLogFileName(){
	   return logFile.getAbsolutePath();
   }
   public String getLogDirectory(){
	   return logDirectory.getAbsolutePath();
   }
   public String getLogerror(){
	   return logerror;
   }
   public void log (InputStream stream) {
       try{
            Timer timer = new Timer();
            streamBuffer = new BufferedInputStream(stream);
            timer.scheduleAtFixedRate(new LogTask(), new Date(), 500);
       }catch(Exception e){
       }
   }
   public class LogTask extends TimerTask {
          public void run(){
              try {
                    byte[] byteArray = new byte[streamBuffer.available()];
                    streamBuffer.read(byteArray);
                    if (byteArray.length > 0){
                        log(new String(byteArray));
                    }
              }catch (Exception e){
              list("Logging exception in LogTask thread"+e.toString()+":"+e.getStackTrace().toString());
              }//horrid kluj
           }
   }
   private void list(String logString){
       if (messageList==null){
           messageList=new String[MESSAGE_LIST_SIZE];
       }
       for (int i=(messageList.length-1); i>0; i--){
           messageList[i]=messageList[i-1];
       }
       messageList[0]=logString;
   }
   private void makeList(){
       messageList=new String[MESSAGE_LIST_SIZE];
       for (int i=0; i<messageList.length; i++){
           messageList[i]="";
       }
   }
   public String[] getMessageList(){
       return messageList;
   }
 
   
  
}
  
    

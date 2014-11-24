/*
 * Shell.java
 *
 * Created on 29 October 2004, 09:54
 * Updated 25 sep 2015 to replace Vector with ArrayList<String>
 */

package uk.co.platosys.util;
import java.util.ArrayList;
import java.io.*;

/**
 * This is a utility to issue commands to the native system in which the JVM operates. 
 * It obtains and  manages a java.lang.Process object, logs actions and errors via two separate Logger
 * objects,
 * It logs all errors; and the system calls depending on the debug level.
 * 
 * Internally, it keeps command tokens in a Vector, so you can add as many tokens such as 
 * command-line options as you need. This lets you build up, quite simply, an arbitrarily
 * complex command.  
 * 
 * If getting the process's standard or error output is important (stdout/stderr),
 * then you will need to know whether it produces multiline or single line output on 
 * each of those streams. The constructor requires two boolean parameters; 
 * generally, it will work OK if these are both false but if you need 
 * to get hold of screens of output stuff then try setting either to true.
 * 
 *
 * 
 */
public class SystemCommand extends Thread {
   /**
     */
    private Runtime runtime = Runtime.getRuntime();//the current runtime object
    private Process process;//the process managed by this class
    private ArrayList<String> commands = new ArrayList<String>(); //the command array to execute
    private ArrayList<String> outputVector = new ArrayList<String>();
    private ArrayList<String> errorVector = new ArrayList<String>();
    private Logger logger = new Logger("system");//the logger used to log routine commands
    private Logger errorLogger= new Logger("system_error");//the error logger
    private BufferedReader inBuffer;//holds standard input from the process
    private BufferedReader errorBuffer;//holds error input from the process
    //private int tokenIndex=0;
    private int exitValue;
    private String outputString="";
    private String errorString="";
    private String errorMessage="";
    private String outputMessage="";
    //private String newLine = System.getProperty("line.separator");
    private boolean running = false;
    private boolean finished = false;
    private boolean mlout = false;
    private boolean mlerror = false;
    //private boolean strace = true;//a deep system debug option using the strace tool, which must be installed.

    /** Creates a new SystemCommand.
     * If the command produces only a single line output (e.g.
     * cd-discid) then multiline must be false.
     * 
     *    
     */
    public SystemCommand(boolean mlout, boolean mlerror)  {
    	this.mlout=mlout;
    	this.mlerror= mlerror;
      
        
    }

    public void exec(ArrayList<String> commands)  throws Exception {
        try {
        	   this.commands=commands;
        	   start();
        }catch(Exception e){
        	errorLogger.log("problem executing command:" + (String)commands.get(0), e);
        	throw new  Exception("problem executing command: "+(String)commands.get(0), e);
        }
    }
        	
    public void run(){
    	 try{
    	 	   String[] commandArray = new String[commands.size()];
    	 	   String commandString="";
    	 	   for (int i=0; i<commands.size(); i++){
    	 	   	commandArray[i]= commands.get(i);
    	 	   	commandString=commandString+" "+commandArray[i];
    	 	   }
    	 	   try {
    	 	   	logger.log("running command: "+commandString);
    	 	   	process=runtime.exec(commandArray);
    	 	   	logger.log(process.toString());
    	 	   }catch (Exception e){errorLogger.log("runtime problem",e);}
    	 		if (process!=null){running = true;}
            inBuffer = new BufferedReader (new InputStreamReader(process.getInputStream()));
            errorBuffer = new BufferedReader (new InputStreamReader(process.getErrorStream()));
            
        }catch (Exception e){
           	errorLogger.log("problem starting command:" + (String)commands.get(0), e);
        }
        try{
        		while(running){
        			//logger.log("running, multiline="+Boolean.toString(multiline));
        			while(inBuffer.ready()){
        				if (mlout) {outputVector.add(inBuffer.readLine());}
        				else{
        					char ch = (char) inBuffer.read();
        					//logger.log ("added "+ch);
        					outputString += ch;
          			}
        			}
        			while(errorBuffer.ready()){
        				if (mlerror){errorVector.add(errorBuffer.readLine());}
        				else {
        				  errorString += (char) errorBuffer.read();
        			//	  errorVector.add(errorString);
        				}
        			}
        			try{
        				exitValue=process.exitValue();//throws error if not finished
        				running=false;
        				finished=true;
        			}catch(Exception e){
        			//catch error, do nothing, process hasn't exited yet.
        			}
        		}
        }catch(Exception e){errorLogger.log("problem running command: " + (String)commands.get(0), e);}
    }
    
    //getters and setters
    //
    public Process getProcess(){
    	while (process==null){}
    	return process;
    }
    
    public void addToken(String token){
    	commands.add(token);
    }
    public void kill(){
    	logger.log("command killed?");
    	process.destroy();
    	running=false;
    }
    
    public String getOutputString(){
    	return outputString;
    }
  
    public String getErrorString(){
    	return errorString;
    }
  
    public ArrayList<String> getOutputVector(){
    	return outputVector;
    }
    public ArrayList<String> getErrorVector(){
    	return errorVector;
    }
    
    public int getExitValue(){
    	return exitValue;
    }
    public boolean isFinished(){
    	return finished;
    }
}
 
package uk.co.platosys.platax.server.core;

import java.util.HashMap;

import uk.co.platosys.platax.shared.Message;
import uk.co.platosys.util.PlatosysProperties;

public class SystemMessages  {
   private static HashMap<String, Message> messages = new HashMap<String, Message>();
   public static final String WELCOME = "welcome";
   static final Message WELCOME_MESSAGE = new Message("Hello and Welcome to Platax");
   public static final String COMPANY = "You have selected for-profit company. You will be prompted for the details of directors, etc later and should give them access" +
   		"to ";
   static final Message COMPANY_MESSAGE = new Message("You have selected for-profit company. You will be prompted for the details of directors, etc later.");
   public static final String PROPERTY = "property";
   static final Message PROPERTY_MESSAGE = new Message("This will set up a chart of accounts etc suitable for a business owning and managing properties for rental and for capital gains");
   public static final String CATERING = "catering";
   static final Message CATERING_MESSAGE = new Message("This will set up a chart of accounts suitable for a restaurant and catering business");
   public static final String TERMS = "terms";
   static final Message TERMS_MESSAGE = new Message("By signing up to Platax you are agreeing to the terms and conditions of the service");
   static {
    	messages.put(WELCOME, WELCOME_MESSAGE);
    	messages.put(PROPERTY,  PROPERTY_MESSAGE);
    	messages.put(COMPANY, COMPANY_MESSAGE);
    	messages.put(CATERING, CATERING_MESSAGE);
    	messages.put(TERMS, TERMS_MESSAGE);
    }
    
    
    
    
    
    
    
    
   public static Message getMessage(String key){
	   return messages.get(key);
   }
  	 
}

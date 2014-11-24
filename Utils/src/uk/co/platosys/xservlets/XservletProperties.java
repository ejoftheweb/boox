/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.xservlets;

import uk.co.platosys.util.Logger;
import uk.co.platosys.util.PlatosysProperties;

/**
 *
 * @author edward
 */
public class XservletProperties extends PlatosysProperties {
   public static String APPLICATION_NAME=readProperty("application.name");
   
   public static Logger XSERVLET_LOGGER=Logger.getLogger(APPLICATION_NAME);
   protected static String RESET_PASSWORD_LINK= readProperty("link.reset.password");
   protected static String DEVELOPER_EMAIL = readProperty("email.developer");
   protected static String INSTALLATION_EMAIL = readProperty("email.installation");
   protected static String MAIL_HOST= readProperty("mail.host");
   private static String readProperty(String propertyName){
      return readProperty("xservlets.xml", propertyName);
  }
}

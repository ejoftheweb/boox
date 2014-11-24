/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.xmail;

import uk.co.platosys.util.Logger;
import uk.co.platosys.util.PlatosysProperties;
import uk.co.platosys.xservlets.XservletProperties;

/**
 *
 * @author edward
 */
public class XmailProperties extends PlatosysProperties {
    public static final String INSTALLATION_HOST=readProperty("installation.host");
    public final static String APPLICATION_NAME=XservletProperties.APPLICATION_NAME;
    public final static String MAIL_HOST=readProperty("mail.host");
    public static final String ADMINSISTRATOR_EMAIL=readProperty("administrator.email");
    public static final boolean CC_SUPPORT=readBooleanProperty("cc.support");
    public static final Logger XMAIL_LOGGER = XservletProperties.XSERVLET_LOGGER;
    
private static String readProperty(String propertyName){
    
      return readProperty("xservlets.xml", propertyName);
  }

private static boolean readBooleanProperty(String propertyName){
    return readBooleanProperty("xservlets.xml", propertyName);
}
}
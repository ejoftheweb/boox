package uk.co.platosys.xuser;

import uk.co.platosys.util.PlatosysProperties;

public class XuserConstants extends PlatosysProperties {
 /**
	 * 
	 */
private static final long serialVersionUID = -6909855421570502449L;
protected static boolean EMAIL_IS_USERNAME=false;
 protected static String DATABASE_NAME = readProperty("xusers", "xuser.database");
 protected static String INSTALLATION_NAME=readProperty("xusers", "xuser.installation");
 protected static String APPLICATION_NAME=readProperty("xusers", "application.name");
 protected static String CONFIRMATION_SERVLET_URL=readProperty("xusers", "xuser.confirmation.servlet");
 protected static String RESET_PASSWORD_LINK=readProperty("xusers", "reset.password");
 protected static String SYSTEM_FROM_EMAIL=readProperty("xusers", "system.from.email");
 protected static String PASSWORD_SERVLET_URL=readProperty("xusers", "xuser.password.servlet");
 protected static String CONFIRMATION_MAIL_FILE=readProperty("xusers", "xuser.confirmation.mail.file");
 protected static String REGISTRATION_MAIL_FILE=readProperty("xusers", "xuser.registration.mail.file");
}

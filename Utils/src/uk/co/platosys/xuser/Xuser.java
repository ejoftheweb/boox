package uk.co.platosys.xuser;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailException;

import uk.co.platosys.db.ColumnNotFoundException;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.RowNotFoundException;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.HashPass;
import uk.co.platosys.util.ISODate;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.RandomString;
import uk.co.platosys.util.URLcleaner;
import uk.co.platosys.xmail.Xmail;
import uk.co.platosys.xmail.XmailException;

/**
 * Xuser models a user.
 * Xuser provides a user-management mechanism for a web-app 
 * Xuser can be extended.
 * 
 * The best way of using Xuser is to extend this class. It is not declared abstract; you can instantiate an Xuser, but for 
 * most webapps, extending it to provide app-specific features you need makes more sense. 
 * 
 * An Xuser is authenticated at login and is then attached to the user session. On logout, or when the session is invalidated, 
 * the xuser disappears.
 * 
 *
 * TODO: Login history
 * TODO: session management
 * 
 * 
 * @author edward
 * 
 */
public class Xuser {
   private String username=null;
   private String email=null;
   private ISODate lastLogin=null;
   private String lastLoginFrom=null;
   private ISODate lastLogout=null;
   private ISODate currentLogin=null;
   private String currentLoginFrom=null;
   public static final String XUSERS_TABLENAME="xusers";
   public static final String XUSERID_COLNAME="xuserid";
  public static final String EMAIL_COLNAME="email";// usersTable.addColumn("email",JDBCTable.TEXT_COLUMN);
  public static final String PASSWORD_COLNAME="password"; //usersTable.addColumn("password", JDBCTable.TEXT_COLUMN);
  public static final String REG_KEY_COLNAME="regkey";//usersTable.addColumn("regkey", JDBCTable.TEXT_COLUMN);
  public static final String REGISTERED_COLNAME="registered";//usersTable.addColumn("registered",JDBCTable.DATE_COLUMN);
  public static final String CONFIRMED_COLNAME="confirmed";//usersTable.addColumn("confirmed", JDBCTable.DATE_COLUMN);
  public static final String USERNAME_COLNAME="username";//usersTable.addColumn("username",JDBCTable.TEXT_COLUMN);
  public static final String FULLNAME_COLNAME="fullname";
  public static final String LASTLOGIN_COLNAME="lastlogin";//usersTable.addColumn("lastlogin", JDBCTable.TIMESTAMP_COLUMN);
  public static final String LASTLOGINFROM_COLNAME="lastloginfrom";//usersTable.addColumn("lastloginfrom", JDBCTable.TEXT_COLUMN);
  public static final String LASTLOGOUT_COLNAME="lastlogout";//usersTable.addColumn("lastlogout",JDBCTable.TIMESTAMP_COLUMN);
  public static final String XCONTACTID_COLNAME="xcontactid";//usersTable.addColumn("xcontactid",JDBCTable.TEXT_COLUMN);
  static final String [] ALL_COLNAMES ={XUSERID_COLNAME, EMAIL_COLNAME, PASSWORD_COLNAME, REG_KEY_COLNAME, REGISTERED_COLNAME, CONFIRMED_COLNAME, 
	  USERNAME_COLNAME, FULLNAME_COLNAME, LASTLOGIN_COLNAME, LASTLOGINFROM_COLNAME,LASTLOGOUT_COLNAME, XCONTACTID_COLNAME};
  static final String [] ADD_COLNAMES ={EMAIL_COLNAME, PASSWORD_COLNAME, REG_KEY_COLNAME, REGISTERED_COLNAME, CONFIRMED_COLNAME, 
	  USERNAME_COLNAME,FULLNAME_COLNAME, LASTLOGIN_COLNAME, LASTLOGINFROM_COLNAME,LASTLOGOUT_COLNAME, XCONTACTID_COLNAME};
  
  private boolean isAuthenticated=false;
   private static boolean emailIsUsername=XuserConstants.EMAIL_IS_USERNAME;
   private String xuserID="dumbxid";
   private static String databaseName = XuserConstants.DATABASE_NAME;
   private static Logger logger = Logger.getLogger(XuserConstants.APPLICATION_NAME);
   private static JDBCTable xusersTable=initTable();
   private String xContactID=null;
  // private XContact xContact=null;
	
	/**
	 * creates an unauthenticated Xuser object.
	 */
    public Xuser(){
    	
    }
	private Xuser(String xuserID) throws XuserException{
		Row row = null;
		try {
		    row = xusersTable.getRow(xuserID);
		}catch(RowNotFoundException rnf1){
			throw new XuserException("unknown Xuser"+xuserID);
		}catch(PlatosysDBException pdbe){
			throw new XuserException("Xuser PDBEx", pdbe);
		}
		try {
			this.xuserID=row.getString(XUSERID_COLNAME);
			this.email=row.getString(EMAIL_COLNAME);
			this.isAuthenticated=false;
			if(!emailIsUsername){
				this.username=row.getString(USERNAME_COLNAME);
			}
			
		}catch (ColumnNotFoundException cnfe){
			throw new XuserException("Bad Xuser Database JDBCTable", cnfe);
		}
	}
	/**
	 * creates an authenticated Xuser
	 * @param username
	 * @param password
	 */
	public Xuser(String username, char[] password, HttpServletRequest request) throws XuserCredentialsException, XuserException {
		//logger.log("Xuser creating xuser "+username);
		Row row = null;
		try {
		    row = xusersTable.getRow(USERNAME_COLNAME,username);
		    //logger.log("found row with username "+username);
		}catch(RowNotFoundException rnf1){
			//logger.log("username not found, checking email");
			try {
				row = xusersTable.getRow(EMAIL_COLNAME, username);
			}catch(RowNotFoundException rnf2){
				//logger.log("couldn't find either");
				throw new XuserCredentialsException("unknown username or email: "+username);
			}
		}
		try {
			String hashedPassword=row.getString(PASSWORD_COLNAME);
			if(HashPass.check(password, hashedPassword)){
				this.xuserID=row.getString(XUSERID_COLNAME);
				this.email=row.getString(EMAIL_COLNAME);
				this.currentLogin=new ISODate();
				this.currentLoginFrom=request.getRemoteHost();
				this.isAuthenticated=true;
				if(!emailIsUsername){
					this.username=row.getString(USERNAME_COLNAME);
				}
				try{
					this.lastLogin =xusersTable.readTimeStamp(xuserID, LASTLOGIN_COLNAME);
					this.lastLoginFrom=xusersTable.readString(xuserID, LASTLOGINFROM_COLNAME);
					this.lastLogout=xusersTable.readTimeStamp(xuserID, LASTLOGOUT_COLNAME);
				}catch(PlatosysDBException pxe ){
				
						logger.log("xuser-database xception", pxe);
				}
				/*try{
					if(row.getString(XCONTACTID_COLNAME)!=null){
						this.xContactID=row.getString(XCONTACTID_COLNAME);
						this.xContact=new XContact(xContactID);
					}
				}catch(Exception x){
					logger.log("xuser-xcontact exception", x);
				}*/
			}else{
				logger.log("xuser credentials exception bad password");
				throw new XuserCredentialsException("Bad Password");
			}
		}catch (ColumnNotFoundException cnfe){
			throw new XuserException("Bad Xuser Database JDBCTable", cnfe);
		} catch (RowNotFoundException e) {
		    logger.log("corrupt database - missing xuserID row");
		}
	}
	/**
	 * this method authenticates a non-authenticated Xuser with a username and password.
	 * @param username
	 * @param password
	 * @return
	 */
	public String authenticate (String username, char[] password)throws XuserCredentialsException, XuserException{
		Row row = null;
		try {
		    row = xusersTable.getRow(USERNAME_COLNAME,username);
		}catch(RowNotFoundException rnf1){
			try {
				row = xusersTable.getRow(EMAIL_COLNAME, username);
			}catch(RowNotFoundException rnf2){
				throw new XuserCredentialsException("unknown username or email: "+username);
			}
		}
		try{
			String hashedPassword=row.getString(PASSWORD_COLNAME);
			if(HashPass.check(password, hashedPassword)){
				this.xuserID=row.getString(XUSERID_COLNAME);
				this.email=row.getString(EMAIL_COLNAME);
				this.currentLogin=new ISODate();
				this.currentLoginFrom="unknown host";
				this.isAuthenticated=true;
				if(!emailIsUsername){
					this.username=row.getString(USERNAME_COLNAME);
				}
				try{
					this.lastLogin =xusersTable.readTimeStamp(xuserID, LASTLOGIN_COLNAME);
					this.lastLoginFrom=xusersTable.readString(xuserID, LASTLOGINFROM_COLNAME);
					this.lastLogout=xusersTable.readTimeStamp(xuserID, LASTLOGOUT_COLNAME);
				}catch(PlatosysDBException pxe ){
				
				
				}
			}else{
				throw new XuserCredentialsException("Bad Password");
			}
		}catch (ColumnNotFoundException cnfe){
			throw new XuserException("Bad Xuser Database JDBCTable", cnfe);
		} catch (RowNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xuserID;
	}
	/**
	 * This method is used to disauthenticate (logout) an Xuser - call setAuthenticated(false). 
	 * If you try to call setAuthenticated(true) it will throw an exception, so don't. 
	 * @param authenticator
	 * @throws XuserException
	 */
	public void setAuthenticated(boolean authenticator) throws XuserException{
		if(authenticator){
			throw new XuserException("boolean argument to setAuthenticated must be false");
		}else{
			try{
			xusersTable.amend(xuserID, LASTLOGIN_COLNAME, currentLogin);
			xusersTable.amend(xuserID, LASTLOGINFROM_COLNAME, currentLoginFrom);
			xusersTable.amend(xuserID, LASTLOGOUT_COLNAME, new ISODate());
			this.isAuthenticated=false;
			}catch(Exception x){
				logger.log("problem disauthenticationg user", x);
			}
		}
	}
	public boolean isAuthenticated(){
		return isAuthenticated;
	}
	public String getUsername(){
		logger.log("xuser: getUsername");
		return this.username;
	}
	public String getEmail(){
		return this.email;
	}
	/**
	 * registers a user and returns a registration key.
	 * @param email
	 * @param password
	 * @return
	 * @throws XuserException
	 * @throws XuserExistsException
	 */
	public static String register (String email, char [] password) throws XuserException, XuserExistsException {
		if (!emailIsUsername){
			throw new XuserException("Xuser invalid registration method for this setup, need username and email");
		}
		if(!JDBCTable.tableExists(databaseName, XUSERS_TABLENAME)){
			xusersTable=initTable();
			
		}else if (xusersTable==null){
			try {
				xusersTable=new JDBCTable(databaseName,XUSERS_TABLENAME, XUSERID_COLNAME);
			} catch (PlatosysDBException e) {
				logger.log("Xuser - problem inititalising table", e);
				throw new XuserException("could not initialise xusers JDBCTable",e);
			}
		}
		if(xusersTable.rowExists(EMAIL_COLNAME, email)){
			throw new XuserExistsException("mail already exists");
		}else{
			String xuserID=RandomString.getRandomKey();
			String regKey=RandomString.getRandomKey();
			String hashedPass=HashPass.hash(password);
			String[] columns={XUSERID_COLNAME, EMAIL_COLNAME,PASSWORD_COLNAME, REG_KEY_COLNAME};
			String[] values={xuserID, email, hashedPass, regKey};
			try {
				xusersTable.addRow(columns, values);
				xusersTable.amend(xuserID, REGISTERED_COLNAME , new ISODate());
			} catch (PlatosysDBException e) {
				logger.log("Xuser- problem adding row to xusers table", e);
				throw new XuserException("could not add new row to xusers JDBCTable",e);
			}
			return regKey;
			
		}
		
	}
	/**
	 * registers a user  and sends an email containing a clickable link to the given email address .
	 * @param username - the user name, if different from the email address
	 * @param email - the user's email address
	 * @param password - the password, which is stored hashed and salted. 
	 * @return
	 * @throws XuserException
	 * @throws XuserExistsException
	 */
	public static String register ( String email, String username, String fullname, char [] password) throws XuserException, XuserExistsException {
		if (emailIsUsername && !username.equals(email)){
			throw new XuserException("invalid constructor, only need one identifier");
		}	
		if(!JDBCTable.tableExists(databaseName, "xusers")){
			xusersTable = initTable();
			
		}else if (xusersTable==null){
			try {
				xusersTable=new JDBCTable(databaseName,XUSERS_TABLENAME, XUSERID_COLNAME);
			} catch (PlatosysDBException e) {
				throw new XuserException("could not initialise xusers JDBCTable",e);
			}
		}
		if(xusersTable.rowExists(EMAIL_COLNAME, email)){
			throw new XuserExistsException("mail already exists");
		}else{
			String xuserID=RandomString.getRandomKey();
			String regKey=RandomString.getRandomKey();
			String hashedPass=HashPass.hash(password);
		    String[] columns={XUSERID_COLNAME,EMAIL_COLNAME,PASSWORD_COLNAME, REG_KEY_COLNAME, USERNAME_COLNAME, FULLNAME_COLNAME};
			String[] values={xuserID, email, hashedPass, regKey, username, fullname};
			try {
				xusersTable.addRow(columns, values);
			
			xusersTable.amend(xuserID, REGISTERED_COLNAME, new ISODate());
			} catch (PlatosysDBException e) {
				throw new XuserException("could not add xuser row to xusers JDBCTable",e);
			}
			logger.log("Xuser "+username+" entered in table, sending email");
			registerEmailXuser(fullname, email, regKey);
			return regKey;
		}
	}
    /**
     * returns the xuserID if confirmation works, or throws an XuserCredentialsException
     * @param email
     * @param regKey
     * @return
     */
	public static String confirmXuser ( String email, String regKey) throws XuserCredentialsException, XuserException{
		String xuserid = getXuserID(email);
		String key = null;
		key = getParameter(xuserid, REG_KEY_COLNAME);
		if (key.equals(regKey)){
			try {
				xusersTable.amend(xuserid, CONFIRMED_COLNAME, new ISODate());
				xusersTable.amend(xuserid, LASTLOGIN_COLNAME,new ISODate());
				xusersTable.amend(xuserid, LASTLOGOUT_COLNAME, new ISODate());
				xusersTable.amend(xuserid, LASTLOGINFROM_COLNAME, "confirmation");
			} catch (PlatosysDBException e) {
				logger.log("XconfirmX - date amend issue", e );
				throw new XuserException("problem registering confirmation code", e);
			}
			try {
				confirmEmailXuser( email);
			}catch (XuserException xe){
				logger.log("problemn with confirmation email", xe);
				throw xe;
			}
			return xuserid;
		}else{
			throw new XuserCredentialsException ("confirmation failed, bad registration key");
		}
	}
	/**
	 *  This is the method that actually sends the email to the user.
	 *  
	 * @param email
	 * @param regkey
	 * @throws XuserException
	 */
	private static boolean registerEmailXuser(String name, String email, String regkey) throws XuserException {
		logger.log("Xuser starting email sending method emailing to "+email);
		Xmail xmail;
		try {
			xmail = Xmail.build(new File(XuserConstants.REGISTRATION_MAIL_FILE), true);
		} catch (XmailException e1) {
			logger.log("XuserREX- problem build xmail", e1);
			throw new XuserException("Problem building xmail", e1);
		}
		String regurl=XuserConstants.CONFIRMATION_SERVLET_URL+"?"+"email="+email+"&regkey="+regkey;
		regurl=URLcleaner.safeURL(regurl);
		logger.log("registration url = "+regurl);
		try {
			xmail.addTo(email);
			xmail.setFrom(XuserConstants.SYSTEM_FROM_EMAIL);
			//TODO: move this email text to a config file.
			xmail.prepend("Thank you for registering at "+XuserConstants.INSTALLATION_NAME);
			xmail.append("To confirm your registration and agree these terms, please click on this link: \n");
			xmail.append(regurl);
			xmail.replaceHolders("name", name);
			xmail.replaceHolders("email", email);
		}catch(EmailException x){
			logger.log("Email exception",x);
			throw new XuserException("creating registration email failed", x);
		}try{
			logger.log("now sending");
			xmail.send();
			logger.log("Xuser - registration email sent");
			return true;
		} catch (EmailException e) {
			logger.log("Email exception",e);
			throw new XuserException("sending registration email failed: check network for smtp connection?");
		}
	}
	
	private static void resetPasswordEmailXuser(String email, String newPass) throws XuserException {
		Xmail xmail = new Xmail();
		String regurl=XuserConstants.PASSWORD_SERVLET_URL+"?"+"email="+email+"&newpass="+newPass;
		regurl=URLcleaner.safeURL(regurl);
		try {
			xmail.addTo(email);
			xmail.setFrom(XuserConstants.SYSTEM_FROM_EMAIL);
			//TODO: move this email text to a config file.
			String msg ="Your password at "+XuserConstants.INSTALLATION_NAME+" has been reset \n";
			msg=msg+"Please click on the link which follows to create a new password \n";
			msg=msg+regurl;
			xmail.setMsg(msg);
		}catch(EmailException x){
			logger.log("Email exception",x);
			throw new XuserException("creating password reset email failed", x);
		}try{
			xmail.send();
			
		} catch (EmailException e) {
			logger.log("Email exception",e);
			throw new XuserException("sending password reset email failed: check network for smtp connection?");
		}
	}
	public static void confirmEmailXuser(String email) throws XuserException {
		Xmail xmail;
		try{
			xmail = Xmail.build(new File(XuserConstants.CONFIRMATION_MAIL_FILE), true);
		}catch(XmailException xme){
			throw new XuserException("", xme);
		}
		//String regurl=XuserConstants.CONFIRMATION_SERVLET_URL+"?"+"email="+email+"&regkey="+regkey;
		try {
			xmail.addTo(email);
			xmail.setFrom(XuserConstants.SYSTEM_FROM_EMAIL);
			xmail.prepend("Thank you for registering at "+XuserConstants.INSTALLATION_NAME);
			
			xmail.append("You should now be able to log in using "+email+"\n and the password you gave when you registered\n");
			xmail.send();
			
		} catch (EmailException e) {
			logger.log("XU confirmation email exception", e);
			throw new XuserException("XU sending confirmation email failed", e);
		}
	}
	/**
	 * 
	 * @param xuserid
	 * @param msg
	 */
	public static void emailXuser (String xuserid, String msg){
	//TODO
	}
	/**
	 * this is kind of critical.
	 * @param email
	 */
	public static void resetPassword(String email)throws XuserException{
		String xuserID = getXuserID(email);
		String newPass=RandomString.getRandomString(8);
		
		try {
			xusersTable.amend(xuserID, PASSWORD_COLNAME, HashPass.hash(newPass));
		} catch (PlatosysDBException e) {
			throw new XuserException("XU-rp problem", e);
		}
				resetPasswordEmailXuser(email, newPass);
		
	}
	
	public static void changePassword (String username, char[] oldPassword, char[] newPassword){
	//TODO
	}
	public static Xuser getXuser(String email, char[] password, HttpServletRequest request) throws XuserCredentialsException, XuserException{
	   return new Xuser (email, password, request);	
	}
	public static Xuser getXuser(String xuserID) throws XuserException{
		   return new Xuser (xuserID);	
		}
	private static JDBCTable initTable(){
		JDBCTable usersTable=null;
		if(!JDBCTable.tableExists(databaseName, XUSERS_TABLENAME)){
			try {
				usersTable=JDBCTable.createTable(databaseName, XUSERS_TABLENAME, XUSERID_COLNAME,JDBCTable.TEXT_COLUMN);
			} catch (PlatosysDBException e) {
				logger.log("problem creating xusers JDBCTable", e);
				
			}
			usersTable.addColumn(EMAIL_COLNAME,JDBCTable.TEXT_COLUMN);
			usersTable.addColumn(PASSWORD_COLNAME, JDBCTable.TEXT_COLUMN);
			usersTable.addColumn(REG_KEY_COLNAME, JDBCTable.TEXT_COLUMN);
			usersTable.addColumn(REGISTERED_COLNAME,JDBCTable.DATE_COLUMN);
			usersTable.addColumn(CONFIRMED_COLNAME, JDBCTable.DATE_COLUMN);
			usersTable.addColumn(USERNAME_COLNAME,JDBCTable.TEXT_COLUMN);
			usersTable.addColumn(FULLNAME_COLNAME, JDBCTable.TEXT_COLUMN);
			usersTable.addColumn(LASTLOGIN_COLNAME, JDBCTable.TIMESTAMP_COLUMN);
			usersTable.addColumn(LASTLOGINFROM_COLNAME, JDBCTable.TEXT_COLUMN);
			usersTable.addColumn(LASTLOGOUT_COLNAME,JDBCTable.TIMESTAMP_COLUMN);
			usersTable.addColumn(XCONTACTID_COLNAME,JDBCTable.TEXT_COLUMN);
			return usersTable;
		}else{
			try {
				usersTable=new JDBCTable(databaseName,XUSERS_TABLENAME,XUSERID_COLNAME);
			} catch (PlatosysDBException e) {
				logger.log("problem initialising xusers JDBCTable",e);
			}
		   return usersTable;	
		}
	}
	
	public ISODate getLastLogin() {
		return lastLogin;
	}
	
	public String getLastLoginFrom() {
		return lastLoginFrom;
	}
	
	public ISODate getLastLogout() {
		return lastLogout;
	}
	public String getXuserID(){
		logger.log("xuser xuserid is "+xuserID);
		return xuserID;
	}
	private static String getXuserID(String email) throws XuserException{
		 if (xusersTable==null){
				try {
					xusersTable=new JDBCTable(databaseName,XUSERS_TABLENAME, XUSERID_COLNAME);
				} catch (PlatosysDBException e) {
					throw new XuserException("XUgXID-could not initialise xusers JDBCTable",e);
				}
			}
		 Row row = null;
		try {
			row = xusersTable.getRow(EMAIL_COLNAME, email);
		} catch (RowNotFoundException e) {
			logger.log("XUgXID - row not found", e );
			throw new XuserException ("email not registered", e);
		}
		
		String xuserid = null;
		try {
			xuserid = row.getString(XUSERID_COLNAME);
		} catch (ColumnNotFoundException e) {
			logger.log("XUgXID - xuserid col not found", e );
			throw new XuserException("bad database JDBCTable, missing xuserid column", e);
			//this should never be thrown.
		}
		return xuserid;
	}
	private static String getParameter(String xuserID, String colname) throws XuserException{
		 if (xusersTable==null){
				try {
					xusersTable=new JDBCTable(databaseName,XUSERS_TABLENAME, XUSERID_COLNAME);
				} catch (PlatosysDBException e) {
					throw new XuserException("XUgXID-could not initialise xusers JDBCTable",e);
				}
			}
		 Row row = null;
		try {
			row = xusersTable.getRow(XUSERID_COLNAME, xuserID);
		} catch (RowNotFoundException e) {
			logger.log("XUgPAR - row not found", e );
			throw new XuserException ("email not registered", e);
		}
		
		String parameter = null;
		try {
			parameter = row.getString(colname);
		} catch (ColumnNotFoundException e) {
			logger.log("XUgPAR  parameter col not found", e );
			throw new XuserException("bad database JDBCTable, missing "+colname+" column", e);
			//this should never be thrown.
		}
		return parameter;
	}
	/**
	 * @return the xContactID
	 */
	public String getXContactID() {
		return xContactID;
	}
	
}

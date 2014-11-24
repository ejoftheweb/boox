/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.platosys.platax.server.core;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.CredentialsException;
import uk.co.platosys.boox.trade.Invoice;
import uk.co.platosys.db.ColumnNotFoundException;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.RowNotFoundException;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.platax.server.services.EnterpriseServiceImpl;
import uk.co.platosys.platax.shared.Constants;
import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.exceptions.PlataxException;
import uk.co.platosys.util.HashPass;
import uk.co.platosys.util.Logger;
import uk.co.platosys.xuser.Xuser;
import uk.co.platosys.xuser.XuserCredentialsException;
import uk.co.platosys.xuser.XuserException;
import uk.co.platosys.xuser.XuserExistsException;

/**
 * A plataxUser is a user of platax. 
 * He/she can be a clerk in any number of enterprises.
 * 
 * He/she can also be a "member" of the Platax Service, and thus has additional rights.
 * 
 * 
 * @author edward
 */

public class PlataxUser extends Xuser {
	public static final String GUEST_STATUS="guest";
	public static final String MEMBER_STATUS="member";
	
	private PXUser pxuser;
	private Map<String, Enterprise> enterprises = new HashMap<String, Enterprise>();
	private Map<String, Invoice> invoices = new HashMap<String, Invoice>();
	private static Logger logger  = Logger.getLogger("platax");
	
	private PlataxUser(){}
	
	
	private PlataxUser(String name, char[] password, HttpServletRequest request) throws XuserCredentialsException, XuserException{
		super(name, password, request);
		//logger.log(4, "pxuser superclass constructor called");
		logger.log(4,"created pxuser with username"+getUsername() );
		logger.log(4, "created pxuser with xuserid "+getXuserID());
		//populate the enterprise array!
		try{
			JDBCTable clerkstable;
			if (!JDBCTable.tableExists(Constants.DATABASE_NAME, "clerks")){
				clerkstable=createClerksTable();
			}else{
				clerkstable = new JDBCTable(Constants.DATABASE_NAME, "clerks");
			}
			
			List<Row> rows = clerkstable.getRows("xuserid", getXuserID());
			Iterator<Row> rit = rows.iterator();
			while(rit.hasNext()){
				Row row = rit.next();
				String enterpriseID = row.getString("enterprise");
				logger.log("PXconstructor: finding enterprise with id "+enterpriseID);
				Enterprise enterprise = Enterprise.getEnterprise(enterpriseID);
				logger.log("PXconstructor: adding enterprise "+enterprise.getName());
				enterprises.put(enterprise.getEnterpriseID(), enterprise);
			}
			
		}catch(Exception x){
			logger.log("PltaxUser: might have been an issue with the clerkstable", x);
		}
		
		pxuser = new PXUser();
		pxuser.setAuthenticated(true);
		pxuser.setUsername(getUsername());
		pxuser.setLastLogin(getLastLogin().dateTimeMs());
		pxuser.setLastLoginfrom(getLastLoginFrom());
		pxuser.setLastLogout(getLastLogout().dateTimeMs());
		Iterator<String> eit = enterprises.keySet().iterator();
		while(eit.hasNext()){
			try{
				Enterprise enterprise = enterprises.get(eit.next());
				Clerk clerk = getClerk(enterprise);
				pxuser.addEnterprise(EnterpriseServiceImpl.convert(enterprise, clerk));
				logger.log("PlataxxUser: pxuser has "+pxuser.getNoOfEnterprises()+" enterprises");
			}catch(Exception x){
				logger.log("plataxuser threw exception", x);
			}
		}
	}
    public static PlataxUser getPlataxUser(String name, char[] password, HttpServletRequest request) throws PlataxException, XuserCredentialsException{
           try {
        	   logger.log("creating new PlataxUser "+name);
			return new PlataxUser(name, password, request);
		} catch (XuserCredentialsException e) {
			logger.log("plataxUser.gpu threw XuserCredentialsXcption", e);
			throw e;
		} catch (XuserException e) {
			logger.log("plataxUser.gpu threw XuserExecption", e);
			throw new PlataxException("plataxUser.gpu threw XuserException",e);
		}
		//return null;
    }
    boolean authenticated=false;
    String name;
  
    
    
    /**
     * Returns an *authenticated* Clerk object associated with this plataxUser at the given Enterprise
     * 
     * @param enterprise
     * @return 
     */
    public Clerk getClerk(Enterprise enterprise) throws PlataxException{
    	/*This implementation opens a mahoosive security hole!
    	 * [cleartext passwords stored in clerkstable]
    	 * TODO: close it!
    	 */
    	JDBCTable clerkstable=null;
    	if(!JDBCTable.tableExists(Constants.DATABASE_NAME, "clerks")){
    		throw new PlataxException("No clerks JDBCTable found in system database");
    	}else{
    		try{
				clerkstable=new JDBCTable(Constants.DATABASE_NAME, "clerks");
			}catch (PlatosysDBException e) {
				logger.log(" gC: problem intialising clerks JDBCTable", e);
				throw new PlataxException(e);
			}
			 
			try {
				String[] cols= {"enterprise", "xuserid"};
				String[] vals= {enterprise.getEnterpriseID(), getXuserID()};
				Row row = clerkstable.getRow(cols, vals);
				String clerkname = row.getString("clerkname");
				String pwd=row.getString("pwd");
				return new Clerk(enterprise, clerkname, pwd);
			} catch (RowNotFoundException e) {
				logger.log("row not found in clerks JDBCTable", e);
				throw new PlataxException(e);
			} catch (ColumnNotFoundException e) {
				logger.log("column not found in clerks JDBCTable", e);
				throw new PlataxException(e);
			} catch (CredentialsException e) {
				logger.log("bad clerk credentials", e);
				throw new PlataxException(e);
			} catch (BooxException e) {
				logger.log("some other shit happened", e);
				throw new PlataxException(e);
			}
    	}
    }
    /**
     * 
     * @param enterprise
     * @param clerk
     * @param password
     */
    public void addEnterprise(Enterprise enterprise, Clerk clerk, String password){
    	logger.log(2, "adding enterprise "+enterprise.getName()+" as clerk "+clerk.getName()+" to pxusr "+ getUsername());
    	JDBCTable clerkstable = null;
    	/*
    	 * note that the passwords are stored here in the clear, which isn't smart. 
    	 * TODO try and figure out a smarter way of doing it.
    	 */
    	if(!JDBCTable.tableExists(Constants.DATABASE_NAME, "clerks")){
    		clerkstable=createClerksTable();
    	}else{
    		try{
				clerkstable=new JDBCTable(Constants.DATABASE_NAME, "clerks");
			}catch (PlatosysDBException e) {
				logger.log("problem intialising clerks JDBCTable", e);
			}
    	}
		String[] cols= {"enterprise", "xuserid", "clerkname","pwd"};
		String[] vals= {enterprise.getEnterpriseID(), getXuserID(), clerk.getName(), password};
		try {
			clerkstable.addRow(cols, vals);
		} catch (PlatosysDBException e) {
			logger.log("problem adding row to clerkstable", e);
		}
    	
    	enterprises.put(enterprise.getEnterpriseID(),enterprise);
    	pxuser.addEnterprise(EnterpriseServiceImpl.convert(enterprise, clerk));
    }

    public boolean isAuthenticated(){
        return authenticated;
    }
    public Collection<Enterprise> getEnterprises(){
        
    	return enterprises.values();
    }
    public boolean isShareholder(Enterprise enterprise){
    	//TODO
    	return false;
    }
    public double getShareholding(Enterprise enterprise){
    	//TODO
    	return 0;
    }
    /**
     * PXUser is a lightweight, serializable distillation of plataxUser, used to convey
     * details to GWT clients
     * @return
     */
    public PXUser getPXUser(){
    	   	
    	
    	return pxuser;
    }
    public static String register(String email, String username, char[] pword, boolean investor)throws XuserException, XuserExistsException{
		logger.log(1, "registering user "+email+" "+username);
		try{
			String regkey = register(email, username, pword);//calls method in superclass Xuser.
			
		if (investor){
			//TODO some more stuff about being an investor
			return regkey;
		}else{
			return regkey;
		}
		}catch (XuserExistsException xee){
			throw xee;
		}catch (XuserException xe){
			logger.log("PXuser registration problem, xe");
			throw xe;
		}catch(Exception e){
			logger.log("PXuser registration problem, e");
		}
		return null;
    }
    private JDBCTable createClerksTable(){
    	logger.log(2, "no clerkstable, creating it");
    	JDBCTable clerkstable;
		try{
			clerkstable=JDBCTable.createTable(Constants.DATABASE_NAME, "clerks", "enterprise",JDBCTable.TEXT_COLUMN, false);
			clerkstable.addColumn("xuserid", JDBCTable.TEXT_COLUMN);
			clerkstable.addColumn("clerkname", JDBCTable.TEXT_COLUMN);
			clerkstable.addColumn("pwd",JDBCTable.TEXT_COLUMN);
			clerkstable.addColumn("status", JDBCTable.TEXT_COLUMN);
			return clerkstable;
		}catch(Exception x){
			logger.log("problem creating clerks JDBCTable", x);
			return null;
		}
    }
    public Enterprise getEnterprise(String enterpriseName) throws PlataxException {
    	if (enterprises.get(enterpriseName)!=null){
    		return (enterprises.get(enterpriseName));
    	}else{
    		throw new PlataxException("plataxUser: enterprise "+enterpriseName+ " is not found here");
    	}
    }
    public Invoice getInvoice(String sin) throws PlataxException {
    	if (invoices.get(sin)!=null){
    		return (invoices.get(sin));
    	}else{
    		throw new PlataxException("plataxUser: invoice "+sin+" is not found here");
    	}
    }
    public void putInvoice(Invoice invoice)throws PlataxException{
    	invoices.put(invoice.getSysname(), invoice);
    }
}

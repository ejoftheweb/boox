package uk.co.platosys.boox.core;

import uk.co.platosys.boox.Body;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.RowNotFoundException;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.ISODate;
import uk.co.platosys.util.Logger;


/**
 * The Directory is a database of Enterprises, trade Customers and trade Suppliers
 * 
 * It is installation-wide, not enterprise-specific. 
 * 
 * @author edward
 *
 */
public class Directory {
	
	//Some constants:
	
	//refers to columns in the directory table
	public static final String SYSNAME_COLNAME="sysname";
	public static final String NAME_COLNAME="name";
	public static final String LEGALNAME_COLNAME="legal_name";
	public static final String DATABASENAME_COLNAME="database_name";
	public static final String RESERVED_COLNAME="reserved";
	public static final String MEMBER_COLNAME="member";
	public static final String TABLENAME = "bx_directory";
	public static final String NAMES_TABLENAME="bx_names";
	static  Logger logger=Logger.getLogger("boox");
    
	static JDBCTable directoryTable=getDirectoryTable();
    static JDBCTable namesTable=getNamesTable();
  
    public static String getName(String sysname) throws PlatosysDBException, RowNotFoundException {
		return directoryTable.readString(sysname, NAME_COLNAME);
	}

	public static String getLegalName(String sysname) throws PlatosysDBException, RowNotFoundException {
		return directoryTable.readString(sysname, LEGALNAME_COLNAME);
	}
	
	public static String getDatabaseName(String sysname) throws PlatosysDBException, RowNotFoundException {
			return directoryTable.readString(sysname, DATABASENAME_COLNAME);
	}
	public static String getSysnameFromName(String name){
		try{
		Row row = directoryTable.getRow(NAME_COLNAME, name);
		return row.getString(SYSNAME_COLNAME);
		}catch(Exception e){
			return null;
	}}
	/**
	 * 
	 * 
	 * @return
	 * @throws PlatosysDBException
	 */
	private static JDBCTable getDirectoryTable() {
		try{
    	JDBCTable directoryTable;
    	if(!(JDBCTable.tableExists(Boox.APPLICATION_DATABASE, TABLENAME))){
    		directoryTable = JDBCTable.createTable(Boox.APPLICATION_DATABASE, TABLENAME, SYSNAME_COLNAME, JDBCTable.TEXT_COLUMN);
    		directoryTable.addColumn(NAME_COLNAME, JDBCTable.TEXT_COLUMN);
    		directoryTable.addColumn(LEGALNAME_COLNAME, JDBCTable.TEXT_COLUMN);
    		directoryTable.addColumn(DATABASENAME_COLNAME, JDBCTable.TEXT_COLUMN);
    		directoryTable.addColumn(MEMBER_COLNAME, JDBCTable.TIMESTAMP_COLUMN);
    	}else{
    		directoryTable=new JDBCTable(Boox.APPLICATION_DATABASE, TABLENAME, SYSNAME_COLNAME);
    	}
    	return directoryTable;
		}catch(Exception e){
			return null;
		}
    }
	 /** 

	 * 
	 * @return
	 * @throws PlatosysDBException
	 */
	private  static JDBCTable getNamesTable() {
		try{
    	JDBCTable namesTable;
    	if(!(JDBCTable.tableExists(Boox.APPLICATION_DATABASE, NAMES_TABLENAME))){
    		namesTable = JDBCTable.createTable(Boox.APPLICATION_DATABASE, NAMES_TABLENAME, NAME_COLNAME, JDBCTable.TEXT_COLUMN);
    		namesTable.addColumn(RESERVED_COLNAME, JDBCTable.TIMESTAMP_COLUMN);
    		namesTable.addColumn(MEMBER_COLNAME, JDBCTable.BOOLEAN_COLUMN);
    	}else{
    		namesTable=new JDBCTable(Boox.APPLICATION_DATABASE, NAMES_TABLENAME, NAME_COLNAME);
    	}
    	return namesTable;
		}catch(Exception e){
			return null;
		}
    }
	/**
     * Checks to see if the name has already been used. 
     * Some applications may need unique system-wide enterprise nicknames, so we 
     * need this method to check that they are so. 
     * Note however that this isn't a necessary constraint for boox, as enterprises are 
     * always referred to by their unique system ID.
     *  
     * @param name
     * @param reserve if the name is to be reserved (for 24 hrs!). 
     * @return true if the name is available for use.
     * @throws BooxException 
     */
    public static boolean isNameOK(String name, boolean reserve) throws BooxException{
    	try{  
        	if(namesTable.rowExists(NAME_COLNAME, name)){
    		   Row row = namesTable.getRow(NAME_COLNAME, name);
    		   if(row.getBoolean(MEMBER_COLNAME)){
    			   return false;
    		   }else{
    			   ISODate date = row.getISODate(RESERVED_COLNAME);
    			   if (date.before(ISODate.getDayAgo())){
    				   if(reserve){
    	    				namesTable.amend(name, RESERVED_COLNAME, new ISODate());
    	    			}
    			       return true;
    			   }else{
    				   return false;
    			   }
    		   }
    		}else{
    			if(reserve){
    				namesTable.amend(name, RESERVED_COLNAME, new ISODate());
    			}
    			return true;
    		}
        }catch(Exception pdbe){
        	throw new BooxException("Directory isNameOK error checking name" +name, pdbe);
        }
    }
    public static boolean bodyExists(String name) {
    	logger.log("Directory checking body exists: "+name );
    	if(directoryTable.rowExists(NAME_COLNAME, name)){
    		return true;
    	}else{
    		return false;
    	}
    }
	public static void addBody(Body body, boolean member) throws PlatosysDBException {
		//non-trade Body s - e.g. private customers and suppliers - are not added to the directory.
		if(body.isTrade()){
			directoryTable.amend(body.getSysname(), NAME_COLNAME, body.getName());
			directoryTable.amend(body.getSysname(), LEGALNAME_COLNAME, body.getLegalName());
			directoryTable.amend(body.getSysname(), DATABASENAME_COLNAME, body.getDatabaseName());
			if(member){
				namesTable.amend(body.getName(), MEMBER_COLNAME, true);
				directoryTable.amend(body.getSysname(), MEMBER_COLNAME, new ISODate());
			}
		}
	}
	public static void addBody(String sysname, String name, String legalName, String databaseName, boolean member) throws PlatosysDBException{
		directoryTable.amend(sysname, NAME_COLNAME, name);
		directoryTable.amend(sysname,  LEGALNAME_COLNAME, legalName);
		directoryTable.amend(sysname, DATABASENAME_COLNAME, databaseName);
		if(member){
			namesTable.amend(name, MEMBER_COLNAME, true);
			directoryTable.amend(sysname, MEMBER_COLNAME, new ISODate());
		}
	}
	public static void addBody(String sysname, String name, String legalName, boolean member) throws PlatosysDBException{
		directoryTable.amend(sysname, NAME_COLNAME, name);
		directoryTable.amend(sysname,  LEGALNAME_COLNAME, legalName);
		directoryTable.amend(sysname, MEMBER_COLNAME, new ISODate());
		if(member){
			namesTable.amend(name, MEMBER_COLNAME, true);
			directoryTable.amend(sysname, MEMBER_COLNAME, new ISODate());
		}
	}
}

package uk.co.platosys.boox.trade;

import java.util.Calendar;








import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.db.ColumnNotFoundException;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.RowNotFoundException;
import uk.co.platosys.db.Table;
import uk.co.platosys.db.jdbc.JDBCRow;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.ISODate;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.ShortHash;

/**
 * Class encapsulating trade terms
 * @author edward
 *
 * TODO serialise via a table.
 *
 */

public class Terms {
	
	static String TERMS_TABLE_NAME ="bx_terms";
	static String TERMS_NAME_COLNAME="name";
	static String TERMS_DAYS_COLNAME="days";
	static String TERMS_QDAYS_COLNAME="qdays";
	static String TERMS_MEND_COLNAME="mend";
	static String TERMS_DISCOUNT_COLNAME="discount";
	static String TERMS_QDISCOUNT_COLNAME="qdiscount";
	static String DEFAULT_TERMS="default";
	static int DEFAULT_DAYS=15;
	static  int DEFAULT_QDAYS=1;
	static boolean DEFAULT_MEND=true;
	static  double DEFAULT_DISCOUNT=0;
	static  double DEFAULT_QUICK_DISCOUNT=0.01;
	static Logger logger = Logger.getLogger("boox");
 int days=15;
 int qDays=1;
 boolean mend=true;
 double discount=0;
 double quickDiscount=0.01;
 String name="DEFAULT";
 Enterprise enterprise;
 

	private Terms(Enterprise enterprise, String name){
		this.name=name;
		this.enterprise=enterprise;
		try{
			if(!JDBCTable.tableExists(enterprise.getDatabaseName(), TERMS_TABLE_NAME)){
				createTerms(enterprise, name);
			}
    		JDBCTable termsTable=JDBCTable.getTable(enterprise.getDatabaseName(), TERMS_TABLE_NAME, TERMS_NAME_COLNAME);
    		try{
    			 Row row = termsTable.getRow(name);
    			 this.days=row.getInt(TERMS_DAYS_COLNAME);
     			this.qDays=row.getInt(TERMS_QDAYS_COLNAME);
     			this.mend=row.getBoolean(TERMS_MEND_COLNAME);
     			this.discount=row.getDouble(TERMS_DISCOUNT_COLNAME);
     			this.quickDiscount=row.getDouble(TERMS_QDISCOUNT_COLNAME);
    		}catch (RowNotFoundException rnfe){
    			createTerms(enterprise, name);
    		}catch (ColumnNotFoundException cnfe){
    			throw new PlatosysDBException("missing col in terms table", cnfe);
    		}
    
    	}catch (PlatosysDBException pde){
    		
    	}
	}
	private Terms(Enterprise enterprise, Row row){
		try {
			this.name=row.getString(TERMS_NAME_COLNAME);
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			logger.log("exception thrown", e);
		} catch (ColumnNotFoundException e) {
			// TODO Auto-generated catch block
			logger.log("exception thrown", e);
		}
		this.enterprise=enterprise;
		try{
			if(!JDBCTable.tableExists(enterprise.getDatabaseName(), TERMS_TABLE_NAME)){
				createTerms(enterprise, name);
			}
    		JDBCTable termsTable=JDBCTable.getTable(enterprise.getDatabaseName(), TERMS_TABLE_NAME, TERMS_NAME_COLNAME);
    		try{
    			
    			 this.days=row.getInt(TERMS_DAYS_COLNAME);
     			this.qDays=row.getInt(TERMS_QDAYS_COLNAME);
     			this.mend=row.getBoolean(TERMS_MEND_COLNAME);
     			this.discount=row.getDouble(TERMS_DISCOUNT_COLNAME);
     			this.quickDiscount=row.getDouble(TERMS_QDISCOUNT_COLNAME);
    		
    		}catch (ColumnNotFoundException cnfe){
    			throw new PlatosysDBException("missing col in terms table", cnfe);
    		}
    
    	}catch (PlatosysDBException pde){
    		
    	}
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public int getqDays() {
		return qDays;
	}
	public void setqDays(int qDays) {
		this.qDays = qDays;
	}
	public boolean isMend() {
		return mend;
	}
	//whether payment period is counted from the month end or the invoice date.
	public void setMend(boolean mend) {
		this.mend = mend;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getQuickDiscount() {
		return quickDiscount;
	}
	public void setQuickDiscount(double quickDiscount) {
		this.quickDiscount = quickDiscount;
	}
   
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public ISODate getDueDate(ISODate valueDate){
		if (mend){
			 valueDate = ISODate.getEndOfMonth(valueDate);
			 
		} 
			return new ISODate (valueDate.getTime()+(days*ISODate.ONE_DAY));
		 
		
	}
	
	  public static Terms getDefaultTerms(Enterprise enterprise){
	    	return new Terms(enterprise, DEFAULT_TERMS);
	    }  
    public static Terms getTerms(Enterprise enterprise, String name) throws PlatosysDBException{
    	JDBCTable termsTable = null;
    	if(!JDBCTable.tableExists(enterprise.getDatabaseName(), TERMS_TABLE_NAME)){
    	     return createTerms(enterprise, name);
    	}else{
    		return new Terms(enterprise, name);
    	}
	}
    
    public void save(){
    	try{
    		JDBCTable termsTable=JDBCTable.getTable(enterprise.getDatabaseName(), TERMS_TABLE_NAME, TERMS_NAME_COLNAME);
    		termsTable.amend(name, TERMS_DAYS_COLNAME, days);
    		termsTable.amend(name, TERMS_QDAYS_COLNAME, qDays);
    		termsTable.amend(name, TERMS_MEND_COLNAME, mend);
    		termsTable.amend(name, TERMS_DISCOUNT_COLNAME, discount);
    		termsTable.amend(name, TERMS_QDISCOUNT_COLNAME, quickDiscount);
    	}catch (PlatosysDBException pde){
    		
    	}
    }
    public static Terms createTerms(Enterprise enterprise, String name) throws PlatosysDBException{
    	//Terms terms = new Terms();
    	JDBCTable termsTable = null;
    	if(!JDBCTable.tableExists(enterprise.getDatabaseName(), TERMS_TABLE_NAME)){
    		try{
    			termsTable = JDBCTable.createTable(enterprise.getDatabaseName(), TERMS_TABLE_NAME, TERMS_NAME_COLNAME, Table.TEXT_COLUMN);
    			termsTable.addColumn(TERMS_DAYS_COLNAME, Table.INTEGER_COLUMN);
    			termsTable.addColumn(TERMS_DISCOUNT_COLNAME, Table.NUMERIC_COLUMN);
    			termsTable.addColumn(TERMS_MEND_COLNAME, Table.BOOLEAN_COLUMN);
    			termsTable.addColumn(TERMS_QDAYS_COLNAME, Table.INTEGER_COLUMN);
    			termsTable.addColumn(TERMS_QDISCOUNT_COLNAME, Table.NUMERIC_COLUMN);
    			termsTable.addRow(TERMS_NAME_COLNAME, DEFAULT_TERMS);
    			termsTable.amend(DEFAULT_TERMS, TERMS_DAYS_COLNAME, DEFAULT_DAYS);
        		termsTable.amend(DEFAULT_TERMS, TERMS_QDAYS_COLNAME,DEFAULT_QDAYS);
        		termsTable.amend(DEFAULT_TERMS, TERMS_MEND_COLNAME, DEFAULT_MEND);
        		termsTable.amend(DEFAULT_TERMS, TERMS_DISCOUNT_COLNAME, DEFAULT_DISCOUNT);
        		termsTable.amend(DEFAULT_TERMS, TERMS_QDISCOUNT_COLNAME, DEFAULT_QUICK_DISCOUNT);
    		}catch (PlatosysDBException pdg){
    			logger.log("error creating terms table", pdg);
    		}
    	}else{
    		try{
    			termsTable=JDBCTable.getTable(enterprise.getDatabaseName(), TERMS_TABLE_NAME, TERMS_NAME_COLNAME);
    		}catch (PlatosysDBException pdg){
    			logger.log("error opening terms table", pdg);
    		}
    	}
  	  //name: should be unique.  
  	  if(termsTable.rowExists(TERMS_NAME_COLNAME, name)){
  		  logger.log("TermsCC: Terms called "+name+" already exists");
  		  //then   Terms of this name already exists so we return it
  		  JDBCRow row;
  		try {
  			row = termsTable.getRow(TERMS_NAME_COLNAME, name);
  		} catch (RowNotFoundException e) {
  			logger.log("Terms.CC: RNF exception thrown", e);
  			//eh? the row exists but exception thrown??
  			throw new PlatosysDBException("Faulty Terms Database: Terms row not found", e);
  		}
  		  try {
  			return new Terms(enterprise, row);
  		} catch (Exception e) {
  			
  			logger.log("exception thrown", e);
  			//col not found exception? Table is buggad. 
  			throw new PlatosysDBException("Terms Database Fault: Terms name col not found", e);
  		}
  	  }else{
  		  //
  		  //Create the Terms:
  		  //logger.log("TermsCC: - creating new Terms:" +TermsName);
  		 if( termsTable.addRow(TERMS_NAME_COLNAME, name)){ //(COLNAME, NAME_COLNAME,TermsName);
	  		 // logger.log("TermsCC: new Terms allocated ID "+Long.toString(id));
	  		  Terms terms = new Terms(enterprise,  name);
	  		  logger.log("TermsCC: created Terms"+terms.getName());
	  		  return terms;
  	     }else{
  	    	 return null;
  	     }
  	  }
    	//return terms;
    }
}

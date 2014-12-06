package uk.co.platosys.boox.core;

import java.util.List;

import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.RowNotFoundException;
import uk.co.platosys.db.Table;
import uk.co.platosys.db.jdbc.JDBCTable;

/**
 *  this  class contains constants referencing the database table holding the chart of accounts.
 *  it also references the bx_chart table.
 * @author edward
 *
 */


public abstract  class Chart {
 static final String TABLENAME="bx_chart";
 static final String SYSNAME_COLNAME="sysname";
 static final String NAME_COLNAME="name";
 static final String FULLNAME_COLNAME="fullname";
 static final String OWNER_COLNAME="owner";
 static final String LEDGER_COLNAME="ledger";
 static final String CURRENCY_COLNAME="currency";
 static final String TYPE_COLNAME="type";
 static final String DESCRIPTION_COLNAME="description";
 static final String[] COLS={SYSNAME_COLNAME,
	 						NAME_COLNAME, 
	 						FULLNAME_COLNAME, 
	 						OWNER_COLNAME, 
	 						LEDGER_COLNAME, 
	 						CURRENCY_COLNAME, 
	 						TYPE_COLNAME, 
	 						DESCRIPTION_COLNAME};	 
public static boolean accountExists (Enterprise enterprise, String sysName) throws PlatosysDBException{
	     try{
	    	 Table table= JDBCTable.getTable(enterprise.getDatabaseName(),TABLENAME);
	            
         	Row row = table.getRow(Chart.SYSNAME_COLNAME, sysName);
 		}catch(RowNotFoundException rnfe){
         	   return false;
            }
         return true;
 }
 public static List<Row> accountRows(Enterprise enterprise) throws PlatosysDBException{
	return getTable(enterprise).getRows();
 }
 public static Table getTable(Enterprise enterprise) throws PlatosysDBException{
	 return JDBCTable.getTable(enterprise.getDatabaseName(),TABLENAME);
 }
 }
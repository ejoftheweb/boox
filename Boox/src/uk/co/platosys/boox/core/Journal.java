/*
 * Journal.java
 *
 *  Copyright (C) 2008  Edward Barrow

 * 
    This class is free software; you can redistribute it and/or
    modify it under the terms of the GNU  General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this program; if not, write to the Free
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * 
 * for more information contact edward.barrow@platosys.co.uk
 
 *Journal.java
 *
 * Created on 24 March 2007, 14:02
 *
 * 
 */

 

package uk.co.platosys.boox.core;

import java.math.BigDecimal;
import java.sql.*;

import uk.co.platosys.boox.money.Currency;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.db.ColumnNotFoundException;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.RowNotFoundException;
import uk.co.platosys.db.SerialTable;
import uk.co.platosys.db.jdbc.ConnectionSource;
import uk.co.platosys.db.jdbc.JDBCSerialTable;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.PlatosysProperties;

/**
 * The journal is simply a list of transactions.
 * 
 * 
 * @author edward
 */
public final class Journal  {
	public static final String TABLENAME="bx_journal";
	public static final String TID_COLNAME="transactionid";
	public static final String DEBIT_COLNAME="debit";
	public static final String CREDIT_COLNAME="credit";
	public static final String AMOUNT_COLNAME="amount";
	public static final String CURRENCY_COLNAME="currency";
	public static final String DATE_COLNAME="date";
	public static final String CLERK_COLNAME="clerk";
	public static final String NOTE_COLNAME="note";
	public static final String STATUS_COLNAME="status";//the status column
	
	
    public static final int MEM_SIZE=Boox.JOURNAL_SIZE; //much higher for real.
    static long indexRoot;
    boolean archive=false;
    private String databaseName;
    private Enterprise enterprise;
    static Logger debugLogger = Logger.getLogger("boox");
    static Logger logger=debugLogger;
    
    

    public static long assignTransactionID( Transaction transaction){
    	long tid=0;
        try {
        	Enterprise enterprise = transaction.getEnterprise();
			JDBCSerialTable journalTable = JDBCSerialTable.openTable(enterprise.getDatabaseName(), TABLENAME,TID_COLNAME);
			tid=journalTable.addSerialRow();
            return tid;
		} catch (PlatosysDBException e) {
			// TODO Auto-generated catch block
			logger.log("Journal assign transaction ID: exception thrown", e);
			return 0;
		}
  
    }
    public static Transaction getTransaction(Enterprise enterprise, long transactionID){
    	
       
    	try {
			JDBCSerialTable journalTable = JDBCSerialTable.openTable(enterprise.getDatabaseName(), TABLENAME,TID_COLNAME);
			Row row =journalTable.getRow(transactionID);
			Clerk transactionClerk =  new Clerk(row.getString(CLERK_COLNAME));
			Money money = new Money(row.getString(CURRENCY_COLNAME), row.getBigDecimal(AMOUNT_COLNAME));
			String credit = row.getString(CREDIT_COLNAME);
			String debit=row.getString(DEBIT_COLNAME);
			String note=row.getString(NOTE_COLNAME);
			java.sql.Timestamp postingTime=new java.sql.Timestamp(row.getTimeStamp(DATE_COLNAME).getTime());
			return new Transaction (transactionClerk, enterprise,  money,  credit, debit, note,  postingTime,  transactionID);
		        
		} catch (PlatosysDBException e) {
			// TODO Auto-generated catch block
			logger.log("exception thrown", e);
			return null;
		} catch (RowNotFoundException e) {
			// TODO Auto-generated catch block
			logger.log("exception thrown", e);
			return null;
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			logger.log("exception thrown", e);
			return null;
		} catch (ColumnNotFoundException e) {
			// TODO Auto-generated catch block
			logger.log("exception thrown", e);
			return null;
		}
 
    }}

    
   
   
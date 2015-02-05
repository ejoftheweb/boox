/*
 * JDBCRow.java
 *
 * Created on 25 September 2007, 12:03
 *
 *  Copyright (C) 2008  Edward Barrow

    This class is free software; you can redistribute it and/or
    modify it under the terms of the GNU Library General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Library General Public License for more details.

    You should have received a copy of the GNU Library General Public
    License along with this library; if not, write to the Free
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * 
 * for more information contact edward.barrow@platosys.co.uk
 *
 */

package uk.co.platosys.db.jdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uk.co.platosys.db.Row;
import uk.co.platosys.db.ColumnNotFoundException;
import uk.co.platosys.db.TypeNotSupportedException;
import uk.co.platosys.util.ISODate;
import uk.co.platosys.util.Logger;

/**
 * This is a JDBC implementation of the Row interface, where the underlying database (e.g. MySQL or Postgresql) is accessed via jdbc. 
 * Use this for server applications.
 * 
 * Copies a row from a database table. The row data is retrieved from the table and is retained in javaspace
 * even when the underlying database connection is closed. 
 *
 * @author edward
 */
public class JDBCRow implements Row {
    private Map<String, Object> row=new HashMap<String, Object>();
    private String rowName;
     private Logger logger = Logger.getLogger("jdplat");
   
     /**
      * The primary constructor takes a single ResultSet argument and creates the Row object from the 
      * current position of the ResultSet cursor.
      * 
      * All the Row constructors are package-protected; you get Row instances by calling one of the 
      * getRow methods on a Table instance. 
      * 
      */
     protected JDBCRow(ResultSet rs) throws TypeNotSupportedException {
    	 logger.log("JDBCRow init");
     	
         this.rowName=""; 
        try{
             ResultSetMetaData rsmd = rs.getMetaData();
             int noOfColumns = rsmd.getColumnCount();
             for (int i=1; i<(noOfColumns+1); i++){
                 String colName = rsmd.getColumnName(i);
                 int type = rsmd.getColumnType(i);
                 //logger.log("JGR "+colName+" type is "+type);
                 switch (type){
                     case Types.INTEGER:
                     	try{
                     		row.put(colName, new Long(rs.getLong(colName)));
                     	}catch(NullPointerException npe){
                     		row.put(colName, null);                    	
                     	}break;
                     case Types.BIGINT: 
                     	try{
                     		row.put(colName, new Long(rs.getLong(colName)));
                     	}catch(NullPointerException npe){
                     		row.put(colName, null);                    	
                     	}break;
                     case Types.CLOB:
                     	try{
                     		row.put(colName, rs.getString(colName));
                     	}catch(NullPointerException npe){
                     		row.put(colName, null);                    	
                     	}break;
                     case Types.LONGVARCHAR:
                     	try{
                     		row.put(colName, rs.getString(colName));
                     	}catch(NullPointerException npe){
                     		row.put(colName, null);                    	
                     	}break;
                     case Types.VARCHAR: 
                     	try{
                     		row.put(colName, rs.getString(colName));
                     	}catch(NullPointerException npe){
                     		row.put(colName, null);                    	
                     	}break;
                     case Types.DATE:
                     	try{
                     		row.put(colName, new ISODate(rs.getDate(colName).getTime()));
                     	}catch(NullPointerException npe){
                     		row.put(colName, null);                    	
                     	}break;
                     case Types.REAL: 
                     	try{
                     		logger.log("JGR: "+colName+" type is REAL");
                     		row.put(colName, (rs.getFloat(colName)));
                     	}catch(NullPointerException npe){
                     		row.put(colName, null);                    	
                     	}break;
                     case Types.TIMESTAMP: 
                     	try{
                     		row.put(colName, new ISODate(rs.getTimestamp(colName).getTime()));
                     	}catch(NullPointerException npe){
                     		row.put(colName, null);                    	
                     	}break;
                     case Types.BOOLEAN:
                     	try{
                     		row.put(colName, new Boolean(rs.getBoolean(colName)));
                     	}catch(NullPointerException npe){
                     		row.put(colName, null);                    	
                     	}break;
                     	
                      case Types.BIT:
                     	 try{
                      		row.put(colName, new Boolean(rs.getBoolean(colName)));
                      	}catch(NullPointerException npe){
                      		row.put(colName, null);                    	
                      	}break;
                      case Types.DECIMAL:
                      	 try{
                      		//logger.log("JGR: "+colName+" type is DECIMAL");
                       		row.put(colName, (rs.getBigDecimal(colName)));
                       	}catch(NullPointerException npe){
                       		row.put(colName, null);                    	
                       	}break;
                      case Types.NUMERIC:
                       	 try{
                       		//logger.log("JGR: "+colName+" type is DECIMAL");
                        		row.put(colName, (rs.getBigDecimal(colName)));
                        	}catch(NullPointerException npe){
                        		row.put(colName, null);                    	
                        	}break;
                      case Types.DOUBLE:
                        	 try{
                        		//logger.log("JGR: "+colName+" type is DECIMAL");
                         		row.put(colName, (rs.getDouble(colName)));
                         	}catch(NullPointerException npe){
                         		row.put(colName, null);                    	
                         	}break;
                     default: throw new TypeNotSupportedException(colName+ " contains a datatype not supported by platosys.db");
                 }
             }
         }catch(TypeNotSupportedException tnse){
            logger.log("JDBCRow constructor threw TypeNotSupportedException:", tnse);
             throw tnse;
         }catch(Exception e){
             logger.log("JDBCRow constructor threw exception:", e);
         }
     }
     
    /**
     *  This creates a Row with the name "rowName", from the current position 
     *  of the ResultSet cursor. It doesn't iterate through the ResultSet; that's
     *  done by Table.
     *  
     *  Creates a new instance of JDBCRow
     *  
     */
    protected JDBCRow(String rowName, ResultSet rs) throws TypeNotSupportedException {
        this(rs);
    	this.rowName=rowName; 
    }
    /**
     * Creates a new instance of JDBCRow populated from
     * the current position of the result set cursor. The rowName is a String-formatted Long
     * from the rowNumber parameter.
     */
    protected JDBCRow(long rowNumber, ResultSet rs) throws TypeNotSupportedException {
    	 this(rs);
     	this.rowName=Long.toString(rowNumber); 
     	logger.log("JDBCRow created with no: "+rowNumber);
	}  
    
      
      
	public Object getColumnContent(String columnName) throws ColumnNotFoundException {
        if(row.containsKey(columnName.toLowerCase())){
            return row.get(columnName.toLowerCase());
        }else{
            throw new ColumnNotFoundException("JDBCRow "+rowName+" does not contain a column called "+columnName);
        }
    }
    public String getString(String columnName) throws ClassCastException, ColumnNotFoundException {
        if(row.containsKey(columnName.toLowerCase())){
            return (String) row.get(columnName.toLowerCase());
        }else{
            throw new ColumnNotFoundException("JDBCRow "+rowName+" does not contain a column called "+columnName);
        }
    }
    public long getLong(String columnName) throws ClassCastException, ColumnNotFoundException {
        if(row.containsKey(columnName.toLowerCase())){
            return ((Long) row.get(columnName.toLowerCase())).longValue();
        }else{
            throw new ColumnNotFoundException("JDBCRow "+rowName+" does not contain a column called "+columnName);
        }
    }
    public ISODate getISODate(String columnName) throws ClassCastException, ColumnNotFoundException {
        if(row.containsKey(columnName.toLowerCase())){
        	      	
            return (ISODate) row.get(columnName.toLowerCase());
        }else{
            throw new ColumnNotFoundException("JDBCRow "+rowName+" does not contain a column called "+columnName);
        }
    }
    public double getDouble(String columnName) throws ClassCastException, ColumnNotFoundException {
        if(row.containsKey(columnName.toLowerCase())){
        	Double dd = ((Double) row.get(columnName.toLowerCase())).doubleValue();
            double d = dd;
            return d;
        }else{
            throw new ColumnNotFoundException("JDBCRow "+rowName+" does not contain a column called "+columnName);
        }
    }
    public int getInt(String columnName) throws ClassCastException, ColumnNotFoundException {
        if(row.containsKey(columnName.toLowerCase())){
            return ((Long) row.get(columnName.toLowerCase())).intValue();
        }else{
            throw new ColumnNotFoundException("JDBCRow "+rowName+" does not contain a column called "+columnName);
        }
    }
     public boolean getBoolean(String columnName) throws ClassCastException, ColumnNotFoundException {
        if(row.containsKey(columnName.toLowerCase())){
            return ((Boolean) row.get(columnName.toLowerCase())).booleanValue();
        }else{
            throw new ColumnNotFoundException("JDBCRow "+rowName+" does not contain a column called "+columnName);
        }
    }
    public String toString(){
        return ("JDBCRow name: "+rowName+"\n"+row.toString());
    }
    public Set<String> getColumnNames(){
        return row.keySet();
    }
	@Override
	public Date getTimeStamp(String colName) throws ClassCastException, ColumnNotFoundException{
	 if(row.containsKey(colName.toLowerCase())){
	     return (Date) row.get(colName.toLowerCase());
     }else{
         throw new ColumnNotFoundException("JDBCRow "+rowName+" does not contain a column called "+colName);
     }
	}
	@Override
	public Date getDate(String colName) throws ClassCastException, ColumnNotFoundException {
		 if(row.containsKey(colName.toLowerCase())){
		     return (Date) row.get(colName.toLowerCase());
	     }else{
	         throw new ColumnNotFoundException("JDBCRow "+rowName+" does not contain a column called "+colName);
	     }
	}

	@Override
	public BigDecimal getBigDecimal(String colName)throws ClassCastException, ColumnNotFoundException {
		if(row.containsKey(colName.toLowerCase())){
		     return (BigDecimal) row.get(colName.toLowerCase());
	     }else{
	         throw new ColumnNotFoundException("JDBCRow "+rowName+" does not contain a column called "+colName);
	     }
	}

	@Override
	public float getFloat(String columnName) throws ClassCastException,ColumnNotFoundException {
		if(row.containsKey(columnName.toLowerCase())){
		     return (float) row.get(columnName.toLowerCase());
	    }else{
	        throw new ColumnNotFoundException("JDBCRow "+rowName+" does not contain a column called "+columnName);
	    }
	}
}

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
 * Models a row in a database table.
 *
 * @author edward
 */
public class JDBCRow implements Row {
    private Map<String, Object> row=new HashMap<String, Object>();
    private String rowName;
     private Logger logger = Logger.getLogger("platosysdb");
   
    /** Creates a new instance of JDBCRow */
    protected JDBCRow(String rowName, ResultSet rs) throws TypeNotSupportedException {
        this.rowName=rowName; 
        //logger.log(5, "ROW-init: "+rowName+", ResultSet is "+rs.toString());
        try{
            ResultSetMetaData rsmd = rs.getMetaData();
            int noOfColumns = rsmd.getColumnCount();
            //logger.log(5, "JDBCRow has "+Integer.toString(noOfColumns)+" columns");
            for (int i=1; i<(noOfColumns+1); i++){
                String colName = rsmd.getColumnName(i);
                logger.log(5, "JDBCRow: column no:"+Integer.toString(i)+" is named "+colName);
                int type = rsmd.getColumnType(i);
                logger.log(5, "JDBCRow: col type is "+Integer.toString(type));
                switch (type){
                    case Types.INTEGER: row.put(colName, new Long(rs.getLong(colName)));break;
                    case Types.BIGINT: row.put(colName, new Long(rs.getLong(colName)));break;
                    case Types.CLOB: row.put(colName, rs.getString(colName));break;
                    case Types.LONGVARCHAR: row.put(colName, rs.getString(colName));break;
                    case Types.VARCHAR: row.put(colName, rs.getString(colName));break;
                    case Types.DATE: row.put(colName, new ISODate(rs.getDate(colName).getTime()));break;
                    case Types.NUMERIC: row.put(colName, new Double(rs.getDouble(colName)));break;
                    case Types.TIMESTAMP: row.put(colName,new ISODate(rs.getTimestamp(colName).getTime()));break;
                    case Types.BOOLEAN: row.put(colName, new Boolean(rs.getBoolean(colName)));break;
                     case Types.BIT: row.put(colName, new Boolean(rs.getBoolean(colName)));break;
                   
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
    private ISODate getTimeStamp(ResultSet rs, String colName){
        try{
            return new ISODate(rs.getTimestamp(colName).getTime());
        }catch(Exception e){
            return new ISODate(0);
        }
    }
     private ISODate getDate(ResultSet rs, String colName){
        try{
            return new ISODate(rs.getDate(colName).getTime());
        }catch(Exception e){
            return new ISODate(0);
        }
    }
        /**
         * Creates a new instance of JDBCRow populated from
         * the current position of the result set cursor.
         * 
         * 
         *
         *
         */
    protected JDBCRow(ResultSet rs) throws TypeNotSupportedException {
        this.rowName=""; 
        //logger.log(5, "ROW-init: "+rowName+", ResultSet is "+rs.toString());
        try{
            ResultSetMetaData rsmd = rs.getMetaData();
            int noOfColumns = rsmd.getColumnCount();
            //logger.log(5, "JDBCRow has "+Integer.toString(noOfColumns)+" columns");
            for (int i=1; i<(noOfColumns+1); i++){
                String colName = rsmd.getColumnName(i);
                //logger.log(5, "JDBCRow: column no:"+Integer.toString(i)+" is named "+colName);
                int type = rsmd.getColumnType(i);
                //logger.log(5, "JDBCRow: col type is "+Integer.toString(type));
                //DONE: deal with NPE thrown when columns are empty/null
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
                    case Types.NUMERIC: 
                    	try{
                    		row.put(colName, new Double(rs.getDouble(colName)));
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
    public JDBCRow(long primaryKeyTest, ResultSet resultSet) {
			// TODO Auto-generated constructor stub
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
	public Date getTimeStamp(String colName) throws ClassCastException,
			uk.co.platosys.db.ColumnNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Date getDate(String colName) throws ClassCastException,
			uk.co.platosys.db.ColumnNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
}

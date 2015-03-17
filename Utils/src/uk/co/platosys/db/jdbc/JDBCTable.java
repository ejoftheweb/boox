/*
 * JDBCTable.java
 *
 * Created on 24 September 2007, 12:06
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
 *
 */

package uk.co.platosys.db.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.postgresql.util.PSQLException;

import uk.co.platosys.util.ISODate;
import uk.co.platosys.util.Logger;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.RowNotFoundException;
import uk.co.platosys.db.Table;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.TypeNotSupportedException;

/**
 * The purpose of this class is to avoid having to write (and debug) carefully escaped sql strings whenever writing or reading from a sql database.
 * However it doesn't get close to matching the full flexibility of sql; I think I ought to learn Spring.
 *
 * At the moment it handles very simple sql tables, with a text primary key (which will thus identify a table row).
 *  
 * Support for integer primary keys and sequences  may be added later.
 *
 * It also only handles text, numeric, integer, date and timestamp types for the data columns.
 * 
 * It's meant for reading and writing atomic values to and from one table in the database, referenced by rowKey/columnName 
 *
 * It's probably pretty slow, but the use of the pooling ConnectionSource should reduce the connection/authentication overhead.
 *
 * some examples:
 * - to add a timestamp column: table.addColumn("timestamp", JDBCTable.TIMESTAMP_COLUMN);
 * - amend an item in a row: table.amend(rowKey, columnName, newValue); (if it can't find the row, a new one is added).
 * - read a date: ISODate isoDate = table.readDate(rowKey, columnName);
 *
 * Revisions Dec2014
 * 
 * Support has now been added for integer (long) primary keys and for the decimal column type. Decimal maps to a java.math.BigDecimal leaving numeric
 * for other floating-point types. 
 * Support for serial primary keys is now provided in the SerialTable class which is an extension of this. 
 * 
 * @author edward
 */
public class JDBCTable implements Table {
    public static final String TEXT_COLUMN="text";
    public static final String BOOLEAN_COLUMN="boolean";
    public static final String NUMERIC_COLUMN="double precision";
    public static final String INTEGER_COLUMN="integer";
    public static final String DATE_COLUMN="timestamp";
    public static final String TIMESTAMP_COLUMN="timestamp";
    public static final String DECIMAL_COLUMN="decimal";
    public static final String DEFAULT_DATABASE="musite";//HOBBLEDY_HOOP should not be here!
    protected String databaseName=DEFAULT_DATABASE;
    String tableName;
    String primaryKeyColumnName;
	private String primaryKeyColumnType;
	
   protected static Logger logger = Logger.getLogger("jdplat");
   /**
    * protected no-args constructor for subclasses in the same package
    */
   protected JDBCTable(){}
    /**
     *Creates a table in databaseName, with the name tableName, and a primary key column named primaryKeyColumnName
     *
     */
   
    public static JDBCTable createTable(
    		String databaseName,
    		String tableName, 
    		String primaryKeyColumnName, 
    		String primaryKeyColumnType) 
    throws PlatosysDBException {
        Connection connection=null;
        Statement statement=null;
        ResultSet rs=null;
            connection=ConnectionSource.getConnection(databaseName);
            try{
                statement=connection.createStatement();
                statement.execute("CREATE TABLE "+tableName+" ("+primaryKeyColumnName+ " "+primaryKeyColumnType+" PRIMARY KEY)");
                connection.close();
                return new JDBCTable(databaseName, tableName, primaryKeyColumnName, primaryKeyColumnType);
            }catch(Exception ex){
                logger.log("problem creating table "+tableName+"  in db "+databaseName, ex);
                try{connection.close();}catch(Exception p){}
               return null; 
            }finally{
            	try{
            		if (rs!=null){rs.close();}
            		if (statement!=null){statement.close();}
            		if (connection!=null){connection.close();}
            	}catch(Exception x){}
            }
    }
    
    /**
     *Creates a table in databaseName, with the name tableName, and  column named columnName of type columnType, which will be
     * a primary key column (i.e. UNIQUE and NOT NULL) if primaryKey is true.
     *
     */
    public static JDBCTable createTable(
    		String databaseName, 
    		String tableName, 
    		String columnName, 
    		String columnType, 
    		boolean primaryKey)
    throws PlatosysDBException {
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		 connection=ConnectionSource.getConnection(databaseName);
            try{
                statement=connection.createStatement();
                String sqlString = null;
                if (primaryKey){
                    sqlString="CREATE TABLE "+tableName+" ("+columnName+ " "+columnType+" PRIMARY KEY)";
                }else{
                    sqlString="CREATE TABLE "+tableName+" ("+columnName+ " "+columnType+")";
                }
                statement.execute(sqlString);
                connection.close();
                return new JDBCTable(databaseName, tableName);
            }catch(Exception ex){
               try{connection.close();}catch(Exception p){}

               return null;
            }finally{
            	try{
            		if (rs!=null){rs.close();}
            		if (statement!=null){statement.close();}
            		if (connection!=null){connection.close();}
            	}catch(Exception x){}
            }
         }
    
    /**
     * Creates a table in databaseName, with the name tableName, and a foreign key column named foreignKeyColumnName, 
     * referencing the primary key column of the table foreignKeyTableName. The column names must be the same.
     * 
     */
           public static JDBCTable createForeignKeyTable(
        		   String databaseName, 
        		   String tableName, 
        		   String foreignKeyColumnName, 
        		   String foreignKeyTableName ) 
        	throws PlatosysDBException {
        	   Connection connection=null;
        		ResultSet rs=null;
        		Statement statement=null;
        		connection=ConnectionSource.getConnection(databaseName);
            String SQLString="";
            try{
               statement=connection.createStatement();
                rs =statement.executeQuery("SELECT "+foreignKeyColumnName+" FROM "+foreignKeyTableName);//this will only work if column names are the same!!
                ResultSetMetaData rsmd = rs.getMetaData();
                int keyType = rsmd.getColumnType(rs.findColumn(foreignKeyColumnName));
                rs.close();
                SQLString =("CREATE TABLE "+tableName+" ("+foreignKeyColumnName+ " "+getTypeName(keyType)+" REFERENCES "+foreignKeyTableName+ ")");
                statement.execute(SQLString);
                connection.close();
                return new JDBCTable(databaseName, tableName, foreignKeyColumnName, false);
            }catch(Exception ex){
                logger.log(1, SQLString);
                logger.log("JDBCTable couldn't create fk table "+tableName, ex);
                try{connection.close();}catch(Exception p){}

               return null; 
            }finally{
            	try{
            		if (rs!=null){rs.close();}
            		if (statement!=null){statement.close();}
            		if (connection!=null){connection.close();}
            	}catch(Exception x){}
            }
    }
   /**
     * Creates a table in databaseName, with the name tableName, and a foreign key column named foreignKeyColumnName, referencing the column of the table foreignKeyTableName named foreignTableColumn.
     * Use this method to create a table that needs to reference a column other than a primary key column. 
    */
           public static JDBCTable createForeignKeyTable(
        		   String databaseName, 
        		   String tableName, 
        		   String foreignKeyColumnName, 
        		   String foreignKeyTableName, 
        		   String foreignTableColumnName)
           throws PlatosysDBException {
        	   Connection connection=null;
        		ResultSet rs=null;
        		Statement statement=null;
        		connection=ConnectionSource.getConnection(databaseName);
            try{
                statement=connection.createStatement();
                rs =statement.executeQuery("SELECT "+foreignTableColumnName+" FROM "+foreignKeyTableName);
                ResultSetMetaData rsmd = rs.getMetaData();
                int keyType = rsmd.getColumnType(rs.findColumn(foreignTableColumnName));
                rs.close();
                statement.execute("CREATE TABLE "+tableName+" ("+foreignKeyColumnName+ " "+getTypeName(keyType)+" REFERENCES "+foreignKeyTableName+ "("+foreignTableColumnName+"))");
                connection.close();
                return new JDBCTable(databaseName, tableName, foreignKeyColumnName, false);
            }catch(Exception ex){
                try{connection.close();}catch(Exception p){}

               return null; 
            }finally{
            	try{
            		if (rs!=null){rs.close();}
            		if (statement!=null){statement.close();}
            		if (connection!=null){connection.close();}
            	}catch(Exception x){}
            }
        }
          /**
           * Converts from integer to String type references
           * @param keyType
           * @return
         * @throws TypeNotSupportedException 
           */
           public static String getTypeName(int keyType) throws TypeNotSupportedException{
               switch (keyType){
                   case  Types.INTEGER: return INTEGER_COLUMN;
                   case  Types.VARCHAR: return TEXT_COLUMN;
                   case  Types.DATE: return DATE_COLUMN;
                   case  Types.NUMERIC: return NUMERIC_COLUMN;
                   case  Types.TIMESTAMP: return TIMESTAMP_COLUMN;
                   case  Types.BOOLEAN: return BOOLEAN_COLUMN;
                   case  Types.DECIMAL: return DECIMAL_COLUMN;
                   
                   default: return Integer.toString(keyType);
                	   //throw new TypeNotSupportedException(Integer.toString(keyType));
               }
           }
           /**
            * Converts from String to integer type references
            * @param keyType
            * @return
            */
            public static int getType (String typeName) throws TypeNotSupportedException {
                switch (typeName){
                    case  INTEGER_COLUMN: return Types.INTEGER;
                    case  TEXT_COLUMN: return Types.VARCHAR;
                    //case  DATE_COLUMN: return Types.TIMESTAMP;
                    case  NUMERIC_COLUMN: return Types.NUMERIC;
                    case  TIMESTAMP_COLUMN: return Types.TIMESTAMP;
                    case  BOOLEAN_COLUMN: return Types.BOOLEAN;
                    case DECIMAL_COLUMN: return Types.DECIMAL;
                    default: throw new TypeNotSupportedException(typeName);
                }
            }
    
    /** 
     * Creates a new instance of JDBCTable. The object references the table, and will check that 
     * it exists, but nothing of it is held in memory. You must call one of its access methods to read
     * or write to it.
     * The primary key must be a Text column.  It will throw an exception if it isn't.
     * @param tableName the name of the table to be referenced
     * @param primaryKeyColumnName the column name of the primary key column
     */
    public JDBCTable(String databaseName, String tableName, String primaryKeyColumnName) throws PlatosysDBException  {
        this.databaseName=databaseName;
        this.tableName=tableName;
        this.primaryKeyColumnName=primaryKeyColumnName;
        this.primaryKeyColumnType=TEXT_COLUMN;
        if(!checkCols(databaseName, tableName, primaryKeyColumnName, primaryKeyColumnType)){
        	throw new PlatosysDBException("JDBCTable "+tableName+" is incorrectly configured");
        }
    }
    
    /** 
     * Creates a new instance of JDBCTable.The object references the table, and will check that 
     * it exists, but nothing of it is held in memory. You must call one of its access methods to read
     * or write to it.
     * 
     * @param tableName the name of the table to be referenced
     * @param primaryKeyColumnName the column name of the primary key column
     * @param primaryKeyColumnType the type of the primary key column
     * @throws PlatosysDBException if the table doesn't exist, if the 
     */
    public JDBCTable(String databaseName, String tableName, String primaryKeyColumnName, String primaryKeyColumnType) throws PlatosysDBException  {
        this.databaseName=databaseName;
        this.tableName=tableName;
        this.primaryKeyColumnName=primaryKeyColumnName;
        this.primaryKeyColumnType=primaryKeyColumnType;
        if(!checkCols(databaseName, tableName, primaryKeyColumnName, primaryKeyColumnType)){
        	throw new PlatosysDBException("JDBCTable "+tableName+" is incorrectly configured");
        }
    }
    private boolean checkCols (String databaseName, String tableName, String primaryKeyColumnName, String primaryKeyColumnType) throws PlatosysDBException {
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		connection=ConnectionSource.getConnection(databaseName);
    	 primaryKeyColumnName=primaryKeyColumnName.toLowerCase();
         try{
         	statement=connection.createStatement();
             rs=statement.executeQuery("SELECT * FROM "+tableName);//this is potentially very hungry!
             ResultSetMetaData rsmd = rs.getMetaData();
             boolean colfound=false;
             int cols=rsmd.getColumnCount();
             for(int i=1; i<cols+1; i++){
            	 String colname=rsmd.getColumnName(i);
            	if (colname.equals(primaryKeyColumnName)){
             		colfound=true;
             		int coltype = rsmd.getColumnType(i);
             		String coltypeName = getTypeName(coltype);
             		logger.log("coltype: "+coltype+",name:"+coltypeName);
             		logger.log("declaredColType:"+getType(primaryKeyColumnType)+", name:"+primaryKeyColumnType);
             		if(rsmd.getColumnType(i)!=getType(primaryKeyColumnType)){
             			rs.close();
             			statement.close();
             			connection.close();
             			throw new PlatosysDBException("Primary Key Col "+primaryKeyColumnName+" in "+tableName+" is wrong type");
             		}else{
             			break;
             		}
             	}
             }
             if(!colfound){
            	 connection.close();
            	 throw new PlatosysDBException("column not found in table");
              }else{
            	  connection.close();
            	  return true;
              }
            
         }catch(Exception e){
             logger.log(2,"TABLE checkCols "+tableName+" error ");
             try{connection.close();}catch(Exception p){}
             throw new PlatosysDBException("table "+tableName+" error", e);
         }finally{
         	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
   /** 
     * Creates a new instance of JDBCTable with no primary key
    *  @param databaseName 
     * @param tableName the name of the table to be referenced
     *
     */
    public JDBCTable(String databaseName, String tableName ) throws PlatosysDBException  {
        this.databaseName=databaseName;
        this.tableName=tableName;
        this.primaryKeyColumnName=null;
        Connection connection=null;
 		ResultSet rs=null;
 		Statement statement=null;
 		
       connection=ConnectionSource.getConnection(databaseName);
        try{
        	statement=connection.createStatement();
            rs=statement.executeQuery("SELECT * FROM "+tableName);
            connection.close();
        }catch(Exception e){
            logger.log(2,"TABLE-init: table "+tableName+" does not exist");
            try{connection.close();}catch(Exception p){}

            throw new PlatosysDBException("table "+tableName+" does not exist", e);
                    
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
         /**
     * 
     * Creates a new instance of JDBCTable
     * Note that the primary key should be a Text column
     * @param tableName the name of the table 
     * @param primaryKeyColumnName the column name of the primary key column
     * @param createIfAbsent flag to create the table if it's not in the db.
     */
    public JDBCTable(String databaseName, String tableName, String primaryKeyColumnName, boolean createIfAbsent) throws PlatosysDBException  {
        this.databaseName=databaseName;
        this.tableName=tableName;
        this.primaryKeyColumnName=primaryKeyColumnName;
        Connection connection=null;
 		ResultSet rs=null;
 		Statement statement=null;
 		
        connection=ConnectionSource.getConnection(databaseName);
        try{
            statement=connection.createStatement();
            rs=statement.executeQuery("SELECT * FROM "+tableName);
            statement.close();
            connection.close();
        }catch(Exception e){
            logger.log(2,"TABLE-init: table "+tableName+" does not exist");
            if(createIfAbsent){
                try{
                    statement=connection.createStatement();
                    String sqlString=("CREATE TABLE "+tableName+" ("+primaryKeyColumnName+" text PRIMARY KEY)");
                    logger.log(5, sqlString);
                    statement.execute(sqlString);
                    statement.close();
                    connection.close();
                }catch(Exception ex){
                logger.log("TABLE problem creating table", ex);
                try{connection.close();}catch(Exception p){}

                }
            }else{
                logger.log(2, "TABLE-init: not creating "+tableName);
                try{connection.close();}catch(Exception p){}

            }
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
    public Set<String> getColumnNames(){
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		Set<String> colnames = new HashSet<String>();
        try{
            connection=ConnectionSource.getConnection(databaseName);
            statement=connection.createStatement();
            rs=statement.executeQuery("SELECT *  FROM "+tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int noOfColumns = rsmd.getColumnCount();
            for(int i=0; i<noOfColumns; i++){
                colnames.add(rsmd.getColumnName(i));
            }
            connection.close();
            return colnames;
        }catch(Exception e){
            //Logger.log("JDBCTable.rowExists(): error", e);
            try{connection.close();}catch(Exception cex){}
            return null;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
    /**
     * JDBCTable refers to rows by the primary key.
     * @param primaryKeyTest the value of the primary key to be tested for
     * @return true if the row is found
     */
    public boolean rowExists(String primaryKeyTest){
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		try{
            connection=ConnectionSource.getConnection(databaseName);
            statement=connection.createStatement();
            rs=statement.executeQuery("SELECT "+primaryKeyColumnName+" FROM "+tableName+" WHERE "+primaryKeyColumnName+ " = \'"+primaryKeyTest+"\'");
            if(rs.next()){
                connection.close();
                return true;
            }else{
                connection.close();
                return false;
            }
        }catch(Exception e){
            //Logger.log("JDBCTable.rowExists(): error", e);
            try{connection.close();}catch(Exception cex){}
            return false;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
    
    /**
     * JDBCTable refers to rows by the primary key.
     * @param primaryKeyTest the value of the primary key to be tested for
     * @return true if the row is found
     */
    public boolean rowExists(int primaryKeyTest){
    	Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		try{
            connection=ConnectionSource.getConnection(databaseName);
            statement=connection.createStatement();
            rs=statement.executeQuery("SELECT "+primaryKeyColumnName+" FROM "+tableName+" WHERE "+primaryKeyColumnName+ " = "+primaryKeyTest);
            if(rs.next()){
                connection.close();
                return true;
            }else{
                connection.close();
                return false;
            }
        }catch(Exception e){
            //Logger.log("JDBCTable.rowExists(): error", e);
            try{connection.close();}catch(Exception cex){}
            return false;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
    
    /**
     * Works only for String values!
     * @param columnName 
     * @param columnValue
     * @return true if at lease one row is found where columnName=columnValue
     */
    public boolean rowExists(String columnName, String columnValue){
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		try{
            connection=ConnectionSource.getConnection(databaseName);
            statement=connection.createStatement();
            rs=statement.executeQuery("SELECT "+columnName+" FROM "+tableName+" WHERE "+columnName+ " = \'"+columnValue+"\'");
            if(rs.next()){
                connection.close();
                return true;
            }else{
                connection.close();
                return false;
            }
        }catch(Exception e){
            //Logger.log("JDBCTable.rowExists(): error", e);
            try{connection.close();}catch(Exception cex){}
            return false;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
    
        /**
     * Works only for String values!
     * @param columnName 
     * @param columnValue
     * @return true if at lease one row is found where columnName=columnValue
     */
    public boolean rowExists(String[] columns, String[] values) throws PlatosysDBException{
        if (columns.length != values.length){
            throw new PlatosysDBException ("columns and values arrays don't match");
        }
        Connection connection=null;
 		ResultSet rs=null;
 		Statement statement=null;
 		try{
            connection=ConnectionSource.getConnection(databaseName);
            statement=connection.createStatement();
            String sqlWhereClause =  " WHERE (";
            for (int i=0; i<columns.length; i++){
                sqlWhereClause=sqlWhereClause+"("+columns[i]+" = \'"+values[i]+"\') AND ";
            }
            sqlWhereClause=sqlWhereClause.substring(0, sqlWhereClause.length()-4);
            sqlWhereClause=sqlWhereClause+")";
            String sqlQuery= "SELECT * FROM "+tableName+sqlWhereClause;
            rs = statement.executeQuery(sqlQuery);
            if(rs.next()){
                connection.close();
                return true;
            }else{
                connection.close();
                return false;
            }
             
        }catch(Exception e){
            logger.log("JDBCTable row exits had a problem", e);
            try{connection.close();}catch(Exception p){}

            return false;
        }finally{
            	try{
            		if (rs!=null){rs.close();}
            		if (statement!=null){statement.close();}
            		if (connection!=null){connection.close();}
            	}catch(Exception x){}
            }
    }

     public boolean addRow(String columnName, long columnValue){
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		try{
            connection=ConnectionSource.getConnection(databaseName);
            statement=connection.createStatement();
            statement.execute("INSERT INTO "+tableName+" ("+columnName+") VALUES ("+columnValue+")");
            connection.close();
            return true;
        }catch(Exception e){
            //Logger.log("JDBCTable.addRow(): error", e);
            try{connection.close();}catch(Exception cex){}
            return false;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
   
    public boolean addRow(String columnName, String columnValue){
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		try{
            connection=ConnectionSource.getConnection(databaseName);
            statement=connection.createStatement();
            statement.execute("INSERT INTO "+tableName+" ("+columnName+") VALUES (\'"+columnValue+"\')");
            connection.close();
            return true;
        }catch(Exception e){
            logger.log("JDBCTable.addRow(): error", e);
            try{connection.close();}catch(Exception cex){}
            return false;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
  
    public boolean addRow(String[] columns, String[] values)throws PlatosysDBException{
        if (columns.length!=values.length){
            throw new PlatosysDBException("JDBCTable - Add JDBCRow: not the same number of columns and values");
        }
        Connection connection=null;
 		ResultSet rs=null;
 		Statement statement=null;
 		try{
            connection=ConnectionSource.getConnection(databaseName);
            statement=connection.createStatement();
            String SQLString="INSERT INTO "+tableName+ " (";
            for (int i=0; i<columns.length; i++){
                SQLString=SQLString+columns[i]+",";
            }
            int strlen = SQLString.length();
            SQLString=SQLString.substring(0,strlen-1);//chop off last comma
            SQLString=SQLString+") VALUES (";
            for (int i=0; i<values.length; i++){
                SQLString=SQLString+"\'"+values[i]+"\',";
            }
            strlen = SQLString.length();
            SQLString=SQLString.substring(0,strlen-1);//chop off last comma
            SQLString=SQLString+")";
            //logger.log(5, "JDBCTable-addRow: SQLString  is: "+SQLString);
            statement.execute(SQLString);
            connection.close();
            return true;
        }catch(Exception e){
        	 logger.log("JDBCTable.addRow([][]): error", e);
        	 throw new PlatosysDBException("JDBCTable.addRow([][]): error", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
    public JDBCRow getRow(String primaryKeyTest)throws RowNotFoundException, PlatosysDBException {
    	if (!(primaryKeyColumnType.equals(TEXT_COLUMN))){
    		throw new PlatosysDBException(" primary key is not a text column");
    	}
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		 try{
            connection=ConnectionSource.getConnection(databaseName);
           statement=connection.createStatement();
            rs=statement.executeQuery("SELECT * FROM "+tableName+ " WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
            if(rs.next()){
               //logger.log(5, "JDBCTable found row for "+primaryKeyTest);
               JDBCRow row = new JDBCRow(primaryKeyTest, rs);
               connection.close();
               return row;
            }else{
                connection.close();
                throw new RowNotFoundException("JDBCRow "+primaryKeyTest+" not found in table "+tableName);
            }
        }
        catch(RowNotFoundException rnfe){
            try{connection.close();}catch(Exception p){}
               
            throw rnfe;
        }
        catch(Exception e){
            try{connection.close();}catch(Exception cex){}
            return null;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }    
    }
    
    public JDBCRow getRow(long primaryKeyTest)throws RowNotFoundException, PlatosysDBException {
    	logger.log("JDBCT getting row "+primaryKeyTest+" from table "+tableName);
    	if (!(primaryKeyColumnType.equals(INTEGER_COLUMN))){
    		throw new PlatosysDBException(" primary key is not an integer");
    	}
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		 try{
            connection=ConnectionSource.getConnection(databaseName);
            if(connection==null){throw new PlatosysDBException("couldn't get a db connection wtaf");}
            logger.log("JDBCT got connection to "+databaseName);
        	
            statement=connection.createStatement();
            logger.log("JDBCT created statement for "+tableName);
        	
            rs=statement.executeQuery("SELECT * FROM "+tableName+ " WHERE "+primaryKeyColumnName+" = "+primaryKeyTest);
            logger.log("JDBCT getting result set for "+primaryKeyTest+" from table "+tableName);
        	if(rs.next()){
               //logger.log(5, "JDBCTable found row for "+primaryKeyTest);
               JDBCRow row = new JDBCRow(primaryKeyTest, rs);
               connection.close();
               return row;
            }else{
                connection.close();
                throw new RowNotFoundException("JDBCRow "+primaryKeyTest+" not found in table "+tableName);
            }
        }
        catch(RowNotFoundException rnfe){
            try{connection.close();}catch(Exception p){}
               
            throw rnfe;
        }
        catch(Exception e){
            try{connection.close();}catch(Exception cex){}
            return null;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
    /**
     * Selects a row on something other than the table's primary key. Returns only the first matching 
     * row found, so can only safely be used on columns with a UNIQUE constraint, but it doesn't test for this.
     * Also only works for text/varchar etc cols (which contain Strings).
     * @param queryColumnName the name of the column to search on;
     * @param queryColumnValue the value to search for;
     * @return a JDBCRow object named queryColumnName=queryColumnValue
     */
    
    public JDBCRow getRow(String queryColumnName, String queryColumnValue)throws RowNotFoundException {
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		 try{
            connection=ConnectionSource.getConnection(databaseName);
           statement=connection.createStatement();
            rs=statement.executeQuery("SELECT * FROM "+tableName+ " WHERE "+queryColumnName+" = \'"+queryColumnValue+"\'");
            if(rs.next()){
               //logger.log(5, "JDBCTable found row for "+queryColumnValue);
               JDBCRow row = new JDBCRow(queryColumnName+"="+queryColumnValue, rs);
               connection.close();
               return row;
            }else{
                connection.close();
                throw new RowNotFoundException("JDBCRow "+queryColumnName+"="+queryColumnValue+" not found in table "+tableName);
            }
        }
        catch(RowNotFoundException rnfe){
            try{connection.close();}catch(Exception p){}

            throw rnfe;}
        catch(Exception e){
            try{connection.close();}catch(Exception cex){}
            return null;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }    
    }

    /**
     *Returns the first JDBCRow found where the columns in testColumns have the values in testValues
     */
    public JDBCRow getRow(String[] testColumns, String[] testValues) throws RowNotFoundException {
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		try{
            connection=ConnectionSource.getConnection(databaseName);
            statement=connection.createStatement();
            String sqlWhereClause =  " WHERE (";
            for (int i=0; i<testColumns.length; i++){
                sqlWhereClause=sqlWhereClause+"("+testColumns[i]+" = \'"+testValues[i]+"\') AND ";
            }
            sqlWhereClause=sqlWhereClause.substring(0, sqlWhereClause.length()-4);
            sqlWhereClause=sqlWhereClause+")";
            String sqlQuery= "SELECT * FROM "+tableName+sqlWhereClause;
            rs = statement.executeQuery(sqlQuery);
           if (rs.next()){
              JDBCRow row = new JDBCRow (rs);
               connection.close();
               return row;
            }else{
                connection.close();
                throw new RowNotFoundException( "multi-condition row not found in table "+tableName);
            }
        }catch(RowNotFoundException rnfe){
            throw rnfe;
        }catch(Exception e){

            try{connection.close();}catch(Exception p){}
          
            logger.log("JDBCTable getRows had a problem", e);
            return null;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }

    /**
     *Returns a List of Rows where the columns in testColumns have the values in testValuses
     */
    public List<Row> getRows(String[] testColumns, String[] testValues){
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		List<Row> rows = new ArrayList<Row>();
        try{
            connection=ConnectionSource.getConnection(databaseName);
            statement=connection.createStatement();
            String sqlWhereClause =  " WHERE (";
            for (int i=0; i<testColumns.length; i++){
                sqlWhereClause=sqlWhereClause+"("+testColumns[i]+" = \'"+testValues[i]+"\') AND ";
            }
            sqlWhereClause=sqlWhereClause.substring(0, sqlWhereClause.length()-4);
            sqlWhereClause=sqlWhereClause+")";
            String sqlQuery= "SELECT * FROM "+tableName+sqlWhereClause;
            rs = statement.executeQuery(sqlQuery);
            while(rs.next()){
                rows.add(new JDBCRow (rs));
            }
            connection.close();
            return rows;
        }catch(Exception e){
            try{connection.close();}catch(Exception p){}

            logger.log("JDBCTable getRows had a problem", e);
            return null;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
 /**
     *Returns a List of Rows where the given column name has the given value
     */
    public List<Row> getRows(String column, String value){
        Connection connection=null;
        List<Row> rows = new ArrayList<Row>();
        try{
            connection=ConnectionSource.getConnection(databaseName);
            Statement statement=connection.createStatement();
            String sqlWhereClause =  " WHERE ("+column+" = \'"+value+"\')";

            String sqlQuery= "SELECT * FROM "+tableName+sqlWhereClause;
            ResultSet rs = statement.executeQuery(sqlQuery);
            while(rs.next()){
                rows.add(new JDBCRow (rs));
            }
            connection.close();
            return rows;
        }catch(Exception e){
            try{connection.close();}catch(Exception p){}

            logger.log("JDBCTable getRows had a problem", e);
            return null;
        }
    }
    /**
     * Returns a List of Rows meeting the condition specified as the SQL where clause.
     *
     * @param sqlWhere
     * @return
     */
    public List<Row> getRows(String sqlWhere){
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		 List<Row> rows = new ArrayList<Row>();
        String sqlQuery="";
        try{
            connection=ConnectionSource.getConnection(databaseName);
            statement=connection.createStatement();
            String sqlWhereClause = " WHERE ( "+sqlWhere;
            sqlWhereClause=sqlWhereClause+")";
            sqlQuery= "SELECT * FROM "+tableName+sqlWhereClause;
             rs = statement.executeQuery(sqlQuery);
            while(rs.next()){
                rows.add(new JDBCRow (rs));
            }
            connection.close();
            return rows;
        }catch(Exception e){
            logger.log("JDBCTable getRows(sql) had a problem with sql string: \n"+sqlQuery, e);
            try{connection.close();}catch(Exception p){}

            return null;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
    /**
     * Returns a List of Rows  as a result from a sql query
     * @param sqlWhere
     * @return
     */
    public List<Row> query(String sqlQuery)throws PlatosysDBException{
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		List<Row> rows = new ArrayList<Row>();
        try{
            connection=ConnectionSource.getConnection(databaseName);
           statement=connection.createStatement();
            rs = statement.executeQuery(sqlQuery);
            while(rs.next()){
                rows.add(new JDBCRow (rs));
            }
            connection.close();
            return rows;
        }catch(Exception e){
            logger.log("JDBCTable query(sql) had a problem with sql string: \n"+sqlQuery, e);
            
            try{connection.close();}catch(Exception p){}
            throw new PlatosysDBException("SQL query problem", e);
            

        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
        /**
         * Returns the entire JDBCTable as a List of Rows
         * @return
         */

      public List<Row> getRows(){
    	  Connection connection=null;
   		ResultSet rs=null;
   		Statement statement=null;
   		 List<Row> rows = new ArrayList<Row>();
        String sqlQuery="";
        try{
            connection=ConnectionSource.getConnection(databaseName);
           statement=connection.createStatement();
            sqlQuery= "SELECT * FROM "+tableName;
           rs = statement.executeQuery(sqlQuery);
            while(rs.next()){
                rows.add(new JDBCRow (rs));
            }
            connection.close();
            return rows;
        }catch(Exception e){
            try{connection.close();}catch(Exception p){}

            logger.log("JDBCTable getRows(sql) had a problem with sql string: \n"+sqlQuery, e);
            return null;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
            /**
         * Returns the entire JDBCTable as a List of Rows
         * @return
         */

      public List<Row> getSortedRows(String sortedColumnName){
        Connection connection=null;
        List<Row> rows = new ArrayList<Row>();
        String sqlQuery="";
        try{
            connection=ConnectionSource.getConnection(databaseName);
            Statement statement=connection.createStatement();
            sqlQuery= "SELECT * FROM "+tableName+ " ORDER BY "+ sortedColumnName+ " ASC";
            ResultSet rs = statement.executeQuery(sqlQuery);
            while(rs.next()){
                rows.add(new JDBCRow (rs));
            }
            connection.close();
            return rows;
        }catch(Exception e){
            try{connection.close();}catch(Exception p){}

            logger.log("JDBCTable getSortedRows(sql) had a problem with sql string: \n"+sqlQuery, e);
            return null;
        }
    }
    public boolean columnExists(String columnName){
        //this is likely to be rather slow with a big database, no?
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		boolean exists=false;
  		 try{
            connection=ConnectionSource.getConnection(databaseName);
          statement=connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM "+tableName);
            ResultSetMetaData rsmd=rs.getMetaData();
            int noOfColumns=rsmd.getColumnCount();
            for(int i=0; i<noOfColumns; i++){
                if (rsmd.getColumnName(i).equals(columnName)){
                    exists=true;
                    break;
                }
            }
            connection.close();
            return exists;
        }catch(Exception e){
            try{connection.close();}catch(Exception p){}

            return false;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
    
    
    /**
     * Adds an unconstrained column to a table, of the given type.
     *
     * @param columnName 
     * @param columnType 
     * @return 
     */
    public boolean addColumn(String columnName, String columnType){
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		  try{
            connection=ConnectionSource.getConnection(databaseName);
            statement=connection.createStatement();
            statement.execute("ALTER TABLE "+tableName+" ADD COLUMN "+columnName+" "+columnType);
            connection.close();
            return true;
        }catch (Exception e){
           try{connection.close();}catch(Exception ex){}
             return false;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
    /**Adds several columns to the table. The colnames and coltypes arrays must be of the same 
        * length, obvs - otherwise it will throw an illegal argument exception
        * 
        * 
        *
    */
    public boolean addColumns (String[] colnames, String [] coltypes) throws IllegalArgumentException{
    	if (colnames.length!=coltypes.length){throw new IllegalArgumentException("Table-addColumns: column names and column types don't match in length");}
    	for(int i=0; i<colnames.length; i++){
    		if(! (addColumn(colnames[i],coltypes[i]))){
    			return false;
    		} 
    	}
    	return true;
    }
     /** Adds a column to a table, of the given type, referencing another table
        * Expect errors (which will be logged) if the types/names/etc don't match: you have
        * been warned!
     *
     * @param columnName
     * @param columnType
        * @param foreignKeyTableName
     * @return
     */
    public boolean addForeignKeyColumn(String columnName, String columnType, String foreignKeyTableName)throws PlatosysDBException{
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		  try{
            connection=ConnectionSource.getConnection(databaseName);
           statement=connection.createStatement();
            statement.execute("ALTER TABLE "+tableName+" ADD COLUMN "+columnName+" "+columnType+" REFERENCES "+foreignKeyTableName);
            connection.close();
            return true;
        }catch (Exception e){
            logger.log("add foreign key column error", e);
           try{connection.close();}catch(Exception ex){}
            throw new PlatosysDBException("Problem adding foreign key column - missing table?", e);
             
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
   /**
     * Adds a constrained column to a table.  
     * @param columnName 
     * @param columnType 
    * @param unique UNIQUE constraint toggle
    * @param notNull NOT_NULL constraint toggle
     * @return true if successful
     */
    public boolean addColumn(String columnName, String columnType, boolean unique, boolean notNull){
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		 try{
            String unq="";
            String nnll="";
            if (unique){unq=" UNIQUE";}
            if (notNull){nnll=" NOT NULL";}
            connection=ConnectionSource.getConnection(databaseName);
           statement=connection.createStatement();
            statement.execute("ALTER TABLE "+tableName+" ADD COLUMN "+columnName+" "+columnType+unq+nnll);
            connection.close();
            return true;
        }catch (Exception e){
           try{connection.close();}catch(Exception ex){}
             return false;
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
    /*
    public boolean amend(String primaryKeyTest, String columnName, Object value)throws PlatosysDBException, TypeNotSupportedException{
        if (value instanceof String){return amend(primaryKeyTest, columnName, (String)value);}
        else if(value instanceof Long){return amend(primaryKeyTest, columnName, ((Long)value).longValue());}
        
        else{
            throw new TypeNotSupportedException("what is this type: "+value.getClass().getCanonicalName()+"?");
        }
    }*/
    
    /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * @return true if the amendment was successful
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amend(long primaryKeyTest, String columnName, String value) throws PlatosysDBException {
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		try{
            connection=ConnectionSource.getConnection(databaseName);
            statement = connection.createStatement();
            value=value.replace("\'","\'\'");
            rs = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "+primaryKeyColumnName+" = "+Long.toString(primaryKeyTest));
            if(rs.next()){
                try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = \'"+value+"\' WHERE "+primaryKeyColumnName+" = "+Long.toString(primaryKeyTest));
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?", e);
                }
            }else{
                try {
                    statement.execute("INSERT INTO "+tableName+ "("+primaryKeyColumnName+","+columnName+") VALUES ("+Long.toString(primaryKeyTest)+",\'"+value+"\')");
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?", e);
                }
            }
            //logger.log(5, "JDBCTable amended OK, closing connection");
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with String value "+value+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
  
    
    
    /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * @return true if the amendment was successful
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amend(String primaryKeyTest, String columnName, String value) throws PlatosysDBException {
        if(primaryKeyColumnName==null){
            throw new PlatosysDBException("no primary key column set on table: "+tableName);
        }
        Connection connection=null;
 		ResultSet rs=null;
 		Statement statement=null;
 		try{
            connection=ConnectionSource.getConnection(databaseName);
            statement = connection.createStatement();
            value=value.replace("\'","\'\'");
            rs = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
            if(rs.next()){
                String SQLString="";
                try {
                    SQLString="UPDATE "+tableName+" SET "+columnName+" = \'"+value+"\' WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\' ";
                    statement.execute(SQLString);
                }catch(Exception e){
                    throw new PlatosysDBException("JDBCTable.amend(SSS) error: SQLString is "+SQLString, e);
                }
            }else{
                String SQLString="";
                try {
                    SQLString="INSERT INTO "+tableName+ "("+primaryKeyColumnName+","+columnName+") VALUES (\'"+primaryKeyTest+"\',\'"+value+"\')";
                    statement.execute(SQLString);
                }catch(Exception e){
                    throw new PlatosysDBException("JDBCTable.amend(SSS) error: SQLString is "+SQLString, e);
                }
            }
            //logger.log(5, "JDBCTable amended OK, closing connection");
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with String value "+value+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
    public boolean amendWhere(String testColumn, String testValue, String columnName, String columnValue) throws PlatosysDBException{
        String[] testColumns = {testColumn};
        String[] testValues = {testValue};
        return amendWhere(testColumns, testValues, columnName, columnValue);
    }
     /**
     * Amend a table if a combination of conditions is true (only works for textual conditions!!).
     * @return true if the amendment was successful
     * @param testColumns - a string array of column names;
     * @param testValues - a string array of values;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amendWhere(String[] testColumns, String[] testValues,  String columnName, String value) throws PlatosysDBException {
        if (testColumns.length!=testValues.length){throw new PlatosysDBException("value and column arrays don't match");}
        Connection connection=null;
 		ResultSet rs=null;
 		Statement statement=null;
 		value=value.replace("\'","\'\'");
        try{
            connection=ConnectionSource.getConnection(databaseName);
            statement = connection.createStatement();
            String sqlWhereClause =  " WHERE (";
            for (int i=0; i<testColumns.length; i++){
                sqlWhereClause=sqlWhereClause+"("+testColumns[i]+" = \'"+testValues[i]+"\') AND ";
            }
            sqlWhereClause=sqlWhereClause.substring(0, sqlWhereClause.length()-4);
            sqlWhereClause=sqlWhereClause+")";
            String sqlQuery= "SELECT * FROM "+tableName+sqlWhereClause;
            rs = statement.executeQuery(sqlQuery);
            if(rs.next()){
                try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = \'"+value+"\'"+sqlWhereClause);
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with sql clause:" +sqlWhereClause, e);
                }
            }else{
                String insertClause="";
                String updateClause="";
                try {
                    String columnsClause="";
                    String valuesClause="";
                    for (int i=0; i<testColumns.length; i++){
                        columnsClause=columnsClause+testColumns[i]+"  ,";
                        valuesClause=valuesClause+"\'"+testValues[i]+"\'   ,";
                    }
                    columnsClause = columnsClause.substring(0, columnsClause.length()-2);
                    valuesClause=valuesClause.substring(0, valuesClause.length()-2);
                    insertClause=("INSERT INTO "+tableName+ "("+columnsClause+") VALUES ("+valuesClause+")");
                    updateClause=("UPDATE "+tableName+" SET "+columnName+" = \'"+value+"\'"+sqlWhereClause);
                    statement.execute(insertClause);
                    statement.execute(updateClause);
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with insert clause: "+insertClause+", updateClause:  "+updateClause, e);
                }
            }
            //logger.log(5, "JDBCTable amended OK, closing connection");
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with String value "+value+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
      /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * @return true if the amendment was successful
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amend(long primaryKeyTest, String columnName, long value) throws PlatosysDBException {
    	 if(primaryKeyColumnName==null){
             throw new PlatosysDBException("no primary key column set on table: "+tableName);
         }
    	 Connection connection=null;
  		ResultSet rs=null;
  		Statement statement=null;
  		try{
            connection=ConnectionSource.getConnection(databaseName);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "+primaryKeyColumnName+" = "+Long.toString(primaryKeyTest));
            if(rs.next()){
                try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = "+value+" WHERE "+primaryKeyColumnName+" = "+Long.toString(primaryKeyTest));
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?", e);
                }
            }else{
                try {
                    statement.execute("INSERT INTO "+tableName+ "("+primaryKeyColumnName+","+columnName+") VALUES ("+Long.toString(primaryKeyTest)+","+value+")");
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?", e);
                }
            }
            //logger.log(5, "JDBCTable amended OK, closing connection");
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with String value "+value+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
    /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * @return true if the amendment was successful
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if things go wrong
     */
    public boolean amend(String primaryKeyTest, String columnName, long value) throws PlatosysDBException {
    	 if(primaryKeyColumnName==null){
             throw new PlatosysDBException("no primary key column set on table: "+tableName);
         }
    	 Connection connection=null;
 		ResultSet rs=null;
 		Statement statement=null;
 		try{
            connection=ConnectionSource.getConnection(databaseName);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
            if(rs.next()){
                try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = "+Long.toString(value)+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\' ");
                }catch(Exception e){
                    throw new PlatosysDBException(" error updating row "+primaryKeyTest+", col:"+columnName+ " in table "+tableName+" , pk="+primaryKeyColumnName, e);
                }
            }else{
                try {
                    statement.execute("INSERT INTO "+tableName+ "("+primaryKeyColumnName+","+columnName+") VALUES (\'"+primaryKeyTest+"\',"+Long.toString(value)+")");
                }catch(Exception e){
                    throw new PlatosysDBException(" error adding row "+primaryKeyTest+" with col. "+columnName+ " in table "+tableName+" pk="+primaryKeyColumnName, e);
                }
            }
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with Long value "+Long.toString(value)+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
        /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * @return true if the amendment was successful
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amend(long primaryKeyTest, String columnName, double value) throws PlatosysDBException {
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;try{
            connection=ConnectionSource.getConnection(databaseName);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "+primaryKeyColumnName+" = "+Long.toString(primaryKeyTest));
            if(rs.next()){
                try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = "+value+" WHERE "+primaryKeyColumnName+" = "+Long.toString(primaryKeyTest));
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?", e);
                }
            }else{
                try {
                    statement.execute("INSERT INTO "+tableName+ "("+primaryKeyColumnName+","+columnName+") VALUES ("+Long.toString(primaryKeyTest)+","+value+")");
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?", e);
                }
            }
            //logger.log(5, "JDBCTable amended OK, closing connection");
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with String value "+value+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    } 
    @Override
	public boolean amend(long primaryKeyTest, String columnName,ISODate isoDate) throws PlatosysDBException {
    	 String value = isoDate.dateTimeMs();
    	 Connection connection=null;
 		ResultSet rs=null;
 		Statement statement=null;
 		try{
             connection=ConnectionSource.getConnection(databaseName);
             statement = connection.createStatement();
             rs = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "+primaryKeyColumnName+" = "+Long.toString(primaryKeyTest));
             if(rs.next()){
                 try {
                     statement.execute("UPDATE "+tableName+" SET "+columnName+" = \'"+value+"\' WHERE "+primaryKeyColumnName+" = "+Long.toString(primaryKeyTest));
                 }catch(Exception e){
                     throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?", e);
                 }
             }else{
                 try {
                     statement.execute("INSERT INTO "+tableName+ "("+primaryKeyColumnName+","+columnName+") VALUES ("+Long.toString(primaryKeyTest)+",\'"+value+"\')");
                 }catch(Exception e){
                     throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?", e);
                 }
             }
             //logger.log(5, "JDBCTable amended OK, closing connection");
             connection.close();
             return true;
         }catch(Exception e){
             try{
                 connection.close();
             }catch(Exception ex){}
             throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with String value "+value+", caused by:", e);
         }finally{
         	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
	}
    /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * @return true if the amendment was successful
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if things go wrong
     */
    public boolean amend(String primaryKeyTest, String columnName, double value) throws PlatosysDBException {
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
		try{
            connection=ConnectionSource.getConnection(databaseName);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
            if(rs.next()){
                try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = "+Double.toString(value)+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\' ");
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?", e);
                }
            }else{
                try {
                    statement.execute("INSERT INTO "+tableName+ "("+primaryKeyColumnName+","+columnName+") VALUES (\'"+primaryKeyTest+"\',"+Double.toString(value)+")");
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?",e);
                }
            }
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with Double value "+Double.toString(value)+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
     /**
     * Amend a table if a combination of conditions is true (only works for textual conditions!!).
     * @return true if the amendment was successful
     * @param testColumns - a string array of column names;
     * @param testValues - a string array of values;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amendWhere(String[] testColumns, String[] testValues,  String columnName, double value) throws PlatosysDBException {
        if (testColumns.length!=testValues.length){throw new PlatosysDBException("value and column arrays don't match");}
        Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
        try{
            connection=ConnectionSource.getConnection(databaseName);
            statement = connection.createStatement();
            String sqlWhereClause =  " WHERE (";
            for (int i=0; i<testColumns.length; i++){
                sqlWhereClause=sqlWhereClause+"("+testColumns[i]+" = \'"+testValues[i]+"\') AND ";
            }
            sqlWhereClause=sqlWhereClause.substring(0, sqlWhereClause.length()-4);
            sqlWhereClause=sqlWhereClause+")";
            String sqlQuery= "SELECT * FROM "+tableName+sqlWhereClause;
            rs = statement.executeQuery(sqlQuery);
            if(rs.next()){
                try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = "+Double.toString(value)+" "+sqlWhereClause);
                }catch(Exception e){
                    logger.log(1, sqlQuery);
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with sql clause:" +sqlWhereClause, e);
                }
            }else{
                try {
                    String columnsClause="";
                    String valuesClause="";
                    for (int i=0; i<testColumns.length; i++){
                        columnsClause=columnsClause+testColumns[i]+"  ,";
                        valuesClause=valuesClause+"\'"+testValues[i]+"\'   ,";
                    }
                    columnsClause = columnsClause.substring(0, columnsClause.length()-2);
                    valuesClause=valuesClause.substring(0, valuesClause.length()-2);
                    String sqlInsert="INSERT INTO "+tableName+ "("+columnsClause+") VALUES ("+valuesClause+")";
                    String sqlUpdate="UPDATE "+tableName+" SET "+columnName+" = "+Double.toString(value)+" "+sqlWhereClause;
                    try{
                        statement.execute(sqlInsert);
                    }catch(Exception e){
                        logger.log(sqlInsert);
                        throw new PlatosysDBException(sqlInsert,e);
                    }try{
                         statement.execute(sqlUpdate);
                    }catch(Exception e){
                        logger.log(sqlUpdate);
                        throw new PlatosysDBException(sqlUpdate,e);
                    }
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?", e);
                }
            }
           // logger.log(5, "JDBCTable amended OK, closing connection");
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with double value "+value+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
            /**
     * Amend a table if a combination of conditions is true (only works for textual conditions!!).
     * @return true if the amendment was successful
     * @param testColumns - a string array of column names;
     * @param testValues - a string array of values;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amendWhere(String[] testColumns, String[] testValues,  String columnName, java.sql.Timestamp timestamp) throws PlatosysDBException {
        if (testColumns.length!=testValues.length){throw new PlatosysDBException("value and column arrays don't match");}
        Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
		try{
            connection=ConnectionSource.getConnection(databaseName);
            statement = connection.createStatement();
            String sqlWhereClause =  " WHERE (";
            for (int i=0; i<testColumns.length; i++){
                sqlWhereClause=sqlWhereClause+"("+testColumns[i]+" = \'"+testValues[i]+"\') AND ";
            }
            sqlWhereClause=sqlWhereClause.substring(0, sqlWhereClause.length()-4);
            sqlWhereClause=sqlWhereClause+")";
            String sqlQuery= "SELECT * FROM "+tableName+sqlWhereClause;
            rs = statement.executeQuery(sqlQuery);
            if(rs.next()){
                try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = \'"+timestamp.toString()+"\' "+sqlWhereClause);
                }catch(Exception e){
                    logger.log(1, sqlQuery);
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with sql clause:" +sqlWhereClause, e);
                }
            }else{
                try {
                    String columnsClause="";
                    String valuesClause="";
                    for (int i=0; i<testColumns.length; i++){
                        columnsClause=columnsClause+testColumns[i]+"  ,";
                        valuesClause=valuesClause+"\'"+testValues[i]+"\'   ,";
                    }
                    columnsClause = columnsClause.substring(0, columnsClause.length()-2);
                    valuesClause=valuesClause.substring(0, valuesClause.length()-2);
                    String sqlInsert="INSERT INTO "+tableName+ "("+columnsClause+") VALUES ("+valuesClause+")";
                    String sqlUpdate="UPDATE "+tableName+" SET "+columnName+" = \'"+timestamp.toString()+"\' "+sqlWhereClause;
                    try{
                        statement.execute(sqlInsert);
                    }catch(Exception e){
                        logger.log(sqlInsert);
                        throw new PlatosysDBException(sqlInsert);
                    }try{
                         statement.execute(sqlUpdate);
                    }catch(Exception e){
                        logger.log(sqlUpdate);
                        throw new PlatosysDBException(sqlUpdate);
                    }
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?", e);
                }
            }
           // logger.log(5, "JDBCTable amended OK, closing connection");
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with timestamp value "+timestamp.toString()+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }  /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * @return true if the amendment was successful
     * @param date a java.sql.Date object for the new date
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @throws uk.co.platosys.db.PlatosysDBException 
     */
    public boolean amend(String primaryKeyTest, String columnName, java.sql.Date date) throws PlatosysDBException {
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
		try{
            connection=ConnectionSource.getConnection(databaseName);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
            String sqlString="";
            if(rs.next()){
                try {
                    sqlString=("UPDATE "+tableName+" SET "+columnName+" = \'"+date.toString()+"\' WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\' ");
                    statement.execute(sqlString);
                }catch(Exception e){
                  
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct? \n SqlString read: "+sqlString,e);
                }
            }else{
                try {
                    sqlString=("INSERT INTO "+tableName+ "("+primaryKeyColumnName+","+columnName+") VALUES (\'"+primaryKeyTest+"\',\'"+date.toString()+"\')");
                    statement.execute(sqlString);
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct? \n  SqlString read:"+sqlString,e);
                }
            }
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with Date value "+date.toString()+", caused by:" +e.getMessage(), e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
           /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * 
     * @return true if the amendment was successful
     * @param timestamp 
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @throws uk.co.platosys.db.PlatosysDBException 
     */
    public boolean amend(String primaryKeyTest, String columnName, java.sql.Timestamp timestamp) throws PlatosysDBException {
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
        try{
            connection=ConnectionSource.getConnection(databaseName);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
            if(rs.next()){
                try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = \'"+timestamp.toString()+"\' WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\' ");
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?");
                }
            }else{
                try {
                    statement.execute("INSERT INTO "+tableName+ "("+primaryKeyColumnName+","+columnName+") VALUES (\'"+primaryKeyTest+"\',\'"+timestamp.toString()+"\')");
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?");
                }
            }
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with timestamp value "+timestamp.toString()+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }

     public boolean amend(String primaryKeyTest, String columnName, ISODate date) throws PlatosysDBException {
    	 Connection connection=null;
 		ResultSet rs=null;
 		Statement statement=null;
        try{
            connection=ConnectionSource.getConnection(databaseName);
             statement = connection.createStatement();
             rs = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
            if(rs.next()){
                try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = \'"+date.dateTimeMs()+"\' WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\' ");
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?");
                }
            }else{
                try {
                    statement.execute("INSERT INTO "+tableName+ "("+primaryKeyColumnName+","+columnName+") VALUES (\'"+primaryKeyTest+"\',\'"+date.dateTimeMs()+"\')");
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?");
                }
            }
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with timestamp value "+date.dateTimeMs()+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }

   /**

     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * 
     * @return true if the amendment was successful
     * @param value boolean 
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @throws uk.co.platosys.db.PlatosysDBException 
     */
    public boolean amend(String primaryKeyTest, String columnName, boolean value) throws PlatosysDBException {
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
        try{
            connection=ConnectionSource.getConnection(databaseName);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
            if(rs.next()){
                try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = \'"+Boolean.toString(value)+"\' WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\' ");
                }catch(Exception e){
                    throw new PlatosysDBException(" error updating "+columnName+ " in table "+tableName+" -is the type correct?", e);
                }
            }else{
                try {
                    String sql = ("INSERT INTO "+tableName+ "("+primaryKeyColumnName+","+columnName+") VALUES (\'"+primaryKeyTest+"\',\'"+Boolean.toString(value)+"\')");
                    //logger.log(5,sql);
                    statement.execute(sql);
                }catch(Exception e){
                    throw new PlatosysDBException(" error inserting "+columnName+ " in table "+tableName+" -is the type correct?", e);
                }
            }
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with boolean value "+Boolean.toString(value)+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
     /**
     * Amend a table if a combination of conditions is true (only works for textual conditions!!).
     * @return true if the amendment was successful
     * @param testColumns - a string array of column names;
     * @param testValues - a string array of values;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amendWhere(String[] testColumns, String[] testValues,  String columnName, boolean value) throws PlatosysDBException {
        if (testColumns.length!=testValues.length){throw new PlatosysDBException("value and column arrays don't match");}
        Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
        String sqlInsert="";
        String sqlUpdate="";
        String sqlQuery="";
        try{
            connection=ConnectionSource.getConnection(databaseName);
             statement = connection.createStatement();
            String sqlWhereClause =  " WHERE (";
            for (int i=0; i<testColumns.length; i++){
                sqlWhereClause=sqlWhereClause+"("+testColumns[i]+" = \'"+testValues[i]+"\') AND ";
            }
            sqlWhereClause=sqlWhereClause.substring(0, sqlWhereClause.length()-4);
            sqlWhereClause=sqlWhereClause+")";
            sqlQuery= "SELECT * FROM "+tableName+sqlWhereClause;
             rs = statement.executeQuery(sqlQuery);
            if(rs.next()){
                try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = \'"+Boolean.toString(value)+"\'"+sqlWhereClause);
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with sql clause:" +sqlWhereClause, e);
                }
            }else{
                try {
                    String columnsClause="";
                    String valuesClause="";
                    for (int i=0; i<testColumns.length; i++){
                        //logger.log(5, "values clause ="+valuesClause);
                        columnsClause=columnsClause+testColumns[i]+"  ,";
                        //logger.log(5, "adding value["+Integer.toString(i)+"]: "+testValues[i]);
                        valuesClause=valuesClause+"\'"+testValues[i]+"\'   ,";
                        //logger.log(5, "values clause ="+valuesClause);
                    }
                    columnsClause = columnsClause.substring(0, columnsClause.length()-2);
                    valuesClause=valuesClause.substring(0, valuesClause.length()-2);
                    
                    sqlInsert=("INSERT INTO "+tableName+ "("+columnsClause+") VALUES ("+valuesClause+")");
                    statement.execute(sqlInsert);
                    sqlUpdate=("UPDATE "+tableName+" SET "+columnName+" = \'"+Boolean.toString(value)+"\'"+sqlWhereClause);
                    statement.execute(sqlUpdate);
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?\n" +
                            "sqlInser=\t"+sqlInsert+
                            "\nsqlUpdate=\t"+sqlUpdate, e);
                }
            }
           // logger.log(5, "JDBCTable amended OK, closing connection");
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with boolean value "+value+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
     public boolean updateWhere(String testColumn, String testValue, String columnName, String value) throws PlatosysDBException {
    	 Connection connection=null;
 		ResultSet rs=null;
 		Statement statement=null;
         connection=null;
        try{
            connection=ConnectionSource.getConnection(databaseName);
           statement = connection.createStatement();
               try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = \'"+value+"\' WHERE "+testColumn+" = \'"+testValue+"\' ");
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?");
                }
           
            
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with string value "+value+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
     /*
     public boolean updateWhere(String[] testColumns, String[] testValues, String columnName, String value) throws PlatosysDBException {
        Connection connection=null;
        if (testColumns.length!=testValues.length){throw new PlatosysDBException("WHERE arrays do not match");}
        try{
            connection=ConnectionSource.getConnection(databaseName);
            Statement statement = connection.createStatement();
               try {
                   String sqlString = "UPDATE "+tableName+" SET "+columnName+" = \'"+value+"\' WHERE ( ";
                   for (int i =0; i<testColumns.length; i++){
                       
                   }
                    statement.execute(sqlString);
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?");
                }
           
            
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with string value "+value+", caused by:", e);
        }
    }
      */
     public boolean updateWhere(String testColumn, String testValue, String columnName, boolean value) throws PlatosysDBException {
    	 Connection connection=null;
 		ResultSet rs=null;
 		Statement statement=null;
        try{
            connection=ConnectionSource.getConnection(databaseName);
            statement = connection.createStatement();
               try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = \'"+Boolean.toString(value)+"\' WHERE "+testColumn+" = \'"+testValue+"\' ");
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?");
                }
           
            
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with boolean value "+Boolean.toString(value)+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
     /**
     * reads an atomic string value from the table
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public String readString(long primaryKeyTest, String columnName)throws PlatosysDBException, RowNotFoundException{
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
        connection=ConnectionSource.getConnection(databaseName);
        try{
             statement=connection.createStatement();
             rs = statement.executeQuery("SELECT "+columnName+" FROM "+tableName+" WHERE "+primaryKeyColumnName+" = "+Long.toString(primaryKeyTest));
            if(rs.next()){
                try{
                    String string = rs.getString(columnName);
                    connection.close();
                    return string;
                }catch(Exception e){
                    connection.close();
                    throw new PlatosysDBException("Column "+columnName+" is not a text column");
                }
            }else{
                connection.close();
                throw new RowNotFoundException("Primary Key "+primaryKeyTest+ " not found in table "+tableName);
            }
        }catch(RowNotFoundException rnfe){
            try{connection.close();}catch(Exception p){}

            throw rnfe;
        }catch(Exception e){
            try{connection.close();}catch(Exception ex){}
            throw new PlatosysDBException("Problem reading table "+tableName);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
   
     /**
     * reads an atomic string value from the table
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public String readString(String primaryKeyTest, String columnName)throws PlatosysDBException, RowNotFoundException{
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
         connection=ConnectionSource.getConnection(databaseName);
        try{
          statement=connection.createStatement();
            String SQLString=("SELECT "+columnName+" FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
           rs = statement.executeQuery(SQLString);
            if(rs.next()){
                try{
                    String string = rs.getString(columnName);
                    connection.close();
                    return string;
                }catch(Exception e){
                    connection.close();
                    throw new PlatosysDBException("Column "+columnName+" is not a text column", e);
                }
            }else{
                connection.close();
                throw new RowNotFoundException("Primary Key "+primaryKeyTest+ " not found in table "+tableName+" : SQL="+SQLString);
            }
        }catch(RowNotFoundException rnfe){
            try{connection.close();}catch(Exception p){}

            throw rnfe;
        }catch(Exception e){
            try{connection.close();}catch(Exception ex){}
            throw new PlatosysDBException("Problem reading table "+tableName, e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
        /**
     * 
     * reads an atomic integer value from the database
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public long readLong(String primaryKeyTest, String columnName)throws PlatosysDBException, RowNotFoundException {
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
       connection=ConnectionSource.getConnection(databaseName);
        try{
           statement=connection.createStatement();
            rs = statement.executeQuery("SELECT "+columnName+" FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
            if(rs.next()){
                try{
                    long lng = rs.getLong(columnName);
                    connection.close();
                    return lng;
                }catch(Exception e){
                    connection.close();
                    throw new PlatosysDBException("Column "+columnName+" is not an integer column");
                }
            }else{
                connection.close();
                throw new RowNotFoundException("Primary Key "+primaryKeyTest+ " not found in table "+tableName);
            }
        }catch(RowNotFoundException rnfe){
            try{connection.close();}catch(Exception p){}

            throw rnfe;
        }catch(Exception e){
            try{connection.close();}catch(Exception ex){}
            throw new PlatosysDBException("Problem reading table "+tableName);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
   
    /**
     * 
     * reads an atomic integer value from the database
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public long readLong(long primaryKeyTest, String columnName)throws PlatosysDBException, RowNotFoundException {
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
       connection=ConnectionSource.getConnection(databaseName);
        try{
            statement=connection.createStatement();
            rs = statement.executeQuery("SELECT "+columnName+" FROM "+tableName+" WHERE "+primaryKeyColumnName+" = "+Long.toString(primaryKeyTest));
            if(rs.next()){
                try{
                    long lng = rs.getLong(columnName);
                    connection.close();
                    return lng;
                }catch(Exception e){
                    connection.close();
                    throw new PlatosysDBException("Column "+columnName+" is not an integer column");
                }
            }else{
                connection.close();
                throw new RowNotFoundException("Primary Key "+primaryKeyTest+ " not found in table "+tableName);
            }
        }catch(RowNotFoundException rnfe){
            try{connection.close();}catch(Exception p){}

            throw rnfe;
        }catch(Exception e){
            try{connection.close();}catch(Exception ex){}
            throw new PlatosysDBException("Problem reading table "+tableName);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
        /**
     * 
     * reads an atomic date value (as a platosys ISODate) from the databas
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public ISODate readDate(String primaryKeyTest, String columnName)throws PlatosysDBException, RowNotFoundException {
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
        connection=ConnectionSource.getConnection(databaseName);
        try{
           statement=connection.createStatement();
            rs = statement.executeQuery("SELECT "+columnName+" FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
            if(rs.next()){
                try{
                    java.sql.Date sqlDate = rs.getDate(columnName);
                    connection.close();
                    return new ISODate(sqlDate.getTime());
                }catch(Exception e){
                    connection.close();
                    throw new RowNotFoundException("Column "+columnName+" is not a Date column");
                }
            }else{
                connection.close();
                throw new PlatosysDBException("Primary Key "+primaryKeyTest+ " not found in table "+tableName);
            }
        }catch(RowNotFoundException rnfe){
            try{connection.close();}catch(Exception p){}

            throw rnfe;
        }catch(Exception e){
            try{connection.close();}catch(Exception ex){}
            throw new PlatosysDBException("Problem reading table "+tableName);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
        /**
     * 
     * reads an atomic numeric value from the database
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public float readNumber(long primaryKeyTest, String columnName)throws PlatosysDBException, RowNotFoundException {
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
        connection=ConnectionSource.getConnection(databaseName);
        try{
            statement=connection.createStatement();
            rs = statement.executeQuery("SELECT "+columnName+" FROM "+tableName+" WHERE "+primaryKeyColumnName+" = "+Long.toString(primaryKeyTest));
            if(rs.next()){
                try{
                    float dbl  = rs.getFloat(columnName);
                    connection.close();
                    return dbl;
                }catch(Exception e){
                    connection.close();
                    throw new PlatosysDBException("Column "+columnName+" is not a numeric column");
                }
            }else{
                connection.close();
                throw new RowNotFoundException("Primary Key "+primaryKeyTest+ " not found in table "+tableName);
            }
        }catch(RowNotFoundException rnfe){
            try{connection.close();}catch(Exception p){}

            throw rnfe;
        }catch(Exception e){
            try{connection.close();}catch(Exception ex){}
            throw new PlatosysDBException("Problem reading table "+tableName);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
   
    /**
     * 
     * reads an atomic numeric value from the database
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public float readNumber(String primaryKeyTest, String columnName)throws PlatosysDBException, RowNotFoundException {
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
        connection=ConnectionSource.getConnection(databaseName);
        try{
             statement=connection.createStatement();
            rs  = statement.executeQuery("SELECT "+columnName+" FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
            if(rs.next()){
                try{
                    float dbl  = rs.getFloat(columnName);
                    connection.close();
                    return dbl;
                }catch(Exception e){
                    connection.close();
                    throw new PlatosysDBException("Column "+columnName+" is not a numeric column");
                }
            }else{
                connection.close();
                throw new RowNotFoundException("Primary Key "+primaryKeyTest+ " not found in table "+tableName);
            }
        }catch(RowNotFoundException rnfe){
            try{connection.close();}catch(Exception p){}

            throw rnfe;
        }catch(Exception e){
            try{connection.close();}catch(Exception ex){}
            throw new PlatosysDBException("Problem reading table "+tableName);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
        /**
     * 
     * reads an atomic timestamp
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public ISODate readTimeStamp(String primaryKeyTest, String columnName) throws PlatosysDBException, RowNotFoundException {
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
       connection=ConnectionSource.getConnection(databaseName);
        try{
            statement=connection.createStatement();
            rs = statement.executeQuery("SELECT "+columnName+" FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
            if(rs.next()){
                try{
                    java.sql.Timestamp tmstmp = rs.getTimestamp(columnName);
                    connection.close();
                    return new ISODate(tmstmp.getTime());
                }catch(Exception e){
                    connection.close();
                    throw new PlatosysDBException("Column "+columnName+" is not a timestamp column");
                }
            }else{
                connection.close();
                throw new RowNotFoundException("Primary Key "+primaryKeyTest+ " not found in table "+tableName);
            }
         }catch(RowNotFoundException rnfe){
             try{connection.close();}catch(Exception p){}

            throw rnfe;
        }catch(Exception e){
            try{connection.close();}catch(Exception ex){}
            throw new PlatosysDBException("Problem reading table "+tableName);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }
          /**
     * 
     * reads a boolean boolean
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public boolean readBoolean(String primaryKeyTest, String columnName) throws PlatosysDBException, RowNotFoundException {
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
         
        try{
        	connection=ConnectionSource.getConnection(databaseName);
            statement=connection.createStatement();
            String SQLString=("SELECT "+columnName+" FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
             rs = statement.executeQuery(SQLString);
            if(rs.next()){
                try{
                    boolean bool = rs.getBoolean(columnName);
                    connection.close();
                    return bool;
                }catch(Exception e){
                    connection.close();
                    throw new PlatosysDBException("Column "+columnName+" is not a boolean column");
                }
            }else{
                connection.close();
                throw new RowNotFoundException("Primary Key "+primaryKeyTest+ " not found in table "+tableName+ ": SQL is "+SQLString);
            }
         }catch(RowNotFoundException rnfe){
             try{connection.close();}catch(Exception p){}

            throw rnfe;
        }catch(Exception e){
            try{connection.close();}catch(Exception ex){}
            throw new PlatosysDBException("Problem reading table "+tableName);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
    
    public static boolean tableExists(String databaseName, String tableName){
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
         try{
           connection=ConnectionSource.getConnection(databaseName);
          statement = connection.createStatement();
           try {
                 rs = statement.executeQuery("SELECT tablename FROM pg_tables WHERE schemaname = \'public\'");
                while(rs.next()){
                    if (rs.getString("tablename").equalsIgnoreCase(tableName)){
                        rs.close();
                        statement.close();
                    	connection.close();
                        return true;
                    }
                }
                connection.close();
                return false;
           }catch(PSQLException psqe){
               logger.log("JDBCTable - table exists: PSQL exception", psqe);
               connection.close();
               return false;
             
           }
       }catch(Exception e){
           logger.log("TABLE - TableExists? error: ", e);
           return false;
       }finally{
       	try{
    		if (rs!=null){rs.close();}
    		if (statement!=null){statement.close();}
    		if (connection!=null){connection.close();}
    	}catch(Exception x){}
    }
    }
    public int getSize(){
    	Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
        try{
            connection=ConnectionSource.getConnection(databaseName);
           statement = connection.createStatement();
           
   		 
   		
        try{
           int size=0;
            rs = statement.executeQuery("SELECT * FROM "+tableName);
            while (rs.next()){
                    size++;
                }
                connection.close();
                return size;
           }catch(Exception e){
               logger.log("TABLEgetSize error",e);
               try{connection.close();}catch(Exception x){}
               return 0;
           }
       }catch(Exception e){
           

           logger.log("TABLEgetSize - TableExists? error: ", e);
           return 0;
       }finally{
       	try{
    		if (rs!=null){rs.close();}
    		if (statement!=null){statement.close();}
    		if (connection!=null){connection.close();}
    	}catch(Exception x){}
    }
    }
	@Override
	public boolean updateWhere(String[] testColumns, String[] testValues,
			String columnName, String value)
			throws uk.co.platosys.db.PlatosysDBException {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * static wrapper for the constructor to allow better implementations with round tuits. 
	 * (e.g. pooling object references.... asif whenever)
	 * @param dbName
	 * @param tbName
	 * @return
	 * @throws PlatosysDBException
	 */
	public static JDBCTable getTable(String dbName,String tbName)throws PlatosysDBException { 
		if (JDBCTable.tableExists(dbName, tbName)){
			return new JDBCTable(dbName, tbName);
		}else{
			throw new PlatosysDBException("table "+tbName+" does not exist in db "+dbName);
		}
		
	}
	
	/**
	 * static wrapper for the constructor to allow better implementations with round tuits. 
	 * (e.g. pooling object references.... asif whenever)
	 * @param dbName
	 * @param tbName
	 * @return
	 * @throws PlatosysDBException
	 */
	public static JDBCTable getTable(String dbName,String tbName, String pkName)throws PlatosysDBException { 
		if (JDBCTable.tableExists(dbName, tbName)){
			return new JDBCTable(dbName, tbName, pkName);//we should really check that this column exists and is a primary key.
		}else{
			throw new PlatosysDBException("table "+tbName+" does not exist in db "+dbName);
		}
		
	}
	
	/**
	 * static wrapper for the constructor to allow better implementations with round tuits. 
	 * (e.g. pooling object references.... asif whenever)
	 * @param dbName
	 * @param tbName
	 * @return
	 * @throws PlatosysDBException
	 */
	public static JDBCTable getTable(String dbName,String tbName, String pkName, String pkType)throws PlatosysDBException { 
		if (JDBCTable.tableExists(dbName, tbName)){
			return new JDBCTable(dbName, tbName, pkName, pkType);//we should really check that this column exists and is a primary key.
		}else{
			throw new PlatosysDBException("table "+tbName+" does not exist in db "+dbName);
		}
	}
	@Override
	public BigDecimal readDecimal(long primaryKeyTest, String columnName)
			throws PlatosysDBException, RowNotFoundException {
		  Connection connection=ConnectionSource.getConnection(databaseName);
		  ResultSet rs=null;
			Statement statement=null;
	        try{
	          statement=connection.createStatement();
	            String SQLString=("SELECT "+columnName+" FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
	            rs = statement.executeQuery(SQLString);
	            if(rs.next()){
	                try{
	                    BigDecimal bool = rs.getBigDecimal(columnName);
	                    connection.close();
	                    return bool;
	                }catch(Exception e){
	                    connection.close();
	                    throw new PlatosysDBException("Column "+columnName+" is not a boolean column");
	                }
	            }else{
	                connection.close();
	                throw new RowNotFoundException("Primary Key "+primaryKeyTest+ " not found in table "+tableName+ ": SQL is "+SQLString);
	            }
	         }catch(RowNotFoundException rnfe){
	             try{connection.close();}catch(Exception p){}

	            throw rnfe;
	        }catch(Exception e){
	            try{connection.close();}catch(Exception ex){}
	            throw new PlatosysDBException("Problem reading table "+tableName);
	        }finally{
            	try{
            		if (rs!=null){rs.close();}
            		if (statement!=null){statement.close();}
            		if (connection!=null){connection.close();}
            	}catch(Exception x){}
            }
	}
	@Override
	public BigDecimal readDecimal(String primaryKeyTest, String columnName)
			throws PlatosysDBException, RowNotFoundException {
		  Connection connection=ConnectionSource.getConnection(databaseName);
		  
			ResultSet rs=null;
			Statement statement=null;
	        try{
	            statement=connection.createStatement();
	            String SQLString=("SELECT "+columnName+" FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
	           rs = statement.executeQuery(SQLString);
	            if(rs.next()){
	                try{
	                    BigDecimal bool = rs.getBigDecimal(columnName);
	                    connection.close();
	                    return bool;
	                }catch(Exception e){
	                    connection.close();
	                    throw new PlatosysDBException("Column "+columnName+" is not a boolean column");
	                }
	            }else{
	                connection.close();
	                throw new RowNotFoundException("Primary Key "+primaryKeyTest+ " not found in table "+tableName+ ": SQL is "+SQLString);
	            }
	         }catch(RowNotFoundException rnfe){
	             try{connection.close();}catch(Exception p){}

	            throw rnfe;
	        }catch(Exception e){
	            try{connection.close();}catch(Exception ex){}
	            throw new PlatosysDBException("Problem reading table "+tableName);
	        }finally{
            	try{
            		if (rs!=null){rs.close();}
            		if (statement!=null){statement.close();}
            		if (connection!=null){connection.close();}
            	}catch(Exception x){}
            }
	}
	@Override
	public boolean amend(long primaryKeyTest, String columnName,	BigDecimal value) throws PlatosysDBException {
		Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
        try{
            connection=ConnectionSource.getConnection(databaseName);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "+primaryKeyColumnName+" = "+Long.toString(primaryKeyTest));
            if(rs.next()){
                try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = "+value.toPlainString()+" WHERE "+primaryKeyColumnName+" = "+Long.toString(primaryKeyTest));
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?", e);
                }
            }else{
                try {
                    statement.execute("INSERT INTO "+tableName+ "("+primaryKeyColumnName+","+columnName+") VALUES ("+Long.toString(primaryKeyTest)+","+value.toPlainString()+")");
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?", e);
                }
            }
            //logger.log(5, "JDBCTable amended OK, closing connection");
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with String value "+value+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
	}
	@Override
	public boolean amendWhere(String[] testColumns, String[] testValues,String columnName, BigDecimal value) throws PlatosysDBException {
	     if (testColumns.length!=testValues.length){throw new PlatosysDBException("value and column arrays don't match");}
	        Connection connection=null;
	        ResultSet rs=null;
			Statement statement=null;
	        try{
	            connection=ConnectionSource.getConnection(databaseName);
	            statement = connection.createStatement();
	            String sqlWhereClause =  " WHERE (";
	            for (int i=0; i<testColumns.length; i++){
	                sqlWhereClause=sqlWhereClause+"("+testColumns[i]+" = \'"+testValues[i]+"\') AND ";
	            }
	            sqlWhereClause=sqlWhereClause.substring(0, sqlWhereClause.length()-4);
	            sqlWhereClause=sqlWhereClause+")";
	            String sqlQuery= "SELECT * FROM "+tableName+sqlWhereClause;
	            rs = statement.executeQuery(sqlQuery);
	            if(rs.next()){
	                try {
	                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = "+value.toPlainString()+" "+sqlWhereClause);
	                }catch(Exception e){
	                    logger.log(1, sqlQuery);
	                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with sql clause:" +sqlWhereClause, e);
	                }
	            }else{
	                try {
	                    String columnsClause="";
	                    String valuesClause="";
	                    for (int i=0; i<testColumns.length; i++){
	                        columnsClause=columnsClause+testColumns[i]+"  ,";
	                        valuesClause=valuesClause+"\'"+testValues[i]+"\'   ,";
	                    }
	                    columnsClause = columnsClause.substring(0, columnsClause.length()-2);
	                    valuesClause=valuesClause.substring(0, valuesClause.length()-2);
	                    String sqlInsert="INSERT INTO "+tableName+ "("+columnsClause+") VALUES ("+valuesClause+")";
	                    String sqlUpdate="UPDATE "+tableName+" SET "+columnName+" = "+value.toPlainString()+" "+sqlWhereClause;
	                    try{
	                        statement.execute(sqlInsert);
	                    }catch(Exception e){
	                        logger.log(sqlInsert);
	                        throw new PlatosysDBException(sqlInsert,e);
	                    }try{
	                         statement.execute(sqlUpdate);
	                    }catch(Exception e){
	                        logger.log(sqlUpdate);
	                        throw new PlatosysDBException(sqlUpdate,e);
	                    }
	                }catch(Exception e){
	                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?", e);
	                }
	            }
	           // logger.log(5, "JDBCTable amended OK, closing connection");
	            connection.close();
	            return true;
	        }catch(Exception e){
	            try{
	                connection.close();
	            }catch(Exception ex){}
	            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with BigDecimal value "+value+", caused by:", e);
	        }finally{
            	try{
            		if (rs!=null){rs.close();}
            		if (statement!=null){statement.close();}
            		if (connection!=null){connection.close();}
            	}catch(Exception x){}
            }
	}
	@Override
	public boolean amend(String primaryKeyTest, String columnName,			BigDecimal value) throws PlatosysDBException {
		Connection connection=null;
		ResultSet rs=null;
		Statement statement=null;
        try{
            connection=ConnectionSource.getConnection(databaseName);
             statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\'");
            if(rs.next()){
                try {
                    statement.execute("UPDATE "+tableName+" SET "+columnName+" = "+value.toPlainString()+" WHERE "+primaryKeyColumnName+" = \'"+primaryKeyTest+"\' ");
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?", e);
                }
            }else{
                try {
                    statement.execute("INSERT INTO "+tableName+ "("+primaryKeyColumnName+","+columnName+") VALUES (\'"+primaryKeyTest+"\',"+value.toPlainString()+")");
                }catch(Exception e){
                    throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" -is the type correct?",e);
                }
            }
            connection.close();
            return true;
        }catch(Exception e){
            try{
                connection.close();
            }catch(Exception ex){}
            throw new PlatosysDBException(" error amending "+columnName+ " in table "+tableName+" with decimal value "+value.toPlainString()+", caused by:", e);
        }finally{
        	try{
        		if (rs!=null){rs.close();}
        		if (statement!=null){statement.close();}
        		if (connection!=null){connection.close();}
        	}catch(Exception x){}
        }
	}
	@Override
	public boolean setUnique(String name, String[] colnames) throws PlatosysDBException {
		try{
		Connection connection = ConnectionSource.getConnection(databaseName);
		 String query = "ALTER TABLE "+tableName+ " ADD CONSTRAINT "+name+" UNIQUE (";
		 for (String colname:colnames){
			 query=query+colname+",";
		 }
		 query=query.substring(0, query.length()-1);//should chop off the last comma.
		 query=query+")";
		 logger.log(query);
		 Statement statement = connection.createStatement();
		 boolean done = statement.execute(query);
		 statement.close();
		 connection.close();
		 return done;
		}catch(Exception ex){
			logger.log("JDBCTable setUnique threw a problem ", ex);
			throw new PlatosysDBException("JDBCTable setUnique issue", ex);
		}
	}	
}

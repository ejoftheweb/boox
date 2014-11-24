/*ava
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

package uk.co.platosys.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.postgresql.util.PSQLException;

import uk.co.platosys.util.ISODate;
import uk.co.platosys.util.Logger;

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
 * - to add a timestamp column: table.addColumn("timestamp", Table.TIMESTAMP_COLUMN);
 * - amend an item in a row: table.amend(rowKey, columnName, newValue); (if it can't find the row, a new one is added).
 * - read a date: ISODate isoDate = table.readDate(rowKey, columnName);
 *
 *
 * @author edward
 */
public interface Table {
    public static final String TEXT_COLUMN="text";
    public static final String BOOLEAN_COLUMN="boolean";
    public static final String NUMERIC_COLUMN="numeric";
    public static final String INTEGER_COLUMN="integer";
    public static final String DATE_COLUMN="date";
    public static final String TIMESTAMP_COLUMN="timestamp";
    public static final String DEFAULT_DATABASE="musite";//HOBBLEDY_HOOP should not be here!
    

    public Set<String> getColumnNames();
    /**
     * Table refers to rows by the primary key.
     * @param primaryKeyTest the value of the primary key to be tested for
     * @return true if the row is found
     */
    public boolean rowExists(String primaryKeyTest);
    /**
     * Works only for String values!
     * @param columnName 
     * @param columnValue
     * @return true if at lease one row is found where columnName=columnValue
     */
    public boolean rowExists(String columnName, String columnValue);    /**
     * Works only for String values!
     * @param columnName 
     * @param columnValue
     * @return true if at lease one row is found where columnName=columnValue
         * @throws PlatosysDBException  
     */
    public boolean rowExists(String[] columns, String[] values)throws PlatosysDBException;
     public boolean addRow(String columnName, long columnValue);
   
    public boolean addRow(String columnName, String columnValue);
  
    public boolean addRow(String[] columns, String[] values)throws PlatosysDBException;
    public Row getRow(String primaryKeyTest)throws RowNotFoundException ;
    /**
     * Selects a row on something other than the table's primary key. Returns only the first matching 
     * row found, so can only safely be used on columns with a UNIQUE constraint, but it doesn't test for this.
     * Also only works for text/varchar etc cols (which contain Strings).
     * @param queryColumnName the name of the column to search on;
     * @param queryColumnValue the value to search for;
     * @return a Row object named queryColumnName=queryColumnValue
     */
    
    public Row getRow(String queryColumnName, String queryColumnValue)throws RowNotFoundException ;
    /**
     *Returns the first Row found where the columns in testColumns have the values in testValuses
     */
    public Row getRow(String[] testColumns, String[] testValues) throws RowNotFoundException ;
    /**
     *Returns a List of Rows where the columns in testColumns have the values in testValuses
     */
    public List<Row> getRows(String[] testColumns, String[] testValues);
 /**
     *Returns a List of Rows where the given column name has the given value
     */
    public List<Row> getRows(String column, String value);
    /**
     * Returns a List of Rows meeting the condition specified as the SQL where clause.
     *
     * @param sqlWhere
     * @return
     */
    public List<Row> getRows(String sqlWhere);
        /**
         * Returns the entire Table as a List of Rows
         * @return
         */

          public List<Row> getRows();
            /**
         * Returns the entire Table as a List of Rows
         * @return
         */

      public List<Row> getSortedRows(String sortedColumnName);
    public boolean columnExists(String columnName);    
    
    /**
     * Adds an unconstrained column to a table, of the given type.
     *
     * @param columnName 
     * @param columnType 
     * @return 
     */
    public boolean addColumn(String columnName, String columnType);
       /**
     * Adds a column to a table, of the given type, referencing another table
        * Expect errors (which will be logged) if the types/names/etc don't match: you have
        * been warned!
     *
     * @param columnName
     * @param columnType
        * @param foreignKeyTableName
     * @return
     */
    public boolean addForeignKeyColumn(String columnName, String columnType, String foreignKeyTableName)throws PlatosysDBException;;
   /**
     * Adds a constrained column to a table.  
     * @param columnName 
     * @param columnType 
    * @param unique UNIQUE constraint toggle
    * @param notNull NOT_NULL constraint toggle
     * @return true if successful
     */
    public boolean addColumn(String columnName, String columnType, boolean unique, boolean notNull);
    
    public boolean amend(String primaryKeyTest, String columnName, Object value)throws PlatosysDBException, TypeNotSupportedException;;
    
    /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * @return true if the amendment was successful
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amend(long primaryKeyTest, String columnName, String value) throws PlatosysDBException;  
    /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * @return true if the amendment was successful
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amend(String primaryKeyTest, String columnName, String value) throws PlatosysDBException;
    public boolean amendWhere(String testColumn, String testValue, String columnName, String columnValue)throws PlatosysDBException ;
     /**
     * Amend a table if a combination of conditions is true (only works for textual conditions!!).
     * @return true if the amendment was successful
     * @param testColumns - a string array of column names;
     * @param testValues - a string array of values;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amendWhere(String[] testColumns, String[] testValues,  String columnName, String value) throws PlatosysDBException;
      /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * @return true if the amendment was successful
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amend(long primaryKeyTest, String columnName, long value) throws PlatosysDBException ;
    /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * @return true if the amendment was successful
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if things go wrong
     */
    public boolean amend(String primaryKeyTest, String columnName, long value) throws PlatosysDBException ;
        /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * @return true if the amendment was successful
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amend(long primaryKeyTest, String columnName, double value) throws PlatosysDBException ;
    /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * @return true if the amendment was successful
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if things go wrong
     */
    public boolean amend(String primaryKeyTest, String columnName, double value) throws PlatosysDBException;
     /**
     * Amend a table if a combination of conditions is true (only works for textual conditions!!).
     * @return true if the amendment was successful
     * @param testColumns - a string array of column names;
     * @param testValues - a string array of values;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amendWhere(String[] testColumns, String[] testValues,  String columnName, double value) throws PlatosysDBException;
            /**
     * Amend a table if a combination of conditions is true (only works for textual conditions!!).
     * @return true if the amendment was successful
     * @param testColumns - a string array of column names;
     * @param testValues - a string array of values;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amendWhere(String[] testColumns, String[] testValues,  String columnName, java.sql.Timestamp timestamp) throws PlatosysDBException ;  /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * @return true if the amendment was successful
     * @param date a java.sql.Date object for the new date
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @throws uk.co.platosys.db.PlatosysDBException 
     */
    public boolean amend(String primaryKeyTest, String columnName, java.sql.Date date) throws PlatosysDBException;
           /**
     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * 
     * @return true if the amendment was successful
     * @param timestamp 
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @throws uk.co.platosys.db.PlatosysDBException 
     */
    public boolean amend(String primaryKeyTest, String columnName, java.sql.Timestamp timestamp) throws PlatosysDBException;

     public boolean amend(String primaryKeyTest, String columnName, ISODate date) throws PlatosysDBException ;

   /**

     * allows easy amendments to a database table without the need to construct complex escaped sql strings.
     * 
     * @return true if the amendment was successful
     * @param value boolean 
     * @param primaryKeyTest - the primary key identifying the row to be amended;
     * @param columnName - the column to be amended;
     * @throws uk.co.platosys.db.PlatosysDBException 
     */
    public boolean amend(String primaryKeyTest, String columnName, boolean value) throws PlatosysDBException ;
     /**
     * Amend a table if a combination of conditions is true (only works for textual conditions!!).
     * @return true if the amendment was successful
     * @param testColumns - a string array of column names;
     * @param testValues - a string array of values;
     * @param columnName - the column to be amended;
     * @param value - the new value for the column
     * @throws uk.co.platosys.db.PlatosysDBException if wrong
     */
    public boolean amendWhere(String[] testColumns, String[] testValues,  String columnName, boolean value) throws PlatosysDBException;
     public boolean updateWhere(String testColumn, String testValue, String columnName, String value) throws PlatosysDBException;
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
     public boolean updateWhere(String testColumn, String testValue, String columnName, boolean value) throws PlatosysDBException;
     /**
     * reads an atomic string value from the table
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public String readString(long primaryKeyTest, String columnName)throws PlatosysDBException, RowNotFoundException;
     /**
     * reads an atomic string value from the table
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public String readString(String primaryKeyTest, String columnName)throws PlatosysDBException, RowNotFoundException;
        /**
     * 
     * reads an atomic integer value from the database
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public long readLong(String primaryKeyTest, String columnName)throws PlatosysDBException, RowNotFoundException ;
   
    /**
     * 
     * reads an atomic integer value from the database
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public long readLong(long primaryKeyTest, String columnName)throws PlatosysDBException, RowNotFoundException ;
        /**
     * 
     * reads an atomic date value (as a platosys ISODate) from the databas
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public ISODate readDate(String primaryKeyTest, String columnName)throws PlatosysDBException, RowNotFoundException ;
        /**
     * 
     * reads an atomic numeric value from the database
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public Double readNumber(long primaryKeyTest, String columnName)throws PlatosysDBException, RowNotFoundException;
   
    /**
     * 
     * reads an atomic numeric value from the database
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public Double readNumber(String primaryKeyTest, String columnName)throws PlatosysDBException, RowNotFoundException ;
        /**
     * 
     * reads an atomic timestamp
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public ISODate readTimeStamp(String primaryKeyTest, String columnName) throws PlatosysDBException, RowNotFoundException ;
          /**
     * 
     * reads a boolean boolean
     * @param primaryKeyTest the primary key identifying the table row;
     * @param columnName the name of the column to be read;
     * @return the value read from the table;
     * @throws PlatosysDBException if the column type is wrong or the primary key doesn't exist.
     */
    public boolean readBoolean(String primaryKeyTest, String columnName) throws PlatosysDBException, RowNotFoundException ;
    public void setDatabaseName(String databaseName) ;
    
    //public static boolean tableExists(String databaseName, String tableName);
    public int getSize();
	boolean amend(long primaryKeyTest, String columnName, ISODate isoDate)
			throws PlatosysDBException;
	boolean updateWhere(String[] testColumns, String[] testValues,
			String columnName, String value) throws PlatosysDBException;
}
/*
 * Copyright Edward Barrow and Platosys.
 * This software is licensed under the Free Software Foundation's
General Public Licence, version 2 ("the GPL").
The full terms of the licence can be found online at http://www.fsf.org/

In brief, you are free to copy and to modify the code in any way you wish, but if you
publish the modified code you may only do so under the GPL, and (if asked) you must
 supply a copy of the source code alongside any compiled code.

Platosys software can also be licensed on negotiated terms if the GPL is inappropriate.
For further information about this, please contact software.licensing@platosys.co.uk
 */

package uk.co.platosys.db.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.TimestampedTable;
import uk.co.platosys.util.ISODate;
import uk.co.platosys.util.Logger;

/**
 * TimestampedTables are Tables with an additional Timestamp column.
 * @author edward
 */
public class JDBCTimestampedTable extends JDBCTable implements TimestampedTable {
    public static final String TT_NAME="db_timestamp";
static Logger plogger = new Logger("platosys", false);

    public JDBCTimestampedTable(String databaseName, String tableName, String primaryKeyColumnName)throws PlatosysDBException{
        super(databaseName, tableName, primaryKeyColumnName);
        //this.addColumn("timestamp", JDBCTable.TIMESTAMP_COLUMN);
    }
       public JDBCTimestampedTable(String databaseName, String tableName)throws PlatosysDBException{
        super(databaseName, tableName);
    }

   /**
     *Creates a table in databaseName, with the name tableName, and a primary key column named primaryKeyColumnName
     *
     */

    public static JDBCTimestampedTable createTable(String databaseName, String tableName, String primaryKeyColumnName, String primaryKeyColumnType) throws PlatosysDBException {
        //this.databaseName=databaseName;
        Connection connection=null;
            connection=ConnectionSource.getConnection(databaseName);
            try{
                Statement statement=connection.createStatement();
                statement.execute("CREATE TABLE "+tableName+" ("+primaryKeyColumnName+ " "+primaryKeyColumnType+" PRIMARY KEY, "+TT_NAME+" TIMESTAMP )");
                connection.close();
                return new JDBCTimestampedTable(databaseName, tableName, primaryKeyColumnName);
            }catch(Exception ex){
                plogger.log("problem creating timestamped table "+tableName+"  in db "+databaseName);
                try{connection.close();}catch(Exception p){}
               return null;
            }
    }
    /**
     *Creates a table in databaseName, with the name tableName, and  column named columnName of type columnType, which will be
     * a primary key column (i.e. UNIQUE and NOT NULL) if primaryKey is true.
     *
     */
    
    public static JDBCTimestampedTable createTable(String databaseName, String tableName, String columnName, String columnType, boolean primaryKey) throws PlatosysDBException {
        //this.databaseName=databaseName;
        Connection connection=null;
            connection=ConnectionSource.getConnection(databaseName);
            try{
                Statement statement=connection.createStatement();
                String sqlString = null;
                if (primaryKey){
                    sqlString="CREATE TABLE "+tableName+" ("+columnName+ " "+columnType+" PRIMARY KEY, "+TT_NAME+" TIMESTAMP )";
                }else{
                    sqlString="CREATE TABLE "+tableName+" ("+columnName+ " "+columnType+", "+TT_NAME+" TIMESTAMP )";
                }
                statement.execute(sqlString);
                connection.close();
                return new JDBCTimestampedTable(databaseName, tableName);
            }catch(Exception ex){
               try{connection.close();}catch(Exception p){}

               return null;
            }
    }
        public boolean addTimestampedRow(String columnName, String columnValue) throws PlatosysDBException{
        Connection connection=null;
        try{
            connection=ConnectionSource.getConnection(super.databaseName);
            Statement statement=connection.createStatement();
            statement.execute("INSERT INTO "+tableName+" ("+columnName+", db_timestamp) VALUES (\'"+columnValue+"\',\'"+new ISODate().dateTimeMs()+"\')");
            connection.close();
            return true;
        }catch(Exception e){
            //Logger.log("JDBCTable.addRow(): error", e);
            try{connection.close();}catch(Exception cex){}
          throw new PlatosysDBException("problem addint timestamped row", e);
        }
    }
     public List<Row> getRowsUntil(String[] columns, String[] values, ISODate timestamp){

        Connection connection=null;
        List<Row> rows = new ArrayList<Row>();
        try{
            connection=ConnectionSource.getConnection(databaseName);
            Statement statement=connection.createStatement();
            String sqlWhereClause =  " WHERE ("+TT_NAME+" <= \'"+timestamp.dateTimeMs()+"\')";

            String sqlQuery= "SELECT * FROM "+tableName+sqlWhereClause;
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while(resultSet.next()){
                rows.add(new JDBCRow (resultSet));
            }
            connection.close();
            return rows;
        }catch(Exception e){
            try{connection.close();}catch(Exception p){}

            plogger.log("tsTable getRows had a problem", e);
            return null;
        }
    }

        public List<Row> getRowsSince(String[] columns, String[] values, ISODate timestamp) throws PlatosysDBException {
        Connection connection=null;
        if(columns.length!=values.length){
            throw new PlatosysDBException("getRowsSince: mismatched test arrays");
        }
        List<Row> rows = new ArrayList<Row>();
        try{
            connection=ConnectionSource.getConnection(databaseName);
            Statement statement=connection.createStatement();
            String sqlWhereClause =  " WHERE (("+TT_NAME+" >= \'"+timestamp.dateTimeMs()+"\') AND";
            for (int i=0; i<columns.length; i++){
                sqlWhereClause=sqlWhereClause+"("+columns[i]+" = \'"+values[i]+"\') AND ";
            }
            sqlWhereClause=sqlWhereClause.substring(0, sqlWhereClause.length()-4);
            sqlWhereClause=sqlWhereClause+")";
            String sqlQuery= "SELECT * FROM "+tableName+sqlWhereClause;
            //logger.log(sqlQuery);
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while(resultSet.next()){
                rows.add(new JDBCRow (resultSet));
            }
            connection.close();
            return rows;
        }catch(Exception e){
            try{connection.close();}catch(Exception p){}

            plogger.log("tsTable getRows had a problem", e);
            return null;
        }
        }

        public int countRowsUntil(String[] columns, String[] values, ISODate timestamp){
            Connection connection=null;
         int rows=0;
            try{
            connection=ConnectionSource.getConnection(databaseName);
            Statement statement=connection.createStatement();
            String sqlWhereClause =  " WHERE ("+TT_NAME+" <= \'"+timestamp.dateTimeMs()+"\')";

            String sqlQuery= "SELECT * FROM "+tableName+sqlWhereClause;
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while(resultSet.next()){
                rows++;
            }
            connection.close();
            return rows;
        }catch(Exception e){
            try{connection.close();}catch(Exception p){}

            plogger.log("tsTable getRows had a problem", e);
            return 0;
        }
        }
        public int countRowsSince(String[] columns, String[] values, ISODate timestamp) throws PlatosysDBException{
            //logger.log(5, "counting rows since "+timestamp.toString());
            if(columns.length!=values.length){
            throw new PlatosysDBException("getRowsSince: mismatched test arrays");
        }
         Connection connection=null;
      int rows=0;
        try{
            connection=ConnectionSource.getConnection(databaseName);
            Statement statement=connection.createStatement();
         String sqlWhereClause =  " WHERE (("+TT_NAME+" >= \'"+timestamp.dateTimeMs()+"\') AND";
            for (int i=0; i<columns.length; i++){
                sqlWhereClause=sqlWhereClause+"("+columns[i]+" = \'"+values[i]+"\') AND ";
            }
            sqlWhereClause=sqlWhereClause.substring(0, sqlWhereClause.length()-4);
            sqlWhereClause=sqlWhereClause+")";
            String sqlQuery= "SELECT * FROM "+tableName+sqlWhereClause;
            //logger.log(sqlQuery);
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while(resultSet.next()){
                rows++;
            }
            connection.close();
            return rows;
        }catch(Exception e){
            try{connection.close();}catch(Exception p){}

            plogger.log("tsTable getRows had a problem", e);
            return 0;
        }
        }
}

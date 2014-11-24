package uk.co.platosys.db.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import uk.co.platosys.util.Logger;
import uk.co.platosys.db.ColumnNotFoundException;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.RowNotFoundException;
import uk.co.platosys.db.SerialTable;
import uk.co.platosys.db.Table;

public class JDBCSerialTable extends JDBCTable implements SerialTable {
	static Logger logger = Logger.getLogger("dbplat");
	  public JDBCSerialTable(String databaseName, String tableName,
			String primaryKeyColumnName) throws PlatosysDBException {
		  super(databaseName, tableName, primaryKeyColumnName);
		// TODO Auto-generated constructor stub
	}

	public static JDBCSerialTable createTable(String databaseName, String tableName, String primaryKeyColumnName) throws PlatosysDBException {
	        //this.databaseName=databaseName;  
	        Connection connection=null;
	            connection=ConnectionSource.getConnection(databaseName);
	            try{
	                Statement statement=connection.createStatement();
	                statement.execute("CREATE TABLE "+tableName+" ("+primaryKeyColumnName+ " SERIAL PRIMARY KEY)");
	                connection.close();
	                return new JDBCSerialTable(databaseName, tableName, primaryKeyColumnName);
	            }catch(Exception ex){
	                logger.log( "problem creating serial table "+tableName+"  in db "+databaseName, ex);
	                try{connection.close();}catch(Exception p){}
	               return null; 
	            }
	    }
	public static JDBCSerialTable openTable(String databaseName, String tableName,String primaryKeyColumnName) throws PlatosysDBException {
		return new JDBCSerialTable(databaseName, tableName, primaryKeyColumnName);		       
		
	}
	public long addSerialRow(String primaryKeyColumName, String columnName, String columnValue) throws PlatosysDBException {
		logger.log("JDBCSTasr: adding row with "+columnName+" = "+columnValue);
		if (addRow(columnName, columnValue)){
			JDBCRow row;
			try {
				row = getRow(columnName, columnValue);
			} catch (RowNotFoundException e1) {
				throw new PlatosysDBException("JDBCST: problem adding serial row", e1);
			}
			try {
				
				long id = row.getLong(primaryKeyColumnName);
				logger.log("JDBCSTasr returning id="+Long.toString(id));
				return id;
			} catch (ClassCastException e) {
				logger.log("exception thrown", e);
				throw new PlatosysDBException("JDBCST: problem adding serial row",e);
			} catch (ColumnNotFoundException c) {
				logger.log("exception thrown", c);
				throw new PlatosysDBException("JDBCST: problem adding serial row",c);
			}
		}else{
			throw new PlatosysDBException("JDBCST: problem adding serial row");
		}
	}
	
	 public JDBCRow getRow(long primaryKeyTest)throws RowNotFoundException {
	       Connection connection=null;
	        try{
	            connection=ConnectionSource.getConnection(databaseName);
	            Statement statement=connection.createStatement();
	            ResultSet resultSet=statement.executeQuery("SELECT * FROM "+tableName+ " WHERE "+primaryKeyColumnName+" = "+primaryKeyTest);
	            if(resultSet.next()){
	               //logger.log(5, "JDBCTable found row for "+primaryKeyTest);
	               JDBCRow row = new JDBCRow(resultSet);
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
	        }    
	    }

	@Override
	/**
	 * @return the primary key value of the row added. 
	 */
	public long addSerialRow() {
		  Connection connection=null;
	        try{
	            connection=ConnectionSource.getConnection(databaseName);
	            Statement statement=connection.createStatement();
	            ResultSet rs = statement.executeQuery("INSERT INTO "+tableName+" DEFAULT VALUES RETURNING "+primaryKeyColumnName );
	            rs.next();
	            long ind = rs.getLong(primaryKeyColumnName);
	            connection.close();
	            return ind;
	            
	        }catch(Exception e){
	            logger.log("JDBCSerialTable.addSerilRow(): error", e);
	            try{connection.close();}catch(Exception cex){}
	            return 0;
	        } 
	}
}

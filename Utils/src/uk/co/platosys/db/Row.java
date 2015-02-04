/*
 * JDBCRow.java
 *
 * Created on 25 September 2007, 12:03
 *
 *  Copyright (C) 2008  Edward Barrow

    This class is free software; you can rednterfistribute it and/or
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

package uk.co.platosys.db;


import java.util.Set;
import uk.co.platosys.util.ISODate;
import java.util.Date;



/**
 * Models a row in a database table.
 *
 * @author edward
 */
public interface Row { 

	/**
	 * 
	 * @param columnName
	 * @return the column content, as an Object.
	 * @throws ColumnNotFoundException if a column of this name does not exist
	 */
	public Object getColumnContent(String columnName) throws ColumnNotFoundException;
   /**
    *  returns a time stamp (a date object with ms or ns precision)
    * @param colName
    * @return
    * @throws ClassCastException
    * @throws ColumnNotFoundException
    */
	public  Date getTimeStamp(String colName) throws ClassCastException, ColumnNotFoundException;
    /**
     *  returns a date
     * @param colName
     * @return
     * @throws ClassCastException
     * @throws ColumnNotFoundException
     */
	public  Date getDate (String colName)throws ClassCastException, ColumnNotFoundException;
    /**
     *  returns a String
     * @param columnName
     * @return
     * @throws ClassCastException
     * @throws ColumnNotFoundException
     */
	public String getString(String columnName) throws ClassCastException, ColumnNotFoundException; 
    /**
     * returns a primitive long
     * @param columnName
     * @return
     * @throws ClassCastException
     * @throws ColumnNotFoundException
     */
	public long getLong(String columnName) throws ClassCastException, ColumnNotFoundException;
    /**
     * returns an ISODate where this typing matters
     * @param columnName
     * @return
     * @throws ClassCastException
     * @throws ColumnNotFoundException
     */
	public ISODate getISODate(String columnName) throws ClassCastException, ColumnNotFoundException; 
	 /**
     * returns a double
     * @param columnName
     * @return
     * @throws ClassCastException
     * @throws ColumnNotFoundException
     */

	public double getDouble(String columnName) throws ClassCastException, ColumnNotFoundException;
	 /**
     * returns a float
     * @param columnName
     * @return
     * @throws ClassCastException
     * @throws ColumnNotFoundException
     */
	public float getFloat(String columnName) throws ClassCastException, ColumnNotFoundException;
    /**
     * returns an int
     * @param columnName
     * @return
     * @throws ClassCastException
     * @throws ColumnNotFoundException
     */
	public int getInt(String columnName) throws ClassCastException, ColumnNotFoundException;
    /**
     * returns a boolean
     * @param columnName
     * @return
     * @throws ClassCastException
     * @throws ColumnNotFoundException
     */
	public boolean getBoolean(String columnName) throws ClassCastException, ColumnNotFoundException;
	/**
     * returns a big decimal, full precision number.
     * @param columnName
     * @return
     * @throws ClassCastException
     * @throws ColumnNotFoundException
     */
	public java.math.BigDecimal getBigDecimal (String columnName) throws ClassCastException, ColumnNotFoundException;
    /**
     * returns a Set of the rows column names.
     * @return
     */
	public Set<String> getColumnNames();
}

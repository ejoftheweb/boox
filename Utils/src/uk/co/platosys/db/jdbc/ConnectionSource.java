/*
 * ConnectionSource.java
 *
 * Created on 26 April 2007, 19:38
 *
   This is a static implementation of ConnectionPool; it allows applications to get connections without having to instantiate 
 * and manage references to a connection pool object.
 * Connections can be safely obtained just using the static method ConnectionSource.getConnection()...
 * However with this as with all other pooling implementations connections must be explicitly closed by objects that use them, so they are returned to the pool.
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

import java.sql.Connection;

import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.util.Logger;

/**
 * Connection pooling class built on top of the Jakarta commons implementation to
 * provide  static methods to get reusable poolable Connections 
 *
 */
public class ConnectionSource {
    
        private static Logger logger=DatabaseProperties.DATABASE_LOGGER;
	private static ConnectionBroker connectionBroker =new ConnectionBroker();
   
     /**
    * use ConnectionSource.getConnection(databaseName) to get a pooled connection to a specific database
    *
    *  Always call connection.close() as soon as you have finished with the connection.
     * @return a poolable Connection object
     */ 
    
    public static Connection getConnection(String databaseName)throws PlatosysDBException {
    	logger.log("ConnectionSource getting a connection to "+databaseName);
        try{
            if (connectionBroker==null){
                connectionBroker=new ConnectionBroker();
            }
            return connectionBroker.getConnection(databaseName);
        }catch(PlatosysDBException ude){
            throw ude;
        }catch(Exception e){
            logger.log("ConnectionSource had a problem getting a connection to database "+ databaseName, e);
            return null;
        }
    }
        
    
    
}

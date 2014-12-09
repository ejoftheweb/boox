/*
 * ConnectionBroker.java
 *
 * Created on 03 October 2007, 08:10
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.postgresql.util.PSQLException;

import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.util.Logger;

/**
 * This class supports the ConnectionSource static pooling provider
 * to allow pooling connections to multiple databases
 * 
 *
 * @author edward
 */
public class ConnectionBroker  {
     private static DatabaseProperties properties = new DatabaseProperties(); 
     private Map<String, BasicDataSource>  dataSources=Collections.synchronizedMap(new HashMap<String, BasicDataSource>());     //private Map<String, GenericObjectPool<PoolableConnection>> pools = new HashMap<String, GenericObjectPool<PoolableConnection>>();
     private Set<BasicDataSource> dataSet = Collections.synchronizedSet(new HashSet<BasicDataSource>());  
     private Logger logger = DatabaseProperties.DATABASE_LOGGER;
     private static int MAX_ACTIVE=100;//this should be configurable; but it's dependent on the underlying db's capacity
     private boolean done=false;   
     
    /**
     * Creates a new instance of ConnectionBroker
     */
    protected ConnectionBroker() {
    	logger.log("ConnectionBroker constructor called");
    	init();
    }
    private void init(){
    	
        List<DatabaseCredentials> databases = properties.getDatabases();
        for(DatabaseCredentials credentials:databases){
        
            String name="";
            String driver="";
            String userName="";
            String password="";
            String url="";
           try {
                name = credentials.getName();
                logger.log("ConnectionBroker initialising database "+name);
                driver = credentials.getDriver();
                url = credentials.getUrl();
                userName = credentials.getUsername();
                password = credentials.getPassword();
                logger.log("ConnectionBroker credentials for "+name+ " are "+driver+" "+url+" "+userName+" "+password);
                
            }catch(Exception e){
                logger.log("ConnectionBroker had a problem getting credentials for "+name, e);
            }
           /* try{
            	//although we only use postgresql, there's no reason we couldn't use a different sql rdbms with a different driver for each
            	//database.
                Class.forName(driver);
            }catch(ClassNotFoundException e){
                logger.log("ConnectionBroker couldn't load database driver", e);
            }*/
            try{
                /*connFac = new DriverManagerConnectionFactory(url, userName, password);
                PoolableConnectionFactory poolableConFac = new PoolableConnectionFactory(connFac, null);
                GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<PoolableConnection>(poolableConFac);
                pools.put(name, connectionPool);
                poolableConFac.setPool(connectionPool);
                PoolingDataSource<PoolableConnection> dataSource = new PoolingDataSource<PoolableConnection>(connectionPool);*/
            	 BasicDataSource dataSource = new BasicDataSource();
            	 dataSource.setDriverClassName(driver);
            	 dataSource.setUsername(userName);
            	 dataSource.setPassword(password);
            	 dataSource.setUrl(url);
            	 dataSource.setMinIdle(2);
            	 dataSource.setMaxTotal(MAX_ACTIVE);
            	logger.log(5, "ConnectionBroker has made BasicDataSource "+ dataSource.toString());
            	logger.log("Datasource "+name+" has "+dataSource.getMaxTotal()+" maxConnections");
                try {
                    Connection connection = dataSource.getConnection();
                    connection.close();
                    dataSources.put(name, dataSource);
                    dataSet.add(dataSource);
                    logger.log(5, "ConnectionBroker has initialised database "+name);
                }catch(PSQLException psqe){
                    logger.log("could not make a test connection to database: "+name, psqe);
                } 
           }catch(Exception e){
                logger.log("ConnectionBroker had a problem configuring the pool with "+name, e);
            }
        }
        done=true;
      }
    
    protected Connection getConnection(String databaseName) throws PlatosysDBException{
    	Connection connect;
        while(!done){}//wait for databases to be set up.
            try{
                if (dataSources.containsKey(databaseName)){
                   BasicDataSource dataSource =dataSources.get(databaseName);
                   
                   		/* GenericObjectPool<PoolableConnection> pool = pools.get(databaseName);*/
                   		 //logger.log("1 "+databaseName+" has "+dataSource.getNumIdle()+"  idle plus "+dataSource.getNumActive()+" borrowed");
                         Connection connection = dataSource.getConnection();
                         if (connection!=null){
                        	 //logger.log("2 "+databaseName+" has "+dataSource.getNumIdle()+"  idle plus "+dataSource.getNumActive()+" borrowed");
                              connect= connection;
                         }else{
                        	logger.log("dataSource returned a null connection");
                        	return null;
                         }
                }else{
                	logger.log("not found first time, reloading datasources");
                	done=false;
                    init();
                    while(!done){}//
                	if (dataSources.containsKey(databaseName)){
                        BasicDataSource dataSource =dataSources.get(databaseName);
                        /*GenericObjectPool<PoolableConnection> pool = pools.get(databaseName);*/
                  		 //logger.log("3 "+databaseName+" has "+dataSource.getNumIdle()+"  idle plus "+dataSource.getNumActive()+" borrowed");
                        Connection connection = dataSource.getConnection();
                        if (connection!=null){
                        	// logger.log("4 "+databaseName+" has "+dataSource.getNumIdle()+"  idle plus "+dataSource.getNumActive()+" borrowed");
                              connect= connection;
                        }else{
                       	logger.log("dataSource returned a null connection");
                       	return null;
                        }
                	}else{
                    	 logger.log(2, "CB - Database "+databaseName+ " is not known ");
                    	 throw new PlatosysDBException("CB says Database "+databaseName+ " is not known");
                     }
                }
                //refresh connection counters:
                int idleConnections=0;
                int activeConnections=0;
                int totalConnections=0;
                int maxTotal=0;
                for(BasicDataSource bds:dataSet){
                	idleConnections=idleConnections+bds.getNumIdle();
                	activeConnections=activeConnections+bds.getNumActive();
                	maxTotal=maxTotal+bds.getMaxTotal();
                }
                totalConnections=idleConnections+activeConnections;
                logger.log("CB has "+totalConnections+"; of which "+activeConnections+" active and "+idleConnections+" idle");
                return connect;
            }catch(Exception e){
                throw new PlatosysDBException("ConnectionBroker issues", e);
            }
    }
}

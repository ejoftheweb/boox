/*
 * DatabaseCredentials.java
 *
 * Created on 03 October 2007, 08:13
 *
 * Copyright (C) 2008  Edward Barrow

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

import uk.co.platosys.util.Logger;

/**
 * This class is a simple container for the the four variables normally
 * necessary for a database connection, viz:
 * - the database driver;
 * - the database URL;
 * - the username;
 * - the password.
 *  
 *  plus a programmatic name
 *
 * @author edward
 */
public class DatabaseCredentials {
    private String name;
    private String driver;
    private String url;
    private String username;
    private String password;
    private static Logger logger=new Logger("dbplat");
    
    /** Creates a new instance of DatabaseCredentials */
    public DatabaseCredentials(String name, String driver, String url, String username, String password) {
        this.name=name;
        this.driver=driver;
        this.url=url;
        this.username=username;
        this.password=password;
    }

    protected String getName() {
        return name;
    }

    protected String getDriver() {
        return driver;
    }

    protected String getUrl() {
        return url;
    }

    protected String getUsername() {
        return username;
    }

    protected String getPassword() {
        return password;
    }
    public static DatabaseCredentials getNewCredentials (DatabaseCredentials masterCredentials, String name){
        String masterName = masterCredentials.getName();
    	String masterURL = masterCredentials.getUrl();
        logger.log("DC full masterURL is "+masterURL);
        int b = masterName.length();
        int a = masterURL.length();
        String urlRoot= masterURL.substring(0, a-b);
        logger.log("DC URL root is "+urlRoot);
        String newUrl = urlRoot+name;
        logger.log("DC newURL is"+newUrl);
        return new DatabaseCredentials(
    			name, 
    			masterCredentials.getDriver(),
    			newUrl, 
    			masterCredentials.getUsername(), 
    			masterCredentials.getPassword()
    			);
    }
}

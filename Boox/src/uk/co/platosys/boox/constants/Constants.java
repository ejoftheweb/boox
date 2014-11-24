/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.constants;

import java.io.File;
import java.io.FileInputStream;

import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.util.*;
/**
 *
 * @author edward
 */
public class Constants extends PlatosysProperties{
    private static final Logger logger = PlatosysProperties.DEBUG_LOGGER;
    
    public static final String DATABASE_DRIVER="org.postgresql.Driver";
    public static final String DATABASE_URL="jdbc:postgresql:/localhost:5432/";
    public static final String DATABASE_NAME=readProperty("database-name");
    public static final String APPLICATION_NAME="parsley";
    public static final String INVOICE_STYLESHEET_URL="http://localhost:8084/parsleyWeb/styles/invoices.xsl";
    public static final String INVOICES_PATH="/var/platosys/parsley/invoices/";
    //Labels
    public static final String APPLICATION_URL=readProperty("url");
    public static final String FORBIDDEN="forbidden";
    public static final String DETAIL="Detail";
    public static final String CREDIT="Credit";
    public static final String DEBIT="Debit";
    public static final String LEDGER="Ledger";
    public static final String ACCOUNT="Account";
    public static final String BALANCE="Balance";
    public static final String TID="TransactionID";
    public static final String PARENT="Parent Ledger:";
    public static final String DATE="Date";
    public static final String NOTE="Note";
    public static final String CONTRA="Account";
    public static final String CURRENCY="Currency";
    private static String readProperty (String propertyName){
        try{
        File propertiesFile = new File("/etc/platosys/parsley.xml");//configure this
        java.util.Properties properties = new java.util.Properties();
        properties.loadFromXML(new FileInputStream(propertiesFile));
        return properties.getProperty(propertyName);
        }catch(Exception e){
            logger.log("problem loading property "+propertyName);
            return null;
        }
    }
}

/*
 * Currency.java
 * Copyright (C) 2008  Edward Barrow

  * 
    Copyright (C) 2008  Edward Barrow

    This class is free software; you can redistribute it and/or
    modify it under the terms of the GNU  General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this program; if not, write to the Free
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * 
 * for more information contact edward.barrow@platosys.co.uk
 
 * Clerk.java
 *
 * Created on 24 March 2007, 14:02
 *
 * 
 *

 * 
 *
 *
 */

package uk.co.platosys.boox.money;

import java.util.HashMap;
import java.util.Map;

/**
 * The boox currency object escapes the ISO shackles and allows you to make up
 * currencies of your own. However, in a nod to conformity, if an ISO currency 
 * exists with the given tla, it will have the features of that currency.
 *
 *
 * @author edward
 */
public class Currency {
	private static Map<String, Currency> currencies = new HashMap<String, Currency>();

	public static String DEFAULT_FORMAT="#,###.00";
	   
    private String symbol;
    private String format=DEFAULT_FORMAT;
    private String tla;//a three-letter acronym, e.g. USD or GBP
    private java.util.Currency javaCurrency=null;
    public static Currency DEFAULT_CURRENCY=new Currency("XBX");
	
    /** Creates a new instance of Currency */

   
    private Currency(String symbol){
        this.symbol=symbol;
        this.tla=symbol;
        if(!(tla.equals("XBX"))){
        try{
            this.javaCurrency=java.util.Currency.getInstance(tla);
            String format = "#,###.";
            int decs = javaCurrency.getDefaultFractionDigits();
            for (int i=0; i<decs; i++){
                format=format+"0";
            }
            this.format=format;
        }catch(IllegalArgumentException ie){
           this.format=DEFAULT_FORMAT;
        }
        this.format=DEFAULT_FORMAT;
        }
        currencies.put(tla, this);
    }
    
    private Currency(java.util.Currency javaCurrency){
        this.javaCurrency=javaCurrency;
        this.symbol=javaCurrency.getSymbol();
        this.tla=javaCurrency.getCurrencyCode();
   }
    public String getSymbol(){
        return symbol;
    } 
    public String getFormat(){
        return format;
    }
    public String getTLA(){
        return tla;
    }
    /**
     * Returns the currency instance for the given tla (three-letter acronym).
     * @param tla
     * @return
     */
    public static Currency getCurrency(String tla){
       if(currencies.containsKey(tla)){
            return currencies.get(tla);
        }else{
            return new Currency(tla);
        }
    }


    /**
     *
     * @return the java.util.Currency object associated with this Currency, or null.
     */
    public java.util.Currency getJavaCurrency(){
        return javaCurrency;
    }
    
    public boolean equals (Currency currency){
        if ((currency.getTLA()).equals(this.tla)){
            return true;
           
        }else{
            return false;
        }
    }
    
}

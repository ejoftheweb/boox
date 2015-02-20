/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.stock;

import java.util.List;

import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.util.PlatosysProperties;
import uk.co.platosys.util.Logger;

/**
 * Catalogue models a product catalogue. It provides methods for retrieving catalogue items.
 * It can also be used for stock (inventory) control.
 * @author edward
 */
public abstract class Catalogue {
    Logger logger=PlatosysProperties.DEBUG_LOGGER;
    String databaseName;
    public static final String CATALOGUE_TABLENAME="catalogue";
    public static final String ITEMSYSNAME_COLNAME="sysname";
    public static final String ITEMID_COLNAME="itemid";
    public static final String ITEMNAME_COLNAME="name";
    public static final String ITEMDESC_COLNAME="description";
    public static final String ITEMPRICE_COLNAME="price";
    public static final String ITEMCURRENCY_COLNAME="currency";
    public static final String ITEMSTOCKLEVEL_COLNAME="stocklevel";
    public static final String ITEMTAXBAND_COLNAME ="taxband";
    public static final String ITEMACCOUNT_COLNAME="account";
    public static final String ITEMLEDGER_COLNAME="ledger";
    public static final String ITEMADDEDSTOCK_COLNAME="added";
    public static final String ITEMOPENINGSTOCKLEVEL_COLNAME="opening";
    public static final String ITEMWASTEDSTOCK_COLNAME="wasted"; 
  
    public Catalogue(Enterprise enterprise){
       
    }
    public abstract List<Item> getCatalogueItems();{
       
    }
    public abstract Item getItemById(long catID);
    public abstract Item addCatalogueItem(Enterprise enterprise, Item catalogueItem);
    
}

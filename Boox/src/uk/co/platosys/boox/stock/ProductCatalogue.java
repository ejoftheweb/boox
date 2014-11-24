/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.stock;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;




import uk.co.platosys.boox.trade.Customer;
import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.Boox;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.money.Currency;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.RowNotFoundException;
import uk.co.platosys.db.jdbc.ConnectionSource;
import uk.co.platosys.db.jdbc.JDBCRow;
import uk.co.platosys.db.jdbc.JDBCSerialTable;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.PlatosysProperties;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.ShortHash;

/**
 * ProductCatalogue models a product catalogue. It provides methods for retrieving catalogue items.
 * It can also be used for stock (inventory) control.
 * @author edward
 */
public class ProductCatalogue extends Catalogue {
    static Logger logger=PlatosysProperties.DEBUG_LOGGER;
    String databaseName;
    public static final String PRODUCTCATALOGUE_TABLENAME = "product_catalogue";
    public static final String PRODUCTS_LEDGER_NAME="products";
    public static final String PRODUCT_SYSNAME_PREFIX="p";
    //public static final String 
    static JDBCSerialTable catalogueTable=null;
    public ProductCatalogue(Enterprise enterprise){
    	super(enterprise);
    	try{
    		this.catalogueTable=getCatalogueTable(enterprise);
    	}catch(Exception e){
    		logger.log("PCATinit: exception thrown", e);
    	}
    }
    public List<Item> getCatalogueItems(){
    	 Connection connection=null;
         try{
         List<Item> items =new ArrayList<Item>();
         connection = ConnectionSource.getConnection(databaseName);
         Statement statement = connection.createStatement();
         ResultSet rs = statement.executeQuery("SELECT * FROM "+CATALOGUE_TABLENAME);
         while(rs.next()){
             long id = rs.getLong(ITEMID_COLNAME);
             String name= rs.getString(ITEMNAME_COLNAME);
             String description=rs.getString(ITEMDESC_COLNAME);
             double dprice = rs.getDouble(ITEMPRICE_COLNAME);
             String currency = rs.getString(ITEMCURRENCY_COLNAME);
             Money price = new Money(currency,dprice);
             int stockLevel=0;
             try{
                 stockLevel=rs.getInt(ITEMSTOCKLEVEL_COLNAME);
             }catch(Exception e){}
              Product catalogueItem = new Product(id, name, description, price,stockLevel);
             items.add(catalogueItem);
         }
         connection.close();
         return items;
         }catch(Exception e){
             logger.log("problem getting the list of catalogue items", e);
             try{connection.close();}catch(Exception ex){}
             return null;
         }
   }
    public List<Item> getCatalogueItems(Customer customer){
    	//TODO: implement the proper functionality!
    	return getCatalogueItems();
    }
    public Item getItemById(long catID){
    	{
            try{
            JDBCTable catalogueTable = new JDBCTable(databaseName, PRODUCTCATALOGUE_TABLENAME, ITEMID_COLNAME, true);
            String name = catalogueTable.readString(catID, ITEMNAME_COLNAME);
            String description = catalogueTable.readString(catID, ITEMDESC_COLNAME);
            double dprice = catalogueTable.readNumber(catID, ITEMPRICE_COLNAME);
            String currency = catalogueTable.readString(catID, ITEMCURRENCY_COLNAME);
            Money price = new Money(currency, dprice);
            int stockLevel=(int) catalogueTable.readLong(catID, ITEMSTOCKLEVEL_COLNAME);
            return new Product(catID, name, description, price,stockLevel);
            }catch(Exception e){
                PlatosysProperties.DEBUG_LOGGER.log("problems getting cat item by id", e);
                return null;
            }   
        }
    }
    /**
     * This method is used to add an item to the catalogue
     * @param catalogueItem
     * @return
     */
    
  
    public Item addCatalogueItem(Enterprise enterprise, Clerk clerk, Item catalogueItem){
       
       try{
    	   if(catalogueTable==null){
    		   catalogueTable=getCatalogueTable(enterprise);
    	   }
    	 
    		  if(catalogueTable.rowExists(ITEMNAME_COLNAME, catalogueItem.getName())){
    			  logger.log("PC-aci: item called "+catalogueItem.getName()+" already exists");
    			  //then a product of this name already exists so we return it
    			  JDBCRow row;
    			try {
    				row = catalogueTable.getRow(ITEMNAME_COLNAME, catalogueItem.getName());
    			} catch (RowNotFoundException e) {
    				logger.log("PRoduct: RNF exception thrown", e);
    				//eh? the row exists but exception thrown??
    				throw new PlatosysDBException("Faulty Product Catalogue: product row not found", e);
    			}
    			  try {
    				return Product.getProduct(enterprise, clerk, row.getString(ITEMSYSNAME_COLNAME));
    			} catch (Exception e) {
    				
    				logger.log("exception thrown", e);
    				//col not found exception? Table is buggad. 
    				throw new PlatosysDBException("Customer Database Fault: customer ID col not found", e);
    			}
    		  }else{
    			  //
    			  //Create the product:
    			  
    			  //logger.log("CustomerCC: - creating new customer:" +customerName);
    			  long id = catalogueTable.addSerialRow(ITEMID_COLNAME, ITEMNAME_COLNAME,catalogueItem.getName());
    		     // catalogueItem.setId(id);
    			catalogueTable.amend(id, ITEMSYSNAME_COLNAME, catalogueItem.getSysname());
  	            catalogueTable.amend(id, ITEMDESC_COLNAME, catalogueItem.getDescription());
  	            catalogueTable.amend(id, ITEMPRICE_COLNAME, catalogueItem.getPrice().getAmount().doubleValue());
  	            catalogueTable.amend(id, ITEMCURRENCY_COLNAME, catalogueItem.getPrice().getCurrency().getTLA());
  	            catalogueTable.amend(id, ITEMSTOCKLEVEL_COLNAME, catalogueItem.getStockLevel());
    			  logger.log("ProductCatCP: new product allocated ID "+Long.toString(id));
    			 String sysname=PRODUCT_SYSNAME_PREFIX+ShortHash.hash(Long.toString(id)+catalogueItem.getName()+enterprise.getName());
    			 logger.log("CustomerCC: new customer sysname is"+sysname);
    			  //TODO implement product-level ledgers/accounts!
    			  
    			  //Product needs a Ledger: 
    			 /* 
    			  Ledger ledger = Ledger.createLedger(enterprise, sysname, productsLedger, enterprise.getDefaultCurrency(), clerk, true);
    			  catalogueTable.amend(id, Product.LEDGER_COLNAME, ledger.getName());
    			  //And an Account:
    			  Account account = Account.createAccount(enterprise, sysname, products, clerk, ledger, enterprise.getDefaultCurrency(), customerName);
    			  customersTable.amend(id, Product.catalogueTable ACCOUNT_COLNAME, account.getName());
    			  
    			  */
    		  
    		  
    	  
      
            return catalogueItem;
        }}catch(Exception e){
        	logger.log("failed to add product to catalogue", e);
             return null;
        }
    }
       private static JDBCSerialTable getCatalogueTable(Enterprise enterprise) throws PlatosysDBException{
    	 if(catalogueTable!=null){
    		 return catalogueTable;
    	 }else{
    		 String databaseName=enterprise.getDatabaseName();//
    	        if (!JDBCTable.tableExists(databaseName, PRODUCTCATALOGUE_TABLENAME)){
    	            try{
	    	            catalogueTable = JDBCSerialTable.createTable(databaseName ,PRODUCTCATALOGUE_TABLENAME, ITEMID_COLNAME);
	    	            catalogueTable.addColumn(ITEMSYSNAME_COLNAME, JDBCTable.TEXT_COLUMN);
	    	            catalogueTable.addColumn(ITEMNAME_COLNAME, JDBCTable.TEXT_COLUMN);
	    	            
	    	            catalogueTable.addColumn(ITEMDESC_COLNAME, JDBCTable.TEXT_COLUMN);
	    	            catalogueTable.addColumn(ITEMPRICE_COLNAME, JDBCTable.NUMERIC_COLUMN);
	    	            catalogueTable.addColumn(ITEMCURRENCY_COLNAME, JDBCTable.TEXT_COLUMN);
	    	            catalogueTable.addColumn(ITEMSTOCKLEVEL_COLNAME, JDBCTable.INTEGER_COLUMN);
	    	            catalogueTable.addColumn(ITEMLEDGER_COLNAME, JDBCTable.TEXT_COLUMN);
	    	            catalogueTable.addColumn(ITEMACCOUNT_COLNAME, JDBCTable.TEXT_COLUMN);
		    	          
	    	            //the next line adds the first row to the product catalogue, the generic misc goods/services product 
	    	            
	    	            long id = catalogueTable.addSerialRow(ITEMID_COLNAME, ITEMNAME_COLNAME,Product.DEFAULT_PRODUCT_NAME);
	      		      
	      			catalogueTable.amend(id, ITEMSYSNAME_COLNAME, Long.toString(id));
	    	            catalogueTable.amend(id, ITEMDESC_COLNAME, Product.DEFAULT_PRODUCT_DESCRIPTION);
	    	            catalogueTable.amend(id, ITEMPRICE_COLNAME,Product.DEFAULT_PRODUCT_PRICE.getAmount().doubleValue());
	    	            catalogueTable.amend(id, ITEMCURRENCY_COLNAME, Product.DEFAULT_PRODUCT_PRICE.getCurrency().getTLA());
	    	            catalogueTable.amend(id, ITEMSTOCKLEVEL_COLNAME, Product.DEFAULT_PRODUCT_STOCKLEVEL);
	      			logger.log(4, "product catalogue created OK");
	    	            return catalogueTable;
    	            }catch(Exception e){
    	            	
    	                logger.log("PCAT problem creating product catalogue", e);
    	                return null;
    	            }
    	        }else{
    	            logger.log(3, "PCAT product catalogue exists");
    	            catalogueTable=new JDBCSerialTable(databaseName, PRODUCTCATALOGUE_TABLENAME, ITEMID_COLNAME);
    	            return catalogueTable;
    	        }
    	        
    	 }
	
     }
public static Product getProduct(Enterprise enterprise, String sysname){
	  try{
	
	   if(catalogueTable==null){
   		   catalogueTable=getCatalogueTable(enterprise);
   	   }
	   Row row = catalogueTable.getRow(ITEMSYSNAME_COLNAME, sysname);
	   long id = row.getLong(ITEMID_COLNAME);
	   Money price = new Money(Currency.getCurrency(row.getString(ITEMCURRENCY_COLNAME)), row.getDouble(ITEMPRICE_COLNAME));
	   return new Product(id, row.getString(ITEMNAME_COLNAME), row.getString(ITEMDESC_COLNAME), price, row.getInt(ITEMSTOCKLEVEL_COLNAME));
	  }catch(Exception x){
		  logger.log("problem getting product", x);
		  return null;
	  }
	
}
@Override
public Item addCatalogueItem(Enterprise enterprise, Item catalogueItem) {
	// TODO Auto-generated method stub
	return null;
}



}

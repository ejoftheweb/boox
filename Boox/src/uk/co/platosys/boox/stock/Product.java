/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.boox.constants.Prefixes;
import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.money.Currency;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.boox.trade.Customer;
import uk.co.platosys.boox.trade.TaxedTransaction;
import uk.co.platosys.db.ColumnNotFoundException;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.RowNotFoundException;
import uk.co.platosys.db.jdbc.JDBCRow;
import uk.co.platosys.db.jdbc.JDBCSerialTable;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.ShortHash;

/**
 *
 * @author edward
 */
public class Product extends Item {
	private Catalogue catalogue;
    private long id=0;
    private String sysname;
    private String name;
    private String description;
    private Money price;
    private float stockLevel;
    private float openingStockLevel;
    private float wastedStockLevel;
    private float addedStockLevel;
    private int taxBand=TaxedTransaction.UNTAXED_BAND;
     private Account salesAccount;
     private Account stockAccount;
    private Ledger salesLedger;
    private Ledger stockLedger;
    public static final String PRODUCTCATALOGUE_TABLENAME = "bx_product_catalogue";
    public static final String PRODUCTS_STOCK_LEDGER_NAME="Root:XBX:Current:Assets:Stock:Products";
    public static final String PRODUCTS_SALES_LEDGER_NAME="Root:XBX:Operations:Income:Sales:Products";
    public static final String PRODUCT_SYSNAME_PREFIX=Prefixes.PRODUCT;
    public static String DEFAULT_PRODUCT_NAME="Misc";
    public static final String DEFAULT_PRODUCT_DESCRIPTION="Miscellaneous Goods and Services";
    public static final Money DEFAULT_PRODUCT_PRICE=Money.zero();
    public static final float DEFAULT_PRODUCT_STOCKLEVEL=0;
    private static Logger logger = Logger.getLogger("boox");
    
    public static JDBCSerialTable catalogueTable;
    private Product(){}
    
    protected Product (long id, String name, String description, Money price, int stockLevel){
        this.id=id;
        this.name=name;
        this.description=description;
        this.price=price;
        this.stockLevel=stockLevel;
        
        
    }
    
    
    /**
     * The catalogue object will allocate the ID
     * @param name
     * @param description
     * @param price
     * @param stockLevel
     * @param 
     */
    public Product (Enterprise enterprise, Catalogue catalogue, String name, String description, Money price, int stockLevel){
        this.name=name;
        this.description=description;
        this.price=price;
        this.stockLevel=stockLevel;
        this.catalogue=catalogue;
         catalogue.addCatalogueItem(enterprise, this);
    }
    
    private Product(Enterprise enterprise, Clerk clerk, Row row) throws ClassCastException, ColumnNotFoundException, PermissionsException{
    	//logger.log("creating product");
    	  this.setId(row.getLong(Catalogue.ITEMID_COLNAME));
    	  try{
    		  Currency currency=Currency.getCurrency(row.getString(Catalogue.ITEMCURRENCY_COLNAME));
    		  //logger.log("Pinit - currency "+currency.getTLA());
    		  BigDecimal amt = row.getBigDecimal(Catalogue.ITEMPRICE_COLNAME);
    		 // logger.log("Pinit - amt "+amt);
    		  Money price = new Money(currency, amt);
    		 // logger.log("Pinit - price "+price.toPrefixedString());
    		  this.setPrice(price);
    	  }catch(Exception x){
    		  logger.log("Pinit: issue ", x);
    	  }
    	  try{
	      this.name=row.getString(Catalogue.ITEMNAME_COLNAME);
	      	//logger.log("Pinit - name "+name);
	      this.setSysname(row.getString(Catalogue.ITEMSYSNAME_COLNAME));
	      this.setTaxBand(row.getInt(Catalogue.ITEMTAXBAND_COLNAME));
	      this.description=row.getString(Catalogue.ITEMDESC_COLNAME);
	      this.setSalesAccount(Account.getAccount(enterprise, row.getString(Catalogue.ITEMACCOUNT_COLNAME), clerk));
		  this.setSalesLedger(salesAccount.getLedger());
		  this.setStockLevel(row.getFloat(Catalogue.ITEMSTOCKLEVEL_COLNAME));
		  this.setAddedStockLevel(row.getFloat(Catalogue.ITEMADDEDSTOCK_COLNAME));
		  this.setOpeningStockLevel(row.getFloat(Catalogue.ITEMOPENINGSTOCKLEVEL_COLNAME));
		  this.setWastedStockLevel(row.getFloat(Catalogue.ITEMWASTEDSTOCK_COLNAME)); 
		 // logger.log("Pinit: product "+name+" price "+ price.toPlainString());
    	  }catch(Exception x){
    		  logger.log("problem initialising product", x);
    	  }
    }
	public Product(Enterprise enterprise, Clerk clerk, long long1) {
		
	}
	private void setPrice(Money money) {
		this.price=money;
		
	}
	public static List<Item> getProducts(Enterprise enterprise, Clerk clerk, Customer customer) {
		//TODO: note that this just returns the entire product list. We have not yet implemented the storage of customer-specific products
		return getProducts(enterprise, clerk);
	}
	public static List<Item> getProducts(Enterprise enterprise, Clerk clerk, int selection){
		return getProducts(enterprise, clerk);
	}
	private static List<Item> getProducts(Enterprise enterprise, Clerk clerk){
		logger.log("ProductGP getting product list");
		  List<Item> products = new ArrayList<Item>();
		  try{
			   if(catalogueTable==null){catalogueTable=getCatalogueTable(enterprise, clerk);}
			   List<Row> rows = catalogueTable.getRows();
			   logger.log("ProductGP: there are "+rows.size()+" to list");
			   int p=0;
			   for(Row row: rows){
				   Product product = new Product (enterprise, clerk, row);
			       products.add(product);
			       p++;
			       logger.log("ProductGP: added "+p+" "+product.getName());
			   }
		  }catch(Exception x){
			  logger.log("ProductGPs problem getting products ", x);
			  return products;
		  }
		logger.log("ProductGPs returning "+products.size()+" products");
		return products;
	}
	/**
	 * 
	 * @param enterprise
	 * @param sysname
	 * @return a Product object with the given sysname.
	 */
	public static Product getProduct(Enterprise enterprise, Clerk clerk, String sysname){
		  try{
			   if(catalogueTable==null){catalogueTable=getCatalogueTable(enterprise, clerk);}
			   return new Product(enterprise, clerk, catalogueTable.getRow(Catalogue.ITEMSYSNAME_COLNAME, sysname));
			  
		  }catch(Exception x){
			  logger.log("ProductGP problem getting product with sysname "+sysname, x);
			  return null;
		  }
	}
	/**
	 * Creates the necessary database resources for a Product
	 * 
	 * @param enterprise
	 * @param productName
	 * @param description
	 * @param price
	 * @param clerk
	 * @param taxBand
	 * @param stockLevel
	 * @return
	 */
	public static Product createProduct(Enterprise enterprise,
										String productName,
										String description,
										Money price,
										Clerk clerk,
										int taxBand,
										float stockLevel){
		  try{
	    	   if(catalogueTable==null){
	    		   catalogueTable=getCatalogueTable(enterprise, clerk);
	    	   }
	    	   if(catalogueTable.rowExists(Catalogue.ITEMNAME_COLNAME, productName)){
	    			logger.log("ProductCP: product called "+productName+" already exists");
	    			  //then a product of this name already exists so we return it
	    			  JDBCRow row;
	    			try {
	    				row = catalogueTable.getRow(Catalogue.ITEMNAME_COLNAME, productName);
	    			} catch (RowNotFoundException e) {
	    				logger.log("Customer.CC: RNF exception thrown", e);
	    				//eh? the row exists but exception thrown??
	    				throw new PlatosysDBException("Faulty Catalogue Database: product row not found", e);
	    			}
	    			  try {
	    				return getProduct(enterprise,  clerk, row.getString(Catalogue.ITEMSYSNAME_COLNAME));
	    			} catch (Exception e) {
	    				
	    				logger.log("exception thrown", e);
	    				//col not found exception? Table is buggad. 
	    				throw new PlatosysDBException("Catalogue Fault: sysname col not found", e);
	    			}
	    		  }else{
	    			 // logger.log("ProductCP: creating the product "+productName );
	    			  long id = catalogueTable.addSerialRow(Catalogue.ITEMID_COLNAME, Catalogue.ITEMNAME_COLNAME,productName);
	    		      Product product=new Product();
	    		     // logger.log("ProductCP: "+productName+" allocated ID "+Long.toString(id));
		    			 
	    			  product.setId(id);
	    			  product.setName(productName);
	    			  product.setDescription(description);
	    			  // make a sysname for it:
	    			  String sysname = PRODUCT_SYSNAME_PREFIX+ShortHash.hash(enterprise.getName()+productName+Long.toString(id));
	    			  product.setSysname(sysname);
	    			 // logger.log("ProductCP: "+productName+" sysname is "+product.getSysname());
	    			  catalogueTable.amend(id, Catalogue.ITEMSYSNAME_COLNAME, sysname );
	    			  product.setDescription(description);
	    			  catalogueTable.amend(id, Catalogue.ITEMDESC_COLNAME, description);
	    			  product.setSellingPrice(price);
	    			  catalogueTable.amend(id, Catalogue.ITEMPRICE_COLNAME, price.getAmount().doubleValue());
	  	              catalogueTable.amend(id, Catalogue.ITEMCURRENCY_COLNAME, price.getCurrency().getTLA());
	  	              product.setStockLevel(stockLevel);
	  	              catalogueTable.amend(id, Catalogue.ITEMSTOCKLEVEL_COLNAME, stockLevel);
	  	              product.setTaxBand(taxBand);
	  	              catalogueTable.amend(id, Catalogue.ITEMTAXBAND_COLNAME, taxBand);
	    			 //Product needs a Ledger: 
	    			  //TODO: implement category-level ledgers
	    			  Ledger salesledger = Ledger.createLedger(enterprise, sysname, Ledger.getLedger(enterprise,  PRODUCTS_SALES_LEDGER_NAME), enterprise.getDefaultCurrency(), clerk, true);
	    			  Ledger stockledger = Ledger.createLedger(enterprise, sysname, Ledger.getLedger(enterprise,  PRODUCTS_STOCK_LEDGER_NAME), enterprise.getDefaultCurrency(), clerk, true);
	    			  
	    			  catalogueTable.amend(id, Catalogue.ITEMLEDGER_COLNAME, salesledger.getFullName());
	    			  product.setSalesLedger(salesledger);
	    			  product.setStockLedger(stockledger);
	    			  //There is one account per product.
	    			  Account salesaccount = Account.createAccount(enterprise, sysname,  clerk, salesledger, enterprise.getDefaultCurrency(), product.getName(), true);
	    			  catalogueTable.amend(id, Catalogue.ITEMACCOUNT_COLNAME, salesaccount.getSysname());
	    			  product.setSalesAccount(salesaccount);
	    			  //logger.log("ProductCP: "+productName+" has account "+salesaccount.getFullName());
	    			  return product;
	    		  }  
	    	   
	        }catch(Exception e){
	        	logger.log("failed to add product to catalogue", e);
	             return null;
	        }
		
	
	}
	 private void setSalesAccount(Account account) {
		this.salesAccount=account;
		
	}
	private void setSalesLedger(Ledger ledger) {
		this.salesLedger=ledger;
		logger.log("Product "+name+" has sales ledger "+ledger.getFullName());
		
	}
	 private void setStockAccount(Account account) {
			this.stockAccount=account;
			
		}
		private void setStockLedger(Ledger ledger) {
			this.stockLedger=ledger;
			
		}
	private void setTaxBand(int taxBand) {
		this.taxBand=taxBand;
		
	}
	private void setSysname(String sysname) {
		this.sysname=sysname;
		
	}
	private static JDBCSerialTable getCatalogueTable(Enterprise enterprise, Clerk clerk) throws PlatosysDBException{
    	 if(catalogueTable!=null){
    		 return catalogueTable;
    	 }else{
    		 String databaseName=enterprise.getDatabaseName();//
    	        if (!JDBCTable.tableExists(databaseName, PRODUCTCATALOGUE_TABLENAME)){
    	            try{
	    	            catalogueTable = JDBCSerialTable.createTable(databaseName ,PRODUCTCATALOGUE_TABLENAME, Catalogue.ITEMID_COLNAME);
	    	            catalogueTable.addColumn(Catalogue.ITEMSYSNAME_COLNAME, JDBCTable.TEXT_COLUMN);
	    	            catalogueTable.addColumn(Catalogue.ITEMNAME_COLNAME, JDBCTable.TEXT_COLUMN);
	    	            catalogueTable.addColumn(Catalogue.ITEMDESC_COLNAME, JDBCTable.TEXT_COLUMN);
	    	            catalogueTable.addColumn(Catalogue.ITEMPRICE_COLNAME, JDBCTable.DECIMAL_COLUMN);
	    	            catalogueTable.addColumn(Catalogue.ITEMCURRENCY_COLNAME, JDBCTable.TEXT_COLUMN);
	    	            catalogueTable.addColumn(Catalogue.ITEMSTOCKLEVEL_COLNAME, JDBCTable.REAL_COLUMN);
	    	            catalogueTable.addColumn(Catalogue.ITEMOPENINGSTOCKLEVEL_COLNAME, JDBCTable.REAL_COLUMN);
	    	            catalogueTable.addColumn(Catalogue.ITEMADDEDSTOCK_COLNAME, JDBCTable.REAL_COLUMN);
	    	            catalogueTable.addColumn(Catalogue.ITEMWASTEDSTOCK_COLNAME, JDBCTable.REAL_COLUMN);
		    	                 
	    	            catalogueTable.addColumn(Catalogue.ITEMLEDGER_COLNAME, JDBCTable.TEXT_COLUMN);
	    	            catalogueTable.addColumn(Catalogue.ITEMACCOUNT_COLNAME, JDBCTable.TEXT_COLUMN);
	    	            catalogueTable.addColumn(Catalogue.ITEMTAXBAND_COLNAME, JDBCTable.INTEGER_COLUMN);
		    	            
	    	            //the next line adds the first row to the product catalogue, the generic misc goods/services product 
	    	            Product defaultProduct = createProduct(enterprise, 
	    	            		DEFAULT_PRODUCT_NAME, 
	    	            		DEFAULT_PRODUCT_DESCRIPTION,  
	    	            		DEFAULT_PRODUCT_PRICE,  
	    	            		clerk, 
	    	            		TaxedTransaction.STANDARD_BAND, 
	    	            		DEFAULT_PRODUCT_STOCKLEVEL
	    	            		);
	    	            /*
	    	            long id = catalogueTable.addSerialRow(Catalogue.ITEMID_COLNAME, Catalogue.ITEMNAME_COLNAME,Product.DEFAULT_PRODUCT_NAME);
	    	            //
	    	            
	    	            
	      		        catalogueTable.amend(id, Catalogue.ITEMSYSNAME_COLNAME, Long.toString(id));
	    	            catalogueTable.amend(id, Catalogue.ITEMDESC_COLNAME, Product.DEFAULT_PRODUCT_DESCRIPTION);
	    	            catalogueTable.amend(id, Catalogue.ITEMPRICE_COLNAME,Product.DEFAULT_PRODUCT_PRICE.getAmount().doubleValue());
	    	            catalogueTable.amend(id, Catalogue.ITEMCURRENCY_COLNAME, Product.DEFAULT_PRODUCT_PRICE.getCurrency().getTLA());
	    	            catalogueTable.amend(id, Catalogue.ITEMSTOCKLEVEL_COLNAME, Product.DEFAULT_PRODUCT_STOCKLEVEL);
	    	            catalogueTable.amend(id, Catalogue.ITEMTAXBAND_COLNAME, TaxedTransaction.STANDARD_BAND);
	    	            //TODO put in ledger and account details for default product.*/
	    	          
	      			//logger.log(4, "product catalogue created OK");
	    	            return catalogueTable;
    	            }catch(Exception e){
    	            	
    	                logger.log("PCAT problem creating product catalogue", e);
    	                return null;
    	            }
    	        }else{
    	            //logger.log(3, "PCAT product catalogue exists");
    	            catalogueTable=new JDBCSerialTable(databaseName, PRODUCTCATALOGUE_TABLENAME, Catalogue.ITEMID_COLNAME);
    	            return catalogueTable;
    	        }
    	 }
	 }
 public Money getBalance(Enterprise enterprise, Clerk clerk) throws PermissionsException{
	 return salesAccount.getBalance(enterprise, clerk);
 }
 /**
  *  These methods should be called by other Boox methods in conjunction
  *  with a transaction to account for the stock value. On their own they
  *  only adjust the stock levels.
  *  
  * @param stock
  * @return
 * @throws PlatosysDBException 
 * @throws BooxException 
  */
 
 public boolean adjustStock(Enterprise enterprise, Clerk clerk, float stockAdjustment) throws PlatosysDBException, BooxException{
	 if(catalogueTable==null){
		 catalogueTable=getCatalogueTable(enterprise, clerk);
	 }
	 if (id==0){throw new BooxException("Product-adjustStock: product has invalid ID");}
	 try{
		 //this is a very inefficient way of doing it. We may need to write a db method to do it in SQL on the server, much better.
	  float oldStock=catalogueTable.readNumber(id, Catalogue.ITEMSTOCKLEVEL_COLNAME);
	  catalogueTable.amend(id, Catalogue.ITEMSTOCKLEVEL_COLNAME, oldStock+stockAdjustment );
	  this.stockLevel=stockLevel+stockAdjustment;
	  return true;
	 }catch(Exception e){
		 logger.log("stockAdjustment Problem", e);
		 
		 return false;
	 }
	  
 }
 /**
  *  These methods should be called by other Boox methods in conjunction
  *  with a transaction to account for the stock value. On their own they
  *  only adjust the stock levels.
  *  
  * @param stock
  * @return
 * @throws PlatosysDBException 
 * @throws BooxException 
  */
 public boolean wasteStock(Enterprise enterprise, Clerk clerk, float stockAdjustment) throws PlatosysDBException, BooxException{
	 if(catalogueTable==null){
		 catalogueTable=getCatalogueTable(enterprise, clerk);
	 }
	 if (id==0){throw new BooxException("Product-wasteStock: product has invalid ID");}
	 try{
		 //this is a very inefficient way of doing it. We may need to write a db method to do it in SQL on the server, much better.
		 //also these should all be batched so they stand and fall together.
	  float oldStock=catalogueTable.readNumber(id, Catalogue.ITEMWASTEDSTOCK_COLNAME);
	  catalogueTable.amend(id, Catalogue.ITEMWASTEDSTOCK_COLNAME, oldStock+stockAdjustment );
	  this.wastedStockLevel=wastedStockLevel+stockAdjustment;
	  return adjustStock(enterprise, clerk, (0-stockAdjustment));
	 }catch(Exception e){
		 logger.log("stockAdjustment Problem", e);
		 
		 return false;
	 }
 }
 /**
  *  These methods should be called by other Boox methods in conjunction
  *  with a transaction to account for the stock value. On their own they
  *  only adjust the stock levels.
  *  
  * @param stock
  * @return
  */
 public boolean addStock(Enterprise enterprise,Clerk clerk, float stockAdjustment) throws PlatosysDBException, BooxException{
	 if(catalogueTable==null){
		 catalogueTable=getCatalogueTable(enterprise, clerk);
	 }
	 if (id==0){throw new BooxException("Product-wasteStock: product has invalid ID");}
	 try{
		 //this is a very inefficient way of doing it. We may need to write a db method to do it in SQL on the server, much better.
		 //also these should all be batched so they stand and fall together.
	  float oldStock=catalogueTable.readNumber(id, Catalogue.ITEMADDEDSTOCK_COLNAME);
	  catalogueTable.amend(id, Catalogue.ITEMADDEDSTOCK_COLNAME, oldStock+stockAdjustment );
	  this.addedStockLevel=addedStockLevel+stockAdjustment;
	  return adjustStock(enterprise, clerk,  (stockAdjustment));
	 }catch(Exception e){
		 logger.log("stockAdjustment Problem", e);
		 
		 return false;
	 }
 }
private void setOpeningStockLevel(float openingStockLevel) {
	//logger.log(name+ "OS="+openingStockLevel);
	this.openingStockLevel = openingStockLevel;
}
private void setWastedStockLevel(float wastedStockLevel) {
	//logger.log(name+ "WS="+wastedStockLevel);
	this.wastedStockLevel = wastedStockLevel;
}
private void setAddedStockLevel(float addedStockLevel) {
	//logger.log(name+ "AS="+addedStockLevel);
	this.addedStockLevel = addedStockLevel;
}
public double getOpeningStockLevel() {
	return openingStockLevel;
}
public double getWastedStockLevel() {
	return wastedStockLevel;
}
public double getAddedStockLevel() {
	return addedStockLevel;
}

public long getId() {
    return id;
}

private void setId(long id) {
    this.id = id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getDescription() {
    return description;
}

public void setDescription(String description) {
    this.description = description;
}

public Money getPrice() {
    return price;
}
public Money getSellingPrice(Customer customer){
	return price;
	
}

public void setSellingPrice(Money price) {
    this.price = price;
}
public void setSellingPrice(Customer customer, Money price){
	
}
public float getStockLevel() {
    return stockLevel;
}

public void setStockLevel(float stockLevel2) {
	//logger.log(name+ "SL="+stockLevel2);
    this.stockLevel = stockLevel2;
}
public int getTaxBand() {
	return taxBand;
}

public int getTaxBand(Customer customer) {
	
	return taxBand;
}
public String getSysname(){
	return sysname;
}

@Override
public ItemCategory getCategory() {
	
	return null;
}

@Override
public Account getAccount() {
	
	return salesAccount;
}

}

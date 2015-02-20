/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.stock;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;




import uk.co.platosys.boox.core.Boox;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.boox.trade.Supplier;
import uk.co.platosys.db.jdbc.ConnectionSource;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.PlatosysProperties;
import uk.co.platosys.util.Logger;

/**
 * Catalogue models a product catalogue. It provides methods for retrieving catalogue items.
 * It can also be used for stock (inventory) control.
 * @author edward
 */
public class ComponentCatalogue extends Catalogue {
    Logger logger=PlatosysProperties.DEBUG_LOGGER;
    String databaseName;
    public ComponentCatalogue(Enterprise enterprise){
    	super(enterprise);
        this.databaseName=enterprise.getDatabaseName();//
        if (!JDBCTable.tableExists(databaseName, "catalogue")){
            try{
            JDBCTable catalogueTable = JDBCTable.createTable(databaseName,"catalogue", "id", JDBCTable.NUMERIC_COLUMN);
            catalogueTable.addColumn("name", JDBCTable.TEXT_COLUMN);
            catalogueTable.addColumn("description", JDBCTable.TEXT_COLUMN);
            catalogueTable.addColumn("price", JDBCTable.NUMERIC_COLUMN);
            catalogueTable.addColumn("currency", JDBCTable.TEXT_COLUMN);
            catalogueTable.addColumn("stockLevel", JDBCTable.INTEGER_COLUMN);
            catalogueTable.addColumn("profitCentre", JDBCTable.TEXT_COLUMN);
            
            catalogueTable.addRow("id", 0);
            catalogueTable.amend(0, "name", "Misc");
            catalogueTable.amend(0, "description", "Misc Goods/Services");
            catalogueTable.amend(0, "price", 0d );
            catalogueTable.amend(0, "currency", Boox.DEFAULT_CURRENCY);
            catalogueTable.amend(0, "stockLevel", 0l);
            catalogueTable.amend(0, "profitCentre", "Sales");
            logger.log(1, "catalogue created OK");
            }catch(Exception e){
              logger.log("problem creating catalogue", e);
            }
        }else{
            logger.log(1, "catalogue exists");
        }
        
    }
    
    
    public static Item getItemById(Enterprise enterprise, long catID){
        try{
        JDBCTable catalogueTable = new JDBCTable(enterprise.getDatabaseName(),"catalogue", "id", true);
        String name = catalogueTable.readString(catID, "name");
        String description = catalogueTable.readString(catID, "description");
        double dprice = catalogueTable.readNumber(catID, "price");
        String currency = catalogueTable.readString(catID, "currency");
        Money price = new Money(currency, dprice);
        int stockLevel=(int) catalogueTable.readLong(catID, "stockLevel");
     //   ProfitCentre profitCentre = new ProfitCentre(catalogueTable.readString(catID, "profitCentre"));
        return new Component(catID, name, description, price,stockLevel);
        }catch(Exception e){
            PlatosysProperties.DEBUG_LOGGER.log("problems getting cat item by id", e);
            return null;
        }   
    }
    public Component addCatalogueItem(Enterprise enterprise, Component catalogueItem){
        Connection connection=null;
        long id=0;
        try{
            connection = ConnectionSource.getConnection(databaseName);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT id FROM catalogue SORT BY id DESCENDING");
            if(rs.next()){
                id = rs.getLong("id");
                id++;
                String name= catalogueItem.getName();
                String description = catalogueItem.getDescription();
                double price = catalogueItem.getCostPrice().getAmount().doubleValue();
                String currency=catalogueItem.getCostPrice().getCurrency().getTLA();
                double stocklevel = catalogueItem.getStockLevel();
                 statement.execute("INSERT INTO catalogue (id, name, description, price, currency, stocklevel) VALUES ("+Long.toString(id)+",\'"+name+"\',\'"+description+"\',"+Double.toString(price)+",\'"+currency+"\',"+Double.toString(stocklevel)+")");
            }
            catalogueItem.setId(id);
            return catalogueItem;
        }catch(Exception e){
            return null;
        }
    }




	@Override
	public List<Item> getCatalogueItems() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Item getItemById(long catID) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Item addCatalogueItem(Enterprise enterprise, Item catalogueItem) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<Item> getCatalogueItems(Enterprise enterprise, Supplier supplier) {
		// TODO Auto-generated method stub
		return null;
	}
}

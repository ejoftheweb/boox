/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.stock;


import java.util.List;

import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.boox.trade.Customer;
import uk.co.platosys.boox.trade.Supplier;

/**
 *
 * @author edward
 */
public class Component extends Item {
    private long id;
    private String name;
    private String description;
    private Money price;
    private int stockLevel;
   
    
    protected Component (long id, String name, String description, Money price, int stockLevel){
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
     * @param profitCentre
     */
    public Component (Enterprise enterprise, ComponentCatalogue catalogue, String name, String description, Money price, int stockLevel){
        this.name=name;
        this.description=description;
        this.price=price;
        this.stockLevel=stockLevel;
               catalogue.addCatalogueItem(enterprise, this);
   }
    

    public long getId() {
        return id;
    }

    protected void setId(long id) {
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

    public Money getCostPrice() {
        return price;
    }
    public Money getCostPrice(Supplier supplier){
		return price;
    	
    }

    public void setCostPrice(Money price) {
        this.price = price;
    }
    public void setCostPrice(Supplier supplier, Money price){
    	
    }
    public double getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

   

	@Override
	public Money getPrice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemCategory getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account getAccount() {
		// TODO Auto-generated method stub
		return null;
	}
	public static List<Item> getComponents(Enterprise enterprise, Clerk clerk,
			Supplier supplier) {
		ComponentCatalogue ComponentCatalogue=new ComponentCatalogue(enterprise);
		List<Item> Components = ComponentCatalogue.getCatalogueItems(enterprise, supplier);
		return Components;
	}
	public String getSysname(){
		return Long.toString(id);
	}

	@Override
	public int getTaxBand() {
		// TODO Auto-generated method stub
		return 0;
	}
    
}

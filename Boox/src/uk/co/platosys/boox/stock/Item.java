/*
 * Item.java
 *
 * Created on 27 June 2007, 23:07
 *
 * This code is copyright PLATOSYS,
 * to the extent that copyright subsists.
 *
 *
 */

package uk.co.platosys.boox.stock;


import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.money.Money;

/**
 *
 * @author edward
 */
public abstract class Item {
    
    /** Creates a new instance of Item */
    protected Item() {
    	
    }
    public abstract String getName();
    public abstract Money getPrice();
    public abstract float getStockLevel();
    public abstract String getDescription();
	//private abstract void setId(long id);
	public abstract long getId();
	public abstract ItemCategory getCategory();
	public abstract Account getAccount();
	public abstract String getSysname();
	public abstract int getTaxBand();
}

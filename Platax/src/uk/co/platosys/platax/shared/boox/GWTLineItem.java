package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

import uk.co.platosys.pws.values.GWTMoney;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * GWTLineItem is an object that encapsulates all the data relevant to one invoice line.
 * @author edward
 *
 */
public class GWTLineItem implements Serializable,  IsSerializable{
	private String sIN=null;
    private int lineNumber=0;
     private String itemName=null;
    private String itemSysname=null;
    private float itemQty=0;
    private GWTEnterprise enterprise=null;
    private GWTCustomer customer=null;
    private GWTSupplier supplier=null;
    private GWTMoney price=new GWTMoney();
    private GWTMoney net=new GWTMoney();
    private GWTMoney tax=new GWTMoney();
    private GWTMoney gross=new GWTMoney();
    private double taxrate;
    
    public GWTLineItem (){}
    
    private void updatefields() throws Exception{
    	net=price.times(itemQty);
    	tax=net.times(taxrate);
    	gross=net.add(tax);
   }
    
    
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemSysname() {
		return itemSysname;
	}
	public void setItemSysname(String itemSysname) {
		this.itemSysname = itemSysname;
	}
	public float getItemQty() {
		return itemQty;
	}
	public void setItemQty(float itemQty) throws Exception {
		this.itemQty = itemQty;
		updatefields();
	}
	public GWTEnterprise getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(GWTEnterprise enterprise) {
		this.enterprise = enterprise;
	}
	public GWTCustomer getCustomer() {
		return customer;
	}
	public void setCustomer(GWTCustomer customer) {
		this.customer = customer;
	}
	public GWTSupplier getSupplier() {
		return supplier;
	}
	public void setSupplier(GWTSupplier supplier) {
		this.supplier = supplier;
	}
	public GWTMoney getPrice() {
		return price;
	}
	public void setPrice(GWTMoney price) throws Exception {
		this.price = price;
		updatefields();
	}
	public GWTMoney getNet() {
		return net;
	}
	
	public GWTMoney getTax() {
		return tax;
	}
	
	public GWTMoney getGross() {
		return gross;
	}
	
	public String getInvoiceSysname() {
		return sIN;
	}

	public void setInvoiceSysname(String sIN) {
		this.sIN = sIN;
	}

	public double getTaxrate() {
		return taxrate;
	}

	public void setTaxrate(double taxrate) throws Exception {
		this.taxrate = taxrate;
		updatefields();
	}

	

	
}

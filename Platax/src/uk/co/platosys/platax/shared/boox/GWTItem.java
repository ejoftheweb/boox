package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

import uk.co.platosys.pws.values.GWTMoney;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GWTItem implements Serializable, IsSerializable{
private String name;
private double stockLevel;
private double openingStockLevel;
private double wastedStockLevel;
private double addedStockLevel;
private GWTMoney balance;
private String itemID;
private String sysname;
private double taxrate;
private GWTMoney price;
public GWTItem(){}

public GWTItem(String name, String sysname, GWTMoney price){
	this.sysname=sysname;
	this.name=name;
	//this.itemID=itemID;
	this.price=price;
}

public void setName(String name) {
	this.name = name;
}
public String getName() {
	return name;
}
public void setItemID(String itemID) {
	this.itemID = itemID;
}
public String getItemID() {
	return itemID;
}

public GWTMoney getPrice() {
	return price;
}

public String getSysname() {
	return sysname;
}

public void setSysname(String sysname) {
	this.sysname = sysname;
}

public void setPrice(GWTMoney price) {
	this.price = price;
}

public double getTaxrate() {
	return taxrate;
}

public void setTaxrate(double taxrate) {
	this.taxrate = taxrate;
}

 

public double getStockLevel() {
	return stockLevel;
}

public void setStockLevel(double d) {
	this.stockLevel = d;
}

public GWTMoney getBalance() {
	return balance;
}

public void setBalance(GWTMoney balance) {
	this.balance = balance;
}

public double getOpeningStockLevel() {
	return openingStockLevel;
}

public void setOpeningStockLevel(double openingStockLevel) {
	this.openingStockLevel = openingStockLevel;
}

public double getWastedStockLevel() {
	return wastedStockLevel;
}

public void setWastedStockLevel(double wastedStockLevel) {
	this.wastedStockLevel = wastedStockLevel;
}

public double getAddedStockLevel() {
	return addedStockLevel;
}

public void setAddedStockLevel(double addedStockLevel) {
	this.addedStockLevel = addedStockLevel;
}
}

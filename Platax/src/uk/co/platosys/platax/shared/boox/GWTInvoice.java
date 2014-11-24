package uk.co.platosys.platax.shared.boox;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;


public class GWTInvoice extends GWTBill implements Serializable, IsSerializable {
	 public static final int SELECTION_OPEN=72;
	   public static final int SELECTION_ALL=64;
	   public static final int SELECTION_PAID=32;
	   public static final int SELECTION_PENDING=24;
	   public static final int SELECTION_OVERDUE=16;
	   public static final int SELECTION_DISPUTED=8;
	   public static final String CUSTOMER_NAME_HEADER="Customer";
	   public static final String VALUE_DATE_HEADER="Date";
	   public static final String DUE_DATE_HEADER="Due";
	   public static final String GROSS_HEADER="Total";
	   public static final String NUMBER_HEADER="Inv.No";
	   public static final String STATUS_HEADER="Status";
GWTCustomer customer=null;
GWTEnterprise enterprise=null;
ArrayList<GWTLineItem> lineItems=new ArrayList<GWTLineItem>();
HashMap<String, GWTItem> products = new HashMap<String, GWTItem>();
Date valueDate=new Date(0);
Date dueDate=new Date(0);
Date raisedDate=new Date(0);
Date createdDate=new Date(0);
Date paidDate=new Date(0);
String status="";
GWTMoney net=new GWTMoney();
GWTMoney tax=new GWTMoney();
GWTMoney gross=new GWTMoney();
String sysname="";
String userno="";

public GWTInvoice(){}

public GWTCustomer getCustomer() {
	return customer;
}
public void setCustomer(GWTCustomer customer) {
	this.customer = customer;
}
public GWTEnterprise getEnterprise() {
	return enterprise;
}
public void setEnterprise(GWTEnterprise enterprise) {
	this.enterprise = enterprise;
}

public GWTMoney getNet() {
	return net;
}
public void setNet(GWTMoney net) {
	this.net = net;
}
public GWTMoney getTax() {
	return tax;
}
public void setTax(GWTMoney tax) {
	this.tax = tax;
}
public GWTMoney getGross() {
	return gross;
}
public void setGross(GWTMoney gross) {
	this.gross = gross;
}

public String getSysname() {
	return sysname;
}

public void setSysname(String sysname) {
	this.sysname = sysname;
}

public ArrayList<GWTLineItem> getLineItems() {
	return lineItems;
}

public void setLineItems(ArrayList<GWTLineItem> lineItems) {
	this.lineItems = lineItems;
}

public void addLineItem(GWTLineItem lineItem) throws Exception{
	this.net=net.add(lineItem.getNet());
	this.tax=tax.add(lineItem.getTax());
	this.gross=gross.add(lineItem.getGross());
	lineItems.add(lineItem);
}

public boolean removeLineItem(GWTLineItem lineItem) throws Exception {
	if (lineItems.remove(lineItem)){
		Iterator<GWTLineItem> lit = lineItems.iterator();
		while(lit.hasNext()){
			 GWTLineItem litem=lit.next();
			 litem.setLineNumber(lineItems.indexOf(litem)+1);
		}
		this.net=net.subtract(lineItem.getNet());
		this.tax=tax.subtract(lineItem.getTax());
		this.gross=gross.subtract(lineItem.getGross());
		return true;
	}else{
		return false;
	}
}
public Date getDueDate() {
	return dueDate;
}

public void setDueDate(Date dueDate) {
	this.dueDate = dueDate;
}

public Date getRaisedDate() {
	return raisedDate;
}

public void setRaisedDate(Date raisedDate) {
	this.raisedDate = raisedDate;
}

public String getUserno() {
	return userno;
}

public void setUserno(String userno) {
	this.userno = userno;
}



public Date getValueDate() {
	return valueDate;
}

public void setValueDate(Date valueDate) {
	this.valueDate = valueDate;
}

public Date getCreatedDate() {
	return createdDate;
}

public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
}

public Date getPaidDate() {
	return paidDate;
}

public void setPaidDate(Date paidDate) {
	this.paidDate = paidDate;
}
public void setStatus(String status){
	this.status=status;
}

public String getStatus() {
	// TODO Auto-generated method stub
	return status;
}

public ArrayList<GWTItem> getProducts() {
	ArrayList<GWTItem> items=new ArrayList<GWTItem>();
	Iterator<String> kit = products.keySet().iterator();
	while(kit.hasNext()){
		items.add(products.get(kit.next()));
	}
	return items;
}

public void setProducts(ArrayList<GWTItem> items) {
	Iterator<GWTItem> pit = items.iterator();
	while(pit.hasNext()){
		GWTItem item = pit.next();
		products.put(item.getSysname(), item);
	}
}
public GWTItem getItem(String itemSysname){
	return products.get(itemSysname);
}
public void addGWTItem(GWTItem item){
	products.put(item.getSysname(), item);
}
}

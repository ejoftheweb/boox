package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GWTEnterprise implements Serializable, IsSerializable {
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String name="Unknown";
 private String legalName="Unknown";
 private String enterpriseID="";
 private String sysname=enterpriseID;
//S private EnterpriseMenu menu;

private int privileges=0;//this is the level of privilege that the session user has vis-a-vis this enterprise 
 private ArrayList<GWTRatio> ratios = new ArrayList<GWTRatio>();
 private ArrayList<GWTCustomer> customers = new ArrayList<GWTCustomer>();
 private ArrayList<GWTItem> products = new ArrayList<GWTItem>();
 private ArrayList<GWTSupplier> suppliers = new ArrayList<GWTSupplier>();
 private ArrayList<GWTItem> components = new ArrayList<GWTItem>();
 private boolean hasOpenTab=false;
 private int openTabIndex=0;
 
 public GWTEnterprise(){
	
}
 public GWTEnterprise(String enterpriseID, String name, String legalName){
	 this.enterpriseID=enterpriseID;
	 this.setName(name);
	 this.setLegalName(legalName);
 }
 public String getSysname(){
	 return enterpriseID;
 }
public void setName(String name) {
	this.name = name;
}
public String getName() {
	return name;
}
public void setLegalName(String legalName) {
	this.legalName = legalName;
}
public String getLegalName() {
	return legalName;
}
public String getEnterpriseID() {
	return enterpriseID;
}
public int getPrivileges(){
	return privileges;
}
public void setPrivileges(int privileges){
	this.privileges=privileges;
}
public void addRatio(String name, String value, String info){
	ratios.add(new GWTRatio(name, value, info));
}
public ArrayList<GWTRatio> getRatios() {
	// TODO Auto-generated method stub
	return ratios;
}
public boolean hasOpenTab(){
	return hasOpenTab;
}
public void setOpenTab(boolean open){
	this.hasOpenTab=open;
}
public void setOpenTabIndex(int openTabIndex) {
	this.openTabIndex = openTabIndex;
}
public int getOpenTabIndex() {
	return openTabIndex;
}
public void setCustomers(ArrayList<GWTCustomer> customers) {
	this.customers = customers;
}
public List<? extends GWTContact> getCustomers() {
	return customers;
}
public void setSuppliers(ArrayList<GWTSupplier> suppliers) {
	this.suppliers = suppliers;
}
public ArrayList<GWTSupplier> getSuppliers() {
	return suppliers;
}
public boolean addSupplier(GWTSupplier supplier){
	return suppliers.add(supplier);
}
public ArrayList<GWTItem> getProducts() {
	return products;
}
public void setProducts(ArrayList<GWTItem> products) {
	this.products = products;
}
public ArrayList<GWTItem> getComponents() {
	return components;
}
public void setComponents(ArrayList<GWTItem> components) {
	this.components = components;
}
public boolean addCustomer(GWTCustomer customer){
	return customers.add(customer);
}
/*
public void setMenuItemStatus(String menuItem, String status){
	menu.setItemStatus(menuItem, status);
}*/

}

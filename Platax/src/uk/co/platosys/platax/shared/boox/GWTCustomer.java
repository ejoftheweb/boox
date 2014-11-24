package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GWTCustomer extends GWTContact implements Serializable, IsSerializable {

	private GWTMoney balance = new GWTMoney();
	private GWTMoney overdueBalance = new GWTMoney();
	private GWTMoney sales = new GWTMoney();
	private GWTMoney disputedBalance= new GWTMoney();
	
public GWTCustomer(){}

public GWTCustomer(String name, String sysname){
	super(name, sysname);
	this.name=name;
	this.sysname=sysname;
	
}

public GWTMoney getBalance() {
	return balance;
}

public void setBalance(GWTMoney balance) {
	this.balance = balance;
}

public GWTMoney getOverdueBalance() {
	return overdueBalance;
}

public void setOverdueBalance(GWTMoney overdueBalance) {
	this.overdueBalance = overdueBalance;
}

public GWTMoney getSales() {
	return sales;
}

public void setSales(GWTMoney sales) {
	this.sales = sales;
}

public void setDisputedBalance(GWTMoney disputed) {
	this.disputedBalance=disputed;
	
}
public GWTMoney getDisputedBalance(){
	return disputedBalance;
}



}

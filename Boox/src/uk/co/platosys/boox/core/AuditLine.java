package uk.co.platosys.boox.core;

import java.io.Serializable;

import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.money.Currency;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.util.ISODate;

/**
 * The most granular implementation of AuditElement
 * 
 * @author edward
 *
 */
public class AuditLine implements AuditElement, Serializable {
	private Money balance=Money.ZERO;
	private String name;
	private ISODate date;

public AuditLine (Money balance, String name,  ISODate date){
	this.balance=balance;
	this.name=name;
	this.date=date;
}
	
public Money getBalance(){ 
	return balance;
}

public String getName(){
	return name;
}


public ISODate getDate() {
	return date;
}

@Override
public Money getBalance(Enterprise enterprise, Clerk clerk)
		throws PermissionsException {
	// TODO Auto-generated method stub
	return balance;
}

@Override
public Currency getCurrency() {
	// TODO Auto-generated method stub
	return balance.getCurrency();
}
}

package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;
import java.util.Date;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.TreeViewModel;



public class GWTAuditLine extends AbstractDataProvider<GWTAuditable> implements Serializable, TreeViewModel.NodeInfo<GWTAuditable>, GWTAuditElement {
	private GWTMoney balance=new GWTMoney();
	private String name="No Permission";
	private Date date=new Date();
	private boolean hidden=true;
	final MultiSelectionModel<GWTAuditable> selectionModel = new MultiSelectionModel<GWTAuditable>();
   

public GWTAuditLine(){}
public GWTAuditLine(GWTAuditable parent){
	this.balance=parent.getBalance();
	this.name=parent.getName();
	this.date=parent.getDate();
}
	
public GWTAuditLine (GWTMoney balance, String name,  Date date){
	this.balance=balance;
	this.name=name;
	this.date=date;
}
	
public GWTMoney getBalance(){ 
	return balance;
}

public String getName(){
	return name;
}


public Date getDate() {
	return date;
}


public boolean isHidden() {
	return hidden;
}

public void setHidden(boolean hidden) {
	this.hidden = hidden;
}

public void setBalance(GWTMoney balance) {
	this.balance = balance;
}

public void setName(String name) {
	this.name = name;
}

public void setDate(Date date) {
	this.date = date;
}
public String asString(){
	return (name+date.toString()+balance.toPlainString());
}




@Override
public MultiSelectionModel<GWTAuditable> getSelectionModel() {
	return selectionModel;
}




@Override
public void unsetDataDisplay() {
	// TODO Auto-generated method stub
	
}

@Override
public void setDataDisplay(HasData<GWTAuditable> display) {
	// TODO Auto-generated method stub
	
}


@Override
public ProvidesKey<GWTAuditable> getProvidesKey() {
	// TODO Auto-generated method stub
	return (ProvidesKey<GWTAuditable>) getKeyProvider();
}

@Override
public ValueUpdater<GWTAuditable> getValueUpdater() {
	// TODO Auto-generated method stub
	return null;
}
public Object getKey(GWTAuditLine item) {
	// TODO Auto-generated method stub
	return this;
}

@Override
protected void onRangeChanged(HasData<GWTAuditable> display) {
	// TODO Auto-generated method stub
	
}
@Override
public Object getKey(GWTAuditable item) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public GWTAuditLine getBalanceLine() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public Cell<GWTAuditable> getCell() {
	// TODO Auto-generated method stub
	return null;
}


}

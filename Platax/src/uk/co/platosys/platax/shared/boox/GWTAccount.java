package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.platosys.pws.values.GWTMoney;

import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.HasData;

public class GWTAccount extends AbstractDataProvider<GWTAuditable> implements GWTAuditable , Serializable {
private ArrayList <GWTAuditElement> lines=new ArrayList<GWTAuditElement>();
private String name="";
private String sysname="";
private String fullname="";
private GWTMoney balance=new GWTMoney();

	public GWTAccount() {
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<GWTAuditElement> getLines() {
		return lines;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSysname() {
		return sysname;
	}
	public void setSysname(String sysname) {
		this.sysname = sysname;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
    public void addLine(GWTAuditElement line) throws Exception{
    	this.balance=balance.add(line.getBalance());
    	lines.add(line);
    }
    public GWTMoney getBalance(){
    	return balance;
    }

	@Override
	public GWTAuditLine getBalanceLine(){
		return new GWTAuditLine(this);
	}

	

	@Override
	public Date getDate() {
		
		return new Date();
	}

	

	@Override
	public List<GWTAuditElement> audit() {
		// TODO Auto-generated method stub
		//TODO - verify that this array is populated
		return lines;
	}

	@Override
	protected void onRangeChanged(HasData<GWTAuditable> display) {
		// TODO Auto-generated method stub
		
	}
}

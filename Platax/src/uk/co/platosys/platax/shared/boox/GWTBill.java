package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import uk.co.platosys.pws.values.GWTMoney;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class GWTBill implements Serializable, IsSerializable{
	private GWTMoney netMoney=new GWTMoney();
	private GWTMoney taxMoney=new GWTMoney();
	private GWTMoney grossMoney=new GWTMoney();
	Map<Integer, GWTLineItem> lineitems=new HashMap<Integer, GWTLineItem>();
	String sysname="";
	
	public GWTBill()  {
		// TODO Auto-generated constructor stub
	}
   public abstract void addGWTItem(GWTItem item);
   public GWTMoney getNet() {
		return netMoney;
	}
	public void setNet(GWTMoney net) {
		this.netMoney = net;
	}
	public GWTMoney getTax() {
		return taxMoney;
	}
	public void setTax(GWTMoney tax) {
		this.taxMoney = tax;
	}
	public GWTMoney getGross() {
		//Window.alert("getting gross: "+grossMoney.toPlainString());
		return grossMoney;
	}
	public void setGross(GWTMoney gross) {
		this.grossMoney = gross;
	}

	public String getSysname() {
		return sysname;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}
	void clearTotals(){
		netMoney=new GWTMoney();
		taxMoney=new GWTMoney();
		grossMoney=new GWTMoney();
	}
	public void adjustTotals(GWTLineItem gwtLineItem){
		//Window.alert("GWTBill adjusting totals");
		Integer lineNo = gwtLineItem.getLineNumber();
		if (lineitems.containsKey(lineNo)){
			lineitems.remove(lineNo);
		}
		lineitems.put(lineNo, gwtLineItem);
		//Window.alert("there are "+lineitems.size()+" items");
		clearTotals();
		try{
		for(Integer line: lineitems.keySet()){
			GWTLineItem gitem=lineitems.get(line);
			netMoney=netMoney.add(gitem.getNet());
			taxMoney=taxMoney.add(gitem.getTax());
			grossMoney=grossMoney.add(gitem.getGross());
		}
		//Window.alert("gross money is"+grossMoney.toPlainString());
		}catch(Exception e){
			Window.alert("There seems to be a problem");
		}
	}
}

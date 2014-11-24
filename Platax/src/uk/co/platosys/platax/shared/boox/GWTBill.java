package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class GWTBill implements Serializable, IsSerializable{

	public GWTBill()  {
		// TODO Auto-generated constructor stub
	}
   public abstract void addGWTItem(GWTItem item);
}

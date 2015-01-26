package uk.co.platosys.platax.client.services;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.boox.GWTItem;
import uk.co.platosys.platax.shared.boox.GWTTask;

public interface TaskServiceAsync {
	//public void addProduct(String enterpriseID, String productName, String productDescription, double price, int taxBand, boolean exclusive, AsyncCallback<GWTItem> callback);
	public void  listTasks(PXUser user, AsyncCallback<List<GWTTask>> callback);
}

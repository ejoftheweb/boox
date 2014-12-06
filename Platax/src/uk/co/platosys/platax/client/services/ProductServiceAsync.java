package uk.co.platosys.platax.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import uk.co.platosys.platax.shared.boox.GWTItem;

public interface ProductServiceAsync {
	public void addProduct(String enterpriseID, String productName, String productDescription, double price, int taxBand, boolean exclusive, AsyncCallback<GWTItem> callback);
	public void  listProducts(String enterpriseID, int selection, AsyncCallback<ArrayList<GWTItem>> callback);
}

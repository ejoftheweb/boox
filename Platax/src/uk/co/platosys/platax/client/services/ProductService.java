package uk.co.platosys.platax.client.services;

import java.util.ArrayList;

import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTItem;
import uk.co.platosys.platax.shared.boox.GWTMoney;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("productService")
public interface ProductService extends RemoteService {
	public GWTItem addProduct(String enterpriseID, String  productName, String productDescription, double price, int taxBand, boolean exclusive);
	public ArrayList<GWTItem> listProducts(String enterpriseID, int selection);
}

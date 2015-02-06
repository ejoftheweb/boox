package uk.co.platosys.platax.server.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.co.platosys.boox.trade.Customer;
import uk.co.platosys.boox.trade.TaxedTransaction;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.stock.Item;
import uk.co.platosys.boox.stock.Product;
import uk.co.platosys.boox.stock.ProductCatalogue;
import uk.co.platosys.boox.money.CurrencyException;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.server.core.Booxlet;
import uk.co.platosys.platax.server.core.PXConstants;
import uk.co.platosys.platax.server.core.PlataxServer;
import uk.co.platosys.platax.server.core.PlataxUser;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTItem;
import uk.co.platosys.pws.values.GWTMoney;
import uk.co.platosys.xservlets.Xservlet;

public class ProductServiceImpl extends Booxlet implements ProductService {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 96663546207710390L;

	public GWTItem addProduct(String enterpriseID,
							  String productName,
							  String productDescription, 
							  double dprice, 
							  int taxBand, 
							  boolean taxinclusive) {
		try{
			PlataxUser pxuser =  (PlataxUser) getSession().getAttribute(PXConstants.USER);
			Enterprise enterprise = pxuser.getEnterprise(enterpriseID);
			Clerk clerk = pxuser.getClerk(enterprise);
			Money mprice = new Money(enterprise.getDefaultCurrency(), dprice);
			if(taxinclusive){
				double taxRate = TaxedTransaction.getTaxRate(taxBand);
				Money[] taxes = mprice.extractTax(taxRate);
				mprice=taxes[0];
			}
			Product product = Product.createProduct(enterprise,  productName, productDescription, mprice, clerk, taxBand,0);
			return ProductServiceImpl.createGWTItem(product);
		}catch(Exception ex){
			logger.log("APSI failed to add a product because of error" , ex);
			return null;
		}
	}

	public static List<GWTItem> getProducts(GWTEnterprise gwtEnterprise, PlataxUser user, GWTCustomer gwtCustomer){
		List<GWTItem> gwtItems = new ArrayList<GWTItem>();
		
		try{
			Enterprise enterprise = Enterprise.getEnterprise(gwtEnterprise.getEnterpriseID());
			Clerk clerk=user.getClerk(enterprise);
			String sysname = gwtCustomer.getSysname();
			List<Item> items = Product.getProducts(enterprise, clerk, Customer.getCustomer(enterprise, clerk, sysname));
			Iterator<Item>cit=items.iterator();
			while(cit.hasNext()){
				Item item=cit.next();
				gwtItems.add(new GWTItem(item.getName(), item.getSysname(), PlataxServer.convert(item.getPrice())));
			}
		}catch (Exception x){
			//the permissions exception will be caught here for now
			
			PlataxServer.logger.log("plataxSVR exception getting customer list", x);
		}
		return gwtItems;
	}

	public static GWTItem createGWTItem(Item item){
		GWTItem gwtItem = new GWTItem(item.getName(), item.getSysname(), PlataxServer.convert(item.getPrice()));
		int taxBand = item.getTaxBand();
		double taxRate=TaxedTransaction.getTaxRate(taxBand);
		gwtItem.setTaxrate(taxRate/100);
		return gwtItem;
	}

	@Override
	public  ArrayList<GWTItem> listProducts(String enterpriseID, int selection) {
		logger.log("PSIlP called, getting list of products");
		ArrayList<GWTItem> gwtItems = new ArrayList<GWTItem>();
		
		try{
			PlataxUser pxuser =  (PlataxUser) getSession().getAttribute(PXConstants.USER);
			Enterprise enterprise = pxuser.getEnterprise(enterpriseID);
			Clerk clerk = pxuser.getClerk(enterprise);
				List<Item> items = Product.getProducts(enterprise, clerk,  selection);
			for(Item item:items){
				gwtItems.add(new GWTItem(item.getName(), item.getSysname(), PlataxServer.convert(item.getPrice())));
			}
		}catch (Exception x){
			//the permissions exception will be caught here for now
			logger.log("PSIlp exception getting products list", x);
		}
		logger.log("PSIlP returning "+gwtItems.size()+" items");
		return gwtItems;
	}
	
	protected static  GWTItem convert(Product product, Enterprise enterprise, Clerk clerk){
		GWTItem gwtItem=new GWTItem(product.getName(), product.getSysname(), PlataxServer.convert(product.getPrice()));
	   logger.log("PSIconvert has created new GWTItem:"+gwtItem.getName()+" sysname:"+gwtItem.getSysname());
	   try{
		   gwtItem.setBalance(PlataxServer.convert(product.getBalance(enterprise, clerk)));
		   gwtItem.setStockLevel(product.getStockLevel());
		  // gwtItem.setSales(PlataxServer.convert(product.getSales(enterprise, clerk)));
	   }catch(PermissionsException px){//don't do anything about the px
		   logger.log("GWTIc exception thrown", px);
	   }/*catch (PlatosysDBException e) {
		// TODO Auto-generated catch block
		logger.log("GWTI exception thrown", e);
	   }catch (CurrencyException e) {
		// TODO Auto-generated catch block
		logger.log("GWTIc exception thrown", e);
	   }*/catch(Exception x){
		   logger.log("GWTcc exception thrown", x);
	   }
		return gwtItem;
	}
}

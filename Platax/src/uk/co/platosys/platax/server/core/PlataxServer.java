/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.platax.server.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;
import org.jdom2.Namespace;

import uk.co.platosys.boox.trade.Customer;
import uk.co.platosys.boox.trade.Supplier;
import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.Boox;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.stock.Component;
import uk.co.platosys.boox.stock.Item;
import uk.co.platosys.boox.stock.Product;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.db.DBTools;
import uk.co.platosys.db.jdbc.DatabaseCredentials;
import uk.co.platosys.db.jdbc.DatabaseProperties;
import uk.co.platosys.db.Table;
import uk.co.platosys.platax.shared.PXAddress;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTItem;
import uk.co.platosys.platax.shared.boox.GWTRatio;
import uk.co.platosys.platax.shared.boox.GWTSupplier;
import uk.co.platosys.platax.shared.exceptions.PlataxException;
import uk.co.platosys.pws.values.GWTMoney;
import uk.co.platosys.util.HashPass;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.RandomString;
import uk.co.platosys.xuser.Xaddress;

/**
 *
 * @author edward
 */
public class PlataxServer {


private static Namespace ns = Namespace.getNamespace("http://www.platosys.co.uk/parsley");
private static Map<Long, Enterprise> enterprises = new HashMap<Long, Enterprise>();
public static Logger logger  = Logger.getLogger("platax");
 
    public static void createLedgersAndAccounts(Enterprise enterprise, Clerk clerk, Element element, Ledger ledger) throws BooxException, PermissionsException{
        Boox.createLedgersAndAccounts(enterprise, clerk, element, ledger);
        
    }
  
    ///this section has a series of methods to create GWTserializable analogues of Boox accounting objects and the inverse
    public static GWTMoney convert(Money money){
    	if(money==null){
    		logger.log("PXServer convert - null money");
    		return new GWTMoney();
    	}
    	return new GWTMoney(money.getCurrency().getTLA(), money.getAmount().doubleValue());
    }
    public static Money convert(GWTMoney gwtMoney){
    	return new Money(gwtMoney.getCurrencyTLA(), gwtMoney.getAmount());
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
    			gwtItems.add(new GWTItem(item.getName(), item.getSysname(), convert(item.getPrice())));
    		}
    	}catch (Exception x){
    		//the permissions exception will be caught here for now
    		
    		logger.log("plataxSVR exception getting customer list", x);
    	}
    	return gwtItems;
    }
    public static List<GWTItem> getComponents(GWTEnterprise gwtEnterprise, PlataxUser user, GWTSupplier gwtSupplier){
    	List<GWTItem> gwtItems = new ArrayList<GWTItem>();
    	try{
    		Enterprise enterprise = Enterprise.getEnterprise(gwtEnterprise.getEnterpriseID());
    		Clerk clerk=user.getClerk(enterprise);
    		String sysname=gwtSupplier.getSysname();
    		List<Item> items = Component.getComponents(enterprise,clerk, Supplier.getSupplier(enterprise, clerk, sysname));
    		Iterator<Item>cit=items.iterator();
    		while(cit.hasNext()){
    			Item item=cit.next();
    			gwtItems.add(new GWTItem(item.getName(), item.getSysname(), convert(item.getPrice())));
    		}
    	}catch (Exception x){
    		//the permissions exception will be caught here for now
    		
    		logger.log("plataxSVR exception getting customer list", x);
    	}
    	return gwtItems;
    }
    
    
    
    
    
    /**
     * method to get an enterprise to read its current ratios.
     * @param enterprise
     * @param user
     * @param gwtEnterprise
     * @return
     */
    public static GWTEnterprise readRatios (Enterprise enterprise, PlataxUser user, GWTEnterprise gwtEnterprise){
    	try {
			Clerk clerk = user.getClerk(enterprise);
			//Read Net Current Assets:
			Money currentAssets =Boox.openLedger(enterprise, "CurrentAssets", clerk).getBalance(enterprise, clerk);
			Money currentLiabilities=Boox.openLedger(enterprise, "CurrentLiabilities", clerk).getBalance(enterprise, clerk);
			Money netCurrentAssets = currentAssets.subtract(currentLiabilities);
			gwtEnterprise.addRatio("Net Current Assets", netCurrentAssets.toPrefixedString(), "Shows how solvent the enterprise is");
			//Read Owner's Capital
			Money equity = Boox.openLedger(enterprise, "Equity", clerk).getBalance(enterprise, clerk);
			gwtEnterprise.addRatio("Equity Capital", equity.toPrefixedString(), "Book value of the company");
			return gwtEnterprise;
		} catch (Exception e) {
			logger.log("problem reading ratios for enterprise "+enterprise.getName(), e);
			e.printStackTrace();
		}
		return null;
        
    }
 
}


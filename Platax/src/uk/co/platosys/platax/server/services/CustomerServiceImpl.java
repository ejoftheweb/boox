package uk.co.platosys.platax.server.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Directory;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.money.CurrencyException;
import uk.co.platosys.boox.trade.Customer;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.platax.client.services.CustomerService;
import uk.co.platosys.platax.server.core.PXConstants;
import uk.co.platosys.platax.server.core.PlataxServer;
import uk.co.platosys.platax.server.core.PlataxUser;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.exceptions.PlataxException;
import uk.co.platosys.util.Logger;
import uk.co.platosys.xservlets.Xservlet;

public class CustomerServiceImpl extends Xservlet implements CustomerService {
static Logger logger = Logger.getLogger("platax");
	/**
	 * 
	 */
	private static final long serialVersionUID = 2425547946355362843L;

	@Override
	public GWTCustomer addCustomer(String enterpriseName , String name, String customerID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GWTCustomer addCustomer(String enterpriseID, String name, boolean isPrivate) throws PlataxException  {
		logger.log("ACSL calling add customer service");
		PlataxUser pxuser =  (PlataxUser) getSession().getAttribute("PlataxUser");
		Enterprise enterprise = pxuser.getEnterprise(enterpriseID);
		Clerk clerk = pxuser.getClerk(enterprise);
		Ledger ledger = Ledger.getLedger(enterprise, Customer.CUSTOMERS_LEDGER_NAME);
		Customer customer;
		try {
			customer = Customer.createCustomer(enterprise, clerk, ledger, name, isPrivate );
			logger.log("ACSL has created customer:"+customer.getName());
		} catch (PermissionsException e) {
			logger.log("exception thrown", e);
			throw new PlataxException("ACSL: permissionsException", e);
		} catch (PlatosysDBException e) {
			logger.log("exception thrown", e);
			throw new PlataxException("ACSL: platosysDBException", e);
		} catch (BooxException e) {
		    logger.log("exception thrown", e);
			throw new PlataxException("ACSL: booxException", e);
		}
		return convert(customer);
	}

	protected static  GWTCustomer convert(Customer customer){
		GWTCustomer gwtCustomer=new GWTCustomer(customer.getName(), customer.getSysname());
	   logger.log("CSI has created new GWTCustomer:"+gwtCustomer.getName()+" sysname:"+customer.getSysname());
	   
		return gwtCustomer;
	}
	protected static  GWTCustomer convert(Customer customer, Enterprise enterprise, Clerk clerk){
		GWTCustomer gwtCustomer=new GWTCustomer(customer.getName(), customer.getSysname());
	   logger.log("CSI has created new GWTCustomer:"+gwtCustomer.getName()+" sysname:"+customer.getSysname());
	   try{
		   gwtCustomer.setBalance(PlataxServer.convert(customer.getBalance(enterprise, clerk)));
		   gwtCustomer.setOverdueBalance(PlataxServer.convert(customer.getOverdueBalance(enterprise, clerk)));
		   gwtCustomer.setDisputedBalance(PlataxServer.convert(customer.getDisputedBalance(enterprise, clerk)));
		   gwtCustomer.setSales(PlataxServer.convert(customer.getSales(enterprise, clerk)));
	   }catch(PermissionsException px){//don't do anything about the px
		   logger.log("GWTCc exception thrown", px);
	   }catch (PlatosysDBException e) {
		// TODO Auto-generated catch block
		logger.log("GWTCc exception thrown", e);
	   }catch (CurrencyException e) {
		// TODO Auto-generated catch block
		logger.log("GWTCc exception thrown", e);
	   }catch(Exception x){
		   logger.log("GWTcc exception thrown", x);
	   }
		return gwtCustomer;
	}
	protected static Customer convert(GWTCustomer customer){
		//TODO
		return null;
	}

	@Override
	public GWTCustomer editCustomer(GWTCustomer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<GWTCustomer> listCustomers(String enterpriseID, int selection) {
		try{
		PlataxUser pxuser =  (PlataxUser) getSession().getAttribute(PXConstants.USER);
		Enterprise enterprise= pxuser.getEnterprise(enterpriseID);
		Clerk clerk= pxuser.getClerk(enterprise);
		List<Customer> customers = Customer.getCustomers(enterprise, clerk, selection);
		ArrayList<GWTCustomer> gwcusts = new ArrayList<GWTCustomer>();
		Iterator<Customer> cit = customers.iterator();
		while(cit.hasNext()){
			gwcusts.add(convert(cit.next(), enterprise, clerk));
		}
		return gwcusts;
		}catch(Exception x){
			logger.log("CSI problem getting the customer list", x);
			return null;
		}
	}

	@Override
	public GWTCustomer checkName(String customerName) {
		try {
			if(Directory.bodyExists(customerName)){
				String sysname = Directory.getSysnameFromName(customerName);
				GWTCustomer gCustomer = new GWTCustomer(customerName, sysname);
				gCustomer.setLegalName(Directory.getLegalName(sysname));
				return gCustomer;
			}else{
				return null;
			}
		} catch (Exception e) {
			logger.log("error", e);
			return null;
		}
	}

}

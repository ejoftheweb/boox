package uk.co.platosys.platax.server.services;

import java.util.ArrayList;
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
import uk.co.platosys.platax.client.Constants;
import uk.co.platosys.platax.client.services.CustomerService;
import uk.co.platosys.platax.server.core.Booxlet;
import uk.co.platosys.platax.server.core.PXConstants;
import uk.co.platosys.platax.server.core.PlataxServer;
import uk.co.platosys.platax.server.core.PlataxUser;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.exceptions.PlataxException;
import uk.co.platosys.util.Logger;

public class CustomerServiceImpl extends Booxlet implements CustomerService {
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
	public GWTCustomer addCustomer(String enterpriseID, String name, boolean isTrade) throws PlataxException  {
		try{
		logger.log("ACSL calling add customer service "+enterpriseID+" for "+name);
		PlataxUser pxuser =  (PlataxUser) getSession().getAttribute("PlataxUser");
		logger.log("ACSL user in session is "+pxuser.getUsername());
		Enterprise enterprise = pxuser.getEnterprise(enterpriseID);
		logger.log("ACSL ent in session is "+enterprise.getName());
		Clerk clerk = pxuser.getClerk(enterprise);
		logger.log("ACSL clerk is "+clerk.getName());
		
		Ledger ledger = Ledger.getLedger(enterprise, Customer.CUSTOMERS_LEDGER_NAME);
		logger.log("CSL ledger is "+ledger.getFullName());
		Customer customer;
		try {
			customer = Customer.createCustomer(enterprise, clerk, ledger, name, isTrade );
			logger.log("ACSL has created customer:"+customer.getName());
		} catch (PermissionsException e) {
			logger.log("exception thrown", e);
			throw new PlataxException("ACSL: permissionsException", e);
		} catch (PlatosysDBException f) {
			logger.log("exception thrown", f);
			throw new PlataxException("ACSL: platosysDBException", f);
		} catch (BooxException g) {
		    logger.log("exception thrown", g);
			throw new PlataxException("ACSL: booxException", g);
		} catch (Exception h){
			 logger.log("exception thrown", h);
				throw new PlataxException("ACSL: generic Exception", h);
		}
		return convert(customer);
		}catch(Exception e){
			logger.log("CSL exception", e);
			return null;
		}
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
			for(Customer customer:customers){
				if(selection==Constants.ALL_CUSTOMERS_NOFIN){
					//produces a lightweight list with no financial data
					gwcusts.add(convert(customer));
				}else{
					gwcusts.add(convert(customer, enterprise, clerk));
				}
			}
			logger.log("CSI returning a list of "+gwcusts.size()+" gCustomers");
			return gwcusts;
		}catch(Exception x){
			logger.log("CSI problem getting the customer list", x);
			return null;
		}
	}

	@Override
	public GWTCustomer checkName(String customerName) {
		logger.log("Checking customerName "+customerName);
		try {
			if(Directory.bodyExists(customerName)){
				String sysname = Directory.getSysnameFromName(customerName);
				GWTCustomer gCustomer = new GWTCustomer(customerName, sysname);
				gCustomer.setLegalName(Directory.getLegalName(sysname));
				return gCustomer;
			}else{
				logger.log("CustomerName "+customerName +" doesn't exist, returning null");
				return null;
			}
		} catch (Exception e) {
			logger.log("error", e);
			return null;
		}
	}

}

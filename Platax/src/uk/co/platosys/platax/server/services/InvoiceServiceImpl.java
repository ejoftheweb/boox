package uk.co.platosys.platax.server.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.stock.Item;
import uk.co.platosys.boox.stock.Product;
import uk.co.platosys.boox.stock.ProductCatalogue;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.boox.trade.Customer;
import uk.co.platosys.boox.trade.Invoice;
import uk.co.platosys.boox.trade.InvoiceItem;
import uk.co.platosys.platax.client.services.InvoiceService;
import uk.co.platosys.platax.server.core.Booxlet;
import uk.co.platosys.platax.server.core.PXConstants;
import uk.co.platosys.platax.server.core.PlataxServer;
import uk.co.platosys.platax.server.core.PlataxUser;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTInvoice;
import uk.co.platosys.platax.shared.boox.GWTItem;
import uk.co.platosys.platax.shared.boox.GWTLineItem;
import uk.co.platosys.platax.shared.boox.GWTInvoiceList;
import uk.co.platosys.platax.shared.exceptions.PlataxException;
import uk.co.platosys.pws.values.GWTMoney;
import uk.co.platosys.xservlets.Xservlet;

public class InvoiceServiceImpl extends Booxlet implements InvoiceService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public GWTInvoice createInvoice(String enterpriseID, String customerID, Date date) {
		try{
			logger.log("ISL-cI: customer ID is "+customerID);
			PlataxUser pxuser =  (PlataxUser) getSession().getAttribute(PXConstants.USER);
			Enterprise enterprise= getEnterprise(enterpriseID);
			Clerk clerk= getClerk(enterprise);
			GWTEnterprise gEnterprise= EnterpriseServiceImpl.convert(enterprise, clerk);
			
			Customer customer= Customer.getCustomer(enterprise, clerk, customerID);
			logger.log("ISL-cI: got customer, name is:"+customer.getName());
			
			Invoice invoice = Invoice.getInvoice(enterprise, clerk, customer);//(enterprise, clerk, customer);
			pxuser.putInvoice(invoice);
			GWTCustomer gwtCustomer = CustomerServiceImpl.convert(customer);
			if (gwtCustomer==null){throw new PlataxException("ISL create invoice, seem to have no customer?");}
			GWTInvoice gwtInvoice = new GWTInvoice();
			ArrayList<GWTItem> gwtProductList = new ArrayList<GWTItem>();
			for(Item item:Product.getProducts(enterprise, clerk, customer)){
				gwtProductList.add(ProductServiceImpl.createGWTItem(item));
			}
			Map<Integer, InvoiceItem> invoiceItems = invoice.getInvoiceItems();
			logger.log("ISI-cI there are "+invoiceItems.size()+" items on the invoice ");
			for (Integer index:invoiceItems.keySet()){
				InvoiceItem invitem = invoiceItems.get(index);
				logger.log("ISI-cI converting item at "+index);
				logger.log("ISI-cI item name is "+invitem.getProduct().getName());
				logger.log("ISI-cI item qty is "+invitem.getQuantity());
				logger.log("ISI-cI item price is "+invitem.getUnitPrice().toPlainString());
				logger.log("ISI-cI item curr is "+invitem.getUnitPrice().getCurrency().getTLA());
				GWTLineItem gItem=convert(index, invitem);
				gItem.setEnterprise(gEnterprise);
				gItem.setInvoiceSysname(invoice.getSysname());
				gItem.setItemSysname(invitem.getProduct().getSysname());
				gwtInvoice.addLineItem(gItem);
			}
			gwtInvoice.setProducts(gwtProductList);
			gwtInvoice.setCustomer(gwtCustomer);
			gwtInvoice.setEnterprise(EnterpriseServiceImpl.convert(enterprise, clerk));
			logger.log("ISIcI: invoice sysname is"+invoice.getSysname());
			gwtInvoice.setSysname(invoice.getSysname());
			gwtInvoice.setUserno(invoice.getUserInvoiceNumber());
			
			return gwtInvoice;
		}catch(Exception x){
			logger.log("ISL problem creating invoice", x);
			return null;
		}
	}

	@Override
	public GWTLineItem postLine(GWTLineItem line) {
		try{
		//we get a line populated with some meagre data, we do the thang and send it back.
			PlataxUser pxuser =  (PlataxUser) getSession().getAttribute(PXConstants.USER);
			
			logger.log("ISIpl:i line no is:"+line.getLineNumber());
			logger.log("ISIpl:ii SIN is:"+line.getInvoiceSysname());
			logger.log("ISIpl:iii itemsysname is:"+line.getItemSysname());
			logger.log("ISIpl:iv itemname is:"+line.getItemName());
			logger.log("ISIpl:v enterprise is:"+line.getEnterprise().getName());
			logger.log("ISIpl:vi enterpriseID is:"+line.getEnterprise().getSysname());
		//first, get the product:
		Enterprise enterprise = Enterprise.getEnterprise(line.getEnterprise().getEnterpriseID());
		Clerk clerk = pxuser.getClerk(enterprise);
		Product product = Product.getProduct(enterprise, clerk, line.getItemSysname());
		    logger.log("ISIpl: product is:"+product.getName());
		String sin = line.getInvoiceSysname();
		Invoice invoice = pxuser.getInvoice(sin);
		logger.log("ISIpl: invoice is:"+invoice.getSysname());
		float quantity = line.getItemQty();
		InvoiceItem invoiceItem = InvoiceItem.getInvoiceItem(enterprise, clerk, invoice, product, quantity, line.getLineNumber());
		logger.log("ISIpl: invoice item taxBand is"+invoiceItem.getTaxBand());
		line.setItemName(invoiceItem.getDescription());
		Money price = invoiceItem.getUnitPrice();
		line.setPrice(PlataxServer.convert(price));
		invoice.addItem(invoiceItem, line.getLineNumber());
		return line;
		}catch(Exception x){
			logger.log("ISIpl returning null, error",x);
			return null;
		}
	}
	@Override
	public GWTLineItem voidLine(GWTLineItem line){
		logger.log("ISI - call to void line");
		try{
			Invoice invoice = getInvoice(line.getInvoiceSysname());
			Enterprise enterprise = getEnterprise(line.getEnterprise().getEnterpriseID());
			Clerk clerk = getClerk(enterprise);
			InvoiceItem item = invoice.getInvoiceItemAt(line.getLineNumber());
			if(item.reverse(enterprise, clerk, "Form Void")){
				line.setItemQty(0);
				line.setItemName("void");
				line.setPrice(PlataxServer.convert(Money.zero()));
			logger.log(line.getLineNumber()+" "+ line.getItemName()+" "+line.getItemQty()+" "+line.getPrice().toPlainString()+" "+line.getNet().toPlainString()+" "+line.getGross().toPlainString());
				return line;
			}else{
				logger.log("ISIvoidLine: reverse call failed");return null;
			}
		}catch(Exception x){
			logger.log("problem voiding the line..ooops", x);
			return null;
		}
	}
	public GWTLineItem convert(Integer index, InvoiceItem invitem){
		GWTLineItem item=convert(invitem);
		item.setLineNumber(index);
		return item;
	}
	
	public GWTLineItem convert(InvoiceItem invitem){
		try{
			GWTLineItem lineItem = new GWTLineItem();
			lineItem.setItemName(invitem.getProduct().getName());
			lineItem.setItemQty((float) invitem.getQuantity());
			lineItem.setPrice(PlataxServer.convert(invitem.getUnitPrice()));
			return lineItem;
		}catch(Exception x){
			logger.log("ISI problem converting line item ", x);
			return null;
		}
	}
	@Override	
	public GWTInvoice raiseInvoice(GWTInvoice gwtInvoice)  {
		try{
			String sysname = gwtInvoice.getSysname();
		    logger.log("ISIri- raising invoice "+sysname+ " for "+gwtInvoice.getGross().toPlainString());
			String enterpriseID = gwtInvoice.getEnterprise().getEnterpriseID();
			PlataxUser pxuser =  (PlataxUser) getSession().getAttribute(PXConstants.USER);
			Enterprise enterprise= pxuser.getEnterprise(enterpriseID);
			Clerk clerk= pxuser.getClerk(enterprise);
			Invoice invoice = pxuser.getInvoice(sysname);
			if (invoice==null){
				invoice=Invoice.openInvoice(enterprise, clerk, sysname);
			}
			//now we iterate through the lines on the GWTinvoice:
			List<GWTLineItem> lineItems = gwtInvoice.getLineItems();
			Iterator<GWTLineItem> lit = lineItems.iterator();
			while(lit.hasNext()){
				postLine(lit.next());
			}
			Money raised=invoice.raise(enterprise);
			Money gross = PlataxServer.convert(gwtInvoice.getGross());
			if(raised.equals(gross)){
				gwtInvoice.setRaisedDate(new Date());
				return gwtInvoice;
			}else{
				logger.log("ISIrI: raised:"+raised.toPrefixedString()+", gross:"+gross.toPrefixedString());
				return gwtInvoice;
			}
		}catch(Exception e){
			logger.log("problem raising the invoice", e);
		}
		return null;
	}

	@Override
	public GWTInvoiceList listInvoices(String enterpriseID, int selection) {
		try{
			logger.log("ISI getting a listy of invoices "+selection);
			PlataxUser pxuser =  (PlataxUser) getSession().getAttribute(PXConstants.USER);
			Enterprise enterprise= pxuser.getEnterprise(enterpriseID);
			Clerk clerk= pxuser.getClerk(enterprise);
		     int i=0;
		     List<Invoice> invoices =Invoice.getInvoices(enterprise, clerk, selection);
			 GWTInvoiceList gwinvoices = new GWTInvoiceList();
			 Iterator<Invoice> it = invoices.iterator();
			 while (it.hasNext()){
				 Invoice invoice = it.next();
				 logger.log("ISI-listInvoices converting invoice "+invoice.getSystemInvoiceNumber());
				 gwinvoices.add(convert(invoice, clerk));
			 }
			 logger.log("ISI-lI, the invoice list is now ready and it lists "+gwinvoices.size()+" invoices");
			 //debugging code
			 try{
				 File testdir = new File("/var/platosys/test");
				 FileOutputStream fout = new FileOutputStream(new File(testdir, enterpriseID)); 
				 ObjectOutputStream oout= new ObjectOutputStream(fout);
				 oout.writeObject(gwinvoices);
				 oout.flush();
				 oout.close();
			 }catch(Exception e){}//if the debugging code is broken, don't break everything else.
			 //end debugging code
			return gwinvoices;
		}catch(Exception px){
			logger.log("ISI propblem getting invoice list", px);
			return null;
		}
	}
	private GWTInvoice convert(Invoice invoice, Clerk clerk){
		try{
		logger.log("converting invoice number " +invoice.getSystemInvoiceNumber());
		GWTInvoice gwinvoice = new GWTInvoice();
		//identifiers:
		gwinvoice.setSysname(invoice.getSysname());
		logger.log("sysname is: "+gwinvoice.getSysname());
		gwinvoice.setUserno(invoice.getUserInvoiceNumber());
		logger.log("userno is: "+gwinvoice.getUserno());
		
		//dates
		gwinvoice.setCreatedDate(new Date(invoice.getCreatedDate().getTime()));
		logger.log("createdate is: "+gwinvoice.getCreatedDate().toGMTString());
		
		gwinvoice.setValueDate(new Date(invoice.getValueDate().getTime()));
		logger.log("valueedate is: "+gwinvoice.getValueDate().toGMTString());
		gwinvoice.setDueDate(new Date(invoice.getDueDate().getTime()));
		logger.log("duedate is: "+gwinvoice.getDueDate().toGMTString());
		gwinvoice.setRaisedDate(new Date(invoice.getRaisedDate().getTime()));
		logger.log("raisedate is: "+gwinvoice.getRaisedDate().toGMTString());
		//values
		gwinvoice.setNet(PlataxServer.convert(invoice.getNet()));
		logger.log("net is: "+gwinvoice.getNet().toPlainString());
		gwinvoice.setTax(PlataxServer.convert(invoice.getTax()));
		logger.log("tax is: "+gwinvoice.getTax().toPlainString());
		gwinvoice.setGross(PlataxServer.convert(invoice.getTotal()));
		logger.log("gross is: "+gwinvoice.getGross().toPlainString());
		gwinvoice.setStatus(invoice.getStatus());
		logger.log("status is: "+gwinvoice.getStatus());
		
		//members
		gwinvoice.setCustomer(CustomerServiceImpl.convert(invoice.getCustomer()));
		logger.log("customer is: "+gwinvoice.getCustomer().getName());
		
		gwinvoice.setEnterprise(EnterpriseServiceImpl.convert(invoice.getEnterprise(), clerk));
		logger.log("ent is: "+gwinvoice.getEnterprise().getName());
		
		return gwinvoice;
		}catch(Exception x){
			logger.log("ISI-convert: issue converting invoice to GWT format", x);
			return null;
		}
	}
	
	private Invoice convert(GWTInvoice gwinvoice){
		//TODO
		return null;
	}
    
    private Invoice getInvoice(String sin) throws PlataxException{
    	PlataxUser pxuser =  (PlataxUser) getSession().getAttribute(PXConstants.USER);
		return pxuser.getInvoice(sin);
    }

	@Override
	public GWTInvoice deleteInvoice(GWTInvoice ginvoice) {
		try{
			Invoice invoice = getInvoice(ginvoice.getSysname());
			invoice.voidInvoice();
			return null;
		}catch(Exception x){
			return null;
		}
	}

	@Override
	public GWTInvoice saveInvoice(GWTInvoice invoice) {
		// TODO Auto-generated method stub
		return null;
	}
    
}

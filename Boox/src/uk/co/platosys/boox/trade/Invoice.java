/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.trade;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import uk.co.platosys.util.DocMan;
import uk.co.platosys.util.ISODate;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.ShortHash;
import uk.co.platosys.boox.core.Boox;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Journal;
import uk.co.platosys.boox.core.Permission;
import uk.co.platosys.boox.stock.Product;
import uk.co.platosys.boox.money.Currency;
import uk.co.platosys.boox.money.CurrencyException;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.boox.core.Transaction;
import uk.co.platosys.boox.constants.Constants;
import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.db.ColumnNotFoundException;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.RowNotFoundException;
import uk.co.platosys.db.SerialTable;
import uk.co.platosys.db.Table;
import uk.co.platosys.db.jdbc.JDBCRow;
import uk.co.platosys.db.jdbc.JDBCSerialTable;
import uk.co.platosys.db.jdbc.JDBCTable;

/**
 *The Invoice class models a real-world invoice. 
 * An invoice creates a debt owed by a customer to the business: the basic transaction is
 * debit-customer, credit-sales. However, nothing is quite so simple.
 * Bear in mind that Boox uses an Account for every item.
 * 
 *<h2>SDE Invoicing, Tax and Settlement - a primer</h2>
 * <p>
 * Invoices bundle several transactions: line-entry transactions credit individual items. 
 * and output VAT is payable from the tax-point, which is generally the invoice date.(technically the earlier of the
 * supply date or the invoice date).
 *  <p>
 * Under Boox-SDE, Invoices are Accounts; adding an InvoiceItem to an Invoice 
 * creates a Transaction on the Account (two if there is any tax involved) . These transactions are:
 * <ul>
 * <li>**credit item, debit invoice
 * <li>**credit outputTax, debit invoice.
 * </ul>
 *  <p>
 *
 * The process of raising always involves creating a document of record, the invoice document. 
 * This is written and stored as an xml Document (in Boox' own xml schema) so it can be recreated and styled with an
 * xsl stylesheet for despatch to the customer. The styling allows a transformation to a form the customer can use.
 *  <p>
 * The customer, we hope, will settle their debt. This creates another transaction. Let's assume that the customer also
 * takes a settlement discount. You must raise a credit note for this; a credit note is an invoice with a credit rather than
 * a debit balance so we don't need a separate class.
 * <ul>
 * <li>**debit cash, credit customer total discounted;
 * <li>**debit cashdiscounts, credit creditNote pretax discount;
 * <li>**debit OutputTax, credit creditNote discount-on-the-tax;  
 * <li>**debit creditNote, credit customer discount. 
 * </ul>
 * This will leave a credit balance on the customer account equal to the value of the invoice, so the transaction:
 *  <ul>
 * <li>**credit invoice, debit customer </li>
 * </ul>
 * will clear the invoice.
 * <p>The advantage of not clearing the invoice until it is paid is that it allows easier monitoring of aged debt receivables, so this is the default. However
 * it requires an additional clerical process - allocating cash to invoices, and it gets complicated when there are, for example, credit notes issued which span
 * several invoices. Suppose you are supplying a customer on a trial sale-or-return basis for a limited period, agreeing to credit unsold stock at the end of each w
 * week. You invoice for daily deliveries, but credit weekly. You have no way of knowing which of what credit goes to which invoice.
 * <p>Whichever system is used, in all cases the invoice accounts and the customer account are kept in the same (customer) ledger, so a call to ledger.getBalance() will
 * always return the amount owed by the customer</p> 
 * <h2>Invoice Numbering</h2>
 *  <p>
 * Invoices have three identifiers: a system-generated systemInvoiceNumber  (SIN) (actually a long), a String sysname, and optionally a
 * userInvoiceNumber (UIN), which is a String. SINs are unique with enterprise scope. UINs should be unique with customer 
 * scope. The UIN is by default the same as the SIN but can be manually overridden (and should be when, for example, invoices
 * are written on numbered preprinted forms). 
 
 * 
 * @author edward
 */
public class Invoice  {
   private Enterprise enterprise;
   private Account account;
   private Customer customer;
   private Clerk clerk;
   public static final int SELECTION_OPEN=72;
   public static final int SELECTION_ALL=64;
   public static final int SELECTION_PAID=32;
   public static final int SELECTION_PENDING=24;
   public static final int SELECTION_OVERDUE=16;
   public static final int SELECTION_DISPUTED=8;
   public static final String OPEN="open";
   public static final String PAID="PAID";
   public static final String PENDING="PENDING";
   public static final String OVERDUE="OVERDUE";
   public static final String DISPUTED="DISPUTED";
      
   private static final String INVOICE_DESCRIPTION="invoice";
   private static final String INVOICE_TABLENAME="invoice_list";
   private static final String INVOICE_NUMBER_COLNAME="invoice_no";
   private static final String INVOICE_SYSNAME_COLNAME="sysname";
   private static final String INVOICE_USERNO_COLNAME="user_invoice_no";
   private static final String INVOICE_ACCOUNT_COLNAME="account";
   private static final String INVOICE_CUSTOMER_ACCOUNT_COLNAME="customer_account";
   private static final String INVOICE_CUSTOMER_SYSNAME_COLNAME="customer";
   private static final String INVOICE_CREATED_DATE_COLNAME="date_created";
   private static final String INVOICE_VALUE_DATE_COLNAME="value_date";
   private static final String INVOICE_RAISED_DATE_COLNAME="date_raised";
   private static final String INVOICE_DUE_DATE_COLNAME="date_due";
   private static final String INVOICE_PAID_DATE_COLNAME="date_paid";
   private static final String INVOICE_STATUS_COLNAME="status";
   private static final String INVOICE_DOCUMENT_FILENAME_COLNAME="filename";
   private static final String INVOICE_TOTAL_COLNAME="total";
   private static final String INVOICE_TAX_COLNAME="tax";
   private static final String INVOICE_NET_COLNAME="net";
   private static final String INVOICE_CURRENCY_COLNAME="currency";
   private static final String INVOICE_ACCOUNT_NAME_PREFIX="a";
   private static final String INVOICE_CLEARED_NOTE="invoice: ";
   private static final String INVOICE_ALLOCATE_NOTE="invoice: ";
   private static final File INVOICES_FOLDER= new File ("/var/platosys/boox/data/");//config-file this do'h
   
   //xml schema names etc
   public static final String ROOT_ELEMENT_NAME="invoice";
   public static final String INFO_ELEMENT_NAME="info";
   public static final String ISSUER_ELEMENT_NAME="issuer";
   public static final String CUSTOMER_ELEMENT_NAME="customer";
   public static final String ITEMS_ELEMENT_NAME="items";
   public static final String ITEM_ELEMENT_NAME="item";
   public static final String TOTALS_ELEMENT_NAME="totals";
   public static final String TOTAL_ELEMENT_NAME="total";
   public static final String TERMS_ELEMENT_NAME="terms";
   public static final String TERM_ELEMENT_NAME="term";
   public static final String ISSUER_ITEM_ELEMENT_NAME="issuer-item";
   public static final String CUSTOMER_ITEM_ELEMENT_NAME="customer-item";
   
   public static final String  LINE_NUMBER_ATTRIBUTE_NAME="line-number";
   public static final String CUSTOMER_REF_ATTRIBUTE_NAME="customer-ref";
   public static final String  CATALOGUE_ID_ATTRIBUTE_NAME="catalogue-id";
   public static final String  DESCRIPTION_ATTRIBUTE_NAME="description";
   public static final String  COMMENT_ATTRIBUTE_NAME="comment";
   public static final String  UNIT_PRICE_ATTRIBUTE_NAME="unit-price";
   public static final String  QUANTITY_ATTRIBUTE_NAME ="quantity";
   public static final String TAXRATE_ATTRIBUTE_NAME="taxrate";
	public static final String DISCOUNT_ATTRIBUTE_NAME="discount";
	public static final String NET_ATTRIBUTE_NAME="net";
	public static final String TAX_ATTRIBUTE_NAME ="tax";
	public static final String GROSS_ATTRIBUTE_NAME="gross";
   
   //Identifier fields
   private String userInvoiceNumber="";
   private long systemInvoiceNumber=0;
   private String sysname="";
   public static final String SYSNAME_PREFIX="inv";
   
   //Date fields
   private ISODate valueDate=new ISODate(0);
   private ISODate raisedDate=new ISODate(0);
   private ISODate dueDate=new ISODate(0);
   private ISODate createdDate=new ISODate(0);
   private ISODate paidDate=new ISODate(0);
   
   //Value Fields
   private Money total = Money.zero();
   private Money tax = Money.zero();
   private Money net = Money.zero();
   private Money outstanding = Money.zero();
   private Currency currency=Currency.DEFAULT_CURRENCY;
   //status field
   private String status="";
   
   //admin etc fields
   private static Logger logger=Logger.getLogger("boox");
   private SerialTable invoicesTable = null;
   private int currentIndex=0;
   private Map<String, Money> totals = new HashMap<String, Money>();
   private Map<Integer, InvoiceItem> invoiceItems= new HashMap<Integer,InvoiceItem>();
   private Element rootElement=null;
   private static Namespace ns = Namespace.getNamespace("http://www.platosys.co.uk/boox/");
   /**
    * This private constructor is called only by the createInvoice method
    * @param enterprise
    * @param clerk
    * @param customer
 * @throws PermissionsException 
    */
   private Invoice(Enterprise enterprise, Clerk clerk, Customer customer, Currency currency) throws PermissionsException{
	try {
		 this.enterprise=enterprise;
		 this.invoicesTable=getInvoicesTable(enterprise);
		 this.clerk=clerk;
		 this.systemInvoiceNumber=getNextInvoiceNumber();//creates row in invoices table.
		 logger.log("invoice: sin is "+systemInvoiceNumber);
		 setCurrency(currency);
		 setCreatedDate(new ISODate());
		 //value and due dates default to the created date, but can be reset later as their setters are public.
		 setValueDate(createdDate);
		 setDueDate(createdDate);
		 //
		 setStatus(PENDING);
		 setCustomer(customer);
		 setSysname(SYSNAME_PREFIX+ShortHash.hash(Long.toString(systemInvoiceNumber)+enterprise.getName()));
		 setUserInvoiceNumber(Long.toString(systemInvoiceNumber));
		 setAccount(getInvoiceAccount(enterprise, customer, clerk, sysname));
		 
		  
	} catch (PlatosysDBException e) {
		// TODO Auto-generated catch block
		logger.log("exception thrown", e);
	} catch (BooxException e) {
		// TODO Auto-generated catch block
		logger.log("exception thrown", e);
	}
	 
	}
   
/**
    * This private constructor is called only by the openInvoice method, it takes a sysname argument
    * @param enterprise
    * @param clerk
    * @param customer
    */
   protected Invoice(Enterprise enterprise, Clerk clerk,  String sysname){
	   //note we don't use the setters to set field values when reading from the table, because the setters write back to the table.
	try {
		 this.enterprise=enterprise;
		this.invoicesTable=getInvoicesTable(enterprise);
		this.clerk=clerk;
		  this.sysname=sysname;
		 Row row = invoicesTable.getRow(INVOICE_SYSNAME_COLNAME, sysname);
		 this.systemInvoiceNumber=row.getLong(INVOICE_NUMBER_COLNAME);
		 this.userInvoiceNumber=row.getString(INVOICE_USERNO_COLNAME);
		 this.createdDate=row.getISODate(INVOICE_CREATED_DATE_COLNAME);
		 this.dueDate=row.getISODate(INVOICE_DUE_DATE_COLNAME);
		 this.valueDate=row.getISODate(INVOICE_VALUE_DATE_COLNAME);
		 this.currency=currency.getCurrency(row.getString(INVOICE_CURRENCY_COLNAME));
		 this.net=new Money(currency, row.getDouble(INVOICE_NET_COLNAME));
		 this.tax=new Money(currency, row.getDouble(INVOICE_TAX_COLNAME));
		 this.total=new Money(currency, row.getDouble(INVOICE_TOTAL_COLNAME));
		  this.account=getInvoiceAccount(enterprise,clerk, sysname);
		  this.outstanding=account.getBalance(enterprise, clerk);
		  if(outstanding.moreThan(Money.zero(currency))){
			  if(dueDate.before(new ISODate())){setStatus(OVERDUE);}
			  if(dueDate.before(ISODate.getMonthAgo())){setStatus(DISPUTED);}
		  }
		  this.customer=Customer.getCustomer(enterprise, clerk, row.getString(INVOICE_CUSTOMER_SYSNAME_COLNAME));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		logger.log("exception thrown", e);
	}
	 
	}
   /**Use this method to create an Invoice in the default currency
    *  
    */
   public static Invoice createInvoice(Enterprise enterprise, Clerk clerk, Customer customer) throws PermissionsException {
	    return new Invoice(enterprise, clerk, customer, enterprise.getDefaultCurrency());
   }  
  
   /**Use this method to create an Invoice in the given currency
    *  NB don't, yet, we need to do much more work to make this class more currency-aware.
    */
   public static Invoice createInvoice(Enterprise enterprise, Clerk clerk, Customer customer, Currency currency) throws PermissionsException {
	    return new Invoice(enterprise, clerk, customer, currency);
   }  
/**
    * This method is used to open an existing Invoice
    * @param clerk
    * @param invoiceNumber
    * @return
    */
   public static Invoice openInvoice(Enterprise enterprise, Clerk clerk, String sysname) throws PermissionsException{
	    return new Invoice(enterprise, clerk,  sysname);
   }
   /**
    * adds an item to the invoice at the given index
    * 
    * @param item
    * @param lineno
    * @return the index if successful, otherwise 0 (if the invoice already contains an item at that index).
    * @throws CurrencyException
    * @throws PlatosysDBException
    */
 protected int addInvoiceItem(InvoiceItem item, int index) throws CurrencyException, PlatosysDBException{
	 setNet(Money.add(net, item.getNetMoney()));
	 setTax(Money.add(tax, item.getTaxMoney()));
	 setTotal(Money.add(net, tax));
	 Integer lineNo=new Integer(index);
	 if (invoiceItems.containsKey(lineNo)){
		 return 0;
     }else{
    	 invoiceItems.put(lineNo, item);
     	 return index;
     }
 }
   /**
    * Creates the invoice document
    *
    * @return the invoice Document (a jdom Document);
    * @throws PermissionsException
    */
   public Document raise() throws PermissionsException{
	   try{
		    	Document invoiceDocument = new Document();
		    	this.rootElement = new Element(ROOT_ELEMENT_NAME, ns);
		    	invoiceDocument.setRootElement(rootElement);
		    	Element infoElement = new Element(INFO_ELEMENT_NAME, ns);
		    	//TODO: populate the infoElement
		    	rootElement.addContent(infoElement);
		    	Element issuerElement=new Element(ISSUER_ELEMENT_NAME, ns);
		    	 Map<String, String> info = enterprise.getInfo();
		    	 issuerElement=populateInfoElement(issuerElement, info, ISSUER_ITEM_ELEMENT_NAME);//
		    	 rootElement.addContent(issuerElement);
		    	 Element customerElement=new Element(CUSTOMER_ELEMENT_NAME, ns);
		    	 info=customer.getInfo();
		    	 customerElement=populateInfoElement(customerElement, info, CUSTOMER_ITEM_ELEMENT_NAME);
		    	 rootElement.addContent(customerElement);
		    	 Element itemsElement = new Element(ITEMS_ELEMENT_NAME, ns);
		    	 rootElement.addContent(itemsElement);
		    	 Iterator<Integer> kit = invoiceItems.keySet().iterator();
		    	 while(kit.hasNext()){
		    		 InvoiceItem item = invoiceItems.get(kit.next());
		    		 Element itemElement= new Element(ITEM_ELEMENT_NAME, ns);
		    		 itemElement.setAttribute(LINE_NUMBER_ATTRIBUTE_NAME, item.getLineNumber());
		    		 itemElement.setAttribute(CUSTOMER_REF_ATTRIBUTE_NAME, item.getCustomerRef());
		    		 itemElement.setAttribute(CATALOGUE_ID_ATTRIBUTE_NAME, item.getCatalogueID());
		    		 itemElement.setAttribute(DESCRIPTION_ATTRIBUTE_NAME, item.getDescription());
		    		 itemElement.setAttribute(COMMENT_ATTRIBUTE_NAME, item.getComment());
		    		 itemElement.setAttribute(UNIT_PRICE_ATTRIBUTE_NAME,item.getUnitPrice().toPlainString());
		    		 itemElement.setAttribute(QUANTITY_ATTRIBUTE_NAME, Double.toString(item.getQuantity() ));
		    		 itemElement.setAttribute(TAXRATE_ATTRIBUTE_NAME, Double.toString(item.getTaxRate()));
		    		 itemElement.setAttribute(DISCOUNT_ATTRIBUTE_NAME, Double.toString(item.getDiscount()));
		    		 itemElement.setAttribute(NET_ATTRIBUTE_NAME, item.getNetMoney().toPlainString());
		    		 itemElement.setAttribute(TAX_ATTRIBUTE_NAME, item.getTaxMoney().toPlainString());
		    		 itemElement.setAttribute(GROSS_ATTRIBUTE_NAME, item.getTotal().toPlainString());
		    		 itemsElement.addContent(itemElement);
		    	 }
		    	 Element totalsElement=new Element(TOTALS_ELEMENT_NAME, ns);
		    	 rootElement.addContent(totalsElement);
		    	 Iterator<String> tit = totals.keySet().iterator();
		    	 while(tit.hasNext()){
		    		 String totalname=tit.next();
		    		 Element totalElement=new Element(TOTAL_ELEMENT_NAME, ns);
		    		 totalElement.setAttribute("name",totalname);
		    		 totalElement.setAttribute("value", (totals.get(totalname)).toPlainString());
		    		 totalsElement.addContent(totalElement);
		    	 }
		    	 //TODO: the terms!!
		    	 File invoiceFile=new File(INVOICES_FOLDER, sysname);
		    	 DocMan.write(invoiceFile,invoiceDocument);
		    	setRaisedDate(new ISODate());
		    	return invoiceDocument;
		    
	   }catch(Exception x){
		   logger.log("error raising invoice", x);
	   }
	   return null;
   }
   /**
    * This operation clears the invoice balance by transferring it to the Customer account.
    * @param clerk
    * @return
    * @throws PermissionsException
 * @throws PlatosysDBException 
    */
   public Money clear(Clerk clerk) throws PermissionsException, PlatosysDBException{
	   Money money = account.getBalance(enterprise, clerk);
	    logger.log("balance of invoice account"+account.getName()+" = "+money.toPlainString());
	    Transaction transaction = new Transaction(enterprise, clerk,money, account, customer.getAccount(), INVOICE_CLEARED_NOTE+sysname);
	    if(transaction.post()>0){
	    	setPaidDate(new ISODate());
	    	setStatus(PAID);
	    	return money;
	    }else{
	    	return null;
	    }
   }
  

/**
    * This operation allocates value to the invoice balance by transferring it from the Customer account.
    * @param clerk
    * @return
    * @throws PermissionsException
    */
   public Money allocate(Money settlement, Clerk clerk) throws PermissionsException{
	   Money money = account.getBalance(enterprise, clerk);
	    logger.log("balance of invoice account"+account.getName()+" = "+money.toPlainString());
	    Transaction transaction = new Transaction(enterprise, clerk,settlement, account, customer.getAccount(), INVOICE_ALLOCATE_NOTE+sysname);
	    if(transaction.post()>0){
	    	money = account.getBalance(enterprise, clerk);
		    logger.log("balance of invoice account"+account.getName()+" now = "+money.toPlainString());
		    return money;
		   
	    }else{
	    	return null;
	    }
   }
   private Element populateInfoElement(Element element, Map<String, String> infoMap, String itemElementName){
		Iterator<String> mit = infoMap.keySet().iterator();
		while(mit.hasNext()){
	   		String key = mit.next();
	   		Element itemElement = new Element(itemElementName, ns);
	   		itemElement.setAttribute("key", key);
	   		itemElement.setAttribute("value", infoMap.get(key));
	   		element.addContent(itemElement);
		}
		//rootElement.addContent(element);
		return element;
   }
   
 
/**
 * This method with the private one which follows it returns a list of all the enterprise's invoices.
 * 
 * @param enterprise
 * @param selection
 * @return
 * @throws PlatosysDBException 
 */
public static List<Invoice> getInvoices(Enterprise enterprise, Clerk clerk, int selection) throws PlatosysDBException{
	switch (selection){
		case SELECTION_ALL:
			return getInvoices( getInvoicesTable(enterprise).getRows(), enterprise, clerk);
		case  SELECTION_OPEN: 
		    return getInvoices( getInvoicesTable(enterprise).getRows(INVOICE_STATUS_COLNAME, OPEN), enterprise, clerk);
		case   SELECTION_PAID:
			return getInvoices( getInvoicesTable(enterprise).getRows(INVOICE_STATUS_COLNAME, PAID), enterprise, clerk);
		case  SELECTION_PENDING:
			return getInvoices( getInvoicesTable(enterprise).getRows(INVOICE_STATUS_COLNAME, PENDING), enterprise, clerk);
		case  SELECTION_OVERDUE:
			return getInvoices( getInvoicesTable(enterprise).getRows(INVOICE_STATUS_COLNAME, OVERDUE), enterprise, clerk);
		case  SELECTION_DISPUTED:
			return getInvoices( getInvoicesTable(enterprise).getRows(INVOICE_STATUS_COLNAME, DISPUTED), enterprise, clerk);
		default:
			return getInvoices( getInvoicesTable(enterprise).getRows(), enterprise, clerk);
	}
}
/**
 * This method with the private one which follows it returns a list of all the enterprise's invoices.
 * 
 * @param enterprise
 * @param selection
 * @return
 * @throws PlatosysDBException 
 */
public static List<Invoice> getInvoices(Enterprise enterprise, Clerk clerk, Customer customer, int selection) throws PlatosysDBException{
	String[] cols={INVOICE_CUSTOMER_SYSNAME_COLNAME, INVOICE_STATUS_COLNAME};
	String cust= customer.getSysname();
	String[] open = {cust, OPEN};
	String[] paid = {cust, PAID};
	String[] pending = {cust,PENDING};
	String[] overdue = {cust, OVERDUE};
	String[] disputed = {cust, DISPUTED};
	
	switch (selection){
		case SELECTION_ALL:
			return getInvoices( getInvoicesTable(enterprise).getRows(INVOICE_CUSTOMER_SYSNAME_COLNAME,cust), enterprise, clerk);
		case  SELECTION_OPEN: 
		    return getInvoices( getInvoicesTable(enterprise).getRows(cols, open ), enterprise, clerk);
		case   SELECTION_PAID:
			return getInvoices( getInvoicesTable(enterprise).getRows(cols, paid), enterprise, clerk);
		case  SELECTION_PENDING:
			return getInvoices( getInvoicesTable(enterprise).getRows(cols, pending), enterprise, clerk);
		case  SELECTION_OVERDUE:
			return getInvoices( getInvoicesTable(enterprise).getRows(cols, overdue), enterprise, clerk);
		case  SELECTION_DISPUTED:
			return getInvoices( getInvoicesTable(enterprise).getRows(cols, disputed), enterprise, clerk);
		default:
			return getInvoices( getInvoicesTable(enterprise).getRows(INVOICE_CUSTOMER_SYSNAME_COLNAME,cust), enterprise, clerk);
	}
}
private static List<Invoice> getInvoices(List<Row> rows, Enterprise enterprise, Clerk clerk){
	List<Invoice> invoices=new ArrayList<Invoice>();
	Iterator<Row> rit = rows.iterator();
	try{
	while(rit.hasNext()){
		Row row = rit.next();
		Invoice invoice = openInvoice(enterprise, clerk,  row.getString(INVOICE_SYSNAME_COLNAME));
		invoices.add(invoice);
	}
	}catch(Exception e){logger.log("problem with the invoice list",e);}
	return invoices;
}

/**
 * Returns a list of invoices for a particular customer.
 * @param enterprise
 * @param customer
 * @param selection
 * @return
 */
public static List<Invoice> getInvoices(Enterprise enterprise, Customer customer, int selection){
	//TODO
	return null;
}


private Table getInvoicesTable() {
	return invoicesTable;
}


private long getNextInvoiceNumber()throws PlatosysDBException{
try {
		long sin= invoicesTable.addSerialRow();
		logger.log("invoice: returning new invoice number "+sin);
		return sin;
	} catch (ClassCastException e) {
		throw new PlatosysDBException("class cast exception, type mismatch in invoices table",e);
	}
}

private Account getInvoiceAccount(Enterprise enterprise, Customer customer, Clerk clerk, String sysname)throws BooxException, PermissionsException{
	   if(Account.exists(enterprise.getDatabaseName(), sysname)){
		   return Account.getAccount(enterprise, clerk, sysname);
	   }else{
	   String fullname = customer.getName()+new ISODate().toString();
	   		Account account=null;    
	   		try {
	        	account = Account.createAccount(enterprise, sysname, clerk, customer.getLedger(), enterprise.getDefaultCurrency(), fullname, true);
	        	
	        } catch (Exception e) {
				throw new BooxException("couldn't create new invoice account for "+systemInvoiceNumber, e);
	        }
	   		return account;
       }
}
private Account getInvoiceAccount(Enterprise enterprise, Clerk clerk, String sysname)throws BooxException, PermissionsException{
	    if(Account.exists(enterprise.getDatabaseName(), sysname)){
		   return Account.getAccount(enterprise, clerk, sysname);
	   }else{
		   throw new BooxException("Invoice "+sysname+" doesn't seem to have an account");
    }
}
private static SerialTable getInvoicesTable(Enterprise enterprise) throws PlatosysDBException{
	//check database is setup
		if(!JDBCTable.tableExists(enterprise.getDatabaseName(), INVOICE_TABLENAME)){
				//it's not, so we need to create the table:
				SerialTable invoicesTable;
				invoicesTable=JDBCSerialTable.createTable(enterprise.getDatabaseName(),INVOICE_TABLENAME, INVOICE_NUMBER_COLNAME);
				invoicesTable.addColumn(INVOICE_ACCOUNT_COLNAME, JDBCTable.TEXT_COLUMN);
				invoicesTable.addColumn(INVOICE_CUSTOMER_ACCOUNT_COLNAME, JDBCTable.TEXT_COLUMN);
				invoicesTable.addColumn(INVOICE_CUSTOMER_SYSNAME_COLNAME, JDBCTable.TEXT_COLUMN);
				invoicesTable.addColumn(INVOICE_SYSNAME_COLNAME, JDBCTable.TEXT_COLUMN);
				invoicesTable.addColumn(INVOICE_USERNO_COLNAME, JDBCTable.TEXT_COLUMN);
				invoicesTable.addColumn(INVOICE_CREATED_DATE_COLNAME, JDBCTable.TIMESTAMP_COLUMN);
				invoicesTable.addColumn(INVOICE_VALUE_DATE_COLNAME, JDBCTable.TIMESTAMP_COLUMN);
				invoicesTable.addColumn(INVOICE_RAISED_DATE_COLNAME, JDBCTable.TIMESTAMP_COLUMN);
				invoicesTable.addColumn(INVOICE_DUE_DATE_COLNAME, JDBCTable.TIMESTAMP_COLUMN);//="date_due";
				invoicesTable.addColumn(INVOICE_PAID_DATE_COLNAME, JDBCTable.TIMESTAMP_COLUMN);
				invoicesTable.addColumn(INVOICE_STATUS_COLNAME, JDBCTable.TEXT_COLUMN);
				invoicesTable.addColumn(INVOICE_DOCUMENT_FILENAME_COLNAME, JDBCTable.TEXT_COLUMN);
				invoicesTable.addColumn(INVOICE_NET_COLNAME, JDBCTable.NUMERIC_COLUMN);
				invoicesTable.addColumn(INVOICE_TAX_COLNAME, JDBCTable.NUMERIC_COLUMN);
				invoicesTable.addColumn(INVOICE_TOTAL_COLNAME, JDBCTable.NUMERIC_COLUMN);
				invoicesTable.addColumn(INVOICE_CURRENCY_COLNAME, JDBCTable.TEXT_COLUMN);
				return invoicesTable;
			  
			}else{
				//it is, so we just open the table
				return JDBCSerialTable.openTable(enterprise.getDatabaseName(), INVOICE_TABLENAME, INVOICE_NUMBER_COLNAME);
			}
}
private void setAccount(Account account) throws PlatosysDBException{
	   this.account=account;
	   invoicesTable.amend(systemInvoiceNumber, INVOICE_ACCOUNT_COLNAME, account.getName());
}
public Account getAccount(){
	return account;
}
private void setCustomer(Customer customer) throws PlatosysDBException {
	this.customer=customer;
	invoicesTable.amend(systemInvoiceNumber, INVOICE_CUSTOMER_ACCOUNT_COLNAME, customer.getAccount().getName());
	invoicesTable.amend(systemInvoiceNumber, INVOICE_CUSTOMER_SYSNAME_COLNAME, customer.getSysname());
   }
public Customer getCustomer() {
	return customer;
}
/**
 * @return the createdDate
 */
public ISODate getCreatedDate() {
	return createdDate;
}

/**
 * @param createdDate the createdDate to set
 * @throws PlatosysDBException 
 */
public void setCreatedDate(ISODate createdDate) throws PlatosysDBException {
	this.createdDate = createdDate;
	invoicesTable.amend(systemInvoiceNumber,  INVOICE_CREATED_DATE_COLNAME, createdDate.dateTime());
}

public ISODate getValueDate() {
	return valueDate;
}
public void setValueDate(ISODate valueDate) throws PlatosysDBException {
	this.valueDate = valueDate;
	invoicesTable.amend(systemInvoiceNumber,  INVOICE_VALUE_DATE_COLNAME, valueDate.dateTime());
}
public ISODate getDueDate() {
	return dueDate;
}
public void setDueDate(ISODate dueDate) throws PlatosysDBException {
	this.dueDate = dueDate;
	invoicesTable.amend(systemInvoiceNumber,  INVOICE_DUE_DATE_COLNAME, dueDate.dateTime());
}
public ISODate getRaisedDate() {
	return raisedDate;
}
private void setRaisedDate(ISODate raisedDate) throws PlatosysDBException{
	this.raisedDate=raisedDate;
	invoicesTable.amend(systemInvoiceNumber,  INVOICE_RAISED_DATE_COLNAME, raisedDate.dateTime());
}
private void setPaidDate(ISODate paidDate) throws PlatosysDBException {
	this.paidDate=paidDate;
	invoicesTable.amend(systemInvoiceNumber,  INVOICE_PAID_DATE_COLNAME, paidDate.dateTime());
}
public ISODate getPaidDate(){
	return paidDate;
}

public long getSystemInvoiceNumber() {
		// TODO Auto-generated method stub
		return systemInvoiceNumber;
	}
private void setSysname(String sysname) throws PlatosysDBException {
	   this.sysname=sysname;
	   invoicesTable.amend(systemInvoiceNumber, INVOICE_SYSNAME_COLNAME, sysname);
}
public String getSysname(){
	   return sysname;
}
/**
* @return the invoiceNumber
*/
public String getUserInvoiceNumber() {
	return userInvoiceNumber;
}
/**
 * @param invoiceNumber the invoiceNumber to set
 * @throws PlatosysDBException 
 */
public void setUserInvoiceNumber(String userInvoiceNumber) throws PlatosysDBException {
	this.userInvoiceNumber = userInvoiceNumber;
	invoicesTable.amend(systemInvoiceNumber, INVOICE_USERNO_COLNAME, userInvoiceNumber);
}

public Money getTotal() {
	return total;
}

private void setTotal(Money total) throws PlatosysDBException {
	this.total = total;
	invoicesTable.amend(systemInvoiceNumber, INVOICE_TOTAL_COLNAME, total.getAmount().doubleValue());
	totals.put("gross", total);
}

public Money getTax() {
	
	return tax;
}

private void setTax(Money tax) throws PlatosysDBException {
	invoicesTable.amend(systemInvoiceNumber, INVOICE_TAX_COLNAME, tax.getAmount().doubleValue());
	
	this.tax = tax;
	totals.put("tax", tax);
}

public Money getNet() {
	return net;
}

private void setNet(Money net) throws PlatosysDBException {
	this.net = net;
	invoicesTable.amend(systemInvoiceNumber, INVOICE_NET_COLNAME, net.getAmount().doubleValue());
	totals.put("net", net);
}

/**
 * @return the status
 */
public String getStatus() {
	return status;
}

/**
 * @param status the status to set
 * @throws PlatosysDBException 
 */
private void setStatus(String status) throws PlatosysDBException {
	this.status = status;
	invoicesTable.amend(systemInvoiceNumber, INVOICE_STATUS_COLNAME, status);
}

public Enterprise getEnterprise() {
	return enterprise;
}

public Currency getCurrency() {
	return currency;
}

private void setCurrency(Currency currency) throws PlatosysDBException  {
	this.currency = currency;
	invoicesTable.amend(systemInvoiceNumber,INVOICE_CURRENCY_COLNAME, currency.getTLA());
}
public InvoiceItem getInvoiceItemAt(int index)throws BooxException {
	Integer iNdex = new Integer(index);
	if (invoiceItems.containsKey(iNdex)){
		return invoiceItems.get(iNdex);
	}else throw new BooxException("can't find invoice item with index "+index+"in invoice"+getSysname());
}
public Money getBalance(Clerk clerk) throws PermissionsException{
	return account.getBalance(enterprise, clerk);
}

}
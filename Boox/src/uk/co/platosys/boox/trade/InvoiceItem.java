/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.trade;

import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.stock.Product;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.boox.core.exceptions.PermissionsException;

/**
 * <item lineNumber="n">
            <customerRef></customerRef>
            <catalogueID></catalogueID>
            <description></description>
            <comment></comment>
            <unitPrice></unitPrice>
            <quantity></quantity>
            <taxRate></taxRate>
            <value></value>
        </item>   
 * @author edward
 */
public class InvoiceItem extends TaxedTransaction {
	private String invoiceSysname=null;
    private String customerRef="0";
    private String catalogueID="0";
    private String description="0";
    private String comment="0";
    private Product product=null;
    private Money unitPrice=Money.ZERO;
    private int index=0;
    private float quantity=0;
    private double discount=0d;
    //private String lineNumber="0";
   
    private InvoiceItem(){}
    
    /**the untaxed version
     * @param clerk
     * @param journal
     * @param money
     * @param creditAccountName
     * @param debitAccountName
     * @param note*/
    private InvoiceItem (  
    		Enterprise enterprise,
            Clerk clerk,
            Money money,
            String creditAccountName,
            String debitAccountName,
            String note )
    throws PermissionsException{
        super(enterprise, clerk,money,creditAccountName,debitAccountName,note);  
        logger.log("II untaxed init done");
    } 
    
    /** the taxed version
     *  @param enterprise - the enterprise in question
     *  @param clerk - the clerk opening the item
     *  @param money - the amount of the item
     *  @param creditAccountName - the name of the account to be credited
     *  @param debitAccountName - the name of the account to be debited
     *  @param note - a free-form note
     *  @param inclusive - whether the amount is or is not inclusive
     *  @param taxBand the tax band, see TaxedTransaction for the possible values. 
     *  @throws PermissionsException */
    private InvoiceItem (   Enterprise enterprise,
                            Clerk clerk,
                            Money money,
                            String creditAccountName,
                            String debitAccountName,
                            String note,
                            boolean inclusive,
                            int taxBand,
                            int lineno)                            
    throws PermissionsException{
        super(enterprise, clerk,money,creditAccountName,debitAccountName,note,inclusive,false,taxBand, lineno);  
        this.index=lineno;
        logger.log("II init done");
    }
    
    /** <ul>
     * 		<li>**credit item, debit invoice
     * 		<li>**credit outputTax, debit invoice.
     * </ul>
     * @param enterprise
     * @param clerk
     * @param customer
     * @param product
     * @param quantity
     * @return */
    
    public static InvoiceItem getInvoiceItem (Enterprise enterprise, Clerk clerk, Invoice invoice, Product product, double quantity, int lineno) throws PermissionsException{
    	
    	Money price = product.getSellingPrice(invoice.getCustomer());
    	int taxBand = product.getTaxBand(invoice.getCustomer());
    	logger.log("II says taxband is "+taxBand);
    	Money amount = Money.multiply(price, quantity);
    	String creditAccountName = product.getAccount().getSysname();
    	String debitAccountName = invoice.getSysname();
    	logger.log("II says: credit "+creditAccountName+", debit "+debitAccountName);
    	InvoiceItem invoiceItem= new InvoiceItem(enterprise, clerk, amount, creditAccountName, debitAccountName, "", false, taxBand, lineno);
        invoiceItem.setProduct(product);
    	invoiceItem.setQuantity(quantity);
        invoiceItem.setUnitPrice(price);
        invoiceItem.setDescription(product.getName());
        invoiceItem.setIndex(lineno);//perhaps this should be done somewhere else?
        invoiceItem.setInvoiceSysname(invoice.getSysname());
        try {
			//invoice.addInvoiceItem(invoiceItem, lineno);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log("exception thrown", e);
		}
    	return invoiceItem;
    }
    
    protected static InvoiceItem getInvoiceItem(){
    	return new InvoiceItem();
    }
    
    public String getCustomerRef() {
        return customerRef;
    }

    public void setCustomerRef(String customerRef) {
        this.customerRef = customerRef;
    }

    public String getCatalogueID() {
        return catalogueID;
    }

    public void setCatalogueID(String catalogueID) {
        this.catalogueID = catalogueID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Money unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = (float) quantity;
    }
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
 
   
    public String getLineNumber(){
        return Integer.toString(index);
    }

	/**
	 * @return the product*/
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product the product to set*/
	public void setProduct(Product product) {
		this.product = product;
		this.description=product.getDescription();
		this.catalogueID=product.getSysname();
	}

	public String getInvoiceSysname() {
		return invoiceSysname;
	}

	public void setInvoiceSysname(String invoiceSysname) {
		this.invoiceSysname = invoiceSysname;
	}}
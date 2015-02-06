/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.trade;

import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.Boox;
import uk.co.platosys.boox.core.Chart;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Journal;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.Permission;
import uk.co.platosys.boox.money.CurrencyException;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.boox.core.Transaction;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.constants.Constants;
import uk.co.platosys.db.ColumnNotFoundException;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.RowNotFoundException;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.Logger;
/**
 * A TaxedTransaction wraps two Transactions into one.
 * 
 * 
 * We make use of lineno attribute here. Although each transaction -the tax and the value transaction - will have a separate TID, they
 * will share the same lineno value. The linenos are invoice-specific and allow an invoice to be recreated.
 * 
 * 
 * 
 * @author edward
 */
public class TaxedTransaction {
    private Money netMoney;
    private Money taxMoney;
    private Money total;
    private double taxRate;
   public  static final int HIGHER_BAND=8;
   public  static final int STANDARD_BAND=4;
   public  static final int LOW_BAND=2;
   public  static final int ZERO_BAND=0;
   public static final int UNTAXED_BAND=-1;
   public  static double HIGHER_RATE=20.0d;
   public  static double STANDARD_RATE=20.0d;
   public  static double LOW_RATE=5.0d;
   public  static double ZERO_RATE=0d;
    static String HIGHER_RATE_INPUT_TAX_ACCOUNT_NAME="Root:XBX:Current:Liabilities:Tax:VAT:InputVat#VATInputHigher";
    static String STANDARD_RATE_INPUT_TAX_ACCOUNT_NAME="Root:XBX:Current:Liabilities:Tax:VAT:InputVat#VATInputStandard";
    static String LOW_RATE_INPUT_TAX_ACCOUNT_NAME="Root:XBX:Current:Liabilities:Tax:VAT:InputVat#VATInputLower";
    static String ZERO_RATE_INPUT_TAX_ACCOUNT_NAME="Root:XBX:Current:Liabilities:Tax:VAT:InputVat#VATInputZero";
    static String HIGHER_RATE_OUTPUT_TAX_ACCOUNT_NAME="Root:XBX:Current:Liabilities:Tax:VAT:OutputVat#VATOutputHigher";
    static String STANDARD_RATE_OUTPUT_TAX_ACCOUNT_NAME="Root:XBX:Current:Liabilities:Tax:VAT:OutputVat#VATOutputStandard";
    static String LOW_RATE_OUTPUT_TAX_ACCOUNT_NAME="Root:XBX:Current:Liabilities:Tax:VAT:OutputVat#VATOutputLower";
    static String ZERO_RATE_OUTPUT_TAX_ACCOUNT_NAME="Root:XBX:Current:Liabilities:Tax:VAT:OutputVat#VATOutputZero";
    static String INPUT_TAX_LEDGER_NAME="Root:XBX:Current:Liabilities:Tax:VAT:InputVAT";
    static String OUTPUT_TAX_LEDGER_NAME="Root:XBX:Current:Liabilities:Tax:VAT:OutputVAT";
    
    private Transaction valueTransaction;
    private Transaction taxTransaction;
    private Clerk clerk;
    //private Journal journal;
    private String note;
    private String creditAccountName;
    private String debitAccountName;
    
    private Enterprise enterprise;
    private long valueID=0;
    private long taxTID=0;
    private boolean taxed=true;
    protected static Logger logger = Logger.getLogger("boox");
    private int taxBand=UNTAXED_BAND;
    int lineno;
    
    protected TaxedTransaction(){}
    
    /** This class is misnamed! 
     * This constructor is called when an untaxed line item is added.
     * @param clerk
     * @param journal
     * @param money
     * @param creditAccountName
     * @param debitAccountName
     * @param note
     */
    public TaxedTransaction(
                            Enterprise enterprise,
                            Clerk clerk,
                            Money money,
                            String creditAccountName,
                            String debitAccountName,
                            String note)
            throws PermissionsException{
        this.taxed=false;
        this.clerk=clerk;
        this.creditAccountName=creditAccountName;
        this.debitAccountName=debitAccountName;
        this.note=note;
        this.total=money;
        this.netMoney=money;
        this.taxMoney=Money.zero(money.getCurrency());
        this.valueTransaction=new Transaction(
        		    enterprise,
                    clerk,
                    getNetMoney(),
                    Account.getAccount(enterprise,creditAccountName, clerk, Permission.CREDIT),
                    Account.getAccount(enterprise,debitAccountName,clerk, Permission.DEBIT),
                    note
                    );
            
        
         }
    /**
     * This constructor is called to create a taxed transaction
     * @param enterprise 
     * @param clerk
     * @param money
     * @param creditAccountName
     * @param debitAccountName
     * @param note a freeform String note
     * @param inclusive whether the amount is inclusive or exclusive of tax
     * @param input whether this is an input for VAT purposes
     * @param taxBand an int representing the tax rate applicable to this transaction.
     * @throws PermissionsException
     */
    
    public TaxedTransaction(
                            Enterprise enterprise,
                            Clerk clerk,
                           Money money,
                            String creditAccountSysname,
                            String debitAccountSysname,
                            String note,
                            boolean inclusive,
                            boolean input,
                            int taxBand,
                            int lineno)
            throws PermissionsException
    {
        logger.log("TT init");
        String taxAccountName=null;
    //initialise fields
        this.clerk=clerk;
        //this.journal=enterprise.getJournal();
        this.creditAccountName=creditAccountSysname;
        this.debitAccountName=debitAccountSysname;
        this.lineno=lineno;
        this.note=note;
        this.taxBand=taxBand;
        switch(taxBand){
	        case HIGHER_BAND:taxRate=HIGHER_RATE;break;
	        case STANDARD_BAND:taxRate=STANDARD_RATE;break;
	        case LOW_BAND:taxRate=LOW_RATE;break;
	        case ZERO_BAND:taxRate=ZERO_RATE;break;
	        default: taxRate=STANDARD_RATE;	
        }
    //calculate the tax
        
        if(inclusive){
            this.total=money;
            Money[] monies=money.extractTax(taxRate);
            this.netMoney=monies[0];
            this.taxMoney=monies[1];
        }else{
            this.netMoney=money;
            this.taxMoney=money.fractionOf(taxRate);
            try{
                this.total=Money.add(netMoney, taxMoney);
            }catch(CurrencyException cx){
                logger.log("TT threw a currency exception ",cx);
            }
        }
        logger.log("TT tax done: net="+netMoney.toPrefixedString()+"; tax="+taxMoney.toPrefixedString()+"; gross="+total.toPrefixedString());
     //create the split transactions
        valueTransaction=new Transaction(
        		    enterprise,
                    clerk,
                    getNetMoney(),
                   creditAccountName,
                   debitAccountName,
                      note,
                      lineno
                    );
       if(input){
        	switch(taxBand){
	        case HIGHER_BAND:taxAccountName=HIGHER_RATE_INPUT_TAX_ACCOUNT_NAME;break;
	        case STANDARD_BAND:taxAccountName=STANDARD_RATE_INPUT_TAX_ACCOUNT_NAME;break;
	        case LOW_BAND:taxAccountName=LOW_RATE_INPUT_TAX_ACCOUNT_NAME;break;
	        case ZERO_BAND:taxAccountName=ZERO_RATE_INPUT_TAX_ACCOUNT_NAME;break;
	        default: taxAccountName=STANDARD_RATE_INPUT_TAX_ACCOUNT_NAME;
        }  
        	
            taxTransaction=new Transaction(
            		enterprise,
                    clerk,
                    getTaxMoney(),
                    creditAccountName, 
                    Account.getAccountSysname(enterprise, taxAccountName),
                    note,
                    lineno
                    
                    );
        }else{
        	switch(taxBand){
	        case HIGHER_BAND:taxAccountName=HIGHER_RATE_OUTPUT_TAX_ACCOUNT_NAME;break;
	        case STANDARD_BAND:taxAccountName=STANDARD_RATE_OUTPUT_TAX_ACCOUNT_NAME;break;
	        case LOW_BAND:taxAccountName=LOW_RATE_OUTPUT_TAX_ACCOUNT_NAME;break;
	        case ZERO_BAND:taxAccountName=ZERO_RATE_OUTPUT_TAX_ACCOUNT_NAME;break;
	        default: taxAccountName=STANDARD_RATE_OUTPUT_TAX_ACCOUNT_NAME;break;
        	}
        	taxTransaction=new Transaction(
            		enterprise,
                    clerk,
                    getTaxMoney(),
                   Account.getAccountSysname(enterprise, taxAccountName),
                    debitAccountName, 
                    note,
                    lineno
                    );
        
        }
    }

    /** Posts the value transaction only.
     * @return true if successful.*/
    private long postValue() throws PermissionsException {
        if(!valueTransaction.canPost()){throw new PermissionsException("TT can't post valtrans, doesn't have permission");}
        valueID=valueTransaction.post();
        if (taxed){
            taxTransaction.setNote("Tax on TID:"+Long.toString(valueID)+" "+valueTransaction.getNote());
        }
        //taxTransaction.post();
        if (valueID>0){
            return valueID;
        }else{
            logger.log(3, "TT problem posting value transaction");
            return valueID;
        }
    }
    
    /** Posts the tax transaction.
     * @return the tax transaction ID, or the value transaction ID if this is an untaxed
     * transaction.*/
    private long postTax() throws PermissionsException {
        if (taxed&&(valueID>0)){
        	taxTransaction.setTaxedTid(valueID);
            taxTID =  taxTransaction.post();
           // logger.log("TT posted tax transaction with ID"+taxTID);
            return taxTID;
        }else{
        	logger.log("TT doesn't seem to be taxed, returning id of value transaction only:"+valueID);
            return valueID;
        }
    }
    public long post() throws PermissionsException {
    	//logger.log("TT posting");
    	if(postValue()>0){
    		return postTax();
    	}else{
    		return -1;
    	}
    }
    public Money getNetMoney() {
    	if(netMoney!=null){
        return netMoney;
    	}else{
    		return Money.ZERO;
    	}
    }

    public Money getTaxMoney() {
    	if (taxMoney!=null){
    		return taxMoney;
    	}else{
    		return Money.ZERO;
    	}
    }
    public Money getTotal(){
        return total;
    }
    public String getNote(){
        return note;
    }

    public String getCreditAccountName() {
        return creditAccountName;
    }

    public String getDebitAccountName() {
        return debitAccountName;
    }
 
	public static int getStandardBand() {
		return STANDARD_BAND;
	}
	public int getTaxBand(){
		return taxBand;
	}
    public boolean reverse(Enterprise enterprise, Clerk clerk, String explanation) throws PermissionsException{
    	logger.log("TT-reverse called:"+explanation);
    	if(enterprise==null){throw new PermissionsException("TT-Reverse;null enterprise what-ho");}
    	Transaction reverseValueTransaction = valueTransaction.reverse(enterprise, explanation);
    	Transaction reverseTaxTransaction=taxTransaction.reverse(enterprise, explanation);
    	long rvid=0;
    	long rtid=0;
    	if (valueTransaction.isPosted()){
    		rvid = reverseValueTransaction.post();//>0){
    	}
    	if(taxTransaction.isPosted()){
    		rtid=reverseTaxTransaction.post();
    	}
    	return  ((rvid>0)&&(rtid>0));
    }
    public double getTaxRate(){
    	return taxRate;
    }
    protected void setValueTransaction(Transaction valueTransaction){
    	this.valueTransaction=valueTransaction;
    }
    protected void setTaxTransaction(Transaction taxTransaction){
    	this.taxTransaction=taxTransaction;
    }
    public static double getTaxRate(int taxBand){
    	switch (taxBand) {
		case TaxedTransaction.HIGHER_BAND:  return TaxedTransaction.HIGHER_RATE;
		case TaxedTransaction.STANDARD_BAND:return TaxedTransaction.STANDARD_RATE;
		case TaxedTransaction.LOW_BAND:return TaxedTransaction.LOW_RATE;
		case TaxedTransaction.ZERO_BAND:return TaxedTransaction.ZERO_RATE;
		case TaxedTransaction.UNTAXED_BAND:return TaxedTransaction.ZERO_RATE;
		default: return STANDARD_RATE; 
	}
    }
}

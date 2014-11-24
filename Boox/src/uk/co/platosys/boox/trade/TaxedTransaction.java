/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.trade;

import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.Boox;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Journal;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.Permission;
import uk.co.platosys.boox.money.CurrencyException;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.boox.core.Transaction;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.constants.Constants;
import uk.co.platosys.util.Logger;
/**
 * A TaxedTransaction wraps two Transactions into one.
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
    static String HIGHER_RATE_INPUT_TAX_ACCOUNT_NAME="VATInputHigher";
    static String STANDARD_RATE_INPUT_TAX_ACCOUNT_NAME="VATInputStandard";
    static String LOW_RATE_INPUT_TAX_ACCOUNT_NAME="VATInputLower";
    static String ZERO_RATE_INPUT_TAX_ACCOUNT_NAME="VATInputZero";
    static String HIGHER_RATE_OUTPUT_TAX_ACCOUNT_NAME="VATOutputHigher";
    static String STANDARD_RATE_OUTPUT_TAX_ACCOUNT_NAME="VATOutputStandard";
    static String LOW_RATE_OUTPUT_TAX_ACCOUNT_NAME="VATOutputLower";
    static String ZERO_RATE_OUTPUT_TAX_ACCOUNT_NAME="VATOutputZero";
    
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
    
    /**
     * This class is misnamed! 
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
                            //Journal journal,
                            Money money,
                            String creditAccountName,
                            String debitAccountName,
                            //String lineNumber,
                            String note
                           )
            throws PermissionsException
    {
        logger.log("TT untaxed init");
        this.taxed=false;
    //initialise fields
        this.clerk=clerk;
        //this.journal=enterprise.getJournal();
        this.creditAccountName=creditAccountName;
        this.debitAccountName=debitAccountName;
        //this.lineNumber=lineNumber;
        this.note=note;
    //calculate the tax
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
                            int taxBand)
            throws PermissionsException
    {
        logger.log("TT init");
        String taxAccountName=null;
    //initialise fields
        this.clerk=clerk;
        //this.journal=enterprise.getJournal();
        this.creditAccountName=creditAccountSysname;
        this.debitAccountName=debitAccountSysname;
        //this.lineNumber=lineNumber;
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
        logger.log("TT tax done");
     //create the split transactions
        valueTransaction=new Transaction(
        		    enterprise,
                    clerk,
                    getNetMoney(),
                   Account.getAccount(enterprise,creditAccountName, clerk, Permission.CREDIT),
                    Account.getAccount(enterprise,debitAccountName,clerk, Permission.DEBIT),
                      note
                    );
     
            
        if(input){
        	switch(taxBand){
	        case HIGHER_BAND:taxAccountName=HIGHER_RATE_INPUT_TAX_ACCOUNT_NAME;break;
	        case STANDARD_BAND:taxAccountName=STANDARD_RATE_INPUT_TAX_ACCOUNT_NAME;break;
	        case LOW_BAND:taxAccountName=LOW_RATE_INPUT_TAX_ACCOUNT_NAME;break;
	        case ZERO_BAND:taxAccountName=ZERO_RATE_INPUT_TAX_ACCOUNT_NAME;break;
	        default: taxAccountName=STANDARD_RATE_INPUT_TAX_ACCOUNT_NAME;
        }  
        	Ledger inputTaxLedger = Ledger.getLedger(enterprise, INPUT_TAX_LEDGER_NAME);
            taxTransaction=new Transaction(
            		enterprise,
                    clerk,
                    getTaxMoney(),
                    Account.getAccount(enterprise,creditAccountName, clerk, Permission.CREDIT),
                    Account.getAccount(enterprise,taxAccountName, inputTaxLedger, clerk, Permission.DEBIT),
                    note
                    );
        }else{
        	switch(taxBand){
	        case HIGHER_BAND:taxAccountName=HIGHER_RATE_OUTPUT_TAX_ACCOUNT_NAME;break;
	        case STANDARD_BAND:taxAccountName=STANDARD_RATE_OUTPUT_TAX_ACCOUNT_NAME;break;
	        case LOW_BAND:taxAccountName=LOW_RATE_OUTPUT_TAX_ACCOUNT_NAME;break;
	        case ZERO_BAND:taxAccountName=ZERO_RATE_OUTPUT_TAX_ACCOUNT_NAME;break;
	        default: taxAccountName=STANDARD_RATE_OUTPUT_TAX_ACCOUNT_NAME;break;
        	}
        	Ledger outputTaxLedger = Ledger.getLedger(enterprise, OUTPUT_TAX_LEDGER_NAME);
            
            taxTransaction=new Transaction(
            		enterprise,
                    clerk,
                    getTaxMoney(),
                    Account.getAccount(enterprise,taxAccountName,outputTaxLedger, clerk, Permission.CREDIT),
                    Account.getAccount(enterprise,debitAccountName, clerk, Permission.DEBIT),
                    note 
                    );
        
        }
    }

    /**
     * Posts the value transaction only.
     * @return true if successful.
     */
    public boolean postValue() throws PermissionsException {
        if(!valueTransaction.canPost()){return false;}
        valueID=valueTransaction.post();
        if (taxed){
            taxTransaction.setNote("Tax on TID:"+Long.toString(valueID)+" "+valueTransaction.getNote());
        }
        //taxTransaction.post();
        if (valueID>0){
            return true;
        }else{
            logger.log(3, "TT problem posting value transaction");
            return false;
        }
    }
    /**
     * Posts the tax transaction.
     * @return the tax transaction ID, or the value transaction ID if this is an untaxed
     * transaction.
     */
    public long postTax() throws PermissionsException {
        if (taxed&&(valueID>0)){
        	
            taxTID =  taxTransaction.post();
           // logger.log("TT posted tax transaction with ID"+taxTID);
            return taxTID;
        }else{
        	logger.log("TT doesn't seem to be taxed, returning id of value transaction only:"+valueID);
            return valueID;
        }
    }
    public long post() throws PermissionsException {
    	if(postValue()){
    		return postTax();
    	}else{
    		return -1;
    	}
    }
    public Money getNetMoney() {
        return netMoney;
    }

    public Money getTaxMoney() {
        return taxMoney;
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
}

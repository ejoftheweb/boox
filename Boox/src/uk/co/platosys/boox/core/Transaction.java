/*
 * Transaction.java
 * Copyright (C) 2008  Edward Barrow

     * 
    Copyright (C) 2008  Edward Barrow

    This class is free software; you can redistribute it and/or
    modify it under the terms of the GNU  General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this program; if not, write to the Free
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * 
 * for more information contact edward.barrow@platosys.co.uk
 
 * Transaction.java
 *
 * Created on 24 March 2007, 14:02
 *
 * 
 

 */

package uk.co.platosys.boox.core;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.CredentialsException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.money.Currency;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.db.jdbc.ConnectionSource;
import uk.co.platosys.util.ISODate;
import uk.co.platosys.util.Logger;

/**
 * A Transaction is the fundamental unit of processing in Boox.
 *
 * The basic accounting process is to create a Transaction, using the public constructor,  then post it
 * by calling its post() method.
 *
 * Until it's posted, a Transaction can be lost without a problem.
 * Once it's posted, it can't be undone, but you can always call its reverse() method which
 * returns another, opposite Transaction, which when posted will undo the effects
 * of the first.
 *
 *<h2>Transaction Granularity</h2>
 *<p>It is a matter of design how finely to record transactions.  Obviously every transaction
 *uses some resources - it is recorded as a row in three tables, the credit account, the debit 
 *account and the journal. 
 * 
 * <h2>Use of javax.sql</h2>
 * <p>Transaction is currently the only Boox class to use javax.sql directly; everything else uses
 * the platosysDB classes. At present, platosysDB does not support batch transactions on the DB; it is important
 * that transaction postings either succeed or fail together to retain database integrity</p>
 * 
 * @author edward
 */
public final class Transaction implements AuditElement {
    String creditAccountName;
    String debitAccountName;
    //Account creditAccount;
    //Account debitAccount;
    Money money;
    Currency currency;
    Enterprise enterprise;
    String databaseName;//=Boox.DATABASE_NAME;
    private String note;
    private long taxed_tid=0;
    private int line=0;
    private boolean isTax;
    boolean posted=false;
    boolean canPost=false;
    long transactionID;
    Date date;
   Date postingTime;
    String message="OK";
    public static final String DEFAULT_CURRENCY="GBP";
    Logger logger = Logger.getLogger("boox");
    Clerk clerk;
    Set<Clerk> approvals;
    Set<String> requiredApprovals;
    
    
    
 
   /**
     * This is the constructor for an unposted transaction.
     * you call its post() method to post it. 
     *
     * @param clerk
     * @param money
     * @param creditAccount
     * @param debitAccount
     * @param note
     **/
    @Deprecated
    public Transaction (Enterprise enterprise, Clerk clerk,  Money money, Account creditAccount, Account debitAccount, String note){
        this.clerk=clerk;
        this.enterprise=enterprise;
        this.databaseName=enterprise.getDatabaseName();
        this.money=money;
        //this.creditAccount=creditAccount;
        //this.debitAccount=debitAccount;

        this.creditAccountName=creditAccount.getSysname();
        this.debitAccountName=debitAccount.getSysname();
        //this.debitAccount=new Account(databaseName, debit, clerk);
        this.note=note;
        this.currency = money.getCurrency();
        canPost=checkPostable(enterprise);
        this.approvals=new HashSet<Clerk>();
        this.requiredApprovals=new HashSet<String>();
    }
    /**
     * This is the constructor for an unposted transaction.
     * you call its post() method to post it. 
     *
     * @param clerk
     * @param money
     * @param creditAccount
     * @param debitAccount
     * @param note
     **/
      public Transaction (Enterprise enterprise, Clerk clerk,  Money money, String creditAccountName, String debitAccountName, String note, int lineno){
        this.clerk=clerk;
        this.enterprise=enterprise;
        this.databaseName=enterprise.getDatabaseName();
        this.money=money;
       
        this.creditAccountName=creditAccountName;//.getSysname();
        this.debitAccountName=debitAccountName;//t.getSysname();
        this.note=note;
        this.currency = money.getCurrency();
        this.line=lineno;
        canPost=checkPostable(enterprise);
        this.approvals=new HashSet<Clerk>();
        this.requiredApprovals=new HashSet<String>();
    }
    

      
      
      
      
      
      
      public  void setTaxedTid(long taxed_tid){
    	 this.taxed_tid=taxed_tid;
    	 this.isTax=true;
     }
    /**
     * package-protected constructor recreates a transaction for auditing purposes.
     */
    protected Transaction (Clerk clerk, Enterprise enterprise, Money money, String credit, String debit, String note, Date postingTime, long transactionID){
    	this.clerk=clerk;
        this.enterprise=enterprise;
        this.money=money;
        this.creditAccountName=credit;
        //this.creditAccount = new Account(enterprise, creditAccountName, clerk);
        this.debitAccountName=debit;
        //this.debitAccount = new Account(enterprise, debitAccountName, clerk);
        this.note=note;
        this.currency = money.getCurrency();
        this.postingTime=postingTime;
        this.posted=true;
        this.transactionID=transactionID;
        canPost=false;
    }
    
    /**
     * 
     * @return the unique transaction id, or -1 if there has been a posting error.
     */
    public long post() throws PermissionsException {
        Connection connection = null;
        if ((!posted)&&(canPost)){
        try{
           transactionID=Journal.assignTransactionID(this);
            postingTime=new java.sql.Timestamp(new Date().getTime());
            //this logic dictates that if a transaction is in a private ledger, the 
            //contra account should show only the ledger, not the account. 
            //it isn't quite right, because the parent ledger of most accounts shares 
            //the sysname. So it is currently suspended while we think about 
            //better ways to implement the secrecy.
            String creditAccountContraName=creditAccountName;
            String debitAccountContraName=debitAccountName;
            
            /*
            if(creditAccount.getLedger().isPrivate()){
                creditAccountContraName=creditAccount.getLedger().getName();
            }else{
                creditAccountContraName=creditAccount.getSysname();
            }
            if(debitAccount.getLedger().isPrivate()){
                debitAccountContraName=debitAccount.getLedger().getName();
            }else{
                debitAccountContraName=debitAccount.getSysname();
            }*/
            
            //Create a SQL transaction;
            connection = ConnectionSource.getConnection(databaseName);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.addBatch("BEGIN");
            
            String amountString =  money.toPlainString();
            String creditAmountString = "-"+amountString;
            //the journal first
            statement.addBatch("UPDATE "+Journal.TABLENAME+" SET " +
	            Journal.CREDIT_COLNAME+" = \'"+creditAccountContraName+"\',"+
	            Journal.DEBIT_COLNAME+" = \'"+debitAccountContraName+"\',"+
	            Journal.AMOUNT_COLNAME+" = "+amountString+","+
	            Journal.CURRENCY_COLNAME+" = \'"+currency.getTLA()+"\',"+
	            Journal.DATE_COLNAME+" = \'"+postingTime.toString()+"\',"+
	            Journal.CLERK_COLNAME+" = \'"+clerk.getName()+"\',"+
	            Journal.NOTE_COLNAME+" = \'"+note+"\',"+
	            Journal.STATUS_COLNAME+" = true" +
	            " WHERE ("+Journal.TID_COLNAME+" = "+Long.toString(transactionID)+")");
           
            if(isTax){
	            //credit the credit account
	            //the balance line     
	            try{
	            	statement.addBatch("UPDATE "+creditAccountName+" SET amount = amount -"+amountString+", date = \'"+postingTime.toString()+"\' WHERE contra = \'balance\'");
	            }catch(Exception x){
	            	logger.log("problem in the batch posting", x);
	            }
	            
	            //the transaction line
	            statement.addBatch("INSERT INTO "+creditAccountName+"(transactionID, contra, amount, balance, date, clerk, notes, line, taxed_tid, is_tax) VALUES ("+Long.toString(transactionID)+",\'"+debitAccountContraName+"\',"+creditAmountString+",(SELECT amount FROM "+creditAccountName+" WHERE contra = \'balance\'),\'"+postingTime.toString()+"\',\'"+clerk.getName()+"\',\'"+note+"\',"+line+","+taxed_tid+","+isTax+")");
	            //debit the debit account
	            //the balance line
	            statement.addBatch("UPDATE "+debitAccountName+" SET amount = amount +"+amountString+", date = \'"+postingTime.toString()+"\' WHERE contra = \'balance\'");
	            //the transaction line
	            statement.addBatch("INSERT INTO "+debitAccountName+" (transactionID, contra, amount, balance, date, clerk, notes, line, taxed_tid, is_tax) VALUES ("+Long.toString(transactionID)+",\'"+creditAccountContraName+"\',"+amountString+",(SELECT amount FROM "+debitAccountName+" WHERE contra = \'balance\'),\'"+postingTime.toString()+"\',\'"+clerk.getName()+"\',\'"+note+"\',"+line+","+taxed_tid+","+isTax+")");
	            statement.addBatch("COMMIT");
	            
            }else{
            	//credit the credit account
	            //the balance line     
	            try{
	            	statement.addBatch("UPDATE "+creditAccountName+" SET amount = amount -"+amountString+", date = \'"+postingTime.toString()+"\' WHERE contra = \'balance\'");
	            }catch(Exception x){
	            	logger.log("problem in the batch posting", x);
	            }
	            
	            //the transaction line
	            statement.addBatch("INSERT INTO "+creditAccountName+"(transactionID, contra, amount, balance, date, clerk, notes, line) VALUES ("+Long.toString(transactionID)+",\'"+debitAccountContraName+"\',"+creditAmountString+",(SELECT amount FROM "+creditAccountName+" WHERE contra = \'balance\'),\'"+postingTime.toString()+"\',\'"+clerk.getName()+"\',\'"+note+"\',"+line+")");
	            //debit the debit account
	            //the balance line
	            statement.addBatch("UPDATE "+debitAccountName+" SET amount = amount +"+amountString+", date = \'"+postingTime.toString()+"\' WHERE contra = \'balance\'");
	            //the transaction line
	            statement.addBatch("INSERT INTO "+debitAccountName+" (transactionID, contra, amount, balance, date, clerk, notes, line) VALUES ("+Long.toString(transactionID)+",\'"+creditAccountContraName+"\',"+amountString+",(SELECT amount FROM "+debitAccountName+" WHERE contra = \'balance\'),\'"+postingTime.toString()+"\',\'"+clerk.getName()+"\',\'"+note+"\',"+line+")");
	            statement.addBatch("COMMIT");
	            
            }
            statement.executeBatch();
            statement.close();
            connection.close(); 
            
            posted=true;
            return transactionID;
        }catch (java.sql.BatchUpdateException bue){
            logger.log("Transaction Post: Batch update error", bue);
            Iterator<Throwable> bit =bue.iterator();
            int loopstop=0;
            while(bit.hasNext()&&loopstop<100){
                logger.log("\ncause: "+Integer.toString(loopstop), bue.getNextException());
                loopstop++;
            }
            try{connection.close();}catch (Exception p){}
            return -1;
        }catch(Exception e){
            message=e.getMessage();
           logger.log("Transaction posting error", e);
            try{connection.close();}catch (Exception p){}

            return -1;
        }
        }else{
           if(posted){message="transaction already posted";}
           if(!canPost){
               
               throw new PermissionsException("you are not authorised to post this transaction \n"+message);
           }
           logger.log("Transaction problem "+message);
            try{connection.close();}catch (Exception p){}
            
           return -1;
        }
    }
 
    /**
     * once posted, a Transaction cannot be undone; but its effects can be reversed by posting an equal and opposite transaction.
     * This method returns an unposted equal and opposite transaction, which can be posted by calling its post() method.
     *
     * @return an equal and opposite transaction with the note "Transaction No XX REVERSED"
     *
     */
    public Transaction reverse(Enterprise enterprise){
        if (isPosted()){
            return new Transaction (enterprise, clerk,  money, debitAccountName, creditAccountName, "Transaction No: "+Long.toString(transactionID)+ " REVERSED", line);
        }else{
            return null;
        }
    }
     /**
     * once posted, a Transaction cannot be undone; but its effects can be reversed by posting an equal and opposite transaction.
     * This method returns an unposted equal and opposite transaction, which can be posted by calling its post() method, and allows an
      * explanation for the reversal to be noted.
      *
     * @param explanation an explanation for the reversal.
     * @return an equal and opposite transaction with the note "Transaction No XX REVERSED - explanation: <i>explanation</i>"
     *
     */
    public Transaction reverse(Enterprise enterprise, String explanation){
        if (isPosted()){
            return new Transaction(enterprise, clerk,  money, debitAccountName,creditAccountName, "Transaction No: "+Long.toString(transactionID)+ "REVERSED - explanation: "+explanation, line);
        }else{
            return null;
        }
    
    }
    /**
     *@return true if the transaction has been posted
     */
    public boolean isPosted(){
        return posted;
    }
    /**
     *@return the date the transaction was posted.
     */    
    public Date getPostingTime(){
         return postingTime;
    
    }
    /**
     * 
     * 
     * @return the Money element of this Transaction.
     */
            
    public Money getMoney(){
        return money;
    }
    public Clerk getClerk(){
        return clerk;
    }
    
    public String getDebitAccountName(){
        return debitAccountName;
    }
    public String getCreditAccountName(){
        return creditAccountName;
    }
    public Currency getCurrency(){
        return currency;
    }
    protected void setPostingTime(Date postingTime){
        this.postingTime=postingTime;
    }
    public long getTransactionID(){
        return transactionID;
    }
    //we look at each of the Account objects and query whether they are postable.
    private boolean checkPostable(Enterprise enterprise){
    try {
    	Ledger ledger = Ledger.getAccountLedger(enterprise, debitAccountName);
        logger.log(4, "T checking debit ledger "+ledger.getName());
            //debug code
            if (enterprise==null){logger.log("TcP: null enterprise");}else{logger.log("enterprise:"+enterprise.getName());}
            if (ledger==null){logger.log("TcP: null ledger");}else{logger.log("ledger:"+ledger.getName());}
            if (clerk==null){logger.log("TcP: null clerk");}else{logger.log("clerk:"+clerk.getName());}
            //end debug code
            if (!clerk.canDebit(enterprise, ledger)){
                message=message+" debitAccount can't post: "+clerk.getName()+" does not have debit rights on "+ledger.getName();
                return false;
            }
            ledger = Ledger.getAccountLedger(enterprise, creditAccountName);
               logger.log(4, "T checking credit ledger "+ledger.getName());
            
            if (!clerk.canCredit(enterprise, ledger)){
                message=message+"creditAccount can't post: "+clerk.getName()+" does not have credit rights on "+ledger.getName();
                return false;
            }
            return true;
        }catch(Exception e){
            logger.log("TransactionCP - problem checking accounts", e);
            return false;
        }
        
        
    }
    public boolean canPost(){
        if (requiredApprovals.isEmpty()){
            return canPost; 
        }else{
            return false;
        }
        
    }

    public String getNote() {
        return note;
    }
    public boolean hasApproved(Clerk clerk){
        if((approvals!=null)&&(approvals.contains(clerk))){
            return true;
        }else{ 
            return false;
        }
    }
    public boolean approve(Clerk clerk, String password)throws CredentialsException{
        
        if ((approvals)==null){
            approvals= new HashSet<Clerk>();
        }
        if (requiredApprovals.contains(clerk.getName())){
            requiredApprovals.remove(clerk.getName());
        }
        if (!approvals.contains(clerk)){
            approvals.add(clerk);
            return true;
        }else{
            return false;
        }
     
    }
    public Set<Clerk> getApprovals(){
         return approvals;
    }
    public void addRequiredApproval(String clerkName){
        requiredApprovals.add(clerkName);
    }

    public void setNote(String note) {
        this.note = note;
    }
    public Money getBalance(Enterprise enterprise, Clerk clerk )throws PermissionsException{
        return money;
    }
    public String getName(){
        return Long.toString(transactionID);
    }

    
/*
    public Account getContraAccount(Enterprise enterprise, Account account, Clerk clerk) throws PermissionsException, BooxException{
        if(account.getName().equals(debitAccountName)){
            if(creditAccount.getLedger().isPrivate()){
                throw new PermissionsException("Credit account is in a private ledger");
            }else if(!clerk.canRead(enterprise, creditAccount.getLedger())){
                throw new PermissionsException("Credit account ledger" + (creditAccount.getLedger().getName()) +" is not readable by clerk "+clerk.getName());
            }else{
                return creditAccount;
            }
        }else if(account.getName().equals(creditAccountName)){
            if(debitAccount.getLedger().isPrivate()){
                throw new PermissionsException("Debit account is in a private ledger");
            }else if(!clerk.canRead(enterprise, debitAccount.getLedger())){
                throw new PermissionsException("Debit account ledger" + (debitAccount.getLedger().getName()) +"  is not readable by clerk "+clerk.getName());
            }else{
                return debitAccount;
            }
           
        }else{
            throw new BooxException(account.getName()+" is not of this Transaction "+transactionID);
        }
    }*/
    
    /*
     *use this to get contra account details for audit purposes/creating statements etc.
     * 
     */
   /* public String getContraAccountFullName(Account account, Clerk clerk) throws PermissionsException, BooxException {
        if(account.getName().equals(debitAccountName)){
            if(creditAccount.getLedger().isPrivate()){
                return "Private Ledger";
            }else{
                return creditAccount.getFullName();
            }
        }else if(account.getName().equals(creditAccountName)) {
            if(debitAccount.getLedger().isPrivate()){
                return "Private Ledger";
            }else{
                return debitAccount.getFullName();
            }

        }else{
            throw new BooxException(account.getName()+" is not of this Transaction "+transactionID);
        }
    }*/
     
     public static Transaction getTransaction(Enterprise enterprise, long tid){
    	 return Journal.getTransaction(enterprise, tid);
     }
	public Enterprise getEnterprise() {
		return enterprise;
	}
	@Override
	public ISODate getDate() {
		// TODO Auto-generated method stub
		return new ISODate(postingTime.getTime());
	}
}


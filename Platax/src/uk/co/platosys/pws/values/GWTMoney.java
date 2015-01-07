package uk.co.platosys.pws.values;

import java.io.Serializable;









import com.google.gwt.i18n.client.NumberFormat;

	public final class GWTMoney  implements Serializable, IsFieldValue<GWTMoney> {
	    /**
		 * 
		 */
		private static final long serialVersionUID = -4819616457277822696L;
		public static final String NUL = "NUL";
		private double amount=0;
	    private String currency=NUL;
	    private static final String PATTERN="#,##0.00";//be smarter localising this!
	    
	    
	  /**
	   * null constructor for GWT compliance. Call this constructor for a Zero instance.
	   */
	     public GWTMoney(){
	    	 
	     }
	    /**
	     * Creates a new Money object
	     * @param currency  the currency of this Money object
	     * @param amount a BigDecimal for the amount
	     */
	       public GWTMoney(String currency, double amount){
	        this.currency=currency;
	        this.amount=amount;
	    }
	   
	   
	  
	    /**
	     * Returns the amount of this money object as a BigDecimal
	     * @return the amount
	     */
	    public double getAmount() {
	        return amount;
	    }
	   
	    /**
	     * Returns the amount of this Money object as an unsigned String formatted for display
	     * This method doesn't show whether the result is a debit or a credit; it's useful
	     * for applications which (for example) show credits in red, or in parentheses -
	     * up to you to add those formatting features when rendering.
	     *
	     * @return a string
	     */
	    public String toUnsignedString(){
	    	double abs=0;
	        if (this.amount<0){
	        	abs=0-amount;
	        }else{
	        	abs=amount;
	        }
	   return Double.toString(abs);
	    }
	    /**
	     * Returns the amount of this Money object as an unsigned String formatted without grouping
	     * @return the amount as a plain string without symbol or grouping, preceded by - if a credit.
	     */
	    public String toPlainString(){
	    	double abs=0;
	        if (this.amount<0){
	        	abs=0-amount;
	        }else{
	        	abs=amount;
	        }
	       return Double.toString(abs);
	    }
	    /**
	     * Returns the amount of this Money object as an unsigned String formatted to the
	     * @return the amount as a plain string without symbol or grouping, preceded by - if a credit.
	     */
	    public String toFormattedString(){
	    	double abs=0;
	        if (this.amount<0){
	        	abs=0-amount;
	        }else{
	        	abs=amount;
	        }
	       
	       return NumberFormat.getFormat(PATTERN).format(abs);
	    }
	    /**
	     * Returns the amount of this Money as an unsigned String formatted for display, prefixed
	     * by the currency symbol.
	     *
	     * This method doesn't show whether the result is a debit or a credit; it's useful
	     * for applications which (for example) show credits in red, or in parentheses -
	     * up to you to add those formatting features when rendering.

	     * @return
	     */
	    public String toPrefixedString(){
	        return(this.currency+this.toUnsignedString());
	    }
	    /**
	     * Returns true if this Money is a credit - that is, the amount is a negative value.
	     */
	    public boolean credit(){
	        if (amount<0){
	            return true;
	        }else{
	            return false;
	        }
	    }
	    /**
	     * returns true if this Money is non-zero.
	     * @return
	     */
	    public boolean nonZero(){
	    	if (amount==0){return false;}
	    	else{return true;}
	    }
	   public String getCurrencyTLA(){
		   return currency;
	   }
	   /** Adds two Moneys together
	     *
	     * @param one
	     * @param two
	     * @return the sum of the two Moneys
	     * @throws Exception
	     */
	    public  GWTMoney add(GWTMoney gwtMoney ) throws Exception{
	    	if(this.getCurrencyTLA().equals(NUL)){currency=gwtMoney.getCurrencyTLA();}
	    	if(gwtMoney.getCurrencyTLA().equals(NUL)){return this;}
	    	if(((this.getAmount()!=0))&!(this.getCurrencyTLA().equals(gwtMoney.getCurrencyTLA()))){
	    		throw new Exception("mismatched GWT currencies: amount="+this.getAmount()+" , currencies="+this.getCurrencyTLA()+" vs "+gwtMoney.getCurrencyTLA());
	    	}
	        
	         return new  GWTMoney(gwtMoney.getCurrencyTLA(), this.getAmount()+gwtMoney.getAmount());
	    }
	    /**
	     *  Subtracts the second Money from the first;
	     * @param one
	     * @param two
	     * @return a new Money, the result of the subtraction
	     * @throws CurrencyException
	     */
	    public  GWTMoney subtract(GWTMoney sub) throws Exception{
	    	if(this.getCurrencyTLA().equals(NUL)){currency=sub.getCurrencyTLA();}
	    	
	         if((this.getAmount()!=0)&!(this.getCurrencyTLA().equals(sub.getCurrencyTLA()))){throw new Exception("mismatched GWT currencies");}//: "+one.getCurrency()+" vs. "+two.getCurrency());}
	         return new GWTMoney(sub.getCurrencyTLA(), this.getAmount()-sub.getAmount());
	    }
	    public GWTMoney times (double multiplier){
	    	return new GWTMoney(this.getCurrencyTLA(), (this.getAmount()*multiplier));
	    }
}


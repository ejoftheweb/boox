/*
 * Money.java
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
 
 * Money.java
 *
 * Created on 24 March 2007, 14:02
 *
 * 
 * 
 */

package uk.co.platosys.boox.money;

import java.io.Serializable;
import java.math.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import uk.co.platosys.boox.core.Boox;
import uk.co.platosys.util.Logger;


/**
 * Boox deals with Money. Money is a class that represents two things: an amount,
 * which is a number,  either positive (debit) or negative (credit), and a Currency.
 * The amount is held as a java.math.BigDecimal, to avoid rounding errors.
 *
 * The Money class provides a comprehensive set of arithmetic functions, which
 * should be used in preference to doing arithmetic on the amount.
 *
 * It also provides a limited set of formatting output methods which are basically
 * meant for use in more sophisticated formatting systems.
 *
 * @author edward
 */
public final class Money implements Serializable {
    public static final Money ZERO = Money.zero();
	Currency currency=Currency.DEFAULT_CURRENCY;
    static Currency CURRENCY=Currency.DEFAULT_CURRENCY;
    BigDecimal amount;
    public static MathContext MC = MathContext.DECIMAL128;
    public static NumberFormat NF = NumberFormat.getInstance();
    public static NumberFormat SQLF = NumberFormat.getInstance();
    
    private Logger logger = Logger.getLogger("boox");
    /**
     * Creates a new Money object
     * @param currency  the currency of this Money object
     * @param amount a BigDecimal for the amount
     */
    public Money (Currency currency, BigDecimal amount){
        
        this.currency=currency;
        this.amount=amount;
        //logger.log(5, "new Money created with currency "+this.currency.getTLA());
    }
    /**
     * You can always use an int instead of a BigDecimal. Money converts it
     * to a BigDecimal internally.
     * @param currency
     * @param amount
     */
    public Money (Currency currency, int amount){

        this.currency=currency;
        this.amount= new BigDecimal(amount);
        //logger.log(5, "new Money created with currency "+this.currency.getTLA());
    }
    /**
     * Or a double
     * @param currency
     * @param amount
     */
    public Money (Currency currency, double amount){

        this.currency=currency;
        this.amount= new BigDecimal(amount);
        //logger.log(5, "new Money created with currency "+this.currency.getTLA());
    }
      /**
     * Creates a new Money object using a String to describe the currency
     * @param currency a string describing the currency of this Money object
     * @param amount a BigDecimal for the amount
     */
    public Money(String currency, BigDecimal amount){
        this.currency=Currency.getCurrency(currency);
        this.amount=amount;
    }
    /**
     * Creates a new money object by trying to parse the String amount argument. 
     * It returns a zero if it can't parse it.
     */
    public Money(String currency, String amount){
        this.currency=Currency.getCurrency(currency);
        BigDecimal bg=new BigDecimal(0d);
        try{
            bg=new BigDecimal(Double.parseDouble(amount));
        }catch(Exception e){}
        this.amount=bg;
    }
    /**
     * this constructor uses a String for the currency and a double for the amount.
     * @param currency
     * @param amount
     */
    public Money (String currency, double amount){
        this.currency=Currency.getCurrency(currency);
        this.amount=new BigDecimal(amount);
    }
    /**
     * Returns the currency
     * @return the currency object
     */
    public Currency getCurrency() {
        return currency;
    }
    /**
     * Returns the amount of this money object as a BigDecimal
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }
    /**
    Discounts a money object
     * 
     * Note that these methods take percentages, not floats, as 
     * arguments. Some programmers will find this offensive (since a percentage
     * is just another way of writing a fraction) but hey, get used to it.
     * It really is easier not having to remember to multiply and divide by 100 every 
     * time. 
     * The original Money object is unchanged.
     *
     * @param percent the percentage by which to grow it
     * @return a new bigger money object
     */
    public Money growBy(double percent){
        BigDecimal rate = new BigDecimal(percent/100);
        BigDecimal increment = amount.multiply(rate, MC);
        BigDecimal newAmount=amount.add(increment, MC);
        return new Money(currency, newAmount);
    }
    /**
     * Discounts a money object
     *
     * Note that these methods take percentages, not floats, as
     * arguments. Some programmers will find this offensive (since a percentage
     * is just another way of writing a fraction) but hey, get used to it.
     * It really is easier not having to remember to multiply and divide by 100 every
     * time.
     * The original Money object is unchanged.
     *
     * @param percent the percentage by which to discount
     * 
     * @return a new, discounted money object
     */
    public Money discountBy (double percent){
        BigDecimal rate = new BigDecimal(percent/100);
        BigDecimal decrement = amount.multiply(rate, MC);
        BigDecimal newAmount=amount.subtract(decrement, MC);
        return new Money(currency, newAmount);
    }
    /**
     * returns a smaller money object n percent the size of the original
     *
    * Note that these methods take percentages, not floats, as
     * arguments. Some programmers will find this offensive (since a percentage
     * is just another way of writing a fraction) but hey, get used to it.
     * It really is easier not having to remember to multiply and divide by 100 every
     * time.
     * The original Money object is unchanged.
     * @param percent the percentage
     * @return the smaller money
     */
    public Money fractionOf (double percent){
        BigDecimal rate = new BigDecimal(percent/100);
        BigDecimal newAmount = amount.multiply(rate, MC);
        return new Money(currency, newAmount);
    }
    /**
     * This method splits a Money that includes tax into two separate objects (returned as an array) - the first
     * is the net amount, the second being the amount of tax.
     *
     * Note that these methods take percentages, not floats, as 
     * arguments. Some programmers will find this offensive (since a percentage
     * is just another way of writing a fraction) but hey, get used to it.
     * It really is easier not having to remember to multiply and divide by 100 every 
     * time. 
     * The original Money object is unchanged.
     *
     *@param percent the tax rate
     *@return an array of Money objects
     */
    public Money[] extractTax (double percent){
        BigDecimal rate = new BigDecimal(1+(percent/100));
        BigDecimal netAmount=amount.divide(rate, MC);
        BigDecimal taxAmount=amount.subtract(netAmount, MC);
        Money[] returnArray=new Money[2];
        returnArray[0]=new Money(currency, netAmount);
        returnArray[1]=new Money (currency, taxAmount);
        return returnArray;
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
        BigDecimal abs=amount.abs();
        DecimalFormat format= new DecimalFormat(currency.getFormat());
        return format.format(abs);
    }
    /**
     * Returns the amount of this Money object as an unsigned String formatted without grouping
     * @return the amount as a plain string without symbol or grouping, preceded by - if a credit.
     */
    public String toPlainString(){
        BigDecimal abs=amount.abs();
        SQLF.setGroupingUsed(false);
        return SQLF.format(abs);
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
        return(currency.getSymbol()+toUnsignedString());
    }
    /**
     * Returns true if this Money is a credit - that is, the amount is a negative value.
     */
    public boolean credit(){
        if (amount.signum()==-1){
            return true;
        }else{
            return false;
        }
    }
    /**
     * Compares this Money with the Money argument.
     *
     * Note that a larger credit amount is less than a smaller credit amount,
     * because credits are negative.
     *
     * @param comparator
     * @return
     */
    public boolean lessThan(Money comparator){
        double thisval = amount.doubleValue();
        double thatval = comparator.getAmount().doubleValue();
        if (thisval<thatval){ return true;}
        else{return false;}
    }
    /**
     * Compares this Money with the Money argument.
     *
     * Note that a smaller credit amount is more than a larger credit amount,
     * because credits are negative.
     *
     * @param comparator
     * @return
     */
     public boolean moreThan(Money comparator){
        double thisval = amount.doubleValue();
        double thatval = comparator.getAmount().doubleValue();
        if (thisval>thatval){ return true;}
        else{return false;}
    }
     /**
      * Two Moneys are equal if their amounts and currencies are the same.
      *
      * @param money
      * @return
      */
    public boolean equals(Money money){
        if((money.getCurrency().getTLA().equals(currency.getTLA()))&&
                (money.getAmount().compareTo(amount)==0)){
            return true;
        }else{
            return false;
        }
    }
    /**
      *  Returns a grown money object
     *
     * Note that these methods take percentages, not floats, as
     * arguments. Some programmers will find this offensive (since a percentage
     * is just another way of writing a fraction) but hey, get used to it.
     * It really is easier not having to remember to multiply and divide by 100 every
     * time.
     * The original Money object is unchanged.
     * @param money
     * @param percent
     * @return
     */
     public static Money growBy(Money money, double percent){
        BigDecimal rate = new BigDecimal(percent/100);
        BigDecimal increment = money.getAmount().multiply(rate, MC);
        BigDecimal newAmount=money.getAmount().add(increment, MC);
        return new Money(money.getCurrency(), newAmount);
    }
     /**
      *  Discounts a money object
     *
     * Note that these methods take percentages, not floats, as
     * arguments. Some programmers will find this offensive (since a percentage
     * is just another way of writing a fraction) but hey, get used to it.
     * It really is easier not having to remember to multiply and divide by 100 every
     * time.
     * The original Money object is unchanged.
      * @param money
      * @param percent
      * @return
      */
    public static Money discountBy (Money money, double percent){
        BigDecimal rate = new BigDecimal(percent/100);
        BigDecimal decrement = money.getAmount().multiply(rate, MC);
        BigDecimal newAmount=money.getAmount().subtract(decrement, MC);
        return new Money(money.getCurrency(), newAmount);
    }
    /**
     *  returns a smaller money object n percent the size of the original
     *
    * Note that these methods take percentages, not floats, as
     * arguments. Some programmers will find this offensive (since a percentage
     * is just another way of writing a fraction) but hey, get used to it.
     * It really is easier not having to remember to multiply and divide by 100 every
     * time.
     * The original Money object is unchanged.
     * @param money
     * @param percent
     * @return
     */
    public static Money fractionOf (Money money, double percent){
        BigDecimal rate = new BigDecimal(percent/100);
        BigDecimal newAmount = money.getAmount().multiply(rate, MC);
        return new Money(money.getCurrency(), newAmount);
    }
    /**
     * This method splits a Money that includes tax into two separate objects (returned as an array) - the first
     * is the net amount, the second being the amount of tax.
     *
     * Note that these methods take percentages, not floats, as
     * arguments. Some programmers will find this offensive (since a percentage
     * is just another way of writing a fraction) but hey, get used to it.
     * It really is easier not having to remember to multiply and divide by 100 every
     * time.
     * The original Money object is unchanged.
     * @param money
     * @param percent
     * @return
     */
    public static Money[] extractTax (Money money, double percent){
        BigDecimal rate = new BigDecimal(1+(percent/100));
        BigDecimal netAmount=money.getAmount().divide(rate, MC);
        BigDecimal taxAmount=money.getAmount().subtract(netAmount, MC);
        Money[] returnArray=new Money[2];
        returnArray[0]=new Money(money.getCurrency(), netAmount);
        returnArray[1]=new Money (money.getCurrency(), taxAmount);
        return returnArray;
    }
    /**
     * Returns a Money which is zero, in the given currency.
     *
     * @param currency
     * @return
     */
    public static Money zero(Currency currency){
        return new Money(currency, BigDecimal.ZERO);
    }
    public static Money zero(){
    	return new Money(CURRENCY,BigDecimal.ZERO );
    }
    /**
     * You can only set the Currency of a Zero money object. Use this method when you need to
     * instantiate it before you know what currency it will represent.  
     * @param currency
     * @return
     * @throws CurrencyException
     */
    public Money setCurrency(Currency currency) throws CurrencyException{
    	if (!(this.amount).equals(BigDecimal.ZERO)){
    		throw new CurrencyException("Money cannot set currency on non-zero amount");
    	}else{
    		this.currency=currency;
    		return this;
    	}
    }
    public Money debit(Money sum) throws CurrencyException {
        //logger.log("Money-debit: sum currency is:"+sum.getCurrency().getTLA());
        if(!sum.getCurrency().getTLA().equals(currency.getTLA())){throw new CurrencyException("mismatched currencies: "+sum.getCurrency().getTLA()+" vs. "+currency.getTLA());}
        amount=amount.add(sum.getAmount(), MC);
        return this;
    }
    protected Money credit(Money sum) throws CurrencyException {
         if(!sum.getCurrency().getTLA().equals(currency.getTLA())){throw new CurrencyException("mismatched currencies: "+sum.getCurrency().getTLA()+" vs. "+currency.getTLA());}
        
        amount=amount.subtract(sum.getAmount(), MC);
        return this;
    }
    protected void combine(Money sum) throws CurrencyException{
         if(!sum.getCurrency().getTLA().equals(currency.getTLA())){throw new CurrencyException("mismatched currencies: "+sum.getCurrency().getTLA()+" vs. "+currency.getTLA());}
        
        if (sum.credit()){credit(sum);}
        else{debit(sum);}
    }
    /** Adds two Moneys together
     *
     * @param one
     * @param two
     * @return the sum of the two Moneys
     * @throws CurrencyException
     */
    public static Money add(Money one, Money two) throws CurrencyException{
        if(!one.getCurrency().equals(two.getCurrency())){throw new CurrencyException("mismatched currencies: "+one.getCurrency().getTLA()+" vs. "+two.getCurrency().getTLA());}
        
         return new  Money(one.getCurrency(), (one.getAmount().add(two.getAmount())));
    }
    /**
     *  Subtracts the second Money from the first;
     * @param one
     * @param two
     * @return a new Money, the result of the subtraction
     * @throws CurrencyException
     */
    public static Money subtract(Money one, Money two) throws CurrencyException{
         if(!one.getCurrency().equals(two.getCurrency())){throw new CurrencyException("mismatched currencies: "+one.getCurrency().getTLA()+" vs. "+two.getCurrency().getTLA());}
         return new Money(one.getCurrency(), (one.getAmount().subtract(two.getAmount())));
    }
        /**
     *  Subtracts the argument  from the value of this Money;
     * @param one
     * @param two
     * @return a new Money, the result of the subtraction
     * @throws CurrencyException
     */
    public Money subtract(Money two) throws CurrencyException{
         if(!this.getCurrency().equals(two.getCurrency())){throw new CurrencyException("mismatched currencies: "+this.getCurrency().getTLA()+" vs. "+two.getCurrency().getTLA());}
         return new Money(this.getCurrency(), (this.getAmount().subtract(two.getAmount())));
    }
    /**
     *  Multiplies the Money argument by the float argument.
     * @param money
     * @param quantity
     * @return a new Money
     */
    public static Money multiply(Money money, float quantity){
        BigDecimal amount = money.getAmount();
        amount=amount.multiply(new BigDecimal(quantity));
        return new Money(money.getCurrency(),amount);
    }
    
    /**Divides the Money argument by the float divisor argument.
     * @param money
     * @param quantity
     * @return a new Money*/
    public static Money divide(Money money, float divisor){
        BigDecimal amount = money.getAmount();
        amount=amount.divide(new BigDecimal(divisor));
        return new Money(money.getCurrency(),amount);
    }
   
    /**Divides one Money  by another; returns a BigDecimal.
     * @param dividend
     * @param divisor
     * @return a BigDecimal quotient*/
    public static BigDecimal divide(Money dividend, Money divisor) throws CurrencyException{
    	if(!dividend.getCurrency().equals(divisor.getCurrency())){throw new CurrencyException("mismatched currencies: "+dividend.getCurrency().getTLA()+" vs. "+divisor.getCurrency().getTLA());}
    	BigDecimal amount = dividend.getAmount();
        BigDecimal ddivisor = divisor.getAmount();
        return amount.divide(ddivisor);
    }
}

/*
 * Copyright Edward Barrow and Platosys.
 * This software is licensed under the Free Software Foundation's
General Public Licence, version 2 ("the GPL").
The full terms of the licence can be found online at http://www.fsf.org/

In brief, you are free to copy and to modify the code in any way you wish, but if you
publish the modified code you may only do so under the GPL, and (if asked) you must
 supply a copy of the source code alongside any compiled code.

Platosys software can also be licensed on negotiated terms if the GPL is inappropriate.
For further information about this, please contact software.licensing@platosys.co.uk
 */

package uk.co.platosys.boox.core;

import java.util.Date;


import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.core.exceptions.TimingException;
import uk.co.platosys.boox.money.Currency;
import uk.co.platosys.boox.money.CurrencyException;
import uk.co.platosys.boox.money.Money;

/**
 * 
 * Boox' budgeting is very simple. A budget is a target money value set for a given date, generally but not necessarily the year end.  
 * The getBudget method returns the budget for the given date, provided that the given date is before the last date for which a budget has been set.
 * Otherwise implementations should throw a TimingException. 
 * Implementations calculate the budget to return using an interpolation mode, at present only Linear and Compound are supported. 
 * 
 * 
 * Linear mode: Bt = t(Bf-Bs)/(f-s) where f, s are finishing and start times, t is the time for which the budget is to be determined.
 * 
 * Compound mode: 
 *
 * @author edward
 *
 * Objects
 *
 */
public interface Budgetable {
  public static final int LINEAR_INTERPOLATION_MODE = 2;
  public static final int COMPOUND_INTERPOLATION_MODE=4;
  
  public Money getBudget(Clerk clerk, Date date) throws PermissionsException, TimingException;
  public void setBudget(Clerk clerk, Money money, Date date)throws PermissionsException, CurrencyException, TimingException;
  public void setInterpolationMode(Clerk clerk, int interpolationMode) throws PermissionsException, BooxException;
  public int getInterpolationMode();
  
  public Money getVariance(Clerk clerk, Date date) throws PermissionsException, TimingException;
 

 
  public Currency getCurrency();
  public String getName();
}

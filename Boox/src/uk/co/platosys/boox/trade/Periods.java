/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.trade;

import java.util.Calendar;


/**
 * Periods provides some handy static methods for accounting periods.
 * 
 * 
 * <h2>Accounting Periods</h2>
 * Books deals with accounting periods as follows.
 * 
 * 
 * 
 * 
 * 
 * @author edward
 */
public class Periods {
    public static PeriodPoint END_DECEMBER=new PeriodPoint(2000,0,0,0,0);
    public static PeriodPoint END_JANUARY=new PeriodPoint(2000,1,0,0,0);
    public static PeriodPoint END_FEBRUARY=new PeriodPoint(2000,2,0,0,0);
    public static PeriodPoint END_MARCH=new PeriodPoint(2000,3,0,0,0);
    public static PeriodPoint END_APRIL=new PeriodPoint(2000,4,0,0,0);
    public static PeriodPoint END_MAY=new PeriodPoint(2000,5,0,0,0);
    public static PeriodPoint END_JUNE=new PeriodPoint(2000,6,0,0,0);
    public static PeriodPoint END_JULY=new PeriodPoint(2000,7,0,0,0);
    public static PeriodPoint END_AUGUST=new PeriodPoint(2000,8,0,0,0);
    public static PeriodPoint END_SEPTEMBER=new PeriodPoint(2000,9,0,0,0);
    public static PeriodPoint END_OCTOBER=new PeriodPoint(2000,10,0,0,0);
    public static PeriodPoint END_NOVEMBER=new PeriodPoint(2000,11,0,0,0);
            
    public static PeriodPoint START_JANUARY=END_DECEMBER;
    public static PeriodPoint START_FEBRUARY=END_JANUARY;
    public static PeriodPoint START_MARCH=END_FEBRUARY;
    public static PeriodPoint START_APRIL=END_MARCH;
    public static PeriodPoint START_MAY=END_APRIL;
    public static PeriodPoint START_JUNE=END_MAY;
    public static PeriodPoint START_JULY=END_JUNE;
    public static PeriodPoint START_AUGUST=END_JULY;
    public static PeriodPoint START_SEPTEMBER=END_AUGUST;
    public static PeriodPoint START_OCTOBER=END_SEPTEMBER;
    public static PeriodPoint START_NOVEMBER=END_OCTOBER;
    public static PeriodPoint START_DECEMBER=END_NOVEMBER;
    
    public static int YEAR=365;
    public static int QUARTER=91;
    public static int MONTH=31;
    public static int FOURWEEK=28;
    public static int FORTNIGHT=14;
    public static int WEEK=7;
   
    public static PeriodPoint getCurrentYearEnd(PeriodPoint accountingDate){
        PeriodPoint now = new PeriodPoint();
        PeriodPoint yearEnd = new PeriodPoint(
                now.get(Calendar.YEAR), 
                accountingDate.get(Calendar.MONTH),
                accountingDate.get(Calendar.DAY_OF_MONTH),
                accountingDate.get(Calendar.HOUR_OF_DAY),
                accountingDate.get(Calendar.MINUTE),
                accountingDate.get(Calendar.SECOND));
      if (yearEnd.before(now)){
          yearEnd.set(Calendar.YEAR, (yearEnd.get(Calendar.YEAR)+1));
      }          
        return yearEnd;
    }
    public static PeriodPoint getPreviousYearEnd(PeriodPoint accountingDate){
        PeriodPoint yearEnd = getCurrentYearEnd(accountingDate);
        yearEnd.set(Calendar.YEAR, (yearEnd.get(Calendar.YEAR)-1));
        return yearEnd;
    }
    public static PeriodPoint getCurrentPeriodEnd(PeriodPoint accountingDate, int period)throws PeriodException{
       PeriodPoint now = new PeriodPoint();

      if (period==YEAR){
          return getCurrentYearEnd(accountingDate);
      }else if (period==QUARTER){
          PeriodPoint periodEnd = getCurrentYearEnd(accountingDate);
          now.roll(Calendar.MONTH, 3);
          while(periodEnd.getTimeInMillis() > now.getTimeInMillis()){
              periodEnd.roll(Calendar.MONTH, -3);
          }
          return periodEnd;
      }else if (period==MONTH){
          PeriodPoint periodEnd = getCurrentYearEnd(accountingDate);
          now.roll(Calendar.MONTH, 1);
          while(periodEnd.getTimeInMillis() > now.getTimeInMillis()){
              periodEnd.roll(Calendar.MONTH, -1);
          }
          return periodEnd;
      }else if (period==FOURWEEK){
          PeriodPoint periodEnd = getCurrentYearEnd(accountingDate);
          now.roll(Calendar.WEEK_OF_YEAR, 4);
          while(periodEnd.getTimeInMillis() > now.getTimeInMillis()){
              periodEnd.roll(Calendar.WEEK_OF_YEAR, -4);
          }
          return periodEnd;
      }else if (period==FORTNIGHT){
          PeriodPoint periodEnd = getCurrentYearEnd(accountingDate);
          now.roll(Calendar.WEEK_OF_YEAR, 2);
          while(periodEnd.getTimeInMillis() > now.getTimeInMillis()){
              periodEnd.roll(Calendar.WEEK_OF_YEAR, -2);
          }
          return periodEnd;
      }else if (period==WEEK){
          PeriodPoint periodEnd = getCurrentYearEnd(accountingDate);
          now.roll(Calendar.WEEK_OF_YEAR, 1);
          while(periodEnd.getTimeInMillis() > now.getTimeInMillis()){
              periodEnd.roll(Calendar.WEEK_OF_YEAR, -1);
          }
          return periodEnd;
      }else{
           throw new PeriodException("Period not supported");
      }
    }
    public static Calendar getPreviousPeriodEnd(Calendar accountingDate, int period){
        return new PeriodPoint();
    }
}

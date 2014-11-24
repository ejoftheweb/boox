/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.trade;

import java.util.GregorianCalendar;
import uk.co.platosys.util.ISODate;

/**
 *
 * @author edward
 */
public class PeriodPoint extends GregorianCalendar {
     public PeriodPoint(){
         super();
     }
     public PeriodPoint(int year, int month, int day, int hour, int min, int sec){
         super(year, month, day, hour, min, sec);
     }
     public PeriodPoint(int year, int month, int day_of_month, int hour, int min){
         super(year, month, day_of_month, hour, min);
     }
     public ISODate getISODate(){
         return new ISODate(getTimeInMillis());
     }
}

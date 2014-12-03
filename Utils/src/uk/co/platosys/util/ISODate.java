/*
 * Created on Mar 22, 2005
 *
 * 
 */
package uk.co.platosys.util;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.Serializable;
import java.util.Calendar;



/**
 * @author edward
 * It's a curious omission of java.util.Date and java.util.Calendar
 * that there's no easy way of getting an ISO 8601 compliant date
 * string out of them. A victim perhaps of the struggle for political
 * correctness, they are resolutely locale-independent forms.
 * 
 * Anyway, this class provides a handful of methods to return
 * Strings representing the current date as an ISO 8601 string.
 * 
 * ISO 8601 is a YYYY-MM-DD hh:mm:ss.mmmm notation.
 * 
 * Note this is NOT intended for UI use, Java has other classes - specifically java.util.Calendar -
 * that make a much better job of presenting the date in a user friendly
 * format. This produces a system-independent, readily machine-parseable
 * string that's also easily, if not necessarily comfortably, human-readable. So we don't use alternative calendars - Gregorian only - or time
 * zones - UTC or local only. (this depends on the underlying time source,
 * which *should* be set to UTC+-locale-specific offset, but may be set to local time).
 *   
 * Example: On 4th July 2005 at 3.15pm exactly, 
 * System.out.println(new ISODate().dateString())
 * will print 2005-04-07;
 * 
 * The class also has a load of other utility methods for getting dates day/week/month/year ago. 
 * 
 */
public class ISODate extends Date implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1107010777786265880L;
	SimpleDateFormat simpleDateFormat;
	//Logger logger = new Logger("platosys", false);
        public static final long ONE_DAY=(24*60*60*1000);
        public static final long DAY_AGO=-ONE_DAY;
        public static final long WEEK_AGO=-(7*ONE_DAY);
        public static final long MONTH_AGO=-(30*ONE_DAY);
        public static final long YEAR_AGO=-(365*ONE_DAY);
        public static final long MONTH_AHEAD=(30*ONE_DAY);
	public ISODate(){
		super();
		simpleDateFormat = new SimpleDateFormat();
	}
        public ISODate(Long time){
            super(time.longValue());
            //logger.log(Long.toString(time));
            //logger.log(Long.toString(this.getTime()));
            simpleDateFormat=new SimpleDateFormat();
            //logger.log(this.dateTimeMs());
        }
        public ISODate(int daysOffset){
            super();
            simpleDateFormat = new SimpleDateFormat();
            long time = getTime();
            time=time+daysOffset*86400000;
            this.setTime(time);
        }
        /**
         * Constructor to return an ISODate from three String inputs, e.g. a date entered on a form using drop-down boxes
         * nb this expects the name of the month in full.
         */
        public ISODate(String year, String month, String day)throws ParseException{
           super();
           simpleDateFormat=new SimpleDateFormat();
           String dateString=year+"-"+month+"-"+day;
           simpleDateFormat.applyPattern("yyyy-MMMM-dd");
           Date date = simpleDateFormat.parse(dateString);
           this.setTime(date.getTime());
        }
	        /**
         * Constructor to return an ISODate from two Strings and an int(for the month as a number), e.g. a date entered on a form using drop-down boxes
         * nb this expects the month as an int.
         */
        public ISODate(String year, int month, String day)throws ParseException{
           super();
           simpleDateFormat=new SimpleDateFormat();
           String dateString=year+"-"+Integer.toString(month)+"-"+day;
           simpleDateFormat.applyPattern("yyyy-MM-dd");
           Date date = simpleDateFormat.parse(dateString);
           this.setTime(date.getTime());
        }
        /**
         * takes a millisecond-precise ISO-formatted string and turns it into an ISODate object. (use to recreate ISODates from
         * stored Strings, eg a date attribute in an xml file)
         * @param ISOString
         */
        public ISODate(String ISOString) throws java.text.ParseException {
            super();
            simpleDateFormat= new SimpleDateFormat();
            set(ISOString);
        }
	/**
	 * Returns a string representation of the underlying Date object
	 * in the ISO 8601 standardised form
	 * as date only (day precision) 
	 */
	public String dateString(){
		simpleDateFormat.applyPattern("yyyy-MM-dd");
		return simpleDateFormat.format(this);
	}
        /**
         * Returns a string representing the year only of the underlying Date object.
         * @return
         */
        public String year(){
            simpleDateFormat.applyPattern("yyyy");
            return simpleDateFormat.format(this);
        }
	/**
	 * Returns a string representation of the underlying Date object
	 * in the ISO 8601 standardised form
	 * as time only (second precision) 
	 */
	public String timeString(){
		simpleDateFormat.applyPattern("HH:mm:ss");
		return simpleDateFormat.format(this);
	}
	/**
	 * Returns a string representation of the underlying Date object
	 * in the ISO 8601 standardised form
	 * as date and time  (second precision, ignoring milliseconds) 
	 */
	public String dateTime(){
		simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(this);
	}
	/**
	 * Returns a string representation of the underlying Date object
	 * in the ISO 8601 standardised form
	 * as  time only  with millisecond precision
	 * 
	 * Note: subject to the accuracy of the underlying system clock! 
	 */
	public String timeMsString(){
		simpleDateFormat.applyPattern("HH:mm:ss.SSS");
		return simpleDateFormat.format(this);
	}
	
	/**
	 * Returns a string representation of the underlying Date object
	 * in the ISO 8601 standardised form
	 * as date and  time  with millisecond precision
	 * 
	 * Note: subject to the accuracy of the underlying system clock! 
	 */
	public String dateTimeMs(){
		simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss.SSS");
		return simpleDateFormat.format(this);
	}
	
	/**
	 * Sets this ISODate object to the time value represented by 
	 * the ISO 8601 formatted string argument.
	 * 
	 * (note: doesn't cope with arcane (non-gregorian) variants, or with
	 * timezones specified. Use UTC!!)
	 * 
	 *  */
   public void set(String ISOstring)throws java.text.ParseException
   {
       //logger.log("ISODate "+ISOstring);
            simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss.SSS");
            Date newDate = simpleDateFormat.parse(ISOstring);
            this.setTime(newDate.getTime());

}
   /**
    *Returns this ISODate as a java.sql.Date object
    */
   public java.sql.Date toSQLDate(){
       return new java.sql.Date(getTime());
   }
   /**
    * returns this ISODate as a java.sql.Timestamp object
    */
   public java.sql.Timestamp toSQLTimestamp(){
       return new java.sql.Timestamp(getTime());
   }
   @Override
   public String toString(){
       return dateTimeMs();
   }
   public static ISODate getDayAgo(){
	   return new ISODate(new ISODate().getTime()+DAY_AGO);
   }
 public static ISODate getWeekAgo(){
     return new ISODate(new ISODate().getTime()+WEEK_AGO);
 }
 public static ISODate getMonthAgo(){
     return new ISODate(new ISODate().getTime()+MONTH_AGO);
 }
 public static ISODate getMonthAhead(){
	 return new ISODate(new ISODate().getTime()+MONTH_AHEAD);
 }
 public static ISODate getEndOfMonth(Date isoDate){
	 Calendar cal = Calendar.getInstance();
	 cal.setTime(isoDate);
	 cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	 cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
	 return new ISODate(cal.getTimeInMillis());
 }
}

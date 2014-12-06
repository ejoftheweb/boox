package uk.co.platosys.platax.client.widgets;

import java.util.Date;

import uk.co.platosys.platax.client.constants.DateFormats;

import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;


/**
 * Convenience subclass to eliminate GWT codefoam
 * (codefoam is apparently-pointless complexity added to code, eg the fact
 * that you can't format a DateBox with a String. Or a DateTimeFormat. but only
 * with its own inner class DateBox.Format which has to be instantiated from a DateTimeFormat)
 * So according to Google, to get a DateBox that formats the date sanely, I should do:
 * DateBox db = new DateBox(new DatePicker(), new Date(), new DateBox.DefaultFormat(DateTimeFormat.getFormat(SANE_DATE_FORMAT_STRING);
 * 
 * Instead, DateBox db = new PDateBox() will do finely. 
 * 
 * No doubt there's some internal google-PC reason for this, perhaps I have broken simple i18n 
 * for the sake of simple and readable code. 
 * 
 * This class formats the date according to DateFormats.MED_DATE_FORMAT which is Wed 26 Nov 2014 in en-GB locale.
 * I can do the i18n just in DateFormats when I'm ready. 
 * 
 * @author edward
 *
 */
public class PDateBox extends DateBox {
	/**
	 * creates a new DateBox with no date
	 */
   public PDateBox(){
	   super(new DatePicker(), null, new DateBox.DefaultFormat(DateFormats.MED_DATE_FORMAT));
	   //setFormat(new DateBox.DefaultFormat(DateFormats.MED_DATE_FORMAT));
	   
   }
   /**
    * creates a new DateBox with the given date
    * ERG this doesn't work properly ARG
    * @param date
    */
   public PDateBox(Date date){
	    setFormat(new DateBox.DefaultFormat(DateFormats.MED_DATE_FORMAT));
	    setValue(date);
   }
   
}

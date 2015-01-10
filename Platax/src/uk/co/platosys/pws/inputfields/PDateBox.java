package uk.co.platosys.pws.inputfields;

import java.util.Date;

import uk.co.platosys.platax.client.constants.DateFormats;
import uk.co.platosys.pws.widgets.YearDatePicker;

import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;


/**
 * Creates a date input field. 
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
 * Also we have to use a custom DatePicker because the standard one won't let you scroll years. Much as I love Google....
 * 
 * @author edward
 *
 */
public class PDateBox extends AbstractValueField<Date> {
	DateBox dateBox;
	/**
	 * creates a new DateBox with no date
	 */
   public PDateBox(){
	   dateBox= new DateBox(new YearDatePicker(), null, new DateBox.DefaultFormat(DateFormats.MED_DATE_FORMAT));
		   //setFormat(new DateBox.DefaultFormat(DateFormats.MED_DATE_FORMAT));
	   this.add(dateBox);
   }
   /**
    * creates a new DateBox with the given date
    * ERG this doesn't work properly ARG
    * @param date
    */
   public PDateBox(Date date){
	   dateBox= new DateBox(new DatePicker(), date, new DateBox.DefaultFormat(DateFormats.MED_DATE_FORMAT));
		this.add(dateBox); 
   }
@Override
public boolean isEnabled() {
	return dateBox.isEnabled();
}
@Override
public void setEnabled(boolean enabled) {
	dateBox.setEnabled(enabled);
	
}
@Override
public Date getValue() {
	return dateBox.getValue();
}

@Override
public void setFocus(boolean focused) {
	dateBox.setFocus(focused);
}



@Override
public HandlerRegistration addValueChangeHandler(
		ValueChangeHandler<Date> handler) {
	return dateBox.addValueChangeHandler(handler);
}
@Override
public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
	return null;
}



   
}

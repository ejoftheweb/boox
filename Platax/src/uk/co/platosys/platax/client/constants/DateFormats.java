package uk.co.platosys.platax.client.constants;

import com.google.gwt.i18n.client.DateTimeFormat;

public class DateFormats {

	 
		public static final String SHORT_DATE_FORMAT_STRING="dd MMM yy";
		public static final String MED_DATE_FORMAT_STRING="EEE, dd MMM yyyy";
		public static final String LONG_DATE_FORMAT_STRING="EEEE, dd MMMM yyyy";
	 
        public static final DateTimeFormat SHORT_DATE_FORMAT=DateTimeFormat.getFormat(SHORT_DATE_FORMAT_STRING);
        public static final DateTimeFormat MED_DATE_FORMAT=DateTimeFormat.getFormat(MED_DATE_FORMAT_STRING);
        public static final DateTimeFormat LONG_DATE_FORMAT=DateTimeFormat.getFormat(LONG_DATE_FORMAT_STRING);
        
}

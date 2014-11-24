package uk.co.platosys.platax.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TaxBandChooser extends RadioButtonGroup {
	public static final String ZERO_RATE_LABEL="0%";
	public static final String LOW_RATE_LABEL="5%";
	public static final String STANDARD_RATE_LABEL="20%";
	public static final String HIGHER_RATE_LABEL="20%";
	public static final String ZERO_RATE_VALUE="0%";
	public static final String LOW_RATE_VALUE="5%";
	public static final String STANDARD_RATE_VALUE="20%";
	public static final String HIGHER_RATE_VALUE="20%";//this is kludgy.
	public static final FieldValue ZERO_VALUE= new FieldValue(ZERO_RATE_LABEL,"ZERO" );
	public static final FieldValue STANDARD_VALUE = new FieldValue(STANDARD_RATE_LABEL, "STANDARD");
	public static final FieldValue LOW_VALUE=new FieldValue(LOW_RATE_LABEL, "LOW");
	public static final FieldValue HIGHER_VALUE=new FieldValue(HIGHER_RATE_LABEL, "HIGHER");
	static FieldValue [] rates = {ZERO_VALUE, LOW_VALUE, STANDARD_VALUE};
    static List<FieldValue> values = setValues();
	static final String ITEMS_STYLE="tax-chooser";
	public TaxBandChooser(String radioButtonGroupId, 
			  String itemsStyle, boolean allowHTML) {
		super(radioButtonGroupId, values, "STANDARD", itemsStyle, allowHTML);
		// TODO Auto-generated constructor stub
	}
	public TaxBandChooser(String chooserId) {
		super(chooserId, values, "STANDARD", ITEMS_STYLE, true);
		// TODO Auto-generated constructor stub
	}
	private static List<FieldValue> setValues(){
		List<FieldValue> fvalues= new ArrayList<FieldValue>();
			for (int i=0; i<rates.length; i++){
				fvalues.add(rates[i]);
			}
		 
		return fvalues;
	}
	public int getTaxBand(){
		//TODO fixme
		return 0;//we need to look at the mechanism for handling tax bands; this is a get-it-to-compile klujj
	}
}

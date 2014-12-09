package uk.co.platosys.platax.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Chooser extends RadioButtonGroup {
	static FieldValue[] rates;
    static List<FieldValue> values = setValues();
	static final String ITEMS_STYLE="tax-chooser";
	public Chooser(String radioButtonGroupId, 
			  String itemsStyle, boolean allowHTML) {
		super(radioButtonGroupId, values, "STANDARD", itemsStyle, allowHTML);
		// TODO Auto-generated constructor stub
	}
	public Chooser(String chooserId) {
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
	
}

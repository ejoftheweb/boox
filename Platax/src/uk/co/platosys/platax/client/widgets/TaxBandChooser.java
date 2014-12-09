package uk.co.platosys.platax.client.widgets;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.platax.shared.boox.GWTVat;

public class TaxBandChooser extends RadioButtonGroup {
	public static final String ZERO_RATE_LABEL="0%";
	public static final String LOW_RATE_LABEL="5%";
	public static final String STANDARD_RATE_LABEL="20%";
	public static final String HIGHER_RATE_LABEL="20%";
	public static final String UNTAXED_LABEL="No VAT";
	
	public static final String ZERO_RATE="ZERO";
	public static final String LOW_RATE="LOW";
	public static final String STANDARD_RATE="STANDARD";
	public static final String HIGHER_RATE="HIGHER";
	public static final String UNTAXED_RATE="NOVAT";
	
	
	public static final FieldValue ZERO_VALUE= new FieldValue(ZERO_RATE_LABEL,ZERO_RATE);
	public static final FieldValue STANDARD_VALUE = new FieldValue(STANDARD_RATE_LABEL, STANDARD_RATE);
	public static final FieldValue LOW_VALUE=new FieldValue(LOW_RATE_LABEL, LOW_RATE);
	public static final FieldValue HIGHER_VALUE=new FieldValue(HIGHER_RATE_LABEL, HIGHER_RATE);
	public static final FieldValue UNTAXED_VALUE=new FieldValue(UNTAXED_LABEL, UNTAXED_RATE);
	
	static FieldValue [] rates = {ZERO_VALUE, LOW_VALUE, STANDARD_VALUE, UNTAXED_VALUE};
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
		switch(getValue()){
		case LOW_RATE:return GWTVat.LOW_BAND;
		case HIGHER_RATE:return GWTVat.HIGHER_BAND;
		case STANDARD_RATE:return GWTVat.STANDARD_BAND;
		case ZERO_RATE:return GWTVat.ZERO_BAND;
		case UNTAXED_RATE:return GWTVat.UNTAXED_BAND;
			default:return GWTVat.STANDARD_BAND;
		}
		
	}
}

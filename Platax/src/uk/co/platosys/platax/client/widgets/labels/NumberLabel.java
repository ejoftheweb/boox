package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.pws.values.GWTMoney;

import com.google.gwt.i18n.client.NumberFormat;

public class NumberLabel extends InlineLabel {

	 
	public NumberLabel(float number) {
		super(NumberFormat.getDecimalFormat().format(number));
		setStyleName("moneyValue");
	}
	public NumberLabel(double number) {
		super(NumberFormat.getDecimalFormat().format(number));
		setStyleName("moneyValue");
	}
	public NumberLabel() {
		setStyleName("moneyValue");
	}
	public void setValue(GWTMoney money){
		setText(money.toPlainString());
	}
}

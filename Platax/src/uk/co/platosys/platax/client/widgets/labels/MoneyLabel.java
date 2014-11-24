package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.shared.boox.GWTMoney;

import com.google.gwt.user.client.ui.Label;

public class MoneyLabel extends Label {

	 
	public MoneyLabel(GWTMoney money) {
		super(money.toFormattedString());
		setStyleName("moneyValue");
	}

	public MoneyLabel() {
		setStyleName("moneyValue");
	}
	public void setValue(GWTMoney money){
		setText(money.toPlainString());
	}
}

package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.client.constants.Styles;
import uk.co.platosys.platax.shared.boox.GWTMoney;

import com.google.gwt.user.client.ui.Label;

public class MoneyLabel extends Label {

	 
	public MoneyLabel(GWTMoney money) {
		super(money.toFormattedString());
		setStyleName(Styles.MONEY_LABEL);
	}

	public MoneyLabel() {
		setStyleName(Styles.MONEY_LABEL);
	}
	public void setValue(GWTMoney money){
		setText(money.toPlainString());
	}
}

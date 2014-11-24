package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.shared.boox.GWTMoney;

import com.google.gwt.user.client.ui.Label;

public class MoneyTotalLabel extends MoneyLabel {

	 
	public MoneyTotalLabel(GWTMoney money) {
		super(money);
		if (money.credit()){
			setStyleName("px_money_total_value_credit");
		}else{
			setStyleName("px_money_total_value_debit");
		}
	}

	public MoneyTotalLabel() {
		setStyleName("moneyTotalValue");
	}
	public void setValue(GWTMoney money){
		setText(money.toFormattedString());
		if (money.credit()){
			setStyleName("px_money_total_value_credit");
		}else{
			setStyleName("px_money_total_value_debit");
		}
	}
}

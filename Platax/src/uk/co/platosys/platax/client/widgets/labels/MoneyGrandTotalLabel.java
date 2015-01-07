package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.pws.values.GWTMoney;

import com.google.gwt.user.client.ui.Label;

public class MoneyGrandTotalLabel extends MoneyLabel {

	 
	public MoneyGrandTotalLabel(GWTMoney money) {
		super(money);
		if (money.credit()){
			setStyleName("px_money_grand_total_value_credit");
		}else{
			setStyleName("px_money_grand_total_value_debit");
		}
	}

	public MoneyGrandTotalLabel() {
		setStyleName("moneyGrandTotalValue");
	}
	public void setValue(GWTMoney money){
		if (money.credit()){
			setStyleName("px_money_grand_total_value_credit");
		}else{
			setStyleName("px_money_grand_total_value_debit");
		}
	}
}

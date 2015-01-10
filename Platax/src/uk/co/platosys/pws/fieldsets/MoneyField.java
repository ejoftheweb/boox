package uk.co.platosys.pws.fieldsets;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.inputfields.MoneyBox;
import uk.co.platosys.pws.values.GWTMoney;

public class MoneyField extends AbstractFormField<GWTMoney> {
MoneyBox moneyBox;
	public MoneyField(String[] labelText, int position, Form parent, boolean required)	throws IllegalArgumentException {
		super(labelText,  position, parent, required);
		moneyBox=new MoneyBox("GBP");
		setWidget(moneyBox);
		start();
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		
		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<GWTMoney> handler) {
		return moneyBox.addValueChangeHandler(handler);
	}

}

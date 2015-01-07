package uk.co.platosys.pws.inputfields;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IntegerBox;

import uk.co.platosys.pws.values.IsFieldValue;


public class PIntegerBox extends AbstractValueField<Integer> {
private IntegerBox intBox=new IntegerBox();

public PIntegerBox(){
	this.add(intBox);
}
 
	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Integer> handler) {
		return intBox.addValueChangeHandler(handler);
	}

	@Override
	public IsFieldValue<Integer> getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus(boolean focused) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}

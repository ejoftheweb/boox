package uk.co.platosys.pws.inputfields;

import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CheckBox;

public class TickBox extends AbstractValueField<Boolean> {
    private CheckBox cb = new CheckBox();
    
    public TickBox(){
    	add(cb);
    }
	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return cb.addValueChangeHandler(handler);
	}

	@Override
	public Boolean getValue() {
		return cb.getValue();
	}

	@Override
	public void setFocus(boolean focused) {
		cb.setFocus(focused);
	}

	@Override
	public void setEnabled(boolean enabled) {
		cb.setEnabled(enabled);
		
	}

	@Override
	public boolean isEnabled() {
		return cb.isEnabled();
	}
	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return cb.addKeyDownHandler(handler);
	}

}

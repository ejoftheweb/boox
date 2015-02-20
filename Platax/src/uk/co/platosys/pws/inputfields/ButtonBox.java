package uk.co.platosys.pws.inputfields;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;

public class ButtonBox extends AbstractValueField<Boolean> {
    private Button cb = new Button();
    
    public ButtonBox(){
    	add(cb);
    }
	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return null;
	}

	@Override
	public Boolean getValue() {
		return false;
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
	public HandlerRegistration addClickHandler(ClickHandler handler){
		return cb.addClickHandler(handler);
	}
}

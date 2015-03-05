package uk.co.platosys.pws.inputfields;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;

public class ButtonBox extends AbstractValueField<Boolean> {
    private Button button = new Button();
    
    public ButtonBox(){
    	add(button);
    	button.setText("Click");
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
		button.setFocus(focused);
	}

	@Override
	public void setEnabled(boolean enabled) {
		button.setEnabled(enabled);
		
	}

	@Override
	public boolean isEnabled() {
		return button.isEnabled();
	}
	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		//return button.addKeyDownHandler(handler);
		return null;
	}
	public HandlerRegistration addClickHandler(ClickHandler handler){
		return button.addClickHandler(handler);
	}
}

package uk.co.platosys.pws.inputfields;

import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;


public class PPasswordBox extends AbstractValueField<String> {
    private PasswordTextBox textBox = new PasswordTextBox();
    
    public PPasswordBox(){
    	add(textBox);
    }
	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return textBox.addValueChangeHandler(handler);
	}

	@Override
	public String getValue() {
		return textBox.getValue();
	}

	@Override
	public void setFocus(boolean focused) {
		textBox.setFocus(focused);
	}

	@Override
	public void setEnabled(boolean enabled) {
		textBox.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return textBox.isEnabled();
	}
	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return textBox.addKeyDownHandler(handler);
	}
    public void setValue(String value){
    	textBox.setValue(value);
    }
}

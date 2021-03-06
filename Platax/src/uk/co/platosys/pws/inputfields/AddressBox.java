package uk.co.platosys.pws.inputfields;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;

import uk.co.platosys.pws.fieldsets.AddressField;
import uk.co.platosys.pws.values.PWSAddress;


public class AddressBox extends AbstractValueField<PWSAddress> implements HasValue<PWSAddress>{
    final TextBox textBox=new TextBox();
	PWSAddress value=new PWSAddress();
	String headlabel;
	AddressField parent;
	//boolean addressChangedHandlerInitialised=false;
    public AddressBox (final String headlabel, AddressField parent){
    	this.headlabel=headlabel;
    	this.parent=parent;
    	add(textBox);
    	textBox.addFocusHandler(new FocusHandler(){
    		@Override
			public void onFocus(FocusEvent event) {
    			popupForm();
			}
    	});
    }
	@Override
	public PWSAddress getValue() {
		return value;
	}

	@Override
	public void setFocus(boolean focused) {
		textBox.setFocus(focused);
		
	}
    private void popupForm(){
    	AddressForm addressForm = new AddressForm(headlabel, this, value);
		addressForm.setPopupPositionAndShow(addressForm.poscall);
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
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<PWSAddress> handler) {
		  //if (!addressChangedHandlerInitialised) {
				textBox.addValueChangeHandler(new ValueChangeHandler<String>() {
					public void onValueChange(ValueChangeEvent<String> event) {
						//Window.alert("address value changed to "+textBox.getValue());
						ValueChangeEvent.fire(AddressBox.this, getValue());
				}});
			
			//addressChangedHandlerInitialised = true;
		
		return addHandler(handler, ValueChangeEvent.getType());
	}
	@Override
	public void setValue(PWSAddress value, boolean fireEvents) {
		this.value=value;
		textBox.setValue(value.getShortAddress(), fireEvents);
		
	}
	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return textBox.addKeyDownHandler(handler);
	}
	@Override
	public void setValue(PWSAddress value) {
		setValue(value, false);
		
	}
	
    
}

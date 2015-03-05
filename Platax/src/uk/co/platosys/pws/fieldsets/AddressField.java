package uk.co.platosys.pws.fieldsets;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;

import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.inputfields.AddressBox;
import uk.co.platosys.pws.values.GWTAddress;

/**
 * This field collects a postal address. At present, it is limited to UK format addresses. We intend 
 * to implement a more internationalised version.
 * As soon as the field gets focus, it pops up a form to collect the various fields of the address which
 * are then aggregated into an Address object. A short version of the address is then displayed in the field.
 * 
 * @author edward
 *
 */
public class AddressField extends AbstractFormField<GWTAddress> implements HasValueChangeHandlers<GWTAddress> {
AddressBox addressBox=null;
	public AddressField(String[] labelText, int position, Form parent, boolean required)throws IllegalArgumentException {
		super(labelText, position, parent, required);
		addressBox=new AddressBox(getLabelInfoText(), this);
		setWidget(addressBox);
		start();
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		//Window.alert("AF event fired");
		
	}
	@Override
	protected void start(){
		widget.setEnabled(enabled);
		widget.addValueChangeHandler(new ValueChangeHandler<GWTAddress>(){
			@Override
			public void onValueChange(ValueChangeEvent<GWTAddress> event) {
				//Window.alert("vc event received");
				if(validate()){
					setOK(true);
					moveNext();
				}else{
					setOK(false);
				};
			}
			
		});
		if(!required){
			widget.addKeyDownHandler(new KeyDownHandler(){
				@Override
				public void onKeyDown(KeyDownEvent event) {
					if((event.getNativeKeyCode()==KeyCodes.KEY_ENTER)||(event.getNativeKeyCode()==KeyCodes.KEY_TAB)){
						setOK(true);
						moveNext();
					}
				}
			});
		}
		try {
			parent.addField(this);
		} catch (Exception e) {
			
		}
	}
	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<GWTAddress> handler) {
		return addressBox.addValueChangeHandler(handler);
	}

}

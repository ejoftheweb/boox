package uk.co.platosys.pws.fieldsets;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import uk.co.platosys.platax.client.services.AddressService;
import uk.co.platosys.platax.client.services.AddressServiceAsync;
import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.inputfields.AddressBox;
import uk.co.platosys.pws.values.PWSAddress;

/**
 * This field collects a postal address. At present, it is limited to UK format addresses. We intend 
 * to implement a more internationalised version.
 * As soon as the field gets focus, it pops up a form to collect the various fields of the address which
 * are then aggregated into an Address object. A short version of the address is then displayed in the field while 
 * the address is submitted to the server, which returns an addressID. This string can be used to reference the address
 * anywhere else in the app. 
 * 
 * @author edward
 *
 */
public final class AddressField extends AbstractFormField<PWSAddress> implements HasValueChangeHandlers<PWSAddress> {
AddressBox addressBox=null;
AddressServiceAsync addressService = (AddressServiceAsync) GWT.create(AddressService.class);
private String xAddressID=null;
    private final AsyncCallback<PWSAddress> callBack=new AsyncCallback<PWSAddress>(){
    	@Override
		public void onFailure(Throwable caught) {
    		Window.alert("failed to record address correctly" +caught.getMessage());
		}

		@Override
		public void onSuccess(PWSAddress result) {
			addressBox.setValue(result);
			setxAddressID(result.getxAddressID());
			Window.alert("Address registered with ID "+getxAddressID());
		}
    	
    };

	public AddressField(String[] labelText, int position, Form parent, boolean required)throws IllegalArgumentException {
		super(labelText, position, parent, required);
		addressBox=new AddressBox(getLabelInfoText(), this);
		setWidget(addressBox);
		start();
	}

	@Override
	public boolean validate() {
		addressService.recordAddress(addressBox.getValue(), callBack);
		return true;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		//Window.alert("AF event fired");
		
	}
	@Override
	protected void start(){
		widget.setEnabled(enabled);
		widget.addValueChangeHandler(new ValueChangeHandler<PWSAddress>(){
			@Override
			public void onValueChange(ValueChangeEvent<PWSAddress> event) {
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
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<PWSAddress> handler) {
		return addressBox.addValueChangeHandler(handler);
	}

	/**
	 * @return the xAddressID*/
	
	public String getxAddressID() {
		return xAddressID;
	}

	/**
	 * @param xAddressID the xAddressID to set*/
	
	public void setxAddressID(String xAddressID) {
		this.xAddressID = xAddressID;
	}

}

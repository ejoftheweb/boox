package uk.co.platosys.pws.inputfields;


import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ResetButton;

public class SubmitBox extends AbstractValueField<Boolean> implements HasClickHandlers{
   ResetButton cancelButton=new ResetButton("Cancel");
   Button submitButton=new Button("Submit");
  	
	public SubmitBox(){
	   add(cancelButton);
	   add(submitButton);
	   cancelButton.setEnabled(true);
	   submitButton.setEnabled(false);
   }
	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Boolean> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus(boolean focused) {
		submitButton.setFocus(true);
		
	}

	@Override
	public void setEnabled(boolean enabled) {
		submitButton.setEnabled(true);
		
	}

	@Override
	public boolean isEnabled() {
		return submitButton.isEnabled();
	}
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return submitButton.addClickHandler(handler);
	}
	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

}

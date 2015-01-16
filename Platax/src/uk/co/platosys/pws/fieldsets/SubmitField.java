package uk.co.platosys.pws.fieldsets;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.constants.FieldText;
import uk.co.platosys.pws.inputfields.SubmitBox;

public class SubmitField extends AbstractFormField<Boolean> implements HasClickHandlers{
private SubmitBox sbox=new SubmitBox();
	public SubmitField(int position, Form parent) throws IllegalArgumentException {
		super(FieldText.SUBMIT, position, parent, true);
		setWidget(sbox);
		start();
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

	

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return sbox.addClickHandler(handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return sbox.addValueChangeHandler(handler);
	}

}

package uk.co.platosys.pws.fieldsets;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;

import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.inputfields.ButtonBox;
import uk.co.platosys.pws.inputfields.TickBox;

/**
 * A field that shows one button
 * 
 * @author edward
 *
 */
public class OneButtonField extends AbstractFormField<Boolean> {
ButtonBox buttonBox;
	public OneButtonField(String[] labelText, int position, Form parent, boolean required) throws IllegalArgumentException {
		super(labelText, position, parent, required);
		try{
		ButtonBox buttonBox = new ButtonBox();
		setWidget(buttonBox);
		start();
		}catch(Exception x){
			Window.alert("obf error "+x.getMessage());
		}
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return buttonBox.addValueChangeHandler(handler);
	}

	public HandlerRegistration addClickHander(ClickHandler handler){
		return buttonBox.addClickHandler(handler);
	}

}

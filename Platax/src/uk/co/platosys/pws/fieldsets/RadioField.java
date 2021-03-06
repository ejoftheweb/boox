package uk.co.platosys.pws.fieldsets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.inputfields.RadioBox;
import uk.co.platosys.pws.values.ValuePair;


/**
 * A field that shows a selection of mutually-exclusive radio buttons
 * @author edward
 *
 */
public class RadioField extends AbstractFormField<String> implements HasStringValues {
RadioBox radioBox;
	public RadioField(String name, String[] labelText,  List<? extends ValuePair> values,  int position, Form parent,	boolean required) throws IllegalArgumentException {
		super(labelText, position, parent, required);
		radioBox = new RadioBox(name,  values);
		setWidget (radioBox);
		start();
	}

	@Override
	public boolean validate() {
		return true;
	}

	
		public void setValue(String value){
			
		}
	
		@Override
		public void setValue(String value, boolean fireEvents) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void fireEvent(GwtEvent<?> event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
			return radioBox.addValueChangeHandler(handler);
		}

		@Override
		public List<String> getValues() {
			ArrayList<String> values= new ArrayList<String>();
			values.add(radioBox.getValue());
			return values;
		}

}

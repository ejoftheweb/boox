package uk.co.platosys.pws.inputfields;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.platosys.pws.values.ValuePair;
import uk.co.platosys.util.RandomString;

import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.RadioButton;

/**
 * A widget that implements a group of radio buttons.
 * The labels will be displayed and should be I18nised, the values are 
 * programmatic values
 * @author edward
 *
 */
public class RadioBox extends AbstractValueField<String> {
	Map<RadioButton,String> map = new HashMap<RadioButton, String>();
	
	public RadioBox(String name, List<? extends ValuePair> values, ValuePair defaultValue) throws IllegalArgumentException{
		boolean hasDefault=false;
		for (ValuePair value:values){
			RadioButton rb = new RadioButton(name, value.getLabel());
			rb.setValue(false);
			map.put(rb, value.getValue());
			if(value.getValue().equals(defaultValue.getValue())){
				rb.setValue(true);
				hasDefault=true;
			}
			add(rb);
		}
		if(!hasDefault){throw new IllegalArgumentException("RadioBox default value not listed");}
	}


	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getValue() {
		for(RadioButton rb:map.keySet()){
			if(rb.getValue()){
				return(map.get(rb));
			}
		}
		return null;
	}


	@Override
	public void setFocus(boolean focused) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}

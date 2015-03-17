package uk.co.platosys.pws.widgets;

import java.util.Map;
import java.util.Set;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

public class ValueListBox extends ListBox implements HasValue<String> {
    
	boolean valueChangeHandlerInitialised=false;
	
    @Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
	   		addChangeHandler(new ChangeHandler() {
					public void onChange(ChangeEvent event) {
						//Window.alert("list value changed to "+getValue());
						ValueChangeEvent.fire(ValueListBox.this, getValue());
				}
			});
			valueChangeHandlerInitialised = true;
		
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public String getValue() {
		return getValue(getSelectedIndex());
	}

	@Override
	public void setValue(String value) {
		for (int i = 0; i < getItemCount(); i++) {  
			if (getValue(i).equals(value)) {
				setSelectedIndex(i);
				break;
			}
		}
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		setValue(value);
		if (fireEvents){
			ValueChangeEvent.fire(this, value);
		}
	}
	public void addItems(Map<String, String> items, boolean reverse){
			Set<String> keys = items.keySet();
			for(String key:keys){
				if(reverse){
					addItem(key, items.get(key));
				}else{
					addItem(items.get(key), key);
				}
			}
		
	}
}

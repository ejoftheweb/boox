package uk.co.platosys.pws.inputfields;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import uk.co.platosys.pws.constants.LabelText;
import uk.co.platosys.pws.values.ValuePair;
import uk.co.platosys.pws.widgets.ValueListBox;

import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class ListValueField extends AbstractValueField<String> {
 ValueListBox listBox=new ValueListBox();
 
 public ListValueField (){
	 listBox.addItem(LabelText.PLEASE_SELECT, "");
	 add(listBox);
 }
 public ListValueField (List<ValuePair> items){
	 listBox.addItem(LabelText.PLEASE_SELECT, "");
	 add(listBox);
	 addItems(items);
 }
 public void addItems(List<ValuePair> items){
	 for (ValuePair item:items){
		 listBox.addItem(item.getLabel(), item.getValue());
	 }
 }
	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return listBox.addKeyDownHandler(handler);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return listBox.addValueChangeHandler(handler);
	}

	@Override
	public String getValue() {
		 return listBox.getValue(listBox.getSelectedIndex());
	}

	@Override
	public void setFocus(boolean focused) {
		listBox.setFocus(focused);
		
	}

	@Override
	public void setEnabled(boolean enabled) {
		listBox.setEnabled(enabled);
		
	}

	@Override
	public boolean isEnabled() {
		return listBox.isEnabled();
	}
	public void addItems(Map<String, String> items, boolean reverse){
		listBox.addItems(items, reverse);
	}
}

package uk.co.platosys.pws.fieldsets;

import java.util.List;
import java.util.Map;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.inputfields.CheckList;
import uk.co.platosys.pws.values.ValuePair;


/**
 * A field that shows a list of check-boxes none or more of which can be ticked. 
 * 
 * Its values are added as ValuePair items, that is, a pair of values and labels. The label is displayed, and the value returned
 * is the corresponding string. You can either implement ValuePair in your code or use the provided base implementation, BasicValuePair. 
 * 
 * Doesn't yet support removing items from the list.
 * 
 * The list always has one default item, by default labelled "Please Select.." which returns the empty string (not null). 
 * 
 * @author edward
 *
 */
public class CheckListField extends AbstractFormField <List<String>> implements HasValueChangeHandlers<List<String>>, HasStringValues {
CheckList list;
/**
 * 
 * @param labelText 
 * @param position
 * @param parent
 * @param required
 * @throws IllegalArgumentException
 */
	public CheckListField(String[] labelText, int position, Form parent,	boolean required) throws IllegalArgumentException {
		super(labelText, position, parent, required);
		this.list=new CheckList();
		setWidget(list);
		start();
	}
	/**
	 * add items as a List of <ValuePair>s.
	 * @param items
	 */
	public void addItems(List<? extends ValuePair> items){
		list.addItems(items);
	}
	public void addItems(Map<String, String> items, boolean reverse){
		list.addItems(items, reverse);
	}

	@Override
 	public boolean validate() {
		  if(required){
			  if(list.getValue().equals("")){
				  return false;
			  }else{
				  return true;
			  }
		  }else{
			  return true;
		  }
	}
	@Override
	public void fireEvent(GwtEvent<?> event) {
		// TODO Auto-generated method stub
		
	}
	
	public void addItem(String name, String localisedName) {
		list.addItem(name, localisedName);
		
	}
	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<List<String>> handler) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<String> getValues() {
		return list.getValue();
	}


}

package uk.co.platosys.pws.fieldsets;

import java.util.List;
import java.util.Map;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;

import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.inputfields.ListValueField;
import uk.co.platosys.pws.values.ValuePair;


/**
 * A field that shows a list/combo box. 
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
public class ListField extends AbstractListField implements HasValueChangeHandlers<String> {
ListValueField list;
/**
 * 
 * @param labelText 
 * @param position
 * @param parent
 * @param required
 * @throws IllegalArgumentException
 */
	public ListField(String[] labelText, int position, Form parent,	boolean required)  throws IllegalArgumentException {
		super(labelText, position, parent, required);
		start();
	}
}
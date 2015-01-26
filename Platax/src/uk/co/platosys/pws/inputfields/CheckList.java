package uk.co.platosys.pws.inputfields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.co.platosys.platax.client.widgets.HasMultiValues;
import uk.co.platosys.platax.shared.boox.GWTSelectable;
import uk.co.platosys.pws.values.ValuePair;

import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * CheckPanel is a Panel full of checkboxes
 * It has a getValue method that returns a List of Strings.
 * 
 * @author edward
 *
 */
public class CheckList extends AbstractValueField<List<String>> {
    private ArrayList<CheckBox> boxes = new ArrayList<CheckBox>();
	public CheckList() {}
	
	
    public void addItem(String name, String label){
    	SimplePanel sp = new SimplePanel();
        CheckBox cb = new CheckBox();
    	cb.setName(name);
    	cb.setHTML(label);
    	boxes.add(cb);
    	sp.add(cb);
    	this.add(sp);
    	 
    }
    /**
     * add a single item
     * @param item
     */
    public void addItem(ValuePair item){
    	addItem(item.getValue(), item.getLabel());
    }
    /**
     * add a single item
     * @param item
     */
    public void addItem(ValuePair item, boolean reverse){
    	if(reverse){
    		addItem( item.getLabel(), item.getValue());
    	}else{
    		addItem(item.getValue(), item.getLabel());
    	}
    }
    /**
     * add a Collection of items
     * @param items
     */
    public void addItems(Collection<? extends ValuePair> items){
    	for (ValuePair item: items){
    		addItem(item);
    	}
    }
    /**
     * add a Map of items
     * @param items
     */
    public void addItems(Map<String,String> items, boolean reverse){
    	Set<String> keys = items.keySet();
		for(String key:keys){
			if(reverse){
				addItem(key, items.get(key));
			}else{
				addItem(items.get(key), key);
			}
		}
    }
    public int getCount(){
    	return boxes.size();
    }
    
    /**
     * returns a list of Strings being the values of the checked boxes in the panel.
     */
    public List<String> getValue(){
    	ArrayList<String> values = new ArrayList<String>();
    	for(CheckBox cb:boxes){
    		if (cb.getValue()){
    			values.add(cb.getName());
    		}
    	}
    	return values;
    }
	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		// TODO Auto-generated method stub
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
	
	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<List<String>> handler) {
		// TODO Auto-generated method stub
		return null;
	}

}


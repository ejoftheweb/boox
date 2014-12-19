package uk.co.platosys.platax.client.widgets;

import java.util.ArrayList;
import java.util.Collection;

import uk.co.platosys.platax.shared.boox.GWTSelectable;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * CheckPanel is a Panel full of checkboxes
 * It has a getValues method that returns an arrayList of Strings.
 * 
 * @author edward
 *
 */
public class CheckPanel extends FlowPanel implements HasMultiValues {
    private ArrayList<CheckBox> boxes = new ArrayList<CheckBox>();
	public CheckPanel() {
		// TODO Auto-generated constructor stub
	}
    public void addItem(String name, String label){
        CheckBox cb = new CheckBox();
    	cb.setName(name);
    	cb.setHTML(label);
    	boxes.add(cb);
    	this.add(cb);
    	 
    }
    /**
     * add a single item
     * @param item
     */
    public void addItem(GWTSelectable item){
    	addItem(item.getSysname(), item.getName());
    }
    /**
     * add a Collection of items
     * @param items
     */
    public void addItems(Collection<? extends GWTSelectable> items){
    	for (GWTSelectable item: items){
    		addItem(item);
    	}
    }
    public int getCount(){
    	return boxes.size();
    }
    
    /**
     * returns a list of Strings being the values of the checked boxes in the panel.
     */
    public ArrayList<String> getValues(){
    	ArrayList<String> values = new ArrayList<String>();
    	for(CheckBox cb:boxes){
    		if (cb.getValue()){
    			values.add(cb.getName());
    		}
    	}
    	return values;
    }
}


package uk.co.platosys.platax.client.widgets;

import java.util.ArrayList;

import uk.co.platosys.platax.shared.boox.GWTModule;
import uk.co.platosys.platax.shared.boox.GWTSelectable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.ListBox;

public class HMVListBox extends ListBox implements HasMultiValues {

	public HMVListBox() {
		// TODO Auto-generated constructor stub
	}

	public HMVListBox(boolean isMultipleSelect) {
		super(isMultipleSelect);
		// TODO Auto-generated constructor stub
	}

	public HMVListBox(Element element) {
		super(element);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<String> getValues() {
		ArrayList<String> values = new ArrayList<String>();
		values.add(getValue(getSelectedIndex()));
		return values;
	}

	public void addItem(GWTSelectable selectable) {
		this.addItem(selectable.getDescription(), selectable.getSysname());
		
	}

}


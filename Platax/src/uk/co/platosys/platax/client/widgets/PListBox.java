package uk.co.platosys.platax.client.widgets;

import java.util.ArrayList;

import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.shared.boox.GWTModule;
import uk.co.platosys.platax.shared.boox.GWTSelectable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.ListBox;

public class PListBox extends ListBox implements HasMultiValues {

	public PListBox() {
		addItem(StringText.PLEASE_SELECT, "0");
	}

	public PListBox(boolean isMultipleSelect) {
		super(isMultipleSelect);
		// TODO Auto-generated constructor stub
	}

	public PListBox(Element element) {
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
		this.addItem(selectable.getDescription(), selectable.getName());
		
	}

}


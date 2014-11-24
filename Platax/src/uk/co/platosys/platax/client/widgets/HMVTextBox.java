package uk.co.platosys.platax.client.widgets;

import java.util.ArrayList;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.TextBox;

public class HMVTextBox extends TextBox implements HasMultiValues {

	public HMVTextBox() {
		// TODO Auto-generated constructor stub
	}

	public HMVTextBox(Element element) {
		super(element);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<String> getValues() {
		ArrayList<String> values = new ArrayList<String>();
		values.add(getValue());
		return values;
		
	}

}

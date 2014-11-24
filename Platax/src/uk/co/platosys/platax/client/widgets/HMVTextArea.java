package uk.co.platosys.platax.client.widgets;

import java.util.ArrayList;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.TextArea;

public class HMVTextArea extends TextArea implements HasMultiValues {

	public HMVTextArea() {
		// TODO Auto-generated constructor stub
	}

	public HMVTextArea(Element element) {
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

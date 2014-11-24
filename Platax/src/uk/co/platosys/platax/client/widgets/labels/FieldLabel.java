package uk.co.platosys.platax.client.widgets.labels;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.user.client.ui.Label;

public class FieldLabel extends Label {

	public FieldLabel() {
		// TODO Auto-generated constructor stub
	}

	public FieldLabel(String text) {
		super(text);
		setStyleName("px_field_label");
		
	}

	public FieldLabel(Element element) {
		super(element);
		// TODO Auto-generated constructor stub
	}

	public FieldLabel(String text, Direction dir) {
		super(text, dir);
		// TODO Auto-generated constructor stub
	}

	public FieldLabel(String text, DirectionEstimator directionEstimator) {
		super(text, directionEstimator);
		// TODO Auto-generated constructor stub
	}

	public FieldLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		// TODO Auto-generated constructor stub
	}

}

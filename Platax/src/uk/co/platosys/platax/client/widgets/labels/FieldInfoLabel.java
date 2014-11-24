package uk.co.platosys.platax.client.widgets.labels;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.user.client.ui.Label;

public class FieldInfoLabel extends Label {

	public FieldInfoLabel() {
		// TODO Auto-generated constructor stub
		setStyleName("px_field_info");
	}

	public FieldInfoLabel(String text) {
		super(text);
		setStyleName("px_field_info");
		setWidth("35%");
	}

	public FieldInfoLabel(Element element) {
		super(element);
		// TODO Auto-generated constructor stub
	}

	public FieldInfoLabel(String text, Direction dir) {
		super(text, dir);
		// TODO Auto-generated constructor stub
	}

	public FieldInfoLabel(String text, DirectionEstimator directionEstimator) {
		super(text, directionEstimator);
		// TODO Auto-generated constructor stub
	}

	public FieldInfoLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		// TODO Auto-generated constructor stub
	}

}

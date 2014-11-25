package uk.co.platosys.platax.client.widgets.labels;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.user.client.ui.Label;

public class StatusLabel extends Label {

	public StatusLabel() {
		// TODO Auto-generated constructor stub
	}

	public StatusLabel(String text) {
		super(text);
		setStyleName("px_field_label");
		
	}

	public StatusLabel(Element element) {
		super(element);
		// TODO Auto-generated constructor stub
	}

	public StatusLabel(String text, Direction dir) {
		super(text, dir);
		// TODO Auto-generated constructor stub
	}

	public StatusLabel(String text, DirectionEstimator directionEstimator) {
		super(text, directionEstimator);
		// TODO Auto-generated constructor stub
	}

	public StatusLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		// TODO Auto-generated constructor stub
	}

}

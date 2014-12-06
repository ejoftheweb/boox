package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.client.constants.Styles;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.user.client.ui.Label;

public class BodyNameLabel extends Label {

	public BodyNameLabel() {
		// TODO Auto-generated constructor stub
	}

	public BodyNameLabel(String text) {
		super(text);
		setStyleName(Styles.BODY_NAME_LABEL);
		
	}

	public BodyNameLabel(Element element) {
		super(element);
		// TODO Auto-generated constructor stub
	}

	public BodyNameLabel(String text, Direction dir) {
		super(text, dir);
		// TODO Auto-generated constructor stub
	}

	public BodyNameLabel(String text, DirectionEstimator directionEstimator) {
		super(text, directionEstimator);
		// TODO Auto-generated constructor stub
	}

	public BodyNameLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		// TODO Auto-generated constructor stub
	}

}

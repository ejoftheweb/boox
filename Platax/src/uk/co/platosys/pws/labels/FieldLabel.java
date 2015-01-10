package uk.co.platosys.pws.labels;

import uk.co.platosys.platax.client.constants.Styles;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.user.client.ui.Label;

public class FieldLabel extends Label {

	public FieldLabel() {
		setStyleName(Styles.FIELD_LABEL);
	}

	public FieldLabel(String text) {
		super(text);
		setStyleName(Styles.FIELD_LABEL);
		
	}

	public FieldLabel(Element element) {
		super(element);
		setStyleName(Styles.FIELD_LABEL);
	}

	public FieldLabel(String text, Direction dir) {
		super(text, dir);
		setStyleName(Styles.FIELD_LABEL);
	}

	public FieldLabel(String text, DirectionEstimator directionEstimator) {
		super(text, directionEstimator);
		setStyleName(Styles.FIELD_LABEL);
	}

	public FieldLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		setStyleName(Styles.FIELD_LABEL);
	}

}

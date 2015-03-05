package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.client.constants.Styles;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.user.client.ui.Label;

public class TabPageSubtitleLabel extends Label {

	public TabPageSubtitleLabel() {
		setStyleName(Styles.FORM_SUB_HEADER);
	}

	public TabPageSubtitleLabel(String text) {
		super(text);
		setStyleName(Styles.FORM_SUB_HEADER);
	}

	public TabPageSubtitleLabel(Element element) {
		super(element);
		// TODO Auto-generated constructor stub
	}

	public TabPageSubtitleLabel(String text, Direction dir) {
		super(text, dir);
		// TODO Auto-generated constructor stub
	}

	public TabPageSubtitleLabel(String text, DirectionEstimator directionEstimator) {
		super(text, directionEstimator);
		// TODO Auto-generated constructor stub
	}

	public TabPageSubtitleLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		// TODO Auto-generated constructor stub
	}

}

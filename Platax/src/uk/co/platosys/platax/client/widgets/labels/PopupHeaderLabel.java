package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.client.constants.Styles;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.user.client.ui.Label;

public class PopupHeaderLabel extends Label {

	public PopupHeaderLabel() {
		super();
		setStyleName(Styles.POPUP_HEADER);
	}

	public PopupHeaderLabel(String text) {
		super(text);
		setStyleName(Styles.POPUP_HEADER);
		
	}

	public PopupHeaderLabel(Element element) {
		super(element);
		// TODO Auto-generated constructor stub
	}

	public PopupHeaderLabel(String text, Direction dir) {
		super(text, dir);
		// TODO Auto-generated constructor stub
	}

	public PopupHeaderLabel(String text, DirectionEstimator directionEstimator) {
		super(text, directionEstimator);
		// TODO Auto-generated constructor stub
	}

	public PopupHeaderLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		// TODO Auto-generated constructor stub
	}

}

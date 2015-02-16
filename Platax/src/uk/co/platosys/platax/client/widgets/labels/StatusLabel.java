package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.client.constants.Styles;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.user.client.ui.Label;

public class StatusLabel extends InlineLabel {

	public StatusLabel() {
		// TODO Auto-generated constructor stub
	}

	public StatusLabel(String text) {
		super(text);
		setStyleName(Styles.STATUS_LABEL);
		
	}

	

}

package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.client.constants.Styles;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.user.client.ui.Label;

public class FieldInfoLabel extends Label {

	public FieldInfoLabel() {
		// TODO Auto-generated constructor stub
		setStyleName(Styles.FIELD_INFO_LABEL);
	}

	public FieldInfoLabel(String text) {
		super(text);
		setStyleName(Styles.FIELD_INFO_LABEL);
		//setWidth("35%");
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
    public void setAlarmed(boolean alarmed){
    	if(alarmed){
    		setStyleName(Styles.FIELD_INFO_LABEL_ALARMED);
    	}else{
    		setStyleName(Styles.FIELD_INFO_LABEL);
    	}
    }
}

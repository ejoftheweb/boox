package uk.co.platosys.pws.labels;

import uk.co.platosys.platax.client.constants.Styles;
import uk.co.platosys.pws.constants.LabelText;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;

public class FieldInfoLabel extends Label {
    String infoText;
    String errorText=LabelText.ERROR;
    String okText=LabelText.OK;
    boolean OK=false;
    boolean alarmed=false;
    boolean error=false;
    
	public FieldInfoLabel() {
		setStyleName(Styles.FIELD_INFO_LABEL);
	}

	public FieldInfoLabel(String text) {
		super(text);
		this.infoText=text;
		setStyleName(Styles.FIELD_INFO_LABEL);
	}

	public FieldInfoLabel(Element element) {
		super(element);
		setStyleName(Styles.FIELD_INFO_LABEL);
	}

	public FieldInfoLabel(String text, Direction dir) {
		super(text, dir);
		this.infoText=text;
		setStyleName(Styles.FIELD_INFO_LABEL);
	}

	public FieldInfoLabel(String text, DirectionEstimator directionEstimator) {
		super(text, directionEstimator);
		this.infoText=text;
		setStyleName(Styles.FIELD_INFO_LABEL);
	}

	public FieldInfoLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		this.infoText=text;
		setStyleName(Styles.FIELD_INFO_LABEL);
	}
    public void setAlarmed(boolean alarmed){
    	this.alarmed=alarmed;
    	if(alarmed){
    		setStyleName(Styles.FIELD_INFO_LABEL_ALARMED);
    	}else{
    		setStyleName(Styles.FIELD_INFO_LABEL);
    	}
    }
    public void setOK(boolean OK){
    	this.OK=OK;
    	if(OK){
    		setAlarmed(false);
    		setText(okText);
    	}else{
    		setText(errorText);
    		setAlarmed(true);
    		setError(true);
    	}
    }

	

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public String getOkText() {
		return okText;
	}

	public void setOkText(String okText) {
		this.okText = okText;
	}

	public boolean isOk() {
		return OK;
	}


	public boolean isError() {
		return error;
	}

	private void setError(boolean error) {
		this.error = error;
	}

	public boolean isAlarmed() {
		return alarmed;
	}
}

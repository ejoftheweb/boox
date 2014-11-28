package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.client.constants.Styles;

import com.google.gwt.user.client.ui.Label;

public class FormHeaderLabel extends Label {
	public FormHeaderLabel(String text){
		   super(text);
		   setStyleName(Styles.FORM_HEADER);
	   }

	public FormHeaderLabel() {
		 setStyleName(Styles.FORM_HEADER);
	}
}

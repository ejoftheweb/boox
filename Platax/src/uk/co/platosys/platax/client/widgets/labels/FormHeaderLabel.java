package uk.co.platosys.platax.client.widgets.labels;

import com.google.gwt.user.client.ui.Label;

public class FormHeaderLabel extends Label {
	public FormHeaderLabel(String text){
		   super(text);
		   setStyleName("formHeader");
	   }

	public FormHeaderLabel() {
		 setStyleName("formHeader");
	}
}

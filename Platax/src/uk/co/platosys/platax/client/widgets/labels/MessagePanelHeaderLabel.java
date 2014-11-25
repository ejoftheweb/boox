package uk.co.platosys.platax.client.widgets.labels;

import com.google.gwt.user.client.ui.Label;

public class MessagePanelHeaderLabel extends Label {
	public MessagePanelHeaderLabel(String text){
		   super(text);
		   setStyleName("formHeader");
	   }

	public MessagePanelHeaderLabel() {
		 setStyleName("formHeader");
	}
}

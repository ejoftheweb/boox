package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.client.constants.Styles;

import com.google.gwt.user.client.ui.Label;

public class MessagePanelHeaderLabel extends Label {
	public MessagePanelHeaderLabel(String text){
		   super(text);
		   setStyleName(Styles.MESSAGE_PANEL_HEADER);
	   }

	public MessagePanelHeaderLabel() {
		 setStyleName(Styles.MESSAGE_PANEL_HEADER);
	}
}

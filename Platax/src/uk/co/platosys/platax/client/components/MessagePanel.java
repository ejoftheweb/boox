package uk.co.platosys.platax.client.components;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackLayoutPanel;

public class MessagePanel extends StackLayoutPanel {

	public MessagePanel() {
		super(Unit.PCT);
		add(new Label("message0"), "System", 5);
		add(new Label("message1"), "Email", 5);
		add(new Label("message2"), "Twitter", 5);
		add(new Label("message3"), "Txt", 5);
		
	}

}

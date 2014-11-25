package uk.co.platosys.platax.client.components;

import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.widgets.labels.MessagePanelHeaderLabel;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackLayoutPanel;

public class MessagePanel extends FlowPanel {
private StackLayoutPanel messageStack = new StackLayoutPanel(Unit.PCT);
public MessagePanelHeaderLabel topHead= new MessagePanelHeaderLabel(LabelText.MESSAGE_CENTRE);
	public MessagePanel() {
		super();
		add(topHead);
		messageStack.add(new Label("message0"), LabelText.SYSTEM_MESSAGES, 5);
		messageStack.add(new Label("message1"), LabelText.USER_MESSAGES, 5);
		messageStack.add(new Label("message2"), LabelText.STAFF_MESSAGES, 5);
		messageStack.add(new Label("message3"), "Txt", 5);
		add(messageStack);
		
	}

}

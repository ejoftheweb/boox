package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.shared.boox.GWTMoney;

import com.google.gwt.user.client.ui.Label;

public class InlineLabel extends com.google.gwt.user.client.ui.InlineLabel {
	public InlineLabel(String value) {
		super(value);
		setStyleName("inline_label");
	}

	public InlineLabel() {
		setStyleName("inline_label");
	}
	
}

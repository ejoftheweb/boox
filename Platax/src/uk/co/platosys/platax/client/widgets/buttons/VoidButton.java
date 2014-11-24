package uk.co.platosys.platax.client.widgets.buttons;

import uk.co.platosys.platax.client.constants.ButtonText;

import com.google.gwt.user.client.ui.Button;

public class VoidButton extends Button {
	public VoidButton(){
		super(ButtonText.EXIT);
		setStyleName("voidButton");
	}
}

package uk.co.platosys.platax.client.widgets.buttons;

import uk.co.platosys.platax.client.constants.ButtonText;

import com.google.gwt.user.client.ui.Button;

public class AddButton extends Button {
   public AddButton(){
	   super(ButtonText.ADD_NEW);
	   setStyleName("addButton");
   }
}

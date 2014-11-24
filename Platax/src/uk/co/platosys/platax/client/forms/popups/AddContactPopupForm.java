package uk.co.platosys.platax.client.forms.popups;

import uk.co.platosys.platax.client.widgets.AddressWidget;
import uk.co.platosys.platax.client.widgets.ContactWidget;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.PopupPanel;

public class AddContactPopupForm extends PopupPanel {
	FlexTable table = new FlexTable();
	ContactWidget contactWidget = new ContactWidget();
	AddressWidget addressWidget = new AddressWidget();
	PopupPanel.PositionCallback poscall =(new PopupPanel.PositionCallback() {
        public void setPosition(int offsetWidth, int offsetHeight) {
          int left = (Window.getClientWidth() - offsetWidth) / 2;
          int top = (Window.getClientHeight() - offsetHeight) / 3;
          AddContactPopupForm.this.setPopupPosition(left, top);
        }
      });
	public AddContactPopupForm(){
		this.setWidget(table);
		table.setWidget(1,1, contactWidget);
		table.setWidget(2,1, addressWidget);
		setGlassEnabled(true);
		
		
	}

}

package uk.co.platosys.platax.client.forms.popups;

import uk.co.platosys.platax.client.widgets.labels.PopupHeaderLabel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class AbstractPopupForm extends PopupPanel{
	FlexTable table = new FlexTable();
	PopupHeaderLabel header = new PopupHeaderLabel();
	Button closeButton = new Button();
	public PopupPanel.PositionCallback poscall =(new PopupPanel.PositionCallback() {
        public void setPosition(int offsetWidth, int offsetHeight) {
          int left = (Window.getClientWidth() - offsetWidth) / 2;
          int top = (Window.getClientHeight() - offsetHeight) / 3;
          AbstractPopupForm.this.setPopupPosition(left, top);
        }
      });
	public AbstractPopupForm(String headText){
		header.setText(headText);
		this.setWidget(table);
		setGlassEnabled(true);
		
	    closeButton.addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			
			AbstractPopupForm.this.hide();
		}
		
	});
}}

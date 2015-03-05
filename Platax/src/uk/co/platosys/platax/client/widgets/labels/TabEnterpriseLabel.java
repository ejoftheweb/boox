package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.client.constants.Styles;

import com.google.gwt.user.client.ui.Label;

public class TabEnterpriseLabel extends Label {
	public TabEnterpriseLabel(String text){
		   super(text);
		   setStyleName(Styles.FORM_HEADER);
	   }

	public TabEnterpriseLabel() {
		 setStyleName(Styles.FORM_HEADER);
	}
}

package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.client.constants.Styles;

import com.google.gwt.user.client.ui.Label;

public class TabPageTitleLabel extends Label {
	public TabPageTitleLabel(String text){
		   super(text);
		   setStyleName(Styles.FORM_HEADER);
	   }

	public TabPageTitleLabel() {
		 setStyleName(Styles.FORM_HEADER);
	}
}

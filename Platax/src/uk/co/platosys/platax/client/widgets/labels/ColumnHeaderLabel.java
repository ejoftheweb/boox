package uk.co.platosys.platax.client.widgets.labels;

import com.google.gwt.user.client.ui.Label;

public class ColumnHeaderLabel extends InlineLabel {
   public ColumnHeaderLabel(String text){
	   super(text);
	   setStyleName("columnHeader");
   }
}

package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.client.constants.Styles;

public class ColumnHeaderLabel extends InlineLabel {
   public ColumnHeaderLabel(String text){
	   super(text);
	   setStyleName(Styles.COLUMN_HEADER);
   }
}

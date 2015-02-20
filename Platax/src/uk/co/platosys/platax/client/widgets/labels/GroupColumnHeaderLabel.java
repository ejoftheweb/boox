package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.client.constants.Styles;

public class GroupColumnHeaderLabel extends InlineLabel {
   public GroupColumnHeaderLabel(String text){
	   super(text);
	   setStyleName(Styles.GROUP_COLUMN_HEADER);
   }
}

package uk.co.platosys.platax.client.widgets.labels;

import uk.co.platosys.platax.client.constants.Styles;

import com.google.gwt.user.client.ui.Label;

public class MoneyColumnHeaderLabel extends Label {
	public MoneyColumnHeaderLabel(String text){
		   super(text);
		   setStyleName(Styles.MONEY_COLUMN_HEADER);
}}

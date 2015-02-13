package uk.co.platosys.pws.fieldsets;

import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.constants.Nations;
import uk.co.platosys.pws.inputfields.ListValueField;

public class NationalityField extends ListField {
	public NationalityField(String[] labelText, int position, Form parent,	boolean required) throws IllegalArgumentException {
		super(labelText, position, parent, required);
		this.list=new ListValueField();
		setWidget(list);
		addItems(Nations.getNationalitiesByName(), true);
		
		start();
	}
	
}

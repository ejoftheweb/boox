package uk.co.platosys.platax.client.forms.lists;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTabPanel;
import uk.co.platosys.platax.client.forms.AbstractForm;

public abstract class AbstractList extends AbstractForm  {
public final int SELECTION_ALL=64;
public final int SELECTION_PAID=32;
public final int SELECTION_PENDING=24;
public final int SELECTION_OVERDUE=16;


SimplePager pager= new SimplePager();

//this abstract method must handle paging over longer lists.
	public AbstractList(Platax platax, String header, int list_selection_type) {
		super( platax);
		//form.add(table);
		
	}
	
	/**
	//Use DataGrid
	@Override
	public Widget onInitialize(){
		
	}*/
}

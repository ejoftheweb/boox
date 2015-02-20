package uk.co.platosys.platax.client.forms.lists;

import com.google.gwt.user.cellview.client.SimplePager;
import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.forms.AbstractForm;

public abstract class AbstractList extends AbstractForm  {
public final int SELECTION_ALL=64;
public final int SELECTION_PAID=32;
public final int SELECTION_PENDING=24;
public final int SELECTION_OVERDUE=16;


SimplePager pager= new SimplePager();

//this abstract method must handle paging over longer lists.
	public AbstractList(int list_selection_type) {
		super();
		//form.add(table);
		
	}
	
	/**
	//Use DataGrid
	@Override
	public Widget onInitialize(){
		
	}*/
}

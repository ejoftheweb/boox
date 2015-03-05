package uk.co.platosys.platax.client.forms.lists;

import com.google.gwt.user.cellview.client.SimplePager;

import uk.co.platosys.platax.client.components.EFTab;


public abstract class LTab extends EFTab  {
public final int SELECTION_ALL=64;
public final int SELECTION_PAID=32;
public final int SELECTION_PENDING=24;
public final int SELECTION_OVERDUE=16;


SimplePager pager= new SimplePager();

//this abstract method must handle paging over longer lists.
	public LTab(int list_selection_type) {
		super();
		//form.add(table);
		
	}
	
	/**
	//Use DataGrid
	@Override
	public Widget onInitialize(){
		
	}*/
}

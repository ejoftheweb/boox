package uk.co.platosys.platax.client.widgets;

import java.util.Iterator;
import java.util.List;

import uk.co.platosys.platax.shared.boox.GWTContact;
import uk.co.platosys.platax.shared.boox.GWTItem;

import com.google.gwt.user.client.ui.ListBox;

public class ItemListBox extends ListBox {
	
   public void addItems(List<? extends GWTItem> gwtItems){
	   Iterator<? extends GWTItem> cit = gwtItems.iterator();
	   while(cit.hasNext()){
		   GWTItem gwtItem=cit.next();
		   addItem(gwtItem.getName(),gwtItem.getItemID());
	   }
   }

}

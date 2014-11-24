package uk.co.platosys.platax.client.widgets;

import java.util.Iterator;
import java.util.List;

import uk.co.platosys.boox.trade.Contact;
import uk.co.platosys.platax.shared.boox.GWTContact;

import com.google.gwt.user.client.ui.ListBox;

public class ContactListBox extends ListBox {
	
   public void addContacts(List<? extends GWTContact> contacts){
	   Iterator<? extends GWTContact> cit = contacts.iterator();
	   while(cit.hasNext()){
		   GWTContact contact=cit.next();
		   addItem(contact.getName(), contact.getSysname());
	   }
   }

}

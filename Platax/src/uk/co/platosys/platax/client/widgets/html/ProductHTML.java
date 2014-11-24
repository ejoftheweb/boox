package uk.co.platosys.platax.client.widgets.html;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.forms.CustomerForm;
import uk.co.platosys.platax.client.forms.ProductForm;
import uk.co.platosys.platax.client.widgets.PlataxTabPanel;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;

public class ProductHTML extends AnchorHTML {

	public ProductHTML(final GWTItem item, final PlataxTabPanel context) {
	    super(item.getName(), context);
		setStyleName("px_product_link");
		addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				ProductHTML.this.context.addTab(new ProductForm(context,item));
				
			}
			 
		});
		 
	}

}

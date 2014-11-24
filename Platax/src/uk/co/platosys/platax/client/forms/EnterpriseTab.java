package uk.co.platosys.platax.client.forms;

import java.util.Iterator;
import java.util.List;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.forms.bills.InvoiceForm;
import uk.co.platosys.platax.client.services.EnterpriseService;
import uk.co.platosys.platax.client.services.EnterpriseServiceAsync;
import uk.co.platosys.platax.client.services.UserService;
import uk.co.platosys.platax.client.services.UserServiceAsync;
import uk.co.platosys.platax.client.widgets.AddressWidget;
import uk.co.platosys.platax.client.widgets.EnterpriseMenu;
import uk.co.platosys.platax.client.widgets.PlataxTabPanel;
import uk.co.platosys.platax.client.widgets.labels.FormHeaderLabel;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTRatio;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * this is the root tab for exploring an enterprise's accounts.
 * @author edward
 *
 */

public class EnterpriseTab extends AbstractForm {
				
			//Declare Variables
			
			public EnterpriseTab(Platax platax, GWTEnterprise enterprise){
				super(enterprise.getName());
				reset(platax, enterprise);
			}
			
			private void reset(Platax pplatax, final GWTEnterprise enterprise){
			final Platax platax=pplatax;
			final PlataxTabPanel ptp = pplatax.getPtp();	
		    String name =  LabelText.ENTERPRISE_NAME;
		    String legalName=LabelText.ENTERPRISE_LEGAL_NAME;
		    try{
		    	name=enterprise.getName();
		    	legalName=enterprise.getLegalName();
		    }catch(Exception x){
		    	
		    }
			//Layout Page
			this.setTabHeaderText(name);
			topLabel.setText(legalName);
			form.add(topLabel);
			
			HorizontalPanel hpanel = new HorizontalPanel();
			//The action buttons:
			final Button invoiceButton = new Button(ButtonText.INVOICE);
			final Button billButton= new Button(ButtonText.BILL);
			final Button bankButton = new Button(ButtonText.BANK);
			final Button wageButton = new Button(ButtonText.WAGES);
			final Button exitButton = new Button(ButtonText.EXIT);
			hpanel.add(invoiceButton);
			hpanel.add(billButton);
			hpanel.add(bankButton);
			hpanel.add(wageButton);
			hpanel.add(exitButton);
			form.add(hpanel);
		    EnterpriseMenu eMenu = new EnterpriseMenu(enterprise, platax);
		    form.add(eMenu);
			//handlers:
			invoiceButton.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					InvoiceForm itab = new InvoiceForm(platax, enterprise);	
					platax.addTab(itab);
					int etabindex = ptp.getWidgetIndex(itab);
					ptp.selectTab(etabindex);
				}
			});
			exitButton.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					int etabindex = ptp.getWidgetIndex(EnterpriseTab.this);
					ptp.remove(etabindex);
				}
			});
			//The key ratios table
			if(enterprise!=null){
				table.setWidget(1,1, new Label(LabelText.KEY_RATIOS_HEADER));
				List<GWTRatio> ratioList = enterprise.getRatios();
				Iterator<GWTRatio> rit = ratioList.iterator();
				int rowno=2;
				while (rit.hasNext()){
					GWTRatio ratio = rit.next();
					table.setWidget(rowno, 0, new Label(ratio.getName()));
					table.setWidget(rowno, 1, new Label(ratio.getValue()));
					Anchor anchor = new Anchor(LabelText.MORE);
					anchor.setTarget(ratio.getTarget());
					table.setWidget(rowno, 2, anchor);
					table.setWidget(rowno, 3, new Label(ratio.getInfo()));
					rowno++;
				}
			}			
			form.add(table);
		
		
			
			
			
		}
}

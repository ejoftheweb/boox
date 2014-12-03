package uk.co.platosys.platax.client.forms;

import java.util.Iterator;
import java.util.List;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.Styles;
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
				super(platax, enterprise.getName());
				setStyleName(Styles.PTAB_ENTERPRISE);
				setHeadStyleName(Styles.PTABH_ENTERPRISE);
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
			
			
		    EnterpriseMenu eMenu = new EnterpriseMenu(enterprise, platax);
		    form.add(eMenu);
			//handlers:
			
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

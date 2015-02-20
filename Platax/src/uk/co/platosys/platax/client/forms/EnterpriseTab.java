package uk.co.platosys.platax.client.forms;

import java.util.Iterator;
import java.util.List;

import uk.co.platosys.platax.client.EnterpriseMenu;
import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTabPanel;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.Styles;
import uk.co.platosys.platax.client.widgets.html.StringHTML;
import uk.co.platosys.platax.client.widgets.labels.FormHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyLabel;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTRatio;
import uk.co.platosys.pws.labels.FieldInfoLabel;
import uk.co.platosys.pws.labels.FieldLabel;

/**
 * this is the root tab for exploring an enterprise's accounts.
 * 
 * 
 * @author edward
 *
 */

public class EnterpriseTab extends AbstractForm {
				
			//Declare Variables
			
			public EnterpriseTab(Platax platax, GWTEnterprise enterprise){
				super(platax, enterprise.getName());
				setEnterprise(enterprise);
				setStyleName(Styles.PTAB_ENTERPRISE);
				setHeadStyleName(Styles.PTABH_ENTERPRISE);
				reset(platax, enterprise);
			}
			
			private void reset(Platax pplatax, final GWTEnterprise enterprise){
				
			final Platax platax=pplatax;
			final PTabPanel ptp = pplatax.getPtp();	
			platax.setCurrentEnterprise(enterprise);
		    String name =  LabelText.ENTERPRISE_NAME;
		    String legalName=LabelText.ENTERPRISE_LEGAL_NAME;
		    try{
		    	name=enterprise.getName();
		    	legalName=enterprise.getLegalName();
		    }catch(Exception x){
		    	
		    }
			//Layout Page
			this.setTabHead(new StringHTML(name));
			setTitle(legalName);
			
			
		    EnterpriseMenu eMenu = new EnterpriseMenu(enterprise, platax);
		    //eMenu.configure(enterprise, pUser user);
		    menuPanel.add(eMenu);
			//handlers:
			
			//The key ratios table
			if(enterprise!=null){
				table.setWidget(1,1, new FormHeaderLabel(LabelText.KEY_RATIOS_HEADER));
				List<GWTRatio> ratioList = enterprise.getRatios();
				Iterator<GWTRatio> rit = ratioList.iterator();
				int rowno=2;
				while (rit.hasNext()){
					GWTRatio ratio = rit.next();
					table.setWidget(rowno, 0, new FieldLabel(ratio.getName()));
					table.setWidget(rowno, 1, new MoneyLabel(ratio.getValue()));
					table.setWidget(rowno, 2, new FieldInfoLabel(ratio.getInfo()));
					rowno++;
				}
			}			
			formPanel.add(table);
		
		
			
			
			
		}

			@Override
			public void refresh() {
				// TODO Auto-generated method stub
				
			}
}

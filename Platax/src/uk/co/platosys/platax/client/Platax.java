package uk.co.platosys.platax.client;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.platax.client.forms.AbstractForm;
import uk.co.platosys.platax.client.forms.LoginForm;
import uk.co.platosys.platax.client.forms.UserTab;
import uk.co.platosys.platax.client.components.MessagePanel;
import uk.co.platosys.platax.client.components.BrandingBox;
import uk.co.platosys.platax.client.components.StatusBox;
import uk.co.platosys.platax.client.components.TaskPanel;
import uk.co.platosys.platax.client.forms.RegisterUser;
import uk.co.platosys.platax.client.widgets.PTab;
import uk.co.platosys.platax.client.widgets.PlataxTabPanel;
import uk.co.platosys.platax.shared.PXUser;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
//import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;



public class Platax implements EntryPoint {
	//declare components
	DockLayoutPanel dlp = new DockLayoutPanel(Unit.PCT);
	DockLayoutPanel topPanel = new DockLayoutPanel(Unit.PCT);
	//LoginBox loginBox = new LoginBox();
	BrandingBox brandingBox=new BrandingBox();
	StatusBox statusBox=new StatusBox(this);
	TaskPanel taskPanel = new TaskPanel();
	MessagePanel messagePanel = new MessagePanel();
	PlataxTabPanel tabPanel = new PlataxTabPanel(5, Unit.PCT);
	FlowPanel footPanel=new FlowPanel();
	PXUser pxUser=null;
	
	
	
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	
	
	
	public void onModuleLoad() {
		//create panels and components
		//Top Panel
		topPanel.addWest(brandingBox, 74 );
		
		topPanel.addEast(statusBox, 25);//, DockLayoutPanel.Direction.WEST, 300, null);
		
		dlp.addNorth(topPanel, 10);
		//footPanel
		footPanel.add(new Label("footer"));
		dlp.addSouth(footPanel, 10);
		
		//the task panel (to the left);
		dlp.addWest(taskPanel, 15);
		
		//the message panel
		dlp.addEast(messagePanel, 25);
		
		//the tab panel
		//At startup, we add only these two panels. 
		List<PTab> pTabs=new ArrayList<PTab>();
		pTabs.add(new LoginForm(this));
		pTabs.add(new RegisterUser(this));
	
	
		//tabPanel=new PlataxTabPanel(5, Unit.PCT);
		tabPanel.addTabs(pTabs);		
		dlp.add(tabPanel);
		
		RootLayoutPanel.get().add(dlp);
		
		//TODO add handlers!
	}




	/**
	 * @return the tabPanel
	 */
	public PlataxTabPanel getPtp() {
		return tabPanel;
	}




	public void addTab(PTab itab) {
		tabPanel.addTab(itab);
		
	}

    public void addTab(PTab itab, boolean select){
    	tabPanel.addTab(itab);
    	
    }


	public void setUser(PXUser result) {
		pxUser=result;
		statusBox.login(result.getUsername());
		tabPanel.addTab(new UserTab(this, result),0);
	}




	public void removeTab(PTab tab) {
		tabPanel.remove(tab);
		
	}

    public void removeAllTabs(){
    	tabPanel.clear();
    }


	public void logout() {
		pxUser=null;
		tabPanel.clear();
		tabPanel.addTab(new RegisterUser(this));
		tabPanel.addTab(new LoginForm(this),0);
		tabPanel.selectTab(0);
		
	} 
		
}
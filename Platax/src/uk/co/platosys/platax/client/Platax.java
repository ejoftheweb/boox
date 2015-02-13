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
import uk.co.platosys.platax.client.widgets.PlataxTabPanel;
import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
//import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
/**
 * This is the base class for the application.
 * It extends DockLayoutPanel and is attached to the RootLayoutPanel.
 * 
 * It is essential that this is the ONLY WIDGET attached to the RootLayoutPanel. Otherwise loads of things will break.
 * 
 * @author edward
 *
 */


public class Platax  extends DockLayoutPanel implements EntryPoint{
	//declare components
	//DockLayoutPanel dlp = new DockLayoutPanel(Unit.PCT);
	DockLayoutPanel topPanel = new DockLayoutPanel(Unit.PCT);
	BrandingBox brandingBox=new BrandingBox();
	StatusBox statusBox=new StatusBox(this);
	TaskPanel taskPanel = new TaskPanel(this);
	MessagePanel messagePanel = new MessagePanel();
	PlataxTabPanel tabPanel = new PlataxTabPanel(5, Unit.PCT);
	FlowPanel footPanel=new FlowPanel();
	PXUser pxUser=null;
	GWTEnterprise currentEnterprise;
	
	
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";
   
	private Platax(){
		super(Unit.PCT);
		Window.alert("Platax constructor called");
	}
	
	
	
	public void onModuleLoad() {
		Window.alert("on module load called");
		//create panels and components
		//Top Panel
		topPanel.addWest(brandingBox, 75 );
		
		topPanel.addEast(statusBox, 25);//, DockLayoutPanel.Direction.WEST, 300, null);
		
		this.addNorth(topPanel, 10);
		//footPanel
		footPanel.add(new Label("footer"));
		this.addSouth(footPanel, 10);
		
		//the task panel (to the left);
		this.addWest(taskPanel, 15);
		
		//the message panel
		this.addEast(messagePanel, 25);
		List<PTab> pTabs=new ArrayList<PTab>();
		pTabs.add(new LoginForm(this));
		tabPanel.addTabs(pTabs);		
		this.add(tabPanel);
		try{
		RootLayoutPanel.get().insert(this,0);
		}catch(Exception e){
			Window.alert(e.getMessage());
		}
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
    public static void addPTab(PTab itab){
    	getCurrentInstance().addTab(itab);
    }
    public static Platax getCurrentInstance(){
    	return(Platax) RootLayoutPanel.get().getWidget(0);
    }
    public void addTab(PTab itab, boolean select){
    	tabPanel.addTab(itab);
    }


	public void setUser(PXUser result) {
		pxUser=result;
		statusBox.login(result.getUsername());
		tabPanel.addTab(new UserTab(this, result),0);
		taskPanel.setUser(result);
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
		taskPanel.clear();
		tabPanel.addTab(new RegisterUser(this));
		tabPanel.addTab(new LoginForm(this),0);
		tabPanel.selectTab(0);
		
	} 
	public void setSelectedTab(PTab tab){
		tabPanel.selectTab(tab);
	}



	public GWTEnterprise getCurrentEnterprise() {
		return currentEnterprise;
	}



	public void setCurrentEnterprise(GWTEnterprise currentEnterprise) {
		this.currentEnterprise = currentEnterprise;
		statusBox.setEnterprise(currentEnterprise);
	}
}
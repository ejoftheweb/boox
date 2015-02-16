package uk.co.platosys.platax.client.components;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.Styles;
import uk.co.platosys.platax.client.widgets.labels.StatusLabel;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;

/**
 * The status box displays the login status of the session. It shows the username of the logged-in pxuser and the 
 * usename of the  current enterprise. 
 * 
 * @author edward
 *
 */
public class StatusBox extends Composite {
	//private StatusLabel statusLabel=new StatusLabel();
    private StatusLabel nameLabel=new StatusLabel(LabelText.NOT_LOGGED_IN);
    private StatusLabel enterpriseLabel=new StatusLabel(LabelText.NO_ENTERPRISE);
    private Button logoutButton= new Button(LabelText.LOGOUT);
    private Button cancelButton = new Button(LabelText.CLEAR);
	public StatusBox(final Platax platax) {
		FlowPanel loginPanel = new FlowPanel();
		FlowPanel enterprisePanel = new FlowPanel();
		FlowPanel mainPanel = new FlowPanel();
		initWidget(mainPanel);
		setStyleName(Styles.STATUS_BOX);
		
		
		mainPanel.add(loginPanel);
		loginPanel.add(nameLabel);
		logoutButton.setEnabled(false);
		loginPanel.add(logoutButton);
		logoutButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				if(Window.confirm(StringText.REALLY_LOGOUT)){
					platax.logout();
					logout();
				}
			}
		});
		mainPanel.add(enterprisePanel);
		enterprisePanel.add(enterpriseLabel);
		enterprisePanel.add(cancelButton);
	}
   public void login(String username){
	   //statusLabel.setText(LabelText.LOGGED_IN_AS);
	   nameLabel.setStyleName(Styles.STATUS_LOGGEDIN);
	   nameLabel.setText(username);
	   logoutButton.setEnabled(true);
   }
   public void logout(){
	  // statusLabel.setText(LabelText.NOT_LOGGED_IN);
	   nameLabel.setStyleName(Styles.STATUS_LOGGEDOUT);
	   nameLabel.setText(LabelText.NOT_LOGGED_IN);
	   logoutButton.setEnabled(false);
   }
   public void setEnterprise(GWTEnterprise enterprise){
	   enterpriseLabel.setText(enterprise.getName());
   }
}

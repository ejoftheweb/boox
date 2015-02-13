package uk.co.platosys.platax.client.components;

import uk.co.platosys.platax.client.Platax;
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

public class StatusBox extends Composite {
	private StatusLabel statusLabel=new StatusLabel();
    private StatusLabel nameLabel=new StatusLabel(LabelText.NOT_LOGGED_IN);
    private StatusLabel enterpriseLabel=new StatusLabel(LabelText.NO_ENTERPRISE);
    private Button logoutButton= new Button(LabelText.LOGOUT);
	public StatusBox(final Platax platax) {
		
		FlowPanel mainPanel = new FlowPanel();
		initWidget(mainPanel);
		setStyleName(Styles.STATUS_BOX);
		
		
		mainPanel.add(statusLabel);
		mainPanel.add(nameLabel);
		logoutButton.setEnabled(false);
		mainPanel.add(logoutButton);
		logoutButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				if(Window.confirm(StringText.REALLY_LOGOUT)){
					platax.logout();
					logout();
				}
			}
		});
		mainPanel.add(enterpriseLabel);
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

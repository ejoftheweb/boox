package uk.co.platosys.platax.client.forms;

import java.util.Iterator;
import java.util.List;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTabPanel;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.Styles;
import uk.co.platosys.platax.client.services.LoginService;
import uk.co.platosys.platax.client.services.LoginServiceAsync;
import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;

public class UserTab extends AbstractForm {
	public UserTab(Platax pplatax, PXUser user) {
		super(pplatax);
		
    setCloseEnabled(false);
    setStyleName(Styles.PTAB_CONTENT_STYLE);
    setHeadStyleName(Styles.PTABH_STYLE);
    //pTab info fields
	setTabHeaderText(user.getUsername());
    setTitle(LabelText.USER_HI + user.getUsername());
	setSubTitle(LabelText.USER_SUMMARY);
	final Platax platax = pplatax;
    final PTabPanel ptp = platax.getPtp();
	final FlexTable loggedinTable = new FlexTable();
	//final Button logoutButton = new Button(ButtonText.LOGOUT);	
	
	final Label lastLoginLabel = new Label(LabelText.LAST_LOGIN);
	final Label lastLoginFromLabel = new Label (LabelText.FROM);
	final Label lastLogoutLabel = new Label (LabelText.LAST_LOGOUT);
	
	final Label lastLogin = new Label();
	final Label lastLoginFrom = new Label();
	final Label lastLogout = new Label();
	final LoginServiceAsync loginService = (LoginServiceAsync) GWT.create(LoginService.class);
	
	//callback methods

	final AsyncCallback<Boolean> logoutcallback=new AsyncCallback<Boolean>(){
		public void onSuccess(Boolean result){
			platax.removeTab(UserTab.this);
			platax.logout();
			
			
		}
		public void onFailure(Throwable cause){
			StackTraceElement[] st = cause.getStackTrace();
			   String error = "logout failed\n";
			   error = error+cause.getClass().getName()+"\n";
			   for (int i=0; i<st.length; i++){
				   error = error + st[i].toString()+ "\n";
			   }
				Window.alert(error);
		}
	};
	//layout page
	  //create the loggedout table
		loggedinTable.setWidget(0,0, lastLoginLabel);
        loggedinTable.setWidget(0,1, lastLogin);
		loggedinTable.setWidget(0,2, lastLoginFromLabel);
		loggedinTable.setWidget(0,3, lastLoginFrom);
		loggedinTable.setWidget(1,0, lastLogoutLabel);
		loggedinTable.setWidget(1,1, lastLogout);
		//loggedinTable.setWidget(2,0, logoutButton);
			lastLogin.setText(user.getLastLogin());
			lastLoginFrom.setText(user.getLastLoginfrom());
			lastLogout.setText(user.getLastLogout());
		loggedinTable.setWidget(3,1, new Label(LabelText.YOUR_ENTERPRISES));
		List<GWTEnterprise> enterprises=user.getEnterprises();
		Iterator<GWTEnterprise> it = enterprises.iterator();
		int rowno = 4;
		while(it.hasNext()){
			final GWTEnterprise enterprise = it.next();
			//final EnterpriseTab etab = new EnterpriseTab(platax, enterprise);
			final Anchor anchor = new Anchor(enterprise.getName());
			loggedinTable.setWidget(rowno, 0, new Label(Integer.toString(rowno-3)));
			loggedinTable.setWidget(rowno, 1, anchor);
			
			anchor.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					if (enterprise.hasOpenTab()){
						ptp.selectTab(enterprise.getOpenTabIndex());
					}else{
						platax.addTab(new EnterpriseTab(platax, enterprise));
					}
				}
			});
			rowno++;
		}
		final  Anchor anchor = new Anchor("Register a new one");
		loggedinTable.setWidget(rowno, 1, anchor);
		
		anchor.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
					platax.addTab(new AddEnterpriseForm(platax), true);
				}
			}
		);
		formPanel.add(loggedinTable);
		this.add(formPanel);
		}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

}

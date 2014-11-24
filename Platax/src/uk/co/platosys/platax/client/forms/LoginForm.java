package uk.co.platosys.platax.client.forms;


import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.services.LoginService;
import uk.co.platosys.platax.client.services.LoginServiceAsync;
import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.exceptions.LoginException;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;

public class LoginForm extends AbstractForm { 
	//declare variables
	//final Label topLabel = new Label(StringText.LOGIN_OR_SIGNUP);
	
	final Label emailLabel = new Label(LabelText.EMAIL);
	final TextBox emailTextBox = new TextBox();
	final FlexTable table = new FlexTable();
	final Label passwordLabel=new Label(LabelText.PASSWORD);
	final TextBox passwordTextBox= new PasswordTextBox();
	final CheckBox chckbxNewCheckBox = new CheckBox(LabelText.REMEMBER_ME);
	final Button loginButton = new Button(StringText.LOGIN);
	
	final LoginServiceAsync loginService = (LoginServiceAsync) GWT.create(LoginService.class);

	public LoginForm(Platax pplatax) {
		super();
		//DEV-CODE only//
		emailTextBox.setText("edward@copyweb.co.uk");
		passwordTextBox.setText("P&nNfZ36");
		//end of dev code//
		topLabel.setText(StringText.LOGIN);
		subHeader.setText(StringText.LOGIN_OR_SIGNUP);
		setTabHeaderText(StringText.LOGIN);
		final Platax platax = pplatax;	
		//callback methods
		final AsyncCallback<PXUser> logincallback = new AsyncCallback<PXUser>(){
			public void onSuccess(PXUser result){
				if (result.isAuthenticated()){
					platax.removeAllTabs();
					platax.setUser(result);
					
				}else{
					topLabel.setText(StringText.LOGIN_FAILED);
					form.add(table);
				}
			}
		    public void onFailure(Throwable cause){
		    	StackTraceElement[] st = cause.getStackTrace();
				   String error = "server failure at login\n";
				   error = error+cause.getClass().getName()+"\n";
				   for (int i=0; i<st.length; i++){
					   error = error + st[i].toString()+ "\n";
				   }
					Window.alert(error);
				
					  
		}};
		loginButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (emailTextBox.getText().length() == 0
						|| passwordTextBox.getText().length() == 0) {
						Window.alert(StringText.EMAIL_EMPTY); 
					}
				try {
					loginService.login(emailTextBox.getText(), passwordTextBox.getText(), logincallback);
					form.remove(table);
					topLabel.setText(StringText.THANKYOU);
					subHeader.setText(StringText.WAIT_FOR_SERVER);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					StackTraceElement[] st = e.getStackTrace();
					
					 String error = "server error at login\n";
					   error = error+e.getClass().getName()+"\n";
					   for (int i=0; i<st.length; i++){
						   error = error + st[i].toString()+ "\n";
					   }
						Window.alert(error);
				}
			}
		});
		
		layout();
		//vpanel.add(form);
		//this.add(form);
	}
	
	public void layout (){
	   //this.setHeader(StringText.LOGIN);
	   	//topLabel.setText(StringText.LOGIN_OR_SIGNUP);
		//vpanel.add(topLabel);
		//create the loggedout table
		table.setWidget(0, 0, emailLabel);
		table.setWidget(0, 1, emailTextBox);
		table.setWidget(1, 0, passwordLabel);
		table.setWidget(1, 1, passwordTextBox);
		table.setWidget(2, 1, chckbxNewCheckBox);
		table.setWidget(3, 1, loginButton);
		form.add(table);
		
		
	
	
		
		
	}	
	}



package uk.co.platosys.platax.client.forms;


import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.services.LoginService;
import uk.co.platosys.platax.client.services.LoginServiceAsync;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
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

public class AddProductForm {//extends AbstractForm { 
	//declare variables
	final Label topLabel = new Label("Adding Products to the Catalogue");
	
	final Label emailLabel = new Label("email");
	final TextBox emailTextBox = new TextBox();
	final FlexTable table = new FlexTable();
	final Label passwordLabel=new Label("password");
	final TextBox passwordTextBox= new PasswordTextBox();
	final CheckBox chckbxNewCheckBox = new CheckBox("remember me");
	final Button loginButton = new Button("login");
	
	//final ProductServiceAsync ProductService = (ProductServiceAsync) GWT.create(ProductService.class);

	public AddProductForm(Platax pplatax) {}/*
		super(pplatax.getPtp());
		final Platax platax = pplatax;	
		//callback methods
		final AsyncCallback<PXUser> logincallback = new AsyncCallback<PXUser>(){
			public void onSuccess(PXUser result){
				platax.setUser(result);
				platax.getPtp().remove(AddProductForm.this);
			}
		public void onFailure(Throwable cause){
			topLabel.setText("Login Failed - please try again");
			form.add(table);
			
		   //Debugging code
			StackTraceElement[] st = cause.getStackTrace();
		   String error = "login failed\n";
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
						Window.alert("email or password is empty."); 
					}
				try {
					form.remove(table);
					topLabel.setText("please wait while the server verifies your credentials");
					loginService.login(emailTextBox.getText(), passwordTextBox.getText(), logincallback);
				} catch (LoginException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		layout();
		vpanel.add(form);
		this.add(vpanel);
	}
	
	public void layout (){
	   this.setHeader("Login");
	   	topLabel.setText("Please Login or Register to continue");
		vpanel.add(topLabel);
		//create the loggedout table
		table.setWidget(0, 0, emailLabel);
		table.setWidget(0, 1, emailTextBox);
		table.setWidget(1, 0, passwordLabel);
		table.setWidget(1, 1, passwordTextBox);
		table.setWidget(2, 1, chckbxNewCheckBox);
		table.setWidget(3, 1, loginButton);
		form.setWidget(table);
		
		
	
	
		
		
	}	*/
	}



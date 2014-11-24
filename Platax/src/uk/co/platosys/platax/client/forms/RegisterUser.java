package uk.co.platosys.platax.client.forms;




import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.services.UserService;
import uk.co.platosys.platax.client.services.UserServiceAsync;
import uk.co.platosys.platax.shared.Constants;
import uk.co.platosys.platax.shared.FieldVerifier;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

//TODO Abstract strings to Constants file.

public class RegisterUser extends AbstractForm {
	public RegisterUser(final Platax platax){
		super();
		//PTab/AF fields
		setTabHeaderText(StringText.SIGNUP);
		topLabel.setText(StringText.SIGNUP_HEADER);
		subHeader.setText(StringText.SIGNUP_SUBTEXT);
		
		//Declare Variables
		//final Label topLabel = new Label("Please enter your details to register as a PLATAX user");
		final Button submitButton=new Button("Submit");
		submitButton.setEnabled(false);
		final Label submitInfoLabel=new Label("");
		final Label submitLabel=new Label("submit");
		
		
		//email box
		final TextBox emailBox = new TextBox();
		final Label emailLabel= new Label("email");
		final Label emailInfoLabel=new Label(LabelText.EMAIL);
		emailBox.setEnabled(true);
		
		//username box
		final TextBox usernameBox = new TextBox();
		final Label usernameLabel= new Label("nickname");
		final Label usernameInfoLabel=new Label(LabelText.USERNAME);
		usernameBox.setEnabled(false);
		
		//password box
		final PasswordTextBox passwordBox = new PasswordTextBox();
		final Label passwordLabel= new Label("password");
		final Label passwordInfoLabel=new Label(LabelText.SECURE_PASSWORD);
		passwordBox.setEnabled(false);
		
		final PasswordTextBox confirmBox = new PasswordTextBox();
		final Label confirmLabel= new Label("confirm");
		final Label confirmInfoLabel=new Label(LabelText.CONFIRM_PASSWORD);
		confirmBox.setEnabled(false);
		
		final CheckBox investorBox = new CheckBox("investor");
		
		final Label investorInfoLabel = new Label(LabelText.INVESTOR);
		
		final CheckBox termsBox=new CheckBox("terms");
		final Label termsInfoLabel = new Label(LabelText.TERMS);
		
		final UserServiceAsync userService = (UserServiceAsync) GWT.create(UserService.class);
		final AsyncCallback<String> callback = new AsyncCallback<String>(){
			public void onSuccess(String result){
				if(result.equals(Constants.OK)){
					Window.alert(StringText.CHECK_EMAIL);
					platax.removeTab(RegisterUser.this);
				}else if(result.equals(Constants.USER_EXISTS)){
					if(Window.confirm(StringText.USER_EXISTS)){
						//reset password page;
					}else{
						form.add(table);
					}
				}else{
					topLabel.setText(result);
				}
			}
			public void onFailure(Throwable cause){
			   StackTraceElement[] st = cause.getStackTrace();
			   String error = "registration failed\n";
			   error = error+cause.getClass().getName()+"\n";
			   for (int i=0; i<st.length; i++){
				   error = error + st[i].toString()+ "\n";
			   }
				Window.alert(error);
			}
		};
		//Layout Page
		//this.setHeader("Register");
		//vpanel.add(topLabel);
		table.setWidget(0,0, emailLabel);
		table.setWidget(1,0, usernameLabel);
		table.setWidget(2,0, passwordLabel);
		table.setWidget(3,0, confirmLabel);
		table.setWidget(0,1, emailBox);
		table.setWidget(1,1, usernameBox);
		table.setWidget(2,1, passwordBox);
		table.setWidget(3,1, confirmBox);
		table.setWidget(0,2, emailInfoLabel);
		table.setWidget(1,2, usernameInfoLabel);
		table.setWidget(2,2, passwordInfoLabel);
		table.setWidget(3,2, confirmInfoLabel);
		table.setWidget(4,1, investorBox);
		table.setWidget(4,2, investorInfoLabel);
		table.setWidget(5,1, termsBox);
		table.setWidget(5,2, termsInfoLabel);
		table.setWidget(6,0, submitLabel);
		table.setWidget(6,1, submitButton);
		table.setWidget(6,2, submitInfoLabel);
		form.add(table);
		//vpanel.add(form);
		//this.add(vpanel);
		//Add handlers
		emailBox.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event){
				String email = emailBox.getValue();
				if (FieldVerifier.isValidEmail(email)){
					usernameBox.setEnabled(true);
				     emailBox.setFocus(false);
				     usernameBox.setFocus(true);
				     emailInfoLabel.setText(LabelText.OK);
				}else{
					emailBox.setValue("");
					emailBox.setFocus(true);
					emailInfoLabel.setText(LabelText.BAD_EMAIL);
				}
				
			}
		});
		usernameBox.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event){
				String username = usernameBox.getValue();
				if (FieldVerifier.isValidUsername(username)){
				    passwordBox.setEnabled(true);
				    usernameBox.setFocus(false);
					passwordBox.setFocus(true);
				    usernameInfoLabel.setText(LabelText.OK);
				}else{
					usernameBox.setValue("");
					usernameBox.setFocus(true);
					usernameInfoLabel.setText(LabelText.BAD_USERNAME);
				}
				
			}
		});
		passwordBox.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event){
				String password = passwordBox.getValue();
				if (FieldVerifier.isValidPassword(password)){
				    confirmBox.setEnabled(true);
				    passwordBox.setFocus(false);
					confirmBox.setFocus(true);
				    passwordInfoLabel.setText(LabelText.OK);
				}else{
					passwordBox.setValue("");
					passwordBox.setFocus(true);
					passwordInfoLabel.setText(LabelText.POOR_PASSWORD);
				}
				
			}
		});
		confirmBox.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event){
				String confirm = confirmBox.getValue();
				if (FieldVerifier.confirms(passwordBox.getValue(), confirm)){
					submitButton.setEnabled(true);
					confirmBox.setFocus(false);
				    termsBox.setFocus(true);
				    confirmInfoLabel.setText(LabelText.OK);
				    
				}else{
					passwordBox.setValue("");
					confirmBox.setValue("");
					confirmBox.setEnabled(false);
					passwordBox.setEnabled(true);
					confirmBox.setFocus(false);
					passwordBox.setFocus(true);
					passwordInfoLabel.setText(LabelText.MATCHLESS_PASSWORD);
					confirmInfoLabel.setText(LabelText.MATCHLESS_CONFIRM);
				}
				
			}
		});
		termsBox.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				if (termsBox.getValue()){
					submitInfoLabel.setText("Click \"submit\" to register");
					submitButton.setEnabled(true);
				}else{
					submitButton.setEnabled(false);
				}
				
			}
		});
		submitButton.addClickHandler(new ClickHandler(){
			public void onClick (ClickEvent click){
				String email = emailBox.getValue();
				String username = usernameBox.getValue();
				String password = passwordBox.getValue();
				String confirm = confirmBox.getValue();
				boolean investor = investorBox.getValue();
				boolean accept=termsBox.getValue();
				userService.registerUser(email, username, password, confirm, accept, investor, callback);
				topLabel.setText(StringText.THANKYOU);
				subHeader.setText(StringText.WAIT_FOR_SIGNUP);
				form.remove(table);
			}
		});
		
	}
	
}

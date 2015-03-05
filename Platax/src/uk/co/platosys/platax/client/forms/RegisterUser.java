package uk.co.platosys.platax.client.forms;




import uk.co.platosys.platax.client.components.FTab;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.services.UserService;
import uk.co.platosys.platax.client.services.UserServiceAsync;
import uk.co.platosys.platax.client.widgets.html.StringHTML;
import uk.co.platosys.platax.shared.Constants;
import uk.co.platosys.platax.shared.FieldVerifier;
import uk.co.platosys.pws.labels.FieldInfoLabel;
import uk.co.platosys.pws.labels.FieldLabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

//TODO Abstract strings to Constants file.

public class RegisterUser extends FTab {
	public RegisterUser(){
		super();
		setCloseConfirm(false);
		//PTab/AF fields
		setTabHead(new StringHTML(StringText.SIGNUP));
		setTitle(StringText.SIGNUP_HEADER);
		setSubTitle(StringText.SIGNUP_SUBTEXT);
		
		//Declare Variables
		//final Label topLabel = new Label("Please enter your details to register as a PLATAX user");
		final Button submitButton=new Button("Submit");
		submitButton.setEnabled(false);
		final FieldInfoLabel submitInfoLabel=new FieldInfoLabel("");
		final FieldLabel submitLabel=new FieldLabel("submit");
		
		
		//email box
		final TextBox emailBox = new TextBox();
		final FieldLabel emailLabel= new FieldLabel(LabelText.EMAIL);
		final FieldInfoLabel emailInfoLabel=new FieldInfoLabel(LabelText.EMAIL_INFO);
		emailBox.setEnabled(true);
		//username box
		final TextBox usernameBox = new TextBox();
		final FieldLabel usernameLabel= new FieldLabel(LabelText.USERNAME);
		final FieldInfoLabel usernameInfoLabel=new FieldInfoLabel(LabelText.USERNAME_INFO);
		usernameBox.setEnabled(false);
		//given name box
		final TextBox givennameBox = new TextBox();
		final FieldLabel givennameLabel= new FieldLabel(LabelText.GIVENNAME);
		final FieldInfoLabel givennameInfoLabel=new FieldInfoLabel(LabelText.GIVENNAME_INFO);
		givennameBox.setEnabled(false);
		
		//family name box
		final TextBox familynameBox = new TextBox();
		final FieldLabel familynameLabel= new FieldLabel(LabelText.FAMILYNAME);
		final FieldInfoLabel familynameInfoLabel=new FieldInfoLabel(LabelText.FAMILYNAME_INFO);
		familynameBox.setEnabled(false);
				
		//password box
		final PasswordTextBox passwordBox = new PasswordTextBox();
		final FieldLabel passwordLabel= new FieldLabel(LabelText.PASSWORD);
		final FieldInfoLabel passwordInfoLabel=new FieldInfoLabel(LabelText.SECURE_PASSWORD);
		passwordBox.setEnabled(false);
		
		final PasswordTextBox confirmBox = new PasswordTextBox();
		final FieldLabel confirmLabel= new FieldLabel("confirm");
		final FieldInfoLabel confirmInfoLabel=new FieldInfoLabel(LabelText.CONFIRM_PASSWORD);
		confirmBox.setEnabled(false);
		
		final CheckBox investorBox = new CheckBox("investor");
		
		final FieldInfoLabel investorInfoLabel = new FieldInfoLabel(LabelText.INVESTOR);
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
						add(table);
					}
				}else{
					setTitle(result);
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
		table.setWidget(0,1, emailBox);
		table.setWidget(0,2, emailInfoLabel);
		
		table.setWidget(1,0, usernameLabel);
		table.setWidget(1,1, usernameBox);
		table.setWidget(1,2, usernameInfoLabel);
		
		table.setWidget(2,2, givennameInfoLabel);
		table.setWidget(2,0, givennameLabel);
		table.setWidget(2,1, givennameBox);
		
		table.setWidget(3,0, familynameLabel);
		table.setWidget(3,1, familynameBox);
		table.setWidget(3,2, familynameInfoLabel);
		
		table.setWidget(4,0, passwordLabel);
		table.setWidget(4,1, passwordBox);
		table.setWidget(4,2, passwordInfoLabel);
		
		table.setWidget(5,0, confirmLabel);
		table.setWidget(5,1, confirmBox);
		table.setWidget(5,2, confirmInfoLabel);
		
		table.setWidget(6,1, investorBox);
		table.setWidget(6,2, investorInfoLabel);
		
		table.setWidget(7,0, submitLabel);
		table.setWidget(7,1, submitButton);
		table.setWidget(7,2, submitInfoLabel);
		add(table);
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
				     RegisterUser.this.setCloseConfirm(true);
				     RegisterUser.this.setCloseConfirmMessage(StringText.CLOSE_CONFIRM_LOSE_CHANGES);
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
				    givennameBox.setEnabled(true);
				    usernameBox.setFocus(false);
					givennameBox.setFocus(true);
				    usernameInfoLabel.setText(LabelText.OK);
				}else{
					usernameBox.setValue("");
					usernameBox.setFocus(true);
					usernameInfoLabel.setText(LabelText.BAD_USERNAME);
				}
			}
		});
		
		givennameBox.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event){
				String givenname = givennameBox.getValue();
				if (FieldVerifier.isValidName(givenname)){
				    familynameBox.setEnabled(true);
				    givennameBox.setFocus(false);
					familynameBox.setFocus(true);
				    givennameInfoLabel.setText(LabelText.OK);
				}else{
					givennameBox.setValue("");
					givennameBox.setFocus(true);
					givennameInfoLabel.setText(LabelText.BAD_NAME);
				}
			}
		});
		
		familynameBox.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event){
				String familyname = familynameBox.getValue();
				if (FieldVerifier.isValidName(familyname)){
				    passwordBox.setEnabled(true);
				    familynameBox.setFocus(false);
					passwordBox.setFocus(true);
				    familynameInfoLabel.setText(LabelText.OK);
				}else{
					familynameBox.setValue("");
					familynameBox.setFocus(true);
					familynameInfoLabel.setText(LabelText.BAD_NAME);
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
				    submitButton.setFocus(true);
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
		
		submitButton.addClickHandler(new ClickHandler(){
			public void onClick (ClickEvent click){
				String email = emailBox.getValue();
				String username = usernameBox.getValue();
				String name=givennameBox.getValue()+" "+familynameBox.getValue();
				String password = passwordBox.getValue();
				String confirm = confirmBox.getValue();
				boolean investor = investorBox.getValue();
				userService.registerUser(email, username, name, password, confirm, true, investor, callback);
				setTitle(StringText.THANKYOU);
				setSubTitle(StringText.WAIT_FOR_SIGNUP);
				remove(table);
			}
		});
		
	}

	
	
}

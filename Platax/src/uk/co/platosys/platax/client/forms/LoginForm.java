package uk.co.platosys.platax.client.forms;


import java.util.Iterator;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.FTab;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.TabTops;
import uk.co.platosys.platax.client.services.LoginService;
import uk.co.platosys.platax.client.services.LoginServiceAsync;
import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.pws.fieldsets.FormField;
import uk.co.platosys.pws.fieldsets.LoginField;
import uk.co.platosys.pws.fieldsets.PasswordField;
import uk.co.platosys.pws.fieldsets.SubmitField;
import uk.co.platosys.pws.fieldsets.TextField;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;

public class LoginForm extends FTab { 
	//declare variables
	//final Label topLabel = new Label(StringText.LOGIN_OR_SIGNUP);
	
	
	
	//Add form fields (from uk.co.platosys.pws.fieldsets)  here.
	TextField emailField; 
	PasswordField passwordField;
	//Add callbacks to populate lists etc
	
	LoginField sub;
    //Add handlers as needed
	
	final LoginServiceAsync loginService = (LoginServiceAsync) GWT.create(LoginService.class);

	public LoginForm() {
		super();
		//indow.alert("Login form constructor");
		this.platax=Platax.getCurrentInstance();
		setTabHead(TabTops.LOGIN);
		emailField=  new TextField(FieldText.EMAIL, 1000, this, true);
	    passwordField=   new PasswordField(FieldText.PASSWORD, 2000, this, true);
	    sub= new LoginField(12000, this);
	    final Anchor registerAnchor=new Anchor(StringText.SIGNUP_GO);
		
		//DEV-CODE only//
		emailField.setValue("edward@copyweb.co.uk");
		passwordField.setValue("P&nNfZ36");
		//end of dev code//
		setTitle(StringText.LOGIN);
		setSubTitle(StringText.LOGIN_OR_SIGNUP);
		setCloseEnabled(false);
		registerAnchor.addClickHandler(new ClickHandler(){
			 
			@Override
			public void onClick(ClickEvent event) {
				platax.addTab(new RegisterUser());
				
			}
	    });
		//callback methods
		final AsyncCallback<PXUser> logincallback = new AsyncCallback<PXUser>(){
			public void onSuccess(PXUser result){
				if (result.isAuthenticated()){
					platax.removeAllTabs();
					platax.setUser(result);
					
				}else{
					setTitle(StringText.LOGIN_FAILED);
					//getFormPanel().add(table);
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
		sub.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				try {
					loginService.login(emailField.getValue(), passwordField.getValue(), logincallback);
					//getFormPanel().remove(table);
					setTitle(StringText.THANKYOU);
					setSubTitle(StringText.WAIT_FOR_SERVER);
					LoginForm.this.clear();
					
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
		render();
		add(registerAnchor);
		//Window.alert("Login form constructor done");
	}
	
	

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void add(IsWidget widget) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<Widget> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Widget widget) {
		// TODO Auto-generated method stub
		return false;
	}

}
	


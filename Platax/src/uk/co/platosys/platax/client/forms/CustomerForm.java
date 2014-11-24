package uk.co.platosys.platax.client.forms;


import uk.co.platosys.platax.client.widgets.PlataxTabPanel;
import uk.co.platosys.platax.shared.boox.GWTCustomer;

public class CustomerForm extends AbstractForm {

	public CustomerForm(PlataxTabPanel parent, GWTCustomer gwtCustomer) {
		super( gwtCustomer.getName());
		// TODO Auto-generated constructor stub
	} 
	/*
	 * //declare variables
	final Label topLabel = new Label("Customer Details");
	
	final Label emailLabel = new Label("email");
	final TextBox emailTextBox = new TextBox();
	final FlexTable table = new FlexTable();
	final Label passwordLabel=new Label("password");
	final TextBox passwordTextBox= new PasswordTextBox();
	final CheckBox chckbxNewCheckBox = new CheckBox("remember me");
	final Button loginButton = new Button("login");
	
	final LoginServiceAsync loginService = (LoginServiceAsync) GWT.create(LoginService.class);

	public CustomerForm(platax platax, GWTCustomer customer) {
		super(platax.getPtp());
		final platax platax = platax;	
		
		
		
		
		
		
		
		
		//callback methods
		final AsyncCallback<GWTCustomer> logincallback = new AsyncCallback<GWTCustomer>(){
			public void onSuccess(GWTCustomer result){
				platax.setUser(result);
				platax.getPtp().remove(CustomerForm.this);
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
					//loginService.login(emailTextBox.getText(), passwordTextBox.getText(), logincallback);
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
		*/
		
	
	
		
		
	
	}



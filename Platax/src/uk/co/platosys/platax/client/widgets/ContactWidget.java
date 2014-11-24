package uk.co.platosys.platax.client.widgets;

import java.util.Iterator;

import uk.co.platosys.platax.client.services.AddressService;
import uk.co.platosys.platax.client.services.AddressServiceAsync;
import uk.co.platosys.platax.shared.FieldVerifier;
import uk.co.platosys.platax.shared.PXAddress;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ContactWidget extends Composite implements HasWidgets {
	private final TextBox resultTextBox=new TextBox();//holds the address code
	private final PopupPanel popupPanel= new PopupPanel();
	  
//data collection boxes   
	public static final String[] fieldNames = {"given_name","family_name","title","email","mobile"};
 	
	  private final TextBox givenTextBox=new TextBox();
	  private final TextBox familyTextBox=new TextBox();
	  private final TextBox titleTextBox=new TextBox(); 
	  private final TextBox emailTextBox=new TextBox();
	  private final TextBox mobileTextBox=new TextBox();

	  private final Label givenLabel=new Label("Given Name");
	  private final Label familyLabel=new Label("Family Name");
	  private final Label titleLabel=new Label("Title"); 
	  private final Label emailLabel=new Label("email");
	  private final Label mobileLabel=new Label("mobile");
	  
	  private final Label givenInfoLabel=new Label("First or given name");
	  private final Label familyInfoLabel=new Label("Surname/family name");
	  private final Label titleInfoLabel=new Label("title"); 
	  private final Label emailInfoLabel=new Label("email address");
	  private final Label mobileInfoLabel= new Label("mobile");
	  //result widget
	  private final Label resultLabel = new Label("");
	  private final FlexTable contactTable=new FlexTable();
	  final AddressServiceAsync collectAddressService = (AddressServiceAsync) GWT.create(AddressService.class);
	  
	  /*can't work out what these callbacks are supposed to be doing*/
	 /*final AsyncCallback<PXAddress> callback1 = new AsyncCallback<PXAddress>(){
		public void onSuccess(PXAddress xaddress){
			resultTextBox.setText(xaddress.getAddress(true));
			collectAddressService.getAddress(xaddress.,  callback1);
		}
		public void onFailure(Throwable cause){
		    resultLabel.setText(cause.getMessage());
		    popupPanel.setWidget(resultLabel);
		}
		
	 };
	 final AsyncCallback<String> callback2 = new AsyncCallback<String>(){
		 public void onSuccess(String result) {
			 resultLabel.setText(result);
			 popupPanel.setWidget(resultLabel);
		 }
		 public void onFailure(Throwable cause){
			 resultLabel.setText(cause.getMessage());
			 popupPanel.setWidget(resultLabel);
		 }
	 };*/
	  public ContactWidget(){
		  
		  givenTextBox.setText("");
		  familyTextBox.setText("");
		  familyTextBox.setEnabled(false);
		  titleTextBox.setText("");
		  titleTextBox.setEnabled(false);
		   emailTextBox.setText("");
		   emailTextBox.setEnabled(false);
		  mobileTextBox.setText("");
		  mobileTextBox.setEnabled(false);
		  contactTable.setWidget(0, 0, givenLabel);
		  contactTable.setWidget(0, 1, givenTextBox);
		  contactTable.setWidget(0, 2, givenInfoLabel);
		  contactTable.setWidget(1, 0, familyLabel);
		  contactTable.setWidget(1, 1, familyTextBox);
		  contactTable.setWidget(1, 2, familyInfoLabel);
		  contactTable.setWidget(2, 0, titleLabel);
		  contactTable.setWidget(2, 1, titleTextBox);
		  contactTable.setWidget(2, 2, titleInfoLabel);
		  contactTable.setWidget(3, 0, emailLabel);
		  contactTable.setWidget(3, 1, emailTextBox);
		  contactTable.setWidget(3, 2, emailInfoLabel);
		  contactTable.setWidget(4, 0, mobileLabel);
		  contactTable.setWidget(4, 1, mobileTextBox);
		  contactTable.setWidget(4, 2, mobileInfoLabel);
		  //contactTable.setWidget(6, 0, widget);
		  popupPanel.setWidget(contactTable);
		  //addressTable.setWidget(6, 0, widget);
		  givenTextBox.addChangeHandler(new ChangeHandler(){
			  public void onChange(ChangeEvent event){
				  String given = givenTextBox.getValue();
						 familyTextBox.setEnabled(true);
					     givenTextBox.setFocus(false);
					     familyTextBox.setFocus(true);
					     givenInfoLabel.setText("OK");
				}
			  
		  });
		 
		  familyTextBox.addChangeHandler(new ChangeHandler(){
			  public void onChange(ChangeEvent event){
				  
				  titleTextBox.setEnabled(true);
				     familyTextBox.setFocus(false);
				     titleTextBox.setFocus(true);
				     familyInfoLabel.setText("OK");
			}
			  
		  });
		  titleTextBox.addChangeHandler(new ChangeHandler(){
			  public void onChange(ChangeEvent event){
				  emailTextBox.setEnabled(true);
				     titleTextBox.setFocus(false);
				     emailTextBox.setFocus(true);
				     titleInfoLabel.setText("OK");
			
				}
			  
		  });
		 emailTextBox.addChangeHandler(new ChangeHandler(){
			  public void onChange(ChangeEvent event){
				if(FieldVerifier.isValid(emailTextBox.getValue(), FieldVerifier.EMAIL_REGEX)){
				 mobileTextBox.setEnabled(true);
			     emailTextBox.setFocus(false);
			    mobileTextBox.setFocus(true);
			    emailInfoLabel.setText("OK");
					
				}}
			  
		  });
		  mobileTextBox.addChangeHandler(new ChangeHandler(){
			  public void onChange(ChangeEvent event){
				  
				     mobileTextBox.setFocus(false);
				    mobileInfoLabel.setText("OK");
			}
				
			  
		  });
		  
		  
		
		 initWidget(contactTable);
	  }
	public void setEnabled(boolean enabled) {
		if (enabled){
			popupPanel.show();
		}else{
		    popupPanel.hide();
		}
	}
	public void addCloseHandler(CloseHandler<PopupPanel> closeHandler) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void add(Widget w) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Iterator<Widget> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean remove(Widget w) {
		// TODO Auto-generated method stub
		return false;
	}
}

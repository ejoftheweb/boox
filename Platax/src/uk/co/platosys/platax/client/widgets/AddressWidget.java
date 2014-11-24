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
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddressWidget extends Composite implements HasWidgets {
	private final TextBox resultTextBox=new TextBox();//holds the address code
	
	  
//data collection boxes   
	 
	  private final TextBox buildingTextBox=new TextBox();
	  private final TextBox streetTextBox=new TextBox();
	  private final TextBox districtTextBox=new TextBox(); 
	  private final TextBox townTextBox=new TextBox();
	  private final TextBox countyTextBox=new TextBox();
	  private final TextBox postCodeTextBox=new TextBox();
	  private final Label buildingLabel=new Label("Building");
	  private final Label streetLabel=new Label("Street");
	  private final Label districtLabel=new Label("District"); 
	  private final Label townLabel=new Label("Town");
	  private final Label countyLabel=new Label("County");
	  private final Label postCodeLabel=new Label("Postcode");
	  private final Label buildingInfoLabel=new Label("Unit no, and building name (if needed)");
	  private final Label streetInfoLabel=new Label("street address");
	  private final Label districtInfoLabel=new Label("district name (optional)"); 
	  private final Label townInfoLabel=new Label("Post Town");
	  private final Label countyInfoLabel= new Label("County (optional)");
	  private final Label postCodeInfoLabel= new Label ("Royal Mail postcode");
	  private final Button submitButton = new Button("OK");
//result widget
	  private final Label resultLabel = new Label("");
	  private final FlexTable addressTable=new FlexTable();
	  final AddressServiceAsync addressService = (AddressServiceAsync) GWT.create(AddressService.class);

	 final AsyncCallback<PXAddress> callback = new AsyncCallback<PXAddress>(){
		 public void onSuccess(PXAddress result) {
			 resultLabel.setText(result.getAddress(true));
			 addressTable.clear();
			 addressTable.setWidget(0,0, resultLabel);
		 }
		 public void onFailure(Throwable cause){
			 resultLabel.setText(cause.getMessage());
			
		 }
	 };
	  public AddressWidget(){
		  buildingTextBox.setText("");
		  streetTextBox.setText("");
		 // streetTextBox.setEnabled(false);
		  districtTextBox.setText("");
		 // districtTextBox.setEnabled(false);
		  townTextBox.setText("");
		//  townTextBox.setEnabled(false);
		  countyTextBox.setText("");
		//  countyTextBox.setEnabled(false);
		  postCodeTextBox.setText("");
		 // postCodeTextBox.setEnabled(false);
		  addressTable.setWidget(0, 0, buildingLabel);
		  addressTable.setWidget(0, 1, buildingTextBox);
		  addressTable.setWidget(0, 2, buildingInfoLabel);
		  addressTable.setWidget(1, 0, streetLabel);
		  addressTable.setWidget(1, 1, streetTextBox);
		  addressTable.setWidget(1, 2, streetInfoLabel);
		  addressTable.setWidget(2, 0, districtLabel);
		  addressTable.setWidget(2, 1, districtTextBox);
		  addressTable.setWidget(2, 2, districtInfoLabel);
		  addressTable.setWidget(3, 0, townLabel);
		  addressTable.setWidget(3, 1, townTextBox);
		  addressTable.setWidget(3, 2, townInfoLabel);
		  addressTable.setWidget(4, 0, countyLabel);
		  addressTable.setWidget(4, 1, countyTextBox);
		  addressTable.setWidget(4, 2, countyInfoLabel);
		  addressTable.setWidget(5, 0, postCodeLabel);
		  addressTable.setWidget(5, 1, postCodeTextBox);
		  addressTable.setWidget(5, 2, postCodeInfoLabel);
		  addressTable.setWidget(6, 2, submitButton);
		  //addressTable.setWidget(6, 0, widget);
		  //addressTable.setWidget(6, 0, widget);
		  buildingTextBox.addChangeHandler(new ChangeHandler(){
			  public void onChange(ChangeEvent event){
				 		// streetTextBox.setEnabled(true);
					     buildingTextBox.setFocus(false);
					     streetTextBox.setFocus(true);
					     buildingInfoLabel.setText("OK");
				}
			  
		  });
		 
		  streetTextBox.addChangeHandler(new ChangeHandler(){
			  public void onChange(ChangeEvent event){
				  
				  districtTextBox.setEnabled(true);
				     streetTextBox.setFocus(false);
				     districtTextBox.setFocus(true);
				     streetInfoLabel.setText("OK");
			}
			  
		  });
		  districtTextBox.addChangeHandler(new ChangeHandler(){
			  public void onChange(ChangeEvent event){
				  townTextBox.setEnabled(true);
				     districtTextBox.setFocus(false);
				     townTextBox.setFocus(true);
				     districtInfoLabel.setText("OK");
			
				}
			  
		  });
		 townTextBox.addChangeHandler(new ChangeHandler(){
			  public void onChange(ChangeEvent event){
				  countyTextBox.setEnabled(true);
				     townTextBox.setFocus(false);
				    countyTextBox.setFocus(true);
				    townInfoLabel.setText("OK");
					
				}
			  
		  });
		  countyTextBox.addChangeHandler(new ChangeHandler(){
			  public void onChange(ChangeEvent event){
				  //postCodeTextBox.setEnabled(true);
				     countyTextBox.setFocus(false);
				     postCodeTextBox.setFocus(true);
				     countyInfoLabel.setText("OK");
			}
				
			  
		  });
		  
		  
		 postCodeTextBox.addChangeHandler(new ChangeHandler(){
			  public void onChange(ChangeEvent event){
				  if(FieldVerifier.isValid(postCodeTextBox.getValue(), FieldVerifier.POSTCODE_REGEX)){
					  postCodeInfoLabel.setText("OK");
				   
				  
			  }else{
				  postCodeTextBox.setEnabled(true);
				     postCodeTextBox.setFocus(true);
				    
			  }
			  }
		  }); 
		 submitButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				 String[] vals = new String[PXAddress.FIELD_NAMES.length];
				  vals[0]=buildingTextBox.getText();
				  vals[1]=streetTextBox.getText();
				  vals[2]=districtTextBox.getText();
				  vals[3]=townTextBox.getText();
				  vals[4]=countyTextBox.getText();
				  vals[5]=postCodeTextBox.getText();
				  //PXAddress address= new PXAddress(vals);
				  //resultLabel.setText(address.getAddress(true));
					 //addressTable.clear();
					 addressTable.setWidget(0,0, resultLabel);
				  
				  //Window.alert(address.getAddress(false));
				  addressService.recordAddress(vals, callback);
			}
		 });
		 initWidget(addressTable);
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

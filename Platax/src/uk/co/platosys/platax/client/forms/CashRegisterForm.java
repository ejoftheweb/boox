package uk.co.platosys.platax.client.forms;


import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.forms.bills.InvoiceForm;
import uk.co.platosys.platax.client.forms.popups.AddCRDeptPopupForm;
import uk.co.platosys.platax.client.services.CashService;
import uk.co.platosys.platax.client.services.CashServiceAsync;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.widgets.PTab;
import uk.co.platosys.platax.client.widgets.TaxBandChooser;
import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.boox.GWTCash;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTItem;
import uk.co.platosys.platax.shared.exceptions.LoginException;
import uk.co.platosys.platax.shared.exceptions.PlataxException;
import uk.co.platosys.pws.inputfields.MoneyBox;
import uk.co.platosys.pws.labels.FieldInfoLabel;
import uk.co.platosys.pws.labels.FieldLabel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;

public class CashRegisterForm extends AbstractForm { 
	//declare variables
	
	//services
	final CashServiceAsync cashService = (CashServiceAsync) GWT.create(CashService.class);
	//widgets
	FieldLabel machineName = new FieldLabel(StringText.MACHINE_NAME);
	FieldInfoLabel machineNameInfo = new FieldInfoLabel(StringText.MACHINE_NAME_INFO);
    final TextBox machineNameBox= new TextBox();
    FieldLabel machineDesc = new FieldLabel(StringText.MACHINE_DESC);
	FieldInfoLabel machineDescInfo = new FieldInfoLabel(StringText.MACHINE_DESC_INFO);
    final TextBox machineDescBox= new TextBox();
    FieldLabel deptsNo = new FieldLabel(StringText.MACHINE_DESC);
   	FieldInfoLabel deptsInfo = new FieldInfoLabel(StringText.MACHINE_DESC_INFO);
    final IntegerBox deptsBox= new IntegerBox();
       
    FieldLabel machineFloat = new FieldLabel(StringText.MACHINE_FLOAT);
  	FieldInfoLabel machineFloatInfo = new FieldInfoLabel(StringText.MACHINE_FLOAT_INFO);
    final MoneyBox machineFloatBox= new MoneyBox("GBP");
    FieldLabel machineGT = new FieldLabel(StringText.MACHINE_GT);
  	FieldInfoLabel machineGTInfo = new FieldInfoLabel(StringText.MACHINE_GT_INFO);
    final MoneyBox machineGTBox= new MoneyBox("GBP");
    FieldLabel reportno = new FieldLabel(StringText.MACHINE_REPORTNO);
  	FieldInfoLabel reportnoInfo = new FieldInfoLabel(StringText.MACHINE_REPORTNO_INFO);
    final IntegerBox reportnoBox= new IntegerBox();
    Button submitButton = new Button(ButtonText.CONFIRM);
    final Platax platax;
    GWTCash register=new GWTCash();
    int deptsDone=0;
    int deptsToDo;
    PTab callingTab;
  //callbacks
  		final AsyncCallback<GWTCash> finalCallback=new AsyncCallback<GWTCash>(){
  			@Override
  			public void onFailure(Throwable caught) {
  				Window.alert(StringText.SERVER_ERROR+"PF0");
  			}
  			@Override
  			public void onSuccess(GWTCash result) {
  				setRegister(result);
  				String name = result.getName();
  				if (Window.confirm (name+ StringText.SUCCESS_ANOTHER)){
  					pageReset();
  				}else{
  					pageClose();
  				}
  			}
  		}; 
  	//regular constructor	
	public CashRegisterForm(Platax platax, final GWTEnterprise enterprise) {
		super(platax, enterprise.getName());
		this.platax=platax;
		setTitle(StringText.NEW_PRODUCT);
		setSubTitle(StringText.NEW_PRODUCT_INFO);
		//layout page
	    table.setWidget(0,0, machineName);
	    table.setWidget(0,1, machineNameBox);
	    table.setWidget(0,2, machineNameInfo);
	    table.setWidget(1,0, machineDesc);
	    table.setWidget(1,1, machineDescBox);
	    table.setWidget(1,2, machineDescInfo);
	    table.setWidget(2,0, deptsNo);
	    table.setWidget(2,1, deptsBox);
	    table.setWidget(2,2, deptsInfo);
	  
	    
	    
	    table.setWidget(3,0, reportno);
	    table.setWidget(3,1, reportnoBox);
	    table.setWidget(3,2, reportnoInfo);
	    table.setWidget(4,0, machineFloat);
	    table.setWidget(4,1, machineFloatBox);
	    table.setWidget(4,2, machineFloatInfo);
	    table.setWidget(5,0, machineGT);
	    table.setWidget(5,1, machineGTBox);
	    table.setWidget(5,2, machineGTInfo);
	    table.setWidget(5,1, submitButton);
	    //add change handlers
	    machineNameBox.addChangeHandler(new ChangeHandler(){
	    	@Override
			public void onChange(ChangeEvent event) {
				setTitle(machineNameBox.getValue());
				register.setName(machineNameBox.getValue());
	    		
			}
	     });
	    deptsBox.addChangeHandler(new ChangeHandler(){
	    	@Override
			public void onChange(ChangeEvent event) {
				deptsToDo=deptsBox.getValue();
				new AddCRDeptPopupForm(CashRegisterForm.this, deptsToDo, enterprise).show();
			}
	     });
	     submitButton.addClickHandler(new ClickHandler(){
	    	@Override
			public void onClick(ClickEvent event) {
				GWTCash register= new GWTCash();
				register.setName(machineNameBox.getValue());
	    		register.setModel(machineDescBox.getValue());
	    		register.setSeqno(reportnoBox.getValue());
	    		register.setFloatbal(machineFloatBox.getMoney());
	    		register.setRunningTotal(machineGTBox.getMoney());
	    		try {
					cashService.addCashRegister(register, enterprise.getSysname(), finalCallback);
				} catch (PlataxException e) {
					Window.alert("csACR error:"+e.getMessage());
				}
			}
	     });
	}
    //overloaded constructor with reference to calling tab:
	public CashRegisterForm(Platax platax, final GWTEnterprise enterprise, PTab callingTab) {
		this(platax, enterprise);
		this.callingTab=callingTab;
	}
	/**
	 * Clears the form	
	 */
	protected void pageReset() {
		setTitle(StringText.NEW_PRODUCT);
		setSubTitle(StringText.NEW_PRODUCT_INFO);
		 
	}
	/**
	 * closes the page and returns focus to the calling tab;
	 */
	protected void pageClose(){
		if(callingTab!=null){
			callingTab.refresh();
			platax.setSelectedTab(callingTab);
		}
		platax.removeTab(this);
	}
	@Override
	public void refresh() {
		pageReset();
	}
	public GWTCash getRegister() {
		return register;
	}
	public void setRegister(GWTCash register) {
		this.register = register;
	}
	
	
		
		
	
	}



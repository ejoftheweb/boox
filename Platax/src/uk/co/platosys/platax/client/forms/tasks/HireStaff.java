package uk.co.platosys.platax.client.forms.tasks;



import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTab;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.RadioText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.TabTops;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.platax.client.services.StaffService;
import uk.co.platosys.platax.client.services.StaffServiceAsync;
import uk.co.platosys.platax.shared.boox.GWTEmployee;
import uk.co.platosys.pws.fieldsets.AddressField;
import uk.co.platosys.pws.fieldsets.DateField;
import uk.co.platosys.pws.fieldsets.FileUploadField;
import uk.co.platosys.pws.fieldsets.MoneyField;
import uk.co.platosys.pws.fieldsets.NationalityField;
import uk.co.platosys.pws.fieldsets.RadioField;
import uk.co.platosys.pws.fieldsets.SubmitField;
import uk.co.platosys.pws.fieldsets.TextField;

public class HireStaff extends TTab {
	
	StaffServiceAsync staffService = (StaffServiceAsync) GWT.create(StaffService.class);
	//callbacks
	AsyncCallback<GWTEmployee> hireCallback = new AsyncCallback<GWTEmployee>(){
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Oops there was a problem");
	    }
		@Override
		public void onSuccess(GWTEmployee result) {
			if(result!=null){
				if(Window.confirm("Employee "+result.getName()+" hired OK \n Hire Another?")){
					refresh();
				}else{
					close();
				}
			}else{
				Window.alert("Sorry there was a server error: null employee");
			}
		}
		
	};
	
	//widgets
	Platax platax;
    PTab callingTab;
    //fields:
	TextField givenName;
	TextField familyName;
	
	AddressField address;
    TextField email;
    TextField phoneNo;
    NationalityField nationality;
    TextField natInsNo;
    DateField dob;
    RadioField payFreq;
    MoneyField rate;
    FileUploadField canWork;
    
    public HireStaff(Platax platax) {
    	super();
		setTabHead(TabTops.HIRE);
		givenName= new TextField (FieldText.GIVEN_NAME, 1000, this, true);
		familyName= new TextField (FieldText.FAMILY_NAME, 2000, this, true);
		address= new AddressField (FieldText.ADDRESS,  2500, this, true);
		email= new TextField (FieldText.EMAIL, 3000, this, true);
		phoneNo= new TextField (FieldText.PHONE, 4000, this, true);
		nationality= new NationalityField (FieldText.NATIONALITY,  5000, this, true);
		natInsNo= new TextField (FieldText.NAT_INS,  6000, this, true);
		dob= new DateField (FieldText.DOB,  7000, this, true);
		
		payFreq = new RadioField("pay", FieldText.PAY_FREQ, RadioText.PAY_PER_LIST,  8000, this, true);
		rate = new MoneyField(FieldText.PAY_RATE, 9000, this, true);
		canWork = new FileUploadField(FieldText.CAN_WORK, 10000, this, true);
		SubmitField sub= new SubmitField(12000, this);
	    sub.addClickHandler(new ClickHandler(){
	    	@Override
			public void onClick(ClickEvent event) {
	    		submit();
	    		clear();
			}
	    });
		this.platax=platax;
		setTitle(StringText.NEW_STAFF);
		setSubTitle(StringText.NEW_STAFF_INFO);
		render();
    }

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
	private boolean submit(){
		try{
			GWTEmployee employee = new GWTEmployee();
			employee.setGivenName(givenName.getValue());
			employee.setFamilyName(familyName.getValue());
			employee.setAddress(address.getValue());
			employee.setEmail(email.getValue());
			employee.setPhoneNo(phoneNo.getValue());
			employee.setNationality(nationality.getValue());
			employee.setNatinsno(natInsNo.getValue());
			employee.setDob(dob.getValue());
			employee.setPayFreq(payFreq.getValue());
			employee.setPayRate(rate.getValue());
			employee.setCanWork(canWork.getValue());
			staffService.hireEmployee(employee, getEnterprise().getSysname(), hireCallback);
			return true;
		}catch(Exception x){
			Window.alert("submit error "+x.getMessage());
			return false;
		}
	}

}

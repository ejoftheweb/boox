package uk.co.platosys.platax.client.forms.popups;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.forms.CashRegisterForm;
import uk.co.platosys.platax.client.forms.bills.AbstractBill;
import uk.co.platosys.platax.client.services.CashService;
import uk.co.platosys.platax.client.services.CashServiceAsync;
import uk.co.platosys.platax.client.services.CustomerService;
import uk.co.platosys.platax.client.services.CustomerServiceAsync;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.platax.client.widgets.TaxBandChooser;
import uk.co.platosys.platax.client.widgets.labels.FieldInfoLabel;
import uk.co.platosys.platax.client.widgets.labels.FieldLabel;
import uk.co.platosys.platax.shared.FieldVerifier;
import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.boox.GWTCash;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTItem;

public class AddCRDeptPopupForm extends AbstractPopupForm  {
Logger logger = Logger.getLogger("platax");
	public AddCRDeptPopupForm(final CashRegisterForm parent, final int deptsToDo, final GWTEnterprise enterprise) {
		super(LabelText.ADD_PRODUCT_HEADER);
		final CashServiceAsync cashService = (CashServiceAsync) GWT.create(CashService.class);
		FieldLabel deptNoLabel = new FieldLabel(LabelText.CRDEPT_NO);
		FieldLabel deptNameLabel=new FieldLabel(LabelText.CRDEPT_NAME);
		FieldLabel deptTaxLabel=new FieldLabel(LabelText.CRDEPT_TAX);
		final IntegerBox deptNoBox = new IntegerBox();
		final TextBox deptNameBox=new TextBox();
		final TaxBandChooser taxChooser = new TaxBandChooser(null);
		FieldInfoLabel deptNoInfoLabel = new FieldInfoLabel(LabelText.OF+Integer.toString(deptsToDo));
		FieldInfoLabel deptNameInfoLabel = new FieldInfoLabel(LabelText.CRDEPT_NAME_INFO);
		final GWTCash register= parent.getRegister();
		
Label chooseTaxLabel=new Label(LabelText.CHOOSE_TAX);
		table.getFlexCellFormatter().setColSpan(0, 0, 3);
		table.setWidget(0,0,header);
		table.setWidget(1,0, deptNoLabel);
		table.setWidget(1,1, deptNoBox);
		table.setWidget(1,2, deptNoInfoLabel);
		table.setWidget(2, 0, deptNameLabel);
		table.setWidget(2, 1, deptNameBox);
		table.setWidget(2, 2, deptNameInfoLabel);
		table.setWidget(3,0, deptTaxLabel);
		table.setWidget(3,1, taxChooser);
		table.setWidget(3,2, new Label(""));
		Button submitButton = new Button("submit");
		table.setWidget(4,2, submitButton);
		Button cancelButton= new Button("cancel");
		table.setWidget(4,1, cancelButton);
		deptNoBox.setValue(1);
		deptNameBox.setValue(register.getName()+"1");
		final AsyncCallback<GWTCash> callback = new AsyncCallback<GWTCash>(){
			AddCRDeptPopupForm popupForm = AddCRDeptPopupForm.this;
			public void onSuccess(GWTCash result){
				deptNoBox.setValue(deptNoBox.getValue()+1);
				deptNameBox.setValue(register.getName()+Integer.toString(deptNoBox.getValue()));
				if(deptNoBox.getValue()>deptsToDo){
					//we're done, let's go
					popupForm.hide();
				}
			}
			public void onFailure(Throwable cause){
			   StackTraceElement[] st = cause.getStackTrace();
			   String error = "addProduct failed\n";
			   error = error+cause.getClass().getName()+"\n";
			   for (int i=0; i<st.length; i++){
				   error = error + st[i].toString()+ "\n";
			   }
				Window.alert(error);
		    }};
		
		submitButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				int deptNo = deptNoBox.getValue();
				String deptName = deptNameBox.getText();//
				int taxBand = taxChooser.getTaxBand();//
				cashService.addCRDept(parent.getRegister(), deptNo, deptName, taxBand, callback);
				
			}
		});
		
		deptNoBox.addChangeHandler(new ChangeHandler(){
			@Override
			public void onChange(ChangeEvent event) {
				
			}
		});
	}

}

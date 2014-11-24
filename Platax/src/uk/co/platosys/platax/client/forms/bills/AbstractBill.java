package uk.co.platosys.platax.client.forms.bills;

import java.util.Date;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.forms.AbstractForm;
import uk.co.platosys.platax.client.widgets.ContactListBox;
import uk.co.platosys.platax.client.widgets.ItemListBox;
import uk.co.platosys.platax.client.widgets.QuantityBox;
import uk.co.platosys.platax.client.widgets.buttons.ActionButton;
import uk.co.platosys.platax.client.widgets.labels.FormHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.InlineLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyGrandTotalLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyTotalLabel;
import uk.co.platosys.platax.shared.boox.GWTBill;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTInvoice;
import uk.co.platosys.platax.shared.boox.GWTMoney;

import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.datepicker.client.DateBox;

public abstract class AbstractBill extends AbstractForm {

FlexTable table = new FlexTable();
ContactListBox contactListBox = new ContactListBox();

DateBox dateBox= new DateBox();
DateLabel dateLabel = new DateLabel();
Date date = new Date();
ItemListBox itemListBox=new ItemListBox();
protected GWTCustomer gwtCustomer=null;
protected GWTInvoice gwtInvoice=null;

GWTEnterprise gwtEnterprise=null;
String enterpriseName=null;
String enterpriseID=null;
QuantityBox qtyBox=new QuantityBox();
FlowPanel headPanel=new FlowPanel();
FlowPanel cpartyPanel=new FlowPanel();
FlowPanel lineEntryPanel=new FlowPanel();
FlowPanel submitButtonPanel=new FlowPanel();


public static final int QUANTITY_DIGITS=7;
Button cancelButton=new ActionButton(ButtonText.EXIT);
Button confirmButton=new ActionButton(ButtonText.CONFIRM);
Button saveButton=new ActionButton(ButtonText.SAVE);
Button postButton=new ActionButton(ButtonText.POST);


GWTMoney netMoney =new GWTMoney();
GWTMoney taxMoney = new GWTMoney();
GWTMoney grossMoney = new GWTMoney();
MoneyTotalLabel billNet = new MoneyTotalLabel();
MoneyTotalLabel billTax = new MoneyTotalLabel();
MoneyGrandTotalLabel billGross = new MoneyGrandTotalLabel();
FormHeaderLabel formHeadLabel=new FormHeaderLabel();
ScrollPanel tablePanel = new ScrollPanel();
InlineLabel billNumberLabel=new InlineLabel();
InlineLabel refNumberLabel=new InlineLabel();
TextBox billNumberBox = new TextBox();
TextBox refNumberBox= new TextBox();
	
public AbstractBill(Platax parent, String header) {
		super( header);
		//hpanel.add( new Label(LabelText.CUSTOMER));
				headPanel.add(formHeadLabel);
				headPanel.add(billNumberLabel);
				headPanel.add(billNumberBox);
				headPanel.add( new InlineLabel(LabelText.DATE));
				headPanel.add( dateBox);
		form.add(headPanel);
		form.add(cpartyPanel);
		form.add(lineEntryPanel);
		tablePanel.add(table);
		tablePanel.setAlwaysShowScrollBars(true);
		form.add(tablePanel);
		form.add(submitButtonPanel);
		submitButtonPanel.add(billNet);
		submitButtonPanel.add(billTax);
		submitButtonPanel.add(billGross);
		submitButtonPanel.add(saveButton);
		submitButtonPanel.add(cancelButton);
		submitButtonPanel.add(confirmButton);
		
		qtyBox.setVisibleLength(QUANTITY_DIGITS);
		dateLabel.setValue(dateBox.getValue());
		dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
		      public void onValueChange(ValueChangeEvent<Date> event) {
		        Date date = (Date) event.getValue();
		        setDate(date);
		      }
		    });
}

public abstract GWTBill getGWTBill();

private void setDate(Date date){
		this.date=date;
}
public ItemListBox getItemListBox() {
		return itemListBox;
}
public ContactListBox getContactListBox() {
		return contactListBox;
}
}

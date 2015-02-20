package uk.co.platosys.platax.client.forms.bills;

import java.util.Date;
import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.DateFormats;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.Styles;
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
import uk.co.platosys.platax.shared.boox.GWTLineItem;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
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
protected GWTBill bill=null;
GWTEnterprise gwtEnterprise=null;
String enterpriseName=null;
String enterpriseID=null;
QuantityBox qtyBox=new QuantityBox();
FlowPanel headPanel=new FlowPanel();
HorizontalPanel cpartyPanel=new HorizontalPanel();
FlowPanel cpartyRefPanel = new FlowPanel();
final SimplePanel cpartyNamePanel=new SimplePanel();
//FlowPanel lineEntryPanel=new FlowPanel();
FlowPanel submitButtonPanel=new FlowPanel();


public static final int QUANTITY_DIGITS=7;
Button deleteButton=new ActionButton(ButtonText.EXIT);
//Button confirmButton=new ActionButton(ButtonText.CONFIRM);
Button saveButton=new ActionButton(ButtonText.SAVE);
Button postButton=new ActionButton(ButtonText.POST);


int tableRows=0;
int baseRow=0;
int currentRow(){
	return baseRow;
}
int finalRow(){
	return baseRow+2;
}
int activeRow(){
	return baseRow+1; 
}
void setRow(int row){
	if(row>baseRow){baseRow=row;}
	else{baseRow++;}	
}
void resetRow(){
	baseRow=0;
}


MoneyTotalLabel billNet = new MoneyTotalLabel();
MoneyTotalLabel billTax = new MoneyTotalLabel();
MoneyGrandTotalLabel billGross = new MoneyGrandTotalLabel();
FormHeaderLabel formHeadLabel=new FormHeaderLabel();
ScrollPanel tablePanel = new ScrollPanel();
InlineLabel billNumberLabel=new InlineLabel("No:");
//InlineLabel refNumberLabel=new InlineLabel("No");
final TextBox billNumberBox = new TextBox();
final TextBox refNumberBox= new TextBox();

	
public AbstractBill() {
		super();
		//hpanel.add( new Label(LabelText.CUSTOMER));
		headPanel.add(formHeadLabel);
		headPanel.add(billNumberLabel);
		headPanel.add(billNumberBox);
		headPanel.add( new InlineLabel(LabelText.DATE));
		headPanel.add( dateBox);
		dateBox.setValue(new Date());
		dateBox.setFormat((new DateBox.DefaultFormat( DateFormats.MED_DATE_FORMAT)));
		headPanel.setStyleName(Styles.BILL_HEAD_PANEL);
		cpartyPanel.setStyleName(Styles.BILL_CPARTY_PANEL);
		cpartyPanel.add(cpartyNamePanel);
		cpartyPanel.add(cpartyRefPanel);
		cpartyNamePanel.setWidget(new Label("HalloHalloHallo"));
		panel.add(headPanel);
		panel.add(cpartyPanel);
		//panel.add(lineEntryPanel);
		tablePanel.add(table);
		tablePanel.setAlwaysShowScrollBars(true);
		panel.add(tablePanel);
		panel.add(submitButtonPanel);
		submitButtonPanel.add(billNet);
		submitButtonPanel.add(billTax);
		submitButtonPanel.add(billGross);
		submitButtonPanel.add(saveButton);
		submitButtonPanel.add(deleteButton);
		//submitButtonPanel.add(confirmButton);
		
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
public void clearBill(){
	billNumberBox.setValue("");
	refNumberBox.setValue("");
	table.clear();
	setBill(null);//could be a problemo.
}

private void setDate(Date date){
		this.date=date;
}
public ItemListBox getItemListBox() {
		return itemListBox;
}
public ContactListBox getContactListBox() {
		return contactListBox;
}
void adjustTotals(GWTLineItem gwtLineItem){
	bill.adjustTotals(gwtLineItem);
}
void setBill(GWTBill bill){
	this.bill=bill;
}
}

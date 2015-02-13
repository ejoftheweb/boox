package uk.co.platosys.platax.client.forms.tasks;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.services.CashService;
import uk.co.platosys.platax.client.services.CashServiceAsync;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.platax.client.widgets.PListBox;
import uk.co.platosys.platax.client.widgets.QuantityBox;
import uk.co.platosys.platax.client.widgets.buttons.SubmitButton;
import uk.co.platosys.platax.shared.boox.GWTCash;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTItem;
import uk.co.platosys.platax.shared.boox.GWTSelectable;
import uk.co.platosys.pws.inputfields.MoneyBox;
import uk.co.platosys.pws.labels.FieldInfoLabel;
import uk.co.platosys.pws.labels.FieldLabel;
import uk.co.platosys.pws.values.GWTMoney;

/**
 * This form is for use when cashing-up. it should perhaps be accessible separately? via an iPad or similar?
 * for use by shift supervisor at cashup time. 
 *  
 * @author edward
 *
 */

public class CashUp extends BasicTask {
	//Declare Variables
	//services
		final CashServiceAsync cashService = (CashServiceAsync) GWT.create(CashService.class);
		
	//select the cash register being done
	final PListBox registerList = new PListBox();
	final FieldLabel registerListLabel=new FieldLabel(LabelText.CASH_REGISTER_NAME);			
	final FieldInfoLabel registerListInfoLabel=new FieldInfoLabel(LabelText.CASH_REGISTER_NAME_INFO);
	//select the cashier name
	final PListBox cashierList = new PListBox();
	final FieldLabel cashierListLabel=new FieldLabel(LabelText.CASHIER_NAME);			
	final FieldInfoLabel cashierListInfoLabel=new FieldInfoLabel(LabelText.CASHIER_NAME_INFO);
	//ensures reports are in sequence
	final IntegerBox reportBox = new IntegerBox();
	final FieldLabel reportBoxLabel=new FieldLabel(LabelText.ZREPORT_NUMBER);			
	final FieldInfoLabel reportBoxInfoLabel=new FieldInfoLabel(LabelText.ZREPORT_NUMBER_INFO);
	//Enter the grand total
	final MoneyBox gtBox = new MoneyBox("GBP");
	final FieldLabel gtBoxLabel=new FieldLabel(LabelText.GT_AMOUNT);			
	final FieldInfoLabel gtBoxInfoLabel=new FieldInfoLabel(LabelText.GT_AMOUNT_INFO);
	// here do the Departments using popups:
	
	
	//
	final MoneyBox poBox = new MoneyBox("GBP");
	final FieldLabel poBoxLabel=new FieldLabel(LabelText.PO_AMOUNT);			
	final FieldInfoLabel poBoxInfoLabel=new FieldInfoLabel(LabelText.PO_AMOUNT_INFO);
	//pdq data
	//just three figures: total number of transactions; plus total value on PDQ report, total value on Z-report
	
	//vouchers/paper Brixton Pounds
	
	//Electronic Brixton Pounds?
	
	//the cash in the till
	final MoneyBox cashBox = new MoneyBox("GBP");
	final FieldLabel cashBoxLabel=new FieldLabel(LabelText.CASH_AMOUNT);			
	final FieldInfoLabel cashBoxInfoLabel=new FieldInfoLabel(LabelText.CASH_AMOUNT_INFO);
	//the amount to be banked
	final MoneyBox bankingBox = new MoneyBox("GBP");
	final FieldLabel bankingLabel=new FieldLabel(LabelText.BANK_AMOUNT);			
	final FieldInfoLabel bankingInfoLabel=new FieldInfoLabel(LabelText.BANK_AMOUNT_INFO);
	
	final Button submitButton=new SubmitButton();
	final FieldLabel submitInfoLabel=new FieldLabel("");
	final FieldInfoLabel submitLabel=new FieldInfoLabel(LabelText.ENTERPRISE_REGISTER);
	
	GWTCash cashmc;
	
	//callbacks
	//Get registers callback: populates the registerList
	final AsyncCallback<ArrayList<GWTSelectable>> registersCallback=new AsyncCallback<ArrayList<GWTSelectable>>(){
		@Override
		public void onFailure(Throwable caught) {
			Window.alert(StringText.SERVER_ERROR+"SCR0");
		}
		@Override
		public void onSuccess(ArrayList<GWTSelectable> result) {
			if(result==null){Window.alert(StringText.SERVER_ERROR+"SCR0A");}
			registerList.addItems(result);
		}
	};
	//get cashiers callback: populates the cashierList
	final AsyncCallback<ArrayList<GWTSelectable>> cashiersCallback=new AsyncCallback<ArrayList<GWTSelectable>>(){

		@Override
		public void onFailure(Throwable caught) {
			Window.alert(StringText.SERVER_ERROR+"SCR1");
		}
		@Override
		public void onSuccess(ArrayList<GWTSelectable> result) {
			if(result==null){Window.alert(StringText.SERVER_ERROR+"SCR1A");}
			cashierList.addItems(result);
		}
	};
	//selectMachine callback: retrieves a GWTCash instance representing this register
	final AsyncCallback<GWTCash> machineCallback=new AsyncCallback<GWTCash>(){
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(StringText.SERVER_ERROR+"SCR2");
			}
			@Override
			public void onSuccess(GWTCash result) {
				if(result==null){Window.alert(StringText.SERVER_ERROR+"SCR2A");}
				setCash(result);
			}
	};
	//final callback
	final AsyncCallback<Boolean> finalCallback=new AsyncCallback<Boolean>(){
		@Override
		public void onFailure(Throwable caught) {
			Window.alert(StringText.SERVER_ERROR+"SCR2");
		}
		@Override
		public void onSuccess(Boolean result) {
			if(result){Window.alert(StringText.SUCCESS_ANOTHER);}
			else{Window.alert(StringText.SERVER_ERROR+"SCR2A");}
		}
	};
	
	public CashUp() {
		super();
		setTitle(LabelText.CASHUP);
		setSubTitle(LabelText.CASHUP_SUBHEADER);
		cashService.getCashRegisters(getEnterprise().getSysname(), registersCallback);
		cashService.getCashiers(getEnterprise().getSysname(), cashiersCallback);
		table.setWidget(0,0, registerListLabel  );
		table.setWidget(0,1, registerList);
		table.setWidget(0,2, registerListInfoLabel);
		table.setWidget(1,0, cashierListLabel);
		table.setWidget(1,1, cashierList);
		table.setWidget(1,2, cashierListInfoLabel);
		table.setWidget(2,0, reportBoxLabel);
		table.setWidget(2,1, reportBox);
		table.setWidget(2,2, reportBoxInfoLabel);
		table.setWidget(3,0, gtBoxLabel);
		table.setWidget(3,1, gtBox);
		table.setWidget(3,2, gtBoxInfoLabel);
		table.setWidget(4,0, poBoxLabel);
		table.setWidget(4,1, poBox);
		table.setWidget(4,2, poBoxInfoLabel);
		table.setWidget(5,0, cashBoxLabel);
		table.setWidget(5,1, cashBox);
		table.setWidget(5,2, cashBoxInfoLabel);
		table.setWidget(6,0, bankingLabel);
		table.setWidget(6,1, bankingBox);
		table.setWidget(6,2, bankingInfoLabel);
		table.setWidget(7,1, submitButton);
		
		
		reportBox.addChangeHandler(new ChangeHandler(){
			@Override
			public void onChange(ChangeEvent event) {
				int reportNo=reportBox.getValue();
				if(!checkReportSequence(reportNo)){
					Window.alert("Z Report Number is Out of Sequence");
				}
			}
		});
		poBox.addValueChangeHandler(new ValueChangeHandler(){
			@Override
			public void onValueChange(ValueChangeEvent event) {
				GWTMoney pos = poBox.getMoney();
				if (Window.confirm(pos.toPrefixedString()+" "+LabelText.PO_POPUP_Q)){
					
				}
			}
		});
		submitButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				GWTMoney gt=gtBox.getMoney();
				GWTMoney pos = poBox.getMoney();
				GWTMoney cash= cashBox.getMoney();
				GWTMoney banking=bankingBox.getMoney();
				if(checkSums(gt, pos, cash, banking)){
					//submit the details
				}
			}
		});
}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
    
	private void setCash(GWTCash cashmc){
    	this.cashmc=cashmc;
    }
	
	private boolean checkReportSequence(int reportNo){
		return(reportNo==cashmc.getSeqno()+1);
    }
	private boolean checkSums(GWTMoney gt, GWTMoney pos, GWTMoney cash, GWTMoney banking){
		try{
			GWTMoney registered = gt.subtract(cashmc.getRunningTotal());
			GWTMoney taken=(cash.add(pos)).subtract(cashmc.getFloatbal());
			GWTMoney diff=taken.subtract(registered);
			GWTMoney newFloat=cash.subtract(banking);
			if (diff.nonZero()){
				if (diff.credit()){
					Window.alert("Short by:"+diff.toPrefixedString());
				}else{
					Window.alert("Over by:"+diff.toPrefixedString());
				}
			}else{
				Window.alert("Congratulations! to the penny");
			}
			return (Window.confirm("Shift Takings: "+taken.toPrefixedString()+"\n"
					               +"Over/Short: "+diff.toPlainString()+"\n"
					               +"Paid Out: "+pos.toPrefixedString()+"\n"
					               +"To be banked: "+banking.toPrefixedString()+"\n"
					               +"Float left in Till:"+newFloat.toPrefixedString()
					));
		}catch(Exception xt){
			return false;
		}
	}
}

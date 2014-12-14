package uk.co.platosys.platax.client.forms.tasks;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SubmitButton;
import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.widgets.MoneyBox;
import uk.co.platosys.platax.client.widgets.QuantityBox;
import uk.co.platosys.platax.client.widgets.labels.FieldInfoLabel;
import uk.co.platosys.platax.client.widgets.labels.FieldLabel;

/**
 * This form is for recording the cash taken from a simple cash register, with one running total. 
 * Most cash registers nowadays offer more information.
 * but often a simple business needs simply to record only the total cash takings for the day. For multi-department machines, with hundreds
 * of departments,  the data entry task can become overwhelming.
 *  
 * @author edward
 *
 */

public class SimpleCashRegister extends BasicTask {
	//Declare Variables
	
	//select the cash register being done
	final ListBox registerList = new ListBox();
	final FieldLabel registerListLabel=new FieldLabel(LabelText.CASH_REGISTER_NAME);			
	final FieldInfoLabel registerListInfoLabel=new FieldInfoLabel(LabelText.CASH_REGISTER_NAME_INFO);
	//select the cashier name
	final ListBox cashierList = new ListBox();
	final FieldLabel cashierListLabel=new FieldLabel(LabelText.CASHIER_NAME);			
	final FieldInfoLabel cashierListInfoLabel=new FieldInfoLabel(LabelText.CASHIER_NAME_INFO);
	//select the cash register being done
	final QuantityBox reportBox = new QuantityBox();
	final FieldLabel reportBoxLabel=new FieldLabel(LabelText.ZREPORT_NUMBER);			
	final FieldInfoLabel reportBoxInfoLabel=new FieldInfoLabel(LabelText.ZREPORT_NUMBER_INFO);
	//Enter the grand total
	final MoneyBox gtBox = new MoneyBox();
	final FieldLabel gtBoxLabel=new FieldLabel(LabelText.GT_AMOUNT);			
	final FieldInfoLabel gtBoxInfoLabel=new FieldInfoLabel(LabelText.GT_AMOUNT_INFO);
	//select the cash register being done
	final MoneyBox cashBox = new MoneyBox();
	final FieldLabel cashBoxLabel=new FieldLabel(LabelText.CASH_AMOUNT);			
	final FieldInfoLabel cashBoxInfoLabel=new FieldInfoLabel(LabelText.CASH_AMOUNT_INFO);
	//select the cash register being done
	final MoneyBox bankingBox = new MoneyBox();
	final FieldLabel bankingLabel=new FieldLabel(LabelText.BANK_AMOUNT);			
	final FieldInfoLabel bankingInfoLabel=new FieldInfoLabel(LabelText.BANK_AMOUNT_INFO);
	
	final Button submitButton=new SubmitButton();
	final FieldLabel submitInfoLabel=new FieldLabel("");
	final FieldInfoLabel submitLabel=new FieldInfoLabel(LabelText.ENTERPRISE_REGISTER);
	//callbacks
	//selectMachine callback: retrieves the last Z-no, float and GT values for the m/c
	//final callback
	
	public SimpleCashRegister(Platax parent, String header) {
		super(parent, header);
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
		table.setWidget(4,0, cashBoxLabel);
		table.setWidget(4,1, cashBox);
		table.setWidget(4,2, cashBoxInfoLabel);
		table.setWidget(5,0, bankingLabel);
		table.setWidget(5,1, bankingBox);
		table.setWidget(5,2, bankingInfoLabel);
		table.setWidget(6,1, submitButton);
		
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}

package uk.co.platosys.platax.client.forms.tasks;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.shared.boox.GWTTask;
import uk.co.platosys.pws.fieldsets.ListField;
import uk.co.platosys.pws.fieldsets.MoneyField;
import uk.co.platosys.pws.fieldsets.SubmitField;

public class PettyCash extends TTab {
private Platax platax;
	public PettyCash(Platax platax, GWTTask task){
		super();
		ListField listField = new ListField(FieldText.PETTYCASH, 1000, this, true);
		MoneyField balanceField = new MoneyField(FieldText.PETTYCASH_BALANCE, 2000, this, true);
		SubmitField sub= new SubmitField(12000, this);
		this.platax=platax;
		setTitle(LabelText.PETTY_CASH);
		setSubTitle(LabelText.PETTY_CASH_INFO);
		render();
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}

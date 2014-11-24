package uk.co.platosys.platax.client.forms.popups;

import com.google.gwt.user.client.ui.Label;

import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.forms.AddEnterpriseForm;

public class ShareCompanyPopupForm extends AbstractPopupForm {

	public ShareCompanyPopupForm(final AddEnterpriseForm parent, String legalName) {
		super(LabelText.SHARES_POPUP_HEADER);
		table.setWidget(0,0, new Label(LabelText.ENTERPRISE_LEGAL_NAME));
		table.setWidget(0,1, new Label(legalName));
		table.setWidget(1, 0, new Label(LabelText.SHARES_REGISTERED_NUMBER));
		//TODO: the rest of it.
	}

}

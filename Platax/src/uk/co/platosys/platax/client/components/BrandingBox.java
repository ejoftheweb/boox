package uk.co.platosys.platax.client.components;

import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.Styles;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class BrandingBox extends FlowPanel {
	private String enterpriseName="PACIOLI";

	public BrandingBox() {
		setStyleName(Styles.BRANDING_BOX);
		Label enterpriseLabel = new Label(enterpriseName);
		enterpriseLabel.setStyleName(Styles.BRANDING_LABEL);
		add(enterpriseLabel);
		Label enterpriseSubLabel = new Label(StringText.BRANDING_STRAPLINE);
		enterpriseSubLabel.setStyleName(Styles.BRANDING_SUBLABEL);
		add(enterpriseSubLabel);
	}

}

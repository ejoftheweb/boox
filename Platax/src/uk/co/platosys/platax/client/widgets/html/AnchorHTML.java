package uk.co.platosys.platax.client.widgets.html;
import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.widgets.PlataxTabPanel;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
public  class AnchorHTML extends Anchor {
	protected Platax context;
	public AnchorHTML(String text, Platax context) {
		super(text);
		this.context=context;
	}

}


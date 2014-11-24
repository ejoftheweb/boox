package uk.co.platosys.platax.client.widgets.html;
import uk.co.platosys.platax.client.widgets.PlataxTabPanel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
public abstract class AnchorHTML extends InlineHTML {
	protected PlataxTabPanel context;
	public AnchorHTML(String text, PlataxTabPanel context) {
		super(text);
		this.context=context;
	}

}


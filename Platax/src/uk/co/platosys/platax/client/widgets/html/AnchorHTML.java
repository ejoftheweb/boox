package uk.co.platosys.platax.client.widgets.html;
import uk.co.platosys.platax.client.Platax;
import com.google.gwt.user.client.ui.Anchor;
public  class AnchorHTML extends Anchor {
	protected Platax context;
	public AnchorHTML(String text, Platax context) {
		super(text);
		this.context=context;
	}

}


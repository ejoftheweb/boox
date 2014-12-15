package uk.co.platosys.platax.client.widgets.buttons;

import uk.co.platosys.platax.client.constants.ButtonText;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;

public class SubmitButton extends ActionButton {

	public SubmitButton() {
		setText(ButtonText.SUBMIT);
	}

	public SubmitButton(SafeHtml html) {
		super(html);
		// TODO Auto-generated constructor stub
	}

	public SubmitButton(String html) {
		super(html);
		// TODO Auto-generated constructor stub
	}

	public SubmitButton(Element element) {
		super(element);
		// TODO Auto-generated constructor stub
	}

	public SubmitButton(SafeHtml html, ClickHandler handler) {
		super(html, handler);
		// TODO Auto-generated constructor stub
	}

	public SubmitButton(String html, ClickHandler handler) {
		super(html, handler);
		// TODO Auto-generated constructor stub
	}

}

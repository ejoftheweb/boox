package uk.co.platosys.platax.client.widgets.html;

import com.google.gwt.safehtml.shared.SafeHtml;

public class StringHTML implements SafeHtml {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String string=null;
	public StringHTML(){
		
	}
	public StringHTML(String string){
    	this.string=string;
    }
	@Override
	public String asString() {
		return string;
	}

}

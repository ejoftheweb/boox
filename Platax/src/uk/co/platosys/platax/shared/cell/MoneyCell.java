package uk.co.platosys.platax.shared.cell;

import uk.co.platosys.platax.shared.boox.GWTAuditable;
import uk.co.platosys.pws.values.GWTMoney;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * This is the Cell that renders an Auditable. The minimum - condensed -representation is as a short name plus a balance figure.
 * 
 * @author edward
 *
 */

public class MoneyCell extends AbstractCell<GWTMoney> {
	 /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {
      /**
       * The template for this Cell, which includes styles and a value.
       * 
       * @param styles the styles to include in the style attribute of the div
       * @param value the safe value. Since the value type is {@link SafeHtml},
       *          it will not be escaped before including it in the template.
       *          Alternatively, you could make the value type String, in which
       *          case the value would be escaped.
       * @return a {@link SafeHtml} instance
       */
      @SafeHtmlTemplates.Template( "<span style=\"{0}\">{1}</span>")
      SafeHtml cell(SafeStyles balanceStyle, SafeHtml value);
    }
    /**
     * Create a singleton instance of the templates used to render the cell.
     */
    private static Templates templates = GWT.create(Templates.class);

	private String name=null;
	private GWTMoney balance=null;
    private GWTAuditable value=null;
	public MoneyCell() {
		// TODO Auto-generated constructor stub
	}

      

@Override
public void render(com.google.gwt.cell.client.Cell.Context context, GWTMoney value,
		SafeHtmlBuilder sb) {
	//basic null check.
			if (value == null) { return;}
			//convert value to SafeHtml
	        SafeHtml safeBalance = SafeHtmlUtils.fromString(balance.toPrefixedString());
	        // Use the template to create the Cell's html.
	        SafeStyles balanceStyle;        
	        if (value.credit()){
	        	balanceStyle = SafeStylesUtils.forTrustedColor("red");
	        }else{
	        	balanceStyle = SafeStylesUtils.forTrustedColor("black");
	        }
	        SafeHtml rendered = templates.cell(balanceStyle,  safeBalance);
	        sb.append(rendered);
	
}		
	


}

package uk.co.platosys.platax.shared.cell;

import java.util.List;
import java.util.Set;

import uk.co.platosys.platax.shared.boox.GWTAuditable;
import uk.co.platosys.platax.shared.boox.GWTMoney;
import uk.co.platosys.platax.shared.cell.MoneyCell.Templates;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
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
 * @param <C>
 *
 */

public class AuditCell  extends AbstractCell<GWTAuditable> {
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
      @SafeHtmlTemplates.Template( "<span style=\"{0}\">{1}</span><span style=\"{2}\">{3}</span>")
      SafeHtml cell(SafeStyles nameStyle, SafeHtml name, SafeStyles balStyle, SafeHtml value);
    }
    /** Create a singleton instance of the templates used to render the cell.
    */
   private static Templates templates = GWT.create(Templates.class);
    
	public AuditCell() {
		 
		
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			GWTAuditable value, SafeHtmlBuilder sb) {
		// TODO Auto-generated method stub
		//basic null check.
		if (value == null) { return;}
		//convert value to SafeHtml
		SafeHtml safeName= SafeHtmlUtils.fromString(value.getName());
        SafeHtml safeBalance = SafeHtmlUtils.fromString(value.getBalance().toPrefixedString());
        // Use the template to create the Cell's html.
        SafeStyles balanceStyle;        
        if (value.getBalance().credit()){
        	balanceStyle = SafeStylesUtils.forTrustedColor("red");
        }else{
        	balanceStyle = SafeStylesUtils.forTrustedColor("black");
        }
        SafeHtml rendered = templates.cell(balanceStyle, safeName, balanceStyle,  safeBalance);
        sb.append(rendered);

}		
	


	
	

}

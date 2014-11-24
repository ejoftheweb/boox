package uk.co.platosys.platax.client.cells;

import uk.co.platosys.platax.shared.boox.GWTMoney;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;



public class MoneyCell extends AbstractCell<GWTMoney> {
final static SafeHtml sc = SafeHtmlUtils.fromString("</span>");


interface MoneyTemplate extends SafeHtmlTemplates {
    @Template("<span class=\"{0}\">{1}</span>")
    SafeHtml span(String classname, String value);
  }
private static MoneyTemplate template;

public MoneyCell () {
  if (template == null) {
    template = (MoneyTemplate) GWT.create(MoneyTemplate.class);
  }
}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			GWTMoney value, SafeHtmlBuilder sb) {
		 if (value == null) { return;}
	     if (value.credit()){
	    	 sb.append(template.span("px_cell_credit", value.toPlainString()));
		 }else{
			 sb.append(template.span("px_cell_debit", value.toPlainString()));
		 }
	}
}

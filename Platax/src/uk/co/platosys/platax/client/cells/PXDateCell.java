package uk.co.platosys.platax.client.cells;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.shared.SafeHtmlRenderer;

public class PXDateCell extends DateCell {
	static DateTimeFormat pxFormat = DateTimeFormat.getFormat("d MMM yyyy");

	public PXDateCell() {
		super(pxFormat);
	}

	public PXDateCell(SafeHtmlRenderer<String> renderer) {
		super(renderer);
		// TODO Auto-generated constructor stub
	}

	public PXDateCell(DateTimeFormat format) {
		super(format);
		// TODO Auto-generated constructor stub
	}

	public PXDateCell(DateTimeFormat format, SafeHtmlRenderer<String> renderer) {
		super(format, renderer);
		// TODO Auto-generated constructor stub
	}

}

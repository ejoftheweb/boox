package uk.co.platosys.platax.client.forms.lists;

import java.util.Date;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import uk.co.platosys.platax.client.cells.MoneyCell;
import uk.co.platosys.platax.client.cells.PXDateCell;
import uk.co.platosys.platax.shared.boox.GWTInvoice;
import uk.co.platosys.platax.shared.boox.GWTInvoiceList;
import uk.co.platosys.pws.values.GWTMoney;

public class InvoiceListTable extends CellTable<GWTInvoice> {

	public InvoiceListTable(GWTInvoiceList invoices) {
		super();
		/*GWTInvoice fields: customer enterprise valuedate duedate raiseddate createddate duedate paiddate status net tax gross sysname userno*/ 
		//customer
		TextColumn<GWTInvoice> numberColumn=new TextColumn<GWTInvoice>(){
			@Override
			public String getValue(GWTInvoice invoice) {
				return invoice.getUserno();
			}
		};
		addColumn(numberColumn, GWTInvoice.NUMBER_HEADER);
		TextColumn<GWTInvoice> customerColumn = new TextColumn<GWTInvoice>(){
			@Override
			public String getValue(GWTInvoice invoice) {
				return invoice.getCustomer().getName();
			}
		};
		addColumn(customerColumn, GWTInvoice.CUSTOMER_NAME_HEADER);
		//valueDate
		PXDateCell valueDateCell = new PXDateCell();
		Column<GWTInvoice, Date> valueDateColumn = new Column<GWTInvoice, Date>(valueDateCell){
			@Override
			public Date getValue(GWTInvoice gwtInvoice) {
				return gwtInvoice.getValueDate();
			}
		};
		addColumn(valueDateColumn, GWTInvoice.VALUE_DATE_HEADER);
		//dueDate
		PXDateCell dueDateCell = new PXDateCell();
		Column<GWTInvoice, Date> dueDateColumn = new Column<GWTInvoice, Date>(dueDateCell){
			@Override
			public Date getValue(GWTInvoice gwtInvoice) {
				return gwtInvoice.getDueDate();
			}
		};
		addColumn(dueDateColumn, GWTInvoice.DUE_DATE_HEADER);
		//grossamt
		MoneyCell grossCell = new MoneyCell();
		Column<GWTInvoice, GWTMoney> amountColumn = new Column<GWTInvoice, GWTMoney>(grossCell){
			public GWTMoney getValue(GWTInvoice gwtInvoice) {
				return gwtInvoice.getGross();
			}
			
		};
		addColumn(amountColumn, GWTInvoice.GROSS_HEADER);
		TextColumn<GWTInvoice> statusColumn=new TextColumn<GWTInvoice>(){
			@Override
			public String getValue(GWTInvoice invoice) {
				return invoice.getStatus();
			}
		};
		addColumn(statusColumn, GWTInvoice.STATUS_HEADER);
		// Add a selection model to handle user selection.
	      final SingleSelectionModel<GWTInvoice> selectionModel 
	      = new SingleSelectionModel<GWTInvoice>();
	      setSelectionModel(selectionModel);
	      selectionModel.addSelectionChangeHandler(
	      new SelectionChangeEvent.Handler() {
	         public void onSelectionChange(SelectionChangeEvent event) {
	            GWTInvoice selected = selectionModel.getSelectedObject();
	            if (selected != null) {
	            //should open invoice in new tab.	
	               Window.alert("You selected: " + selected.getSysname());
	            }
	         }
	      });
	      //populate the table.
	      setRowData(invoices);
	}

}

package uk.co.platosys.platax.client.forms.lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.view.client.ListDataProvider;
 

import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.services.CustomerService;
import uk.co.platosys.platax.client.services.CustomerServiceAsync;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.platax.client.widgets.PlataxTabPanel;
import uk.co.platosys.platax.client.widgets.html.CustomerHTML;
import uk.co.platosys.platax.client.widgets.html.ProductHTML;
import uk.co.platosys.platax.client.widgets.labels.ColumnHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.GroupColumnHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyColumnHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyLabel;
import uk.co.platosys.platax.client.widgets.labels.NumberLabel;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTItem;

public class ProductList extends AbstractList {
	
	
	DataGrid<GWTItem> dataGrid=new DataGrid<GWTItem>();
	
	final ListDataProvider<GWTItem> listDataProvider = new ListDataProvider<GWTItem>();
	
	
	final ProductServiceAsync productService = (ProductServiceAsync) GWT.create(ProductService.class);
	 PlataxTabPanel parent;
	
	public ProductList(PlataxTabPanel parent, GWTEnterprise enterprise, int list_selection_type) {
		super(parent, enterprise.getName()+":Products", list_selection_type);
		this.parent=parent;
		 topLabel.setText("List of Products");
		 subHeader.setText("blah blah");
		 productService.listProducts(enterprise.getEnterpriseID(), list_selection_type, productListCallBack);
		 
		 listDataProvider.addDataDisplay(dataGrid);
		 
		 //the columns
		 TextColumn<GWTItem> nameColumn = new TextColumn<GWTItem>(){
			 @Override
			public String getValue(GWTItem item) {
				return item.getName();
			}
		 };
		 TextColumn<GWTItem> stockLevelColumn = new TextColumn<GWTItem>(){
			 @Override
			public String getValue(GWTItem item) {
				return Double.toString(item.getStockLevel());
			}
		 };
		 TextColumn<GWTItem> openingStockLevelColumn = new TextColumn<GWTItem>(){
			 @Override
			public String getValue(GWTItem item) {
				return Double.toString(item.getOpeningStockLevel());
			}
		 };
		 TextColumn<GWTItem> wastedStockLevelColumn = new TextColumn<GWTItem>(){
			 @Override
			public String getValue(GWTItem item) {
				return Double.toString(item.getWastedStockLevel());
			}
		 };
		 TextColumn<GWTItem> addedStockLevelColumn = new TextColumn<GWTItem>(){
			 @Override
			public String getValue(GWTItem item) {
				return Double.toString(item.getAddedStockLevel());
			}
		 };
		 dataGrid.addColumn(nameColumn, "name");
		 dataGrid.addColumn(openingStockLevelColumn, "opening");
		 dataGrid.addColumn(addedStockLevelColumn, "added");
		 dataGrid.addColumn(wastedStockLevelColumn, "wasted");
		 dataGrid.addColumn(stockLevelColumn, "current");
		 this.add(dataGrid);
		 pager.setDisplay(dataGrid);
		 /*
		 //table headers:
		// table.setWidget(0, 0, new ColumnHeaderLabel(LabelText.LIST_INVOICE_NUMBER_HEADER));
		 table.getFlexCellFormatter().setRowSpan(0,0,2); 
		 table.getFlexCellFormatter().setRowSpan(0,1,2); 
			
		 table.setWidget(0, 1, new ColumnHeaderLabel(LabelText.LIST_PRODUCT_NAME_HEADER));
		 table.getFlexCellFormatter().setColSpan(0,2,5);
		 table.getFlexCellFormatter().setColSpan(0,3,3);
			
		 table.setWidget(0, 2, new GroupColumnHeaderLabel(LabelText.LIST_PRODUCT_STOCK_HEADER));
		 table.setWidget(0, 3, new GroupColumnHeaderLabel(LabelText.LIST_PRODUCT_SALES_HEADER));
		 //the rowspan buggers up the logical column numbering here!
		 table.setWidget(1, 0, new MoneyColumnHeaderLabel(LabelText.LIST_PRODUCT_OPENING_HEADER));
		 table.setWidget(1, 1, new MoneyColumnHeaderLabel(LabelText.LIST_PRODUCT_SOLD_HEADER));
		 table.setWidget(1, 2, new MoneyColumnHeaderLabel(LabelText.LIST_PRODUCT_ADDED_HEADER));
		 table.setWidget(1, 3, new MoneyColumnHeaderLabel(LabelText.LIST_PRODUCT_WASTED_HEADER));
		 table.setWidget(1, 4, new MoneyColumnHeaderLabel(LabelText.LIST_PRODUCT_CLOSING_HEADER));
		 table.setWidget(1, 5, new MoneyColumnHeaderLabel(LabelText.LIST_PRODUCT_LAST_HEADER));
		 table.setWidget(1, 6, new MoneyColumnHeaderLabel(LabelText.LIST_PRODUCT_THIS_HEADER));
		 table.setWidget(1, 7, new MoneyColumnHeaderLabel(LabelText.LIST_PRODUCT_CHANGE_HEADER));
		 */
	}
	
	
	final AsyncCallback<ArrayList<GWTItem>> productListCallBack= new AsyncCallback<ArrayList<GWTItem>>(){
		@Override
		public void onSuccess(ArrayList<GWTItem> products){
		Iterator<GWTItem> gwit = products.iterator();
		List<GWTItem> dataList = listDataProvider.getList();
		 int row = 2;
		 while(gwit.hasNext()){
			 GWTItem product = gwit.next();
			 dataList.add(product);
			 
			 /*
			 //note that the column numbering is 2 greater than the headers
			 
			// table.setWidget(row, 0, new InvoiceRefHTML(gwinvoice));
			 table.setWidget(row, 1, new ProductHTML(product, parent));
			table.setWidget(row, 8, new MoneyLabel(product.getBalance()));
			 table.setWidget(row, 6, new NumberLabel(product.getStockLevel()));
			//table.setWidget(row, 4, new MoneyLabel(customer.getDisputedBalance()));
			//table.setWidget(row, 5, new Label(customer.getPreviousSales()));
			// table.setWidget(row, 6, new MoneyLabel(customer.getSales()));
			//table.setWidget(row, 7, new PercentChangeLabel(customer.getSalesChange()));
			 row++;*/
		 }
		}
		
		@Override
		public void onFailure(Throwable cause) {
			 //Debugging code
			
			StackTraceElement[] st = cause.getStackTrace();
		   String error = "get customer list failed\n";
		  
		   error = error+cause.getClass().getName()+"\n";
		   if (cause instanceof StatusCodeException){
			    StatusCodeException sce=(StatusCodeException) cause;
			    int sc = sce.getStatusCode();
				error=error+"Status Code:"+ sc+"\n";
			}
		   for (int i=0; i<st.length; i++){
			   error = error + st[i].toString()+ "\n";
		   }
			Window.alert(error);
		}
	};

	}



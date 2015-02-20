package uk.co.platosys.platax.client.forms.popups;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.forms.bills.AbstractBill;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.platax.client.widgets.TaxBandChooser;
import uk.co.platosys.platax.shared.FieldVerifier;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTItem;

public class AddProductPopupForm extends AbstractPopupForm  {
Logger logger = Logger.getLogger("platax");
	public AddProductPopupForm(final AbstractBill parent, final String customerName, final GWTEnterprise enterprise) {
		super(LabelText.ADD_PRODUCT_HEADER);
		final ProductServiceAsync addProductService = (ProductServiceAsync) GWT.create(ProductService.class);
			Label productNameLabel = new Label(LabelText.PRODUCT);
		Label productPriceLabel=new Label(LabelText.PRODUCT_PRICE);
		
		Label isExclusiveLabel=new Label(LabelText.EXCLUSIVE);
		final TextBox productNameBox = new TextBox();
		final TextBox productPriceBox=new TextBox();
		final CheckBox isExclusiveCheckBox = new CheckBox();
		final TaxBandChooser taxChooser = new TaxBandChooser(customerName);
		Label productInfoLabel = new Label(LabelText.PRODUCT_NAME);
		Label isExclusiveInfoLabel = new Label(LabelText.EXCLUSIVE_INFO +customerName);
		Label doMoreLaterLabel = new Label(LabelText.DOMORELATER);
		Label chooseTaxLabel=new Label(LabelText.CHOOSE_TAX);
		table.getFlexCellFormatter().setColSpan(0, 0, 3);
		table.setWidget(0,0,header);
		table.setWidget(1,0, productNameLabel);
		table.setWidget(1,1, productNameBox);
		table.setWidget(1,2, productInfoLabel);
		table.setWidget(2, 0, productPriceLabel);
		table.setWidget(2, 1, productPriceBox);
		table.setWidget(3,0, isExclusiveLabel);
		table.setWidget(3, 1, isExclusiveCheckBox);
		table.setWidget(3,2, isExclusiveInfoLabel);
		table.setWidget(4,0, chooseTaxLabel);
		table.setWidget(4,1, taxChooser);
		table.getFlexCellFormatter().setColSpan(5,0,3);
		table.setWidget(5,0, doMoreLaterLabel);
		Button submitButton = new Button("submit");
		table.setWidget(6,2, submitButton);
		Button cancelButton= new Button("cancel");
		table.setWidget(6,1, cancelButton);
		final AsyncCallback<GWTItem> callback = new AsyncCallback<GWTItem>(){
			AddProductPopupForm popupForm = AddProductPopupForm.this;
			public void onSuccess(GWTItem result){
				parent.getItemListBox().addItem(result.getName(), result.getSysname());
				parent.getGWTBill().addGWTItem(result);
				popupForm.hide();
			}
			public void onFailure(Throwable cause){
			   StackTraceElement[] st = cause.getStackTrace();
			   String error = "addProduct failed\n";
			   error = error+cause.getClass().getName()+"\n";
			   for (int i=0; i<st.length; i++){
				   error = error + st[i].toString()+ "\n";
			   }
				Window.alert(error);
		    }};
		
		submitButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				boolean isExclusive = isExclusiveCheckBox.getValue();
				String productName = productNameBox.getText();
				String sprice = productPriceBox.getText();//
				double dprice = Double.parseDouble(sprice);
				addProductService.addProduct(enterprise.getEnterpriseID(),customerName,productName,  dprice, taxChooser.getTaxBand(), isExclusive, callback);
				//logger.log(Level.ALL, "submit button clicked");
				//parent.refreshData();
				AddProductPopupForm.this.hide();
			}
		});
		
		productPriceBox.addChangeHandler(new ChangeHandler(){
			@Override
			public void onChange(ChangeEvent event) {
				if (!(FieldVerifier.isValidPrice(productPriceBox.getText()))){
					productPriceBox.setText("0.00");
				}
			}
		});
	}

}

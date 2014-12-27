package uk.co.platosys.platax.client.widgets;

import uk.co.platosys.platax.shared.boox.GWTMoney;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;

public class MoneyBox extends ValueBoxBase {
	private String currency=GWTMoney.NUL; 
    private TextBox textBox=new TextBox();
    private InlineLabel curLabel = new InlineLabel();
    public MoneyBox(){
    	init();
    }
    
    public MoneyBox(String currency){
    	this.currency=currency;
    	init();
    }
    public MoneyBox(GWTMoney money){
    	this.currency=money.getCurrencyTLA();
    	textBox.setValue(money.toPlainString());
    	init();
    }
    public void setAmount(double amount){
    	textBox.setValue(Double.toString(amount));
    }
    
	public double getAmount() {
		try{// TODO Auto-generated method stub
			return Double.parseDouble(textBox.getValue());
		}catch(Exception x){
			//there will be a parse exception here, we need to stop these things happening
			return 0;
		}
	}
  public GWTMoney getMoney(){
	return new GWTMoney(currency,getAmount());
	  
  }
  private void init(){
	  curLabel.setText(currency);
	  add(curLabel);
	  textBox.setVisibleLength(10);
	  add(textBox);
  }
  public void setCurrency(String currency){
	  this.currency=currency;
	  curLabel.setText(currency);
  }



/**
 * @return the currency*/

public String getCurrency() {
	return currency;
}

public void addChangeHandler(ChangeHandler changeHandler) {
	textBox.addChangeHandler(changeHandler);
	
}
}

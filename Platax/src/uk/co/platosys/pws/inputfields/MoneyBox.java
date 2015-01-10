package uk.co.platosys.pws.inputfields;

import uk.co.platosys.pws.values.GWTMoney;


import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.InlineLabel;

public class MoneyBox extends AbstractValueField<GWTMoney> {
	private String currency=GWTMoney.NUL; 
    private DoubleBox textBox=new DoubleBox();
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
    	textBox.setValue(money.getAmount());
    	init();
    }
    public void setAmount(double amount){
    	textBox.setValue(amount);
    }
    
	public double getAmount() {
		try{
			return textBox.getValue();
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



@Override
public GWTMoney getValue() {
	// TODO Auto-generated method stub
	return getMoney();
}



@Override
public void setFocus(boolean focused) {
	textBox.setFocus(focused);
	
}

@Override
public boolean isEnabled() {
	 
	return textBox.isEnabled();
}

@Override
public void setEnabled(boolean enabled) {
	textBox.setEnabled(enabled);
	
}

@Override
public HandlerRegistration addValueChangeHandler(
		ValueChangeHandler handler) {
	return textBox.addValueChangeHandler((ValueChangeHandler<Double>) handler);
}

@Override
public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
	// TODO Auto-generated method stub
	return textBox.addKeyDownHandler(handler);
}




}


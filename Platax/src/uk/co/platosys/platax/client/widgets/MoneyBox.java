package uk.co.platosys.platax.client.widgets;

import com.google.gwt.user.client.ui.TextBox;

public class MoneyBox extends TextBox {
    public MoneyBox(){
    	
    }
    
    
    
    
	public double getMoney() {
		try{// TODO Auto-generated method stub
			return Double.parseDouble(getValue());
		}catch(Exception x){
			//there will be a parse exception here, we need to stop these things happening
			return 0;
		}
	}

}

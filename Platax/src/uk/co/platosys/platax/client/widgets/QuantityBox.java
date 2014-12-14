package uk.co.platosys.platax.client.widgets;

import com.google.gwt.user.client.ui.TextBox;

public class QuantityBox extends TextBox {

	public QuantityBox (){}
	public QuantityBox(float itemQty) {
		super();
		setValue(Float.toString(itemQty));
	}

	public float getQuantity() {
		try{// TODO Auto-generated method stub
			return Float.parseFloat(getValue());
		}catch(Exception x){
			//there will be a parse exception here, we need to stop these things happening
			return 0;
		}
	}

}

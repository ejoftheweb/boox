package uk.co.platosys.platax.client.widgets;

import com.google.gwt.user.client.ui.TextBox;

public class MessageWidget extends TextBox {
  public MessageWidget(){
	  
  }
  
  public void addMessage(String message){
	  String text = getText();
	  text = message + "\n" +text;
	  setText(text);
  }
}

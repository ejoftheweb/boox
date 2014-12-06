package uk.co.platosys.platax.client.widgets;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class TaskWidget extends TextBox {
  public TaskWidget(){
	  
  }
  
  public void addMessage(String message){
	  String text = getText();
	  text = message + "\n" +text;
	  setText(text);
  }
}

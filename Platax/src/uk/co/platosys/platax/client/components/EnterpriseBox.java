package uk.co.platosys.platax.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;

public class EnterpriseBox extends Composite {
	private String enterpriseName="Platosys";

	public EnterpriseBox() {
		
		VerticalPanel mainPanel = new VerticalPanel();
		initWidget(mainPanel);
		mainPanel.setSize("50%", "100%");
		
		Label enterpriseLabel = new Label(enterpriseName);
		enterpriseLabel.addStyleDependentName("enterprise");
		mainPanel.add(enterpriseLabel);
		
			}
public void setEntepriseName(String name){
	this.enterpriseName=name;
}
}

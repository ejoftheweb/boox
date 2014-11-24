package uk.co.platosys.platax.client.components;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackLayoutPanel;

public class TaskPanel extends StackLayoutPanel {

	public TaskPanel() {
		super(Unit.PCT);
		// TODO Auto-generated constructor stub
		add(new Label("Task0"), "Report", 5);
		add(new Label("Task1"), "Reconcile", 5);
		add(new Label("Task2"), "Configure", 5);
		add(new Label("Task3"), "Audit", 5);
	}

}

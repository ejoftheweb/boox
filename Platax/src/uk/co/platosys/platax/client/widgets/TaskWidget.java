package uk.co.platosys.platax.client.widgets;

import java.util.Date;

import uk.co.platosys.platax.client.components.TaskPanel;
import uk.co.platosys.platax.client.constants.DateFormats;
import uk.co.platosys.platax.client.constants.Styles;
import uk.co.platosys.platax.shared.boox.GWTTask;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;

public class TaskWidget extends FlowPanel {
  private Date due;
  public TaskWidget(){
	  
  }
  public TaskWidget( final TaskPanel panel, final GWTTask task){
	  due = task.getDueDate();
	  Anchor headLabel = new Anchor();
	  headLabel.setText(task.getEnterprise().getName()+":"+task.getName()+" "+DateFormats.SHORT_DATE_FORMAT.format(due));
	  add(headLabel);
	  headLabel.addClickHandler(new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			try{
				panel.loadTask(task.getMenuAction(), task);
			}catch(Exception e){
				Window.alert("error with task class spec:"+task.getMenuAction());
			}
		}
	  });
      Date now = new Date();
	  if (due.before(new Date(now.getTime()+DateFormats.LONG_OVERDUE))){
		  setStyleName(Styles.TASK_LONG_OVERDUE);
	  }else if (due.before(now)){
		  setStyleName(Styles.TASK_OVERDUE);
	  }else if (due.before(new Date(now.getTime()+DateFormats.DUE))){
		  setStyleName(Styles.TASK_DUE);
	  }else if (due.before(new Date(now.getTime()+DateFormats.URGENT))){
		  setStyleName(Styles.TASK_URGENT);
	  }else if (due.before(new Date(now.getTime()+DateFormats.SOON))){
		  setStyleName(Styles.TASK_SOON);
	  }else{
		  setStyleName(Styles.TASK_WHENEVER);
	  }
  
  }
  
}

package uk.co.platosys.pws.labels;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * An active field label will be a clickable fieldlabel, rendered as a hyperlink.
 * The hyperlink will open a next pTab.
 * @author edward
 *
 */
public class ActiveFieldLabel extends FieldLabel {
   String command;
   ScheduledCommand scheduledCommand;
   public ActiveFieldLabel(String label, String command){
	   super(label);
	   this.command=command;
	  /* scheduledCommand=new Scheduler.ScheduledCommand() {
			
			@Override
			public void execute() {
				
			
		}
	};
	   addClickHandler(new ClickHandler(){
		   @Override
		   public void onClick(ClickEvent event) {
			   execute();
		   }
	 });
   }
   public void execute(){
	   
   };*/
}}

package uk.co.platosys.platax.client.components;

import java.util.List;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.forms.tasks.*;
import uk.co.platosys.platax.client.services.TaskService;
import uk.co.platosys.platax.client.services.TaskServiceAsync;
import uk.co.platosys.platax.client.widgets.TaskWidget;
import uk.co.platosys.platax.client.widgets.labels.MessagePanelHeaderLabel;
import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.boox.GWTTask;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;

public class TaskPanel extends FlowPanel {
	public MessagePanelHeaderLabel topHead= new MessagePanelHeaderLabel(LabelText.TASK_CENTRE);
	final TaskServiceAsync taskService = (TaskServiceAsync) GWT.create(TaskService.class);
    private Platax platax;
	public TaskPanel (Platax platax) {
		add(topHead);
		this.platax=platax;
	}
	
    AsyncCallback<List<GWTTask>> tasksCallback = new AsyncCallback<List<GWTTask>>(){
    	@Override
		public void onFailure(Throwable caught) {
			Window.alert("error gerring tasks");
		}
    	@Override
		public void onSuccess(List<GWTTask> result) {
    		//Window.alert("Got "+result.size()+" tasks");
			for(GWTTask task:result){
				addTask(task);
			}
		}
    };
	
    public void setUser(PXUser user){
    	//Window.alert("setting user on the TaskPanel");
    	topHead.setText(LabelText.TASKS_FOR+" "+user.getUsername());
    	taskService.listTasks(user, tasksCallback);
    }
    private void addTask(GWTTask task){
    	add(new TaskWidget(this, task));
    }
    
    public void loadTask(String taskname, GWTTask task){
    	switch (taskname){
    		case "VATReturn": platax.addTab(new VATReturn(platax,task)); break;
    		case "VATRegister": platax.addTab(new VATRegister(platax,task));break;
    		case "PettyCash": platax.addTab(new PettyCash(platax, task));break;
    		default: platax.addTab(new HireStaff(platax));break;
    	}
    }
    @Override
    public void clear(){
    	super.clear();
    	add(topHead);
    }
}


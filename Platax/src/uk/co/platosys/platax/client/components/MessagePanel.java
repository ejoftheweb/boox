package uk.co.platosys.platax.client.components;

import java.util.List;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.services.TaskService;
import uk.co.platosys.platax.client.services.TaskServiceAsync;
import uk.co.platosys.platax.client.widgets.MessageWidget;
import uk.co.platosys.platax.client.widgets.labels.MessagePanelHeaderLabel;
import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.boox.GWTTask;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackLayoutPanel;

public class MessagePanel extends FlowPanel {
public MessagePanelHeaderLabel topHead= new MessagePanelHeaderLabel(LabelText.MESSAGE_CENTRE);
private Platax platax;
	public MessagePanel() {
		super();
	}
		public MessagePanel (Platax platax) {
			add(topHead);
			this.platax=platax;
		}
		
	    /*AsyncCallback<List<GWTTask>> tasksCallback = new AsyncCallback<List<GWTTask>>(){
	    	@Override
			public void onFailure(Throwable caught) {
				Window.alert("error gerring tasks");
			}
	    	@Override
			public void onSuccess(List<GWTTask> result) {
	    		Window.alert("Got "+result.size()+" tasks");
				for(GWTTask task:result){
					addTask(task);
				}
			}
	    };*/
		
	    public void setUser(PXUser user){
	    	Window.alert("setting user on the TaskPanel");
	    	topHead.setText(LabelText.TASKS_FOR+" "+user.getUsername());
	    	//taskService.listTasks(user, tasksCallback);
	    }
	}
    


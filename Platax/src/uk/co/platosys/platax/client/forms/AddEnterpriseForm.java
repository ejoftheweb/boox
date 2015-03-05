package uk.co.platosys.platax.client.forms;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.FTab;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.services.EnterpriseService;
import uk.co.platosys.platax.client.services.EnterpriseServiceAsync;
import uk.co.platosys.platax.client.widgets.html.StringHTML;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTModule;
import uk.co.platosys.platax.shared.boox.GWTRole;
import uk.co.platosys.platax.shared.boox.GWTSegment;
import uk.co.platosys.pws.fieldsets.DateField;
import uk.co.platosys.pws.fieldsets.ListField;
import uk.co.platosys.pws.fieldsets.SubmitField;
import uk.co.platosys.pws.fieldsets.TextField;
import uk.co.platosys.pws.fieldsets.TickBoxField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * This panel is used to add an enterprise to a user's portfolio.
 * 
 * AddEnterprise is a multistage form. The first stage involves just registering the
 * enterprise's particulars; the second collects more detail about the operations so
 * as to set up a more customised accounting system. 
 * 
 * 
 * @author edward
 *
 */
//TODO manage the delegation from another user...

public class AddEnterpriseForm extends FTab {
				static final String CAPITAL_SEGMENT_NAME="capital";
			    
				final EnterpriseServiceAsync enterpriseService = (EnterpriseServiceAsync) GWT.create(EnterpriseService.class);
                ArrayList<GWTSegment> segments;
                ArrayList<GWTModule> modules;
                Platax platax;
                
                
				//page1-2 callback
				final AsyncCallback<GWTEnterprise> callback1 = new AsyncCallback<GWTEnterprise>(){
					public void onSuccess(GWTEnterprise result){
						if(result==null){Window.alert("Server error-null result for submit1 call");}
						//Window.alert(result.getName() +" created...");
						platax.addTab(new ModulesForm(platax,  result));
						platax.removeTab(AddEnterpriseForm.this);
					}
					public void onFailure(Throwable cause){
					   StackTraceElement[] st = cause.getStackTrace();
					   String error = "registration failed\n";
					   error = error+cause.getClass().getName()+"\n";
					   for (int i=0; i<st.length; i++){
						   error = error + st[i].toString()+ "\n";
					   }
						Window.alert(error);
					}
				};	
			
				public AddEnterpriseForm(Platax platax){
					super();
					final TextField nameField = new TextField(FieldText.NAME, 1000, this, true);
					final TextField legalNameField = new TextField(FieldText.LEGALNAME, 2000, this, true);
					final ListField orgTypeField=new ListField(FieldText.ENTERPRISE_TYPE, 3000, this, true);
					final ListField roleTypeField=new ListField(FieldText.ENTERPRISE_ROLE, 4000, this, true);
				    final TickBoxField startupField= new TickBoxField(FieldText.ENTERPRISE_STARTUP, 5000, this, true);
				    final DateField dateField=new DateField(FieldText.ENTERPRISE_STARTDATE, 6000, this, true);
				    final SubmitField submitField= new SubmitField(12000, this);
				    final AsyncCallback<Boolean> namecheckCallback = new AsyncCallback<Boolean>(){
						@Override
						public void onFailure(Throwable caught) {
						   StackTraceElement[] st = caught.getStackTrace();
						   String error = "getting segments failed\n";
						   error = error+caught.getClass().getName()+"\n";
						   for (int i=0; i<st.length; i++){
							   error = error + st[i].toString()+ "\n";
						   }
						   Window.alert(error);
						}
						@Override
						public void onSuccess(Boolean result) {
							if(result==null){Window.alert("Server error-null result for namecheck call");}
							if (result.booleanValue()){
								nameField.setOK(true);
								setTabHead(new StringHTML(nameField.getValue()));
								AddEnterpriseForm.this.setCloseConfirm(true);
								AddEnterpriseForm.this.setCloseConfirmMessage(StringText.CLOSE_CONFIRM_LOSE_CHANGES);
								nameField.moveNext();
							}else{
								nameField.setOK(false);
							}
						}
					};
					
				//Segment callback
				final AsyncCallback<ArrayList<GWTSegment>> segmentCallback = new AsyncCallback<ArrayList<GWTSegment>>(){
					@Override
					public void onFailure(Throwable caught) {
						StackTraceElement[] st = caught.getStackTrace();
						   String error = "getting segments failed\n";
						   error = error+caught.getClass().getName()+"\n";
						   for (int i=0; i<st.length; i++){
							   error = error + st[i].toString()+ "\n";
						   }
						   Window.alert(error);
					}
					@Override
					public void onSuccess(ArrayList<GWTSegment> result) {
						if(result==null){Window.alert("Server error-null result for segment call");}
						segments=result;
						for (GWTSegment segment:segments){
							if(segment.getName().equals(CAPITAL_SEGMENT_NAME)){
								orgTypeField.addItems(segment.getModules());
								break;
							}
						}
					}
				};
			    //roleCallback: 
				final AsyncCallback<ArrayList<GWTRole>> roleCallback = new AsyncCallback<ArrayList<GWTRole>>(){
					@Override
					public void onFailure(Throwable caught) {
						StackTraceElement[] st = caught.getStackTrace();
						   String error = "getting roles failed\n";
						   error = error+caught.getClass().getName()+"\n";
						   for (int i=0; i<st.length; i++){
							   error = error + st[i].toString()+ "\n";
						   }
							Window.alert(error);
					}
					@Override
					public void onSuccess(ArrayList<GWTRole> result) {
						if(result==null){Window.alert("Server error-null result for roles call");}
						
						for(GWTRole role:result){
							roleTypeField.addItem(role.getName(), role.getLocalisedName());
						}
					}
				};
			    
			    
				setCloseConfirm(false);
				this.platax=platax;
				setTitle(LabelText.NEW_ENTERPRISE_PAGE_HEAD);
				setSubTitle(LabelText.NEW_ENTERPRISE_PAGE_SUB_HEAD);
				
				
			enterpriseService.getSegments(segmentCallback);
			enterpriseService.getRoles(roleCallback);
			//Layout Page
			this.setTabHead(new StringHTML(LabelText.NEW_ENTERPRISE));
			
			
			
			
			nameField.addValueChangeHandler(new ValueChangeHandler<String>(){
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					enterpriseService.isNameOK(nameField.getValue(), namecheckCallback);
				}
			});
			
			
			submitField.addClickHandler(new ClickHandler(){
				public void onClick (ClickEvent click){
					String name = nameField.getValue();
					String legalname =legalNameField.getValue();
					String orgtype=orgTypeField.getValue();
					String role=roleTypeField.getValue();
					boolean isStartup = startupField.getValue();
					//Date startDate = startDateBox.getValue();
					
					enterpriseService.registerEnterprise(name, legalname, orgtype, role, isStartup, new Date(), callback1);
					setTitle(StringText.THANKYOU);
					setSubTitle(StringText.WAIT_FOR_SERVER);
					clear();
				}
			});
			render();
		}
			

			@Override
			public void refresh() {
				// TODO Auto-generated method stub
				
			}


			@Override
			public void clear() {
				// TODO Auto-generated method stub
				
			}


			@Override
			public Iterator<Widget> iterator() {
				// TODO Auto-generated method stub
				return null;
			}


			@Override
			public boolean remove(Widget widget) {
				// TODO Auto-generated method stub
				return false;
			}
}

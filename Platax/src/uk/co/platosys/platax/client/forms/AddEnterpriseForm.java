package uk.co.platosys.platax.client.forms;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.DateFormats;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.MessageText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.Styles;
import uk.co.platosys.platax.client.services.EnterpriseService;
import uk.co.platosys.platax.client.services.EnterpriseServiceAsync;
import uk.co.platosys.platax.client.services.UserService;
import uk.co.platosys.platax.client.services.UserServiceAsync;
import uk.co.platosys.platax.client.widgets.AddressWidget;
import uk.co.platosys.platax.client.widgets.CheckList;
import uk.co.platosys.platax.client.widgets.CheckPanel;
import uk.co.platosys.platax.client.widgets.HMVListBox;
import uk.co.platosys.platax.client.widgets.PDateBox;
import uk.co.platosys.platax.client.widgets.buttons.CancelButton;
import uk.co.platosys.platax.client.widgets.labels.FieldInfoLabel;
import uk.co.platosys.platax.client.widgets.labels.FieldLabel;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTModule;
import uk.co.platosys.platax.shared.boox.GWTRatio;
import uk.co.platosys.platax.shared.boox.GWTRole;
import uk.co.platosys.platax.shared.boox.GWTSegment;
import uk.co.platosys.platax.shared.boox.GWTSelectable;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

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

public class AddEnterpriseForm extends AbstractForm {
				static final String CAPITAL_SEGMENT_NAME="capital";
			    //Declare Variables
				//Information for the first page.
				//Company Name
				final TextBox nameBox = new TextBox();
				final FieldLabel nameLabel= new FieldLabel(LabelText.ENTERPRISE_NAME);
				final FieldInfoLabel nameInfoLabel=new FieldInfoLabel(LabelText.ENTERPRISE_NAME_INFO);
				//Company Legal Name
				final TextBox legalNameBox = new TextBox();
				final FieldLabel legalNameLabel= new FieldLabel(LabelText.ENTERPRISE_LEGAL_NAME);
				final FieldInfoLabel legalNameInfoLabel=new FieldInfoLabel(LabelText.ENTERPRISE_LEGAL_NAME_INFO);
				//Organisation type
				final ListBox orgTypeList = new ListBox();
				final FieldLabel orgTypeLabel=new FieldLabel(LabelText.ENTERPRISE_TYPE);			
				final FieldInfoLabel orgTypeInfoLabel=new FieldInfoLabel(LabelText.ENTERPRISE_TYPE_INFO);
				//User's role in the business				
				final ListBox roleList = new ListBox();
				final FieldLabel roleLabel=new FieldLabel(LabelText.ENTERPRISE_ROLE);			
				final FieldInfoLabel roleInfoLabel=new FieldInfoLabel(LabelText.ENTERPRISE_ROLE_INFO);
				//Is it a startup?
				final CheckBox isStartupCheckBox = new CheckBox();
				final FieldLabel isStartupLabel=new FieldLabel(LabelText.IS_STARTUP);
				final FieldInfoLabel isStartupInfoLabel = new FieldInfoLabel(LabelText.IS_STARTUP_INFO);
				//Reference date picker
				final DateBox startDateBox = new PDateBox();
				final FieldLabel startDateLabel = new FieldLabel(LabelText.START_DATE);
				final FieldInfoLabel startDateInfoLabel = new FieldInfoLabel(LabelText.START_DATE_INFO);
				//Agree terms?
				final CheckBox termsCheckBox = new CheckBox();
				final FieldLabel termsLabel=new FieldLabel(LabelText.TERMS);
				final FieldInfoLabel termsInfoLabel = new FieldInfoLabel(LabelText.TERMS_INFO);
				//submit button
				final Button submitButton=new Button(ButtonText.NEXT);
				final FieldLabel submitInfoLabel=new FieldLabel("");
				final FieldInfoLabel submitLabel=new FieldInfoLabel(LabelText.ENTERPRISE_REGISTER);
				//////
				final EnterpriseServiceAsync enterpriseService = (EnterpriseServiceAsync) GWT.create(EnterpriseService.class);
                ArrayList<GWTSegment> segments;
                ArrayList<GWTModule> modules;
                Platax platax;
                /////Callbacks:
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
							nameInfoLabel.setText(LabelText.NAME_OK);
							setTabHeaderText(nameBox.getValue());
							AddEnterpriseForm.this.setCloseConfirm(true);
							AddEnterpriseForm.this.setCloseConfirmMessage(StringText.CLOSE_CONFIRM_LOSE_CHANGES);
							nameInfoLabel.setAlarmed(false);
							legalNameBox.setEnabled(true);
							legalNameBox.setFocus(true);
						}else{
							nameInfoLabel.setText(LabelText.NAME_NOT_OK);
							nameInfoLabel.setAlarmed(true);
							nameBox.setEnabled(true);
							nameBox.setValue(null);
							nameBox.setFocus(true);
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
								fillList(orgTypeList, segment);
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
							roleList.addItem(role.getName(), role.getLocalisedName());
						}
					}
				};
				//page1-2 callback
				final AsyncCallback<GWTEnterprise> callback1 = new AsyncCallback<GWTEnterprise>(){
					
					public void onSuccess(GWTEnterprise result){
						if(result==null){Window.alert("Server error-null result for submit1 call");}
						if(!(incrementCounter())){
							Window.alert("page counter error");
						}
						layoutPage2(result);
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
				
				//registration-finished callback
				final AsyncCallback<GWTEnterprise> finishedCallback = new AsyncCallback<GWTEnterprise>(){
					public void onSuccess(GWTEnterprise result){
						if(result==null){Window.alert("Server error-null result for submit2 call");}
						
						platax.removeTab(AddEnterpriseForm.this);
						platax.addTab(new EnterpriseTab(platax, result));
						//platax.addTab(new AddEnterpriseForm(platax));
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
				super(platax, StringText.ADD_NEW, 2);
				setStyleName(Styles.PTAB_ENTERPRISE);
				setHeadStyleName(Styles.PTABH_ENTERPRISE);
				init(platax);
			}
			
			private void init(Platax platax){
				setCloseConfirm(false);
				platax=platax;
				setTitle(LabelText.NEW_ENTERPRISE_PAGE_HEAD);
				setSubTitle(LabelText.NEW_ENTERPRISE_PAGE_SUB_HEAD);
				nameBox.setEnabled(true);
				legalNameBox.setEnabled(false);
				orgTypeList.addItem(StringText.PLEASE_SELECT, StringText.NULL);
				roleList.addItem(StringText.PLEASE_SELECT, StringText.NULL);
				submitButton.setEnabled(false);
				startDateBox.setVisible(true);
				//namecheck callback
				
			enterpriseService.getSegments(segmentCallback);
			enterpriseService.getRoles(roleCallback);
			//Layout Page
			this.setTabHeaderText(LabelText.NEW_ENTERPRISE);
			//vpanel.add(topLabel);
			
			table.setWidget(0,0, nameLabel);
			table.setWidget(0,1, nameBox);
			table.setWidget(0,2, nameInfoLabel);
			
			table.setWidget(1,0, legalNameLabel);
			table.setWidget(1,1, legalNameBox);
			table.setWidget(1,2, legalNameInfoLabel);
			
			table.setWidget(2,0, orgTypeLabel);
			table.setWidget(2,1, orgTypeList);
			table.setWidget(2,2, orgTypeInfoLabel);
			
			table.setWidget(3,0, roleLabel);
			table.setWidget(3,1, roleList);
			table.setWidget(3,2, roleInfoLabel);
			
			table.setWidget(4,0, isStartupLabel);
			table.setWidget(4,1, isStartupCheckBox);
			table.setWidget(4,2, isStartupInfoLabel);
			
			table.setWidget(5,0, startDateLabel);
			table.setWidget(5,1, startDateBox);
			table.setWidget(5,2, startDateInfoLabel);
			
			table.setWidget(6,0, termsLabel);
			table.setWidget(6,1, termsCheckBox);
			table.setWidget(6,2, termsInfoLabel);
			/*while(segments==null){
				//do something smarter here. Actually, we can.
				Window.alert("waiting for segments/smarter solution");
			}*/
			//a row and a listbox for each segment
			
			
			
			table.setWidget(7,0, submitLabel );
			table.setWidget(7,1, submitButton);
			table.setWidget(7,2, new CancelButton());
			
			//form.add(table);
			//vpanel.add(form);
			//this.add(vpanel);
			//Add handlers
			
			
			nameBox.addChangeHandler(new ChangeHandler(){
				@Override
				public void onChange(ChangeEvent event) {
					enterpriseService.isNameOK(nameBox.getValue(), namecheckCallback);
					//legalNameBox.setEnabled(true);
					//legalNameBox.setFocus(true);
					
				}
			});
			
			legalNameBox.addChangeHandler(new ChangeHandler(){
				@Override
				public void onChange(ChangeEvent event) {
					orgTypeList.setEnabled(true);
				}
			});
			
			orgTypeList.addChangeHandler(new ChangeHandler(){
				@Override
				public void onChange(ChangeEvent event) {
					if (!(orgTypeList.isItemSelected(0))){
						roleList.setEnabled(true);
				}}
			});
			roleList.addChangeHandler(new ChangeHandler(){
				@Override
				public void onChange(ChangeEvent event) {
					if (!(roleList.isItemSelected(0))){
						isStartupCheckBox.setEnabled(true);
				}}
			});
			isStartupCheckBox.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					if (isStartupCheckBox.getValue()){
						Window.alert(StringText.IS_STARTUP_CONFIRM);
					}else{
						Window.alert(StringText.IS_NOT_STARTUP_CONFIRM);
					}
					//startDateBox.setVisible(true);
				}
			});
			/*startDateBox.addValueChangeHandler(new ValueChangeHandler<Date>(){
				@Override
				public void onValueChange(ValueChangeEvent<Date> event){
					
				}
			});*/
			termsCheckBox.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					if (termsCheckBox.getValue()){
						Window.alert(StringText.TERMS_CONFIRM);
						submitButton.setEnabled(true);
					}else{
						Window.alert(StringText.TERMS_NOT_CONFIRM);
					}
					
				}
			});
			submitButton.addClickHandler(new ClickHandler(){
				public void onClick (ClickEvent click){
					String name = nameBox.getValue();
					String legalname =legalNameBox.getValue();
					String orgtype=orgTypeList.getValue(orgTypeList.getSelectedIndex());
					String role=roleList.getValue(roleList.getSelectedIndex());
					boolean isStartup = isStartupCheckBox.getValue();
					Date startDate = startDateBox.getValue();
					
					enterpriseService.registerEnterprise(name, legalname, orgtype, role, isStartup, startDate, callback1);
					setTitle(StringText.THANKYOU);
					setSubTitle(StringText.WAIT_FOR_SERVER);
					form.remove(table);
				}
			});
			
		}
			private void fillList(ListBox list, GWTSegment content){
				List<GWTModule> modules = content.getModules();
				for(GWTModule module:modules){
					if (module.getSegment().equals(content.getName())){
						list.addItem( module.getDescription(), module.getName());
					}
				}
			}
			private void layoutPage2(final GWTEnterprise ent){
				setTitle(LabelText.NEW_ENTERPRISE_PAGE2_HEAD+" "+ent.getName());
				setSubTitle(LabelText.NEW_ENTERPRISE_PAGE2_SUB_HEAD);
				int rowno=0;
				Window.alert(Integer.toString(segments.size()));
				for (GWTSegment segment:segments){
					
					table.setWidget(rowno, 0, new FieldLabel(segment.getName()));
									
					if (segment.isMultiSelect()){
						CheckList cp = new CheckList();
						table.setWidget(rowno, 1, cp);
					    cp.addItems(segment.getModules());;
					    table. setWidget(rowno,2, new FieldInfoLabel(segment.getInstructions()));
					
					}else{
						HMVListBox segmods = new HMVListBox();
						table.setWidget(rowno, 1, segmods);
					    fillList(segmods, segment);
					    table. setWidget(rowno,2, new FieldInfoLabel(segment.getInstructions()));
					}
					segment.setRowIndex(rowno);
					rowno++;
				}
				//The submit row:
			    table.setWidget(rowno, 0, new FieldLabel(ButtonText.CONFIRM));
			    final Button confirmButton = new Button(ButtonText.CONFIRM);
				table.setWidget(rowno, 1, confirmButton);
				confirmButton.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						ArrayList<String> modulenames=new ArrayList<String>();
						for(GWTSegment segment:segments){
							if (segment.isMultiSelect()){
								CheckList cp = (CheckList) table.getWidget(segment.getRowIndex(), 1);
								modulenames.addAll(cp.getValues());
							}else{
								HMVListBox box = (HMVListBox) table.getWidget(segment.getRowIndex(), 1);
								modulenames.addAll(box.getValues());
							}
						}
						String confirm=StringText.MODULES_CONFIRM;
						for(String modulename:modulenames){
							confirm=confirm+modulename+"\n";
						}
						if(Window.confirm(confirm)){
							enterpriseService.addEnterpriseModules(ent.getSysname(), modulenames, finishedCallback);
						}
					}
				});
				form.add(table);
			}

			@Override
			public void refresh() {
				// TODO Auto-generated method stub
				
			}
}

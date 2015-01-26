package uk.co.platosys.platax.client.forms;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.DateFormats;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.MessageText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.Styles;
import uk.co.platosys.platax.client.services.EnterpriseService;
import uk.co.platosys.platax.client.services.EnterpriseServiceAsync;
import uk.co.platosys.platax.client.services.UserService;
import uk.co.platosys.platax.client.services.UserServiceAsync;
import uk.co.platosys.platax.client.widgets.AddressWidget;
import uk.co.platosys.platax.client.widgets.HMVListBox;
import uk.co.platosys.platax.client.widgets.buttons.CancelButton;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTModule;
import uk.co.platosys.platax.shared.boox.GWTRatio;
import uk.co.platosys.platax.shared.boox.GWTRole;
import uk.co.platosys.platax.shared.boox.GWTSegment;
import uk.co.platosys.platax.shared.boox.GWTSelectable;
import uk.co.platosys.pws.fieldsets.CheckListField;
import uk.co.platosys.pws.fieldsets.DateField;
import uk.co.platosys.pws.fieldsets.HasStringValues;
import uk.co.platosys.pws.fieldsets.ListField;
import uk.co.platosys.pws.fieldsets.RadioField;
import uk.co.platosys.pws.fieldsets.SubmitField;
import uk.co.platosys.pws.fieldsets.TextField;
import uk.co.platosys.pws.fieldsets.TickBoxField;
import uk.co.platosys.pws.inputfields.CheckList;
import uk.co.platosys.pws.inputfields.PDateBox;
import uk.co.platosys.pws.labels.FieldInfoLabel;
import uk.co.platosys.pws.labels.FieldLabel;
import uk.co.platosys.pws.values.ValuePair;

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
 * This panel is used to add modules to an enterprise.
 * 
 * 
 * @author edward
 *
 */
//TODO we should disable any modules it already has.

public class ModulesForm extends AbstractForm {
				static final String CAPITAL_SEGMENT_NAME="capital";
			    final EnterpriseServiceAsync enterpriseService = (EnterpriseServiceAsync) GWT.create(EnterpriseService.class);
                ArrayList<GWTSegment> segments;
                ArrayList<GWTModule> modules;
                ArrayList<HasStringValues> fields=new ArrayList<HasStringValues>();
                Platax platax;
             	//registration-finished callback
				final AsyncCallback<GWTEnterprise> finishedCallback = new AsyncCallback<GWTEnterprise>(){
					public void onSuccess(GWTEnterprise result){
						if(result==null){Window.alert("Server error-null result for submit2 call");}
						platax.removeTab(ModulesForm.this);
						//platax.addTab(new EnterpriseTab(platax, result));
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
			
			public ModulesForm(Platax platax, final GWTEnterprise enterprise ){
				super(platax, enterprise.getName());
				this.platax=platax;
				segments=enterprise.getSegments();
				int pos=1000;
				for (GWTSegment segment:segments){
					if(!((segment.getName().equals(CAPITAL_SEGMENT_NAME)))){
						String[] segArray = {segment.getName(), segment.getDescription(), null, null};
						if (segment.isMultiSelect()){
							CheckListField clf = new CheckListField(segArray, pos, this, false);
							List<GWTModule> vals=segment.getModules();
							clf.addItems(vals);
							fields.add(clf);
						}else{
							List<GWTModule> vals = segment.getModules();
							RadioField rf = new RadioField(segment.getName(), segArray, vals,  segment.getDefaultModule(), pos, this, false);
							fields.add(rf);
						}
					}
					pos=pos+1000;
				}
	
				SubmitField submitField=new SubmitField(pos, this);
				submitField.setEnabled(true);
				submitField.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						Window.alert("adding Modules");
						ArrayList<String> modulenames=new ArrayList<String>();
						for(HasStringValues field:fields){
							modulenames.addAll(field.getValues());
						}
						String confirm=StringText.MODULES_CONFIRM;
						for(String modulename:modulenames){
							confirm=confirm+modulename+"\n";
						}
						if(Window.confirm(confirm)){
							enterpriseService.addEnterpriseModules(enterprise.getSysname(), modulenames, finishedCallback);
						}
					}
				});
				//setCloseConfirm(false);
				this.platax=platax;
				setTitle(LabelText.NEW_ENTERPRISE_PAGE2_HEAD);
				setSubTitle(LabelText.NEW_ENTERPRISE_PAGE2_SUB_HEAD);
				render();
		}
			
		

			@Override
			public void refresh() {
				// TODO Auto-generated method stub
				
			}
}

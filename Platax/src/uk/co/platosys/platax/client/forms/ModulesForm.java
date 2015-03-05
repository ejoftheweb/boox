package uk.co.platosys.platax.client.forms;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.EFTab;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.services.EnterpriseService;
import uk.co.platosys.platax.client.services.EnterpriseServiceAsync;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTModule;
import uk.co.platosys.platax.shared.boox.GWTSegment;
import uk.co.platosys.pws.fieldsets.CheckListField;
import uk.co.platosys.pws.fieldsets.HasStringValues;
import uk.co.platosys.pws.fieldsets.RadioField;
import uk.co.platosys.pws.fieldsets.SubmitField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This panel is used to add modules to an enterprise.
 * 
 * 
 * @author edward
 *
 */
//TODO we should disable any modules it already has.

public class ModulesForm extends EFTab {
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
				super();
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
							RadioField rf = new RadioField(segment.getName(), segArray, vals,  pos, this, false);
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

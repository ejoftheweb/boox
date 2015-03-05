package uk.co.platosys.platax.client.components;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.Styles;
import uk.co.platosys.platax.client.widgets.labels.TabEnterpriseLabel;
import uk.co.platosys.platax.client.widgets.labels.TabPageTitleLabel;
import uk.co.platosys.platax.client.widgets.labels.TabPageSubtitleLabel;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.fieldsets.FormField;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * An ETab is a component tab in a PlataxTabPanel.
 * 
 * It inherits from UTab and adds a label to the status panel.  
 * 
 * An ETab is always associated with a single enterprise.
 * 
 * 
 * @author edward
 *
 */

public  abstract class FTab extends UTab implements Form {
 protected Platax platax;
 private int index=-2; //the index of this PTab in its parent. 
 private FormPanel formPanel=new FormPanel();
 private SortedMap<Integer, FormField> fields = new TreeMap<Integer, FormField>(); 
 protected FlexTable table=new FlexTable();
	
 public FTab(){
	 platax = Platax.getCurrentInstance();
	 setup(platax);
 }
 @Deprecated
 public  FTab(Platax platax){
	 setup(platax);
 }
 private void setup(Platax platax){
	 contentPage.add(formPanel);
	 formPanel.add(table);
 }

 


//if you want these methods to do anything, override them in the subclass.
public  void clear(){
	formPanel.remove(table);
}

public Iterator<Widget> iterator(){return null;}
public boolean remove(Widget widget){return false;}
public  void refresh(){};




/**This method should be overridden in subclasses.
 * 
 */
public void select(){
	platax.setCurrentEnterprise(null);
}
/**
 * @return the formPanel*/

public FormPanel getFormPanel() {
	return formPanel;
}
public int addField(FormField<?> field) throws Exception {
	//Window.alert("adding formfield "+field.toString());
	Integer finx = new Integer(field.getPosition());
	if (fields.containsKey(finx)){
		Window.alert("panel already contains a field at position "+finx.toString()+"\n"+fields.get(finx).getLabel().toString());
		throw new Exception("panel already contains a field at position "+finx.toString());
	} 
	else{
		fields.put(finx, field);
	    return finx;
	}
}

@SuppressWarnings("rawtypes")
public FormField getNextField(FormField currentField){
	Integer position = currentField.getPosition();
	//Window.alert("There are "+fields.keySet().size()+" fields to check" );
    Iterator<Integer> it = fields.keySet().iterator();
    while(it.hasNext()){
    	Integer k = it.next();
    	if(k.equals(position)){
    		if(it.hasNext()){
    			//Window.alert("getting next field now");
    			return fields.get(it.next());
    		}else{
    			return currentField;
    		}
    	}
    }
    return null;//should never get here.
}
public boolean removeField(FormField<?> field) throws Exception {
	Integer pos = new Integer(field.getPosition());
	return(fields.remove(pos)!=null);
}

public void render(){
	//Window.alert("there are "+fields.size()+" fields");
	
	int i=0;
	for (Integer key:fields.keySet()){
	    table.setWidget(i,0, fields.get(key).getLabel());
		table.setWidget(i,1, fields.get(key).getWidget());
		table.setWidget(i,2, fields.get(key).getInfoLabel());
		i++;
	}
	fields.get(fields.firstKey()).setEnabled(true);
}

}
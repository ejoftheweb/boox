package uk.co.platosys.pws.inputfields;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.fieldsets.FormField;
import uk.co.platosys.pws.labels.PopupHeaderLabel;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.PopupPanel;

public class AbstractPopupForm extends PopupPanel implements Form {
	FormPanel formPanel=new FormPanel();
	FlexTable table = new FlexTable();
	PopupHeaderLabel header = new PopupHeaderLabel();
	@SuppressWarnings("rawtypes")
	private SortedMap<Integer, FormField> fields = new TreeMap<Integer, FormField>(); 
    
	public PopupPanel.PositionCallback poscall =(new PopupPanel.PositionCallback() {
        public void setPosition(int offsetWidth, int offsetHeight) {
          int left = (Window.getClientWidth() - offsetWidth) / 2;
          int top = (Window.getClientHeight() - offsetHeight) / 3;
          AbstractPopupForm.this.setPopupPosition(left, top);
        }
      });
	public AbstractPopupForm(String headText){
		header.setText(headText);
		this.setWidget(formPanel);
		formPanel.add(table);
		setGlassEnabled(true);
	}
	
	@Override
	public int addField(FormField<?> field) throws Exception {
		Integer finx = new Integer(field.getPosition());
		if (fields.containsKey(finx)){
			Window.alert("form already contains a field at position "+finx.toString());
			throw new Exception("form already contains a field at position "+finx.toString());
		} 
		else{
			fields.put(finx, field);
		    return finx;
		}
	}
	@SuppressWarnings("rawtypes")
	@Override
	public FormField getNextField(FormField currentField) {
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
	public void render(){
		table.getFlexCellFormatter().setColSpan(0, 0, 3);
		table.setWidget(0, 0,header);
		int i=1;
		for (Integer key:fields.keySet()){
		    table.setWidget(i,0, fields.get(key).getLabel());
			table.setWidget(i,1, fields.get(key).getWidget());
			table.setWidget(i,2, fields.get(key).getInfoLabel());
			i++;
		}
		fields.get(fields.firstKey()).setEnabled(true);
	}
}

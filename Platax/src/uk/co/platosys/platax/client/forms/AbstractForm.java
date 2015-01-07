package uk.co.platosys.platax.client.forms;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.widgets.CheckPanel;
import uk.co.platosys.platax.client.widgets.PlataxTabPanel;
import uk.co.platosys.platax.client.widgets.buttons.CancelButton;
import uk.co.platosys.platax.client.widgets.labels.FieldInfoLabel;
import uk.co.platosys.platax.client.widgets.labels.FieldLabel;
import uk.co.platosys.platax.client.widgets.labels.FormHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.FormSubHeaderLabel;
import uk.co.platosys.platax.shared.boox.GWTSelectable;
import uk.co.platosys.pws.fieldsets.AbstractFormField;

/**
 *  AbstractForm extends PTab; in Platax forms are always PTabs (unless they're popups).
 *  
 *  This means that multiple forms are kept open until completed.
 *  
 * @author edward
 *
 */
public abstract class AbstractForm extends uk.co.platosys.platax.client.widgets.PTab {
	protected FlexTable table=new FlexTable();
	protected FormPanel formPanel=new FormPanel();
	protected FlowPanel form=new FlowPanel();
	private InlineLabel counter;
	public static PlataxTabPanel parent;
	protected int totalPages;
	protected int pageNumber;
	private final FormHeaderLabel topLabel = new FormHeaderLabel();//"About your enterprise");
    private final FormSubHeaderLabel subHeader = new FormSubHeaderLabel();//"Please fill in as much as you can"
    private SortedMap<Integer, AbstractFormField> fields = new TreeMap<Integer, AbstractFormField>(); 
    public AbstractForm(Platax platax) {
		super(platax);
		form.setWidth("100%");
		form.add(topLabel);
		form.add(subHeader);
		form.add(table);
		table.setWidth("100%");
		formPanel.add(form);
		this.add(formPanel);
		setTabHeaderText("BlankForm");
	}
	/**
	 * use this for multistage forms
	 * it implements a counter
	 * @param pages
	 */
	public AbstractForm(Platax platax, int pages) {
		super(platax);
		setCounter(pages);
		
		form.setWidth("100%");
		form.add(topLabel);
		form.add(subHeader);
		form.add(table);
		table.setWidth("100%");
		
		formPanel.add(form);
		this.add(formPanel);
		setTabHeaderText("BlankForm");
	}
    public AbstractForm(Platax platax, String title){
    	super( platax);
		form.setWidth("100%");
		form.add(topLabel);
		form.add(subHeader);
		form.add(table);
		table.setWidth("100%");
		
		formPanel.add(form);
		this.add(formPanel);
		setTabHeaderText(title);
    }
    public AbstractForm(Platax platax, String title, int pages){
    	super(platax);
    	setCounter(pages);
		
		form.setWidth("100%");
		form.add(topLabel);
		form.add(subHeader);
		form.add(table);
		table.setWidth("100%");
		
		formPanel.add(form);
		this.add(formPanel);
		setTabHeaderText(title);
    }
	public void close(){
		parent.remove(this);
	}
	public void fillList(ListBox listBox, TreeMap<String, GWTSelectable> content){
		listBox.addItem("please select..", "");
		Set<String> keyset =content.keySet();
		Iterator<String> sit = keyset .iterator();
		while (sit.hasNext()){
			String key = sit.next();
			listBox.addItem(content.get(key).getDescription(), key);
		}
	}
	
	public void fillCheckPanel(CheckPanel panel, TreeMap<String, GWTSelectable> content){
		for (String key: content.keySet()){
			GWTSelectable module = content.get(key);
			String desc=module.getDescription();
			String name=module.getSysname();
			panel.addItem(name, desc);
		}
	}
	private void setCounter(int pages){
		counter=getCounter();
		pageNumber=1;
		totalPages=pages;
		counter.setText(" "+Integer.toString(pageNumber)+"/"+Integer.toString(totalPages));
	}
	protected boolean incrementCounter(){
		if (counter==null){return false;}else{
			pageNumber++;
			if (pageNumber>totalPages){return false;}else{
				setCounter(totalPages);	
			}return true; 
		}
	}
	final Button nextButton=new Button(ButtonText.NEXT);
	final FieldInfoLabel nextInfoLabel=new FieldInfoLabel("");
	final FieldLabel nextLabel=new FieldLabel(LabelText.MORE);
	CancelButton cancelButton = new CancelButton();
	public void setTitle(String title){
		topLabel.setText(title);
	}
	public void setSubTitle(String subTitle){
		subHeader.setText(subTitle);
	}
	public void clear(){}
	public void add(IsWidget widget){}
	public Iterator<Widget> iterator(){return null;}
	public boolean remove(Widget widget){return false;}
	public abstract void refresh();
	public int addField(AbstractFormField<?> field) throws Exception {
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
	
	public AbstractFormField getNextField(AbstractFormField currentField){
		return null;
	}
	public boolean removeField(AbstractFormField<?> field) throws Exception {
		//TODO
		return false;
	}
	public void render(){
		int i=0;
		for (Integer key:fields.keySet()){
		    Window.alert("there are "+fields.size()+" fields");
			table.setWidget(i,0, fields.get(key).getLabel());
			table.setWidget(i,1, fields.get(key).getWidget());
			table.setWidget(i,2, fields.get(key).getInfoLabel());
			i++;
		}
		fields.get(fields.firstKey()).setEnabled(true);
	}
}

package uk.co.platosys.platax.client.forms;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTabPanel;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.widgets.buttons.CancelButton;
import uk.co.platosys.platax.client.widgets.labels.FormHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.FormSubHeaderLabel;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTSelectable;
import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.fieldsets.FormField;
import uk.co.platosys.pws.inputfields.CheckList;
import uk.co.platosys.pws.labels.FieldInfoLabel;
import uk.co.platosys.pws.labels.FieldLabel;

/**
 *  AbstractForm extends PTab; in Platax forms are always PTabs (unless they're popups).
 *  
 *  This means that multiple forms are kept open until completed.
 *  
 * @author edward
 *
 */
public abstract class AbstractForm extends uk.co.platosys.platax.client.components.PTab implements Form {
	
	protected FlexTable table=new FlexTable();
	private FormPanel forrmPanel=new FormPanel();
	protected FlowPanel menuPanel=new FlowPanel();
	protected FlowPanel formPanel=new FlowPanel();
	protected FlowPanel panel=new FlowPanel();
	private InlineLabel counter;
	public static PTabPanel parent;
	protected int totalPages;
	protected int pageNumber;
	private final FormHeaderLabel topLabel = new FormHeaderLabel();//"About your enterprise");
    private final FormSubHeaderLabel subHeader = new FormSubHeaderLabel();//"Please fill in as much as you can"
    @SuppressWarnings("rawtypes")
	private SortedMap<Integer, FormField> fields = new TreeMap<Integer, FormField>(); 
    protected GWTEnterprise enterprise; 
    
    public AbstractForm(){
    	super();
    	this.platax=Platax.getCurrentInstance();
    	this.enterprise=platax.getCurrentEnterprise();
    	setup();
    }
    
    @Deprecated
    public AbstractForm(Platax platax) {
		super(platax);
		setup();
		setTabHeaderText("BlankForm");
	}
	/**
	 * use this for multistage forms
	 * it implements a counter
	 * @param pages
	 */
    @Deprecated
	public AbstractForm(Platax platax, int pages) {
		super(platax);
		setCounter(pages);
		
		setup();
		
		this.add(panel);
		setTabHeaderText("BlankForm");
	}
    @Deprecated    
    public AbstractForm(Platax platax, String tabhead){
    	super( platax);
		setup();
		
		this.add(panel);
		setTabHeaderText(tabhead);
    }
   @Deprecated 
   public AbstractForm(Platax platax, String title, int pages){
    	super(platax);
    	setCounter(pages);
		
		setup();
		setTabHeaderText(title);
    }
	
    private void setup(){
    	//Window.alert("setting up AForm");
    	panel.setWidth("100%");
		panel.add(topLabel);
		panel.add(subHeader);
		panel.add(menuPanel);
		panel.add(forrmPanel);
		forrmPanel.add(formPanel);
		formPanel.add(table);
		table.setWidth("100%");
		this.add(panel);
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
	
	public void fillCheckPanel(CheckList panel, TreeMap<String, GWTSelectable> content){
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
	public int addField(FormField<?> field) throws Exception {
		Integer finx = new Integer(field.getPosition());
		if (fields.containsKey(finx)){
			Window.alert("panel already contains a field at position "+finx.toString());
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
	@Override
	public void select(){
		platax.setCurrentEnterprise(enterprise);
	}

	public GWTEnterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(GWTEnterprise enterprise) {
		this.enterprise = enterprise;
	}
}

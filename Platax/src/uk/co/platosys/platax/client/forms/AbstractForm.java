package uk.co.platosys.platax.client.forms;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

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
	protected final FormHeaderLabel topLabel = new FormHeaderLabel();//"About your enterprise");
    protected final FormSubHeaderLabel subHeader = new FormSubHeaderLabel();//"Please fill in as much as you can"
    
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
			String name=module.getName();
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
}

package uk.co.platosys.pws.fieldsets;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;

import uk.co.platosys.platax.client.forms.AbstractForm;
import uk.co.platosys.platax.client.widgets.labels.FieldInfoLabel;
import uk.co.platosys.platax.client.widgets.labels.FieldLabel;
import uk.co.platosys.pws.inputfields.AbstractValueField;

/**
 * A generic class that covers all form fields treated as triples: label/widget/infolabel, normally 
 * arranged in a vertical form layout and completed sequentially.
 * In a parent that extends abstract form these fields are disabled by default and enabled when the preceding
 * field is successfully completed.
 * The class also provides for browser-side validation against a supplied regex. Subclasses can implement 
 * asynchronous server-side validation (e.g. to check for username uniqueness) and in all cases all data
 * must be verified on the server-side to eliminate the possibility of uploading active code; this can usually be done
 * when the form payload is processed.
 * 
 *
 * @author edward
 *
 * @param <T>
 */



public abstract class AbstractFormField<T> implements HasEnabled {
    
	FieldLabel label=new FieldLabel();
	FieldInfoLabel infoLabel = new FieldInfoLabel();
	AbstractForm parent;
	AbstractValueField<T> widget;
	int position=0;
	String errorInfoLabel="";
	String validationRegex;
	boolean enabled=false;
	
	/**
	 * These triples - label, Widget, infolabel - go into tables that make up a Form.
	 * The position parameter sets the ordering. It's good practice to
	 * start forms using 100 or 1000 steps, then it's easy to put intermediate fields in
	 * later on without a painful renumbering. Remember how you did line numbers in BASIC? 
	 * 
	 * This constructor is deprecated, use the version that takes a string array argument.
	 * 
	 * @param labelText
	 * @param infoText
	 * @param widget
	 * @param index
	 */
	@Deprecated
	public AbstractFormField (String labelText, String infoText, AbstractValueField<T> widget, int position, AbstractForm parent){
		label.setText(labelText);
		infoLabel.setText(infoText);
		this.widget=widget;
		this.position=position;
		this.parent=parent;
		widget.setEnabled(enabled);
		widget.addValueChangeHandler(new ValueChangeHandler<T>(){
			@Override
			public void onValueChange(ValueChangeEvent<T> event) {
				validate();
			}
			
		});
		try {
			parent.addField(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//logger.log("error:",e);
		}
	}
	/**
	 * These triples - label, Widget, infolabel - go into tables that make up a Form.
	 * The position parameter sets the ordering. It's good practice to
	 * start forms using 100 or 1000 steps, then it's easy to put intermediate fields in
	 * later on without a painful renumbering. Remember how you did line numbers in BASIC? 
	 * 
	 * This constructor takes a String array argument for the labels and validation regex. 
	 * @param labelText
	 *
	 * @param widget
	 * @param index
	 */
	public AbstractFormField (String[] labelText, AbstractValueField<T> widget, int position, AbstractForm parent) throws IllegalArgumentException{
		if(labelText.length!=4){throw new IllegalArgumentException("label array must be size 4");}
		label.setText(labelText[0]);
		infoLabel.setText(labelText[1]);
		this.errorInfoLabel=(labelText[3]);
		this.validationRegex=labelText[2];
		this.widget=widget;
		this.position=position;
		this.parent=parent;
		widget.setEnabled(enabled);
		widget.addValueChangeHandler(new ValueChangeHandler<T>(){
			@Override
			public void onValueChange(ValueChangeEvent<T> event) {
				validate();
			}
			
		});
		try {
			parent.addField(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//logger.log("error:",e);
		}
	}
	/**
	 * These triples - label, Widget, infolabel - go into tables that make up a Form.
	 * The position parameter sets the ordering. It's good practice to
	 * start forms using 100 or 1000 steps, then it's easy to put intermediate fields in
	 * later on without a painful renumbering. Remember how you did line numbers in BASIC? 
	 * 
	 * This package-protected constructor takes a String array argument for the labels and validation regex. 
	 * 
	 * It doesn't have a widget argument; subclasses must set their own widget (which, actually, needs to 
	 * extend AbstractValueField). 
	 * 
	 * @param labelText
	 *
	 * @param widget
	 * @param index
	 */
	protected AbstractFormField (String[] labelText,  int position, AbstractForm parent) throws IllegalArgumentException{
		if(labelText.length!=4){throw new IllegalArgumentException("label array must be size 4");}
		label.setText(labelText[0]);
		infoLabel.setText(labelText[1]);
		this.errorInfoLabel=(labelText[3]);
		this.validationRegex=labelText[2];
		this.position=position;
		this.parent=parent;
		widget.setEnabled(enabled);
		widget.addValueChangeHandler(new ValueChangeHandler<T>(){
			@Override
			public void onValueChange(ValueChangeEvent<T> event) {
				validate();
			}
			
		});
		try {
			parent.addField(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//logger.log("error:",e);
		}
	}

	/**
	 * @return the label*/
	
	public FieldLabel getLabel() {
		return label;
	}

	/**
	 * @return the infoLabel*/
	
	public FieldInfoLabel getInfoLabel() {
		return infoLabel;
	}

	/**
	 * @return the widget*/
	
	public IsWidget getWidget() {
		return widget;
	}
	


	
	/**
	 * @return the position*/
	
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set*/
	
	public void setPosition(int position) {
		this.position = position;
	}
	public Object getValue(){
		return widget.getValue();
	}


	/**
	 * @return the validationRegex*/
	
	public String getValidationRegex() {
		return validationRegex;
	}


	/**
	 * @param validationRegex the validationRegex to set*/
	
	public void setValidationRegex(String validationRegex) {
		this.validationRegex = validationRegex;
	}


	/**
	 * @param widget the widget to set*/
	
	public void setWidget(AbstractValueField<T>widget) {
		this.widget = widget;
	}


	
	/**
	 * FormField carries out browser-side validation
	 * by default, verifying field values against a specified regex.
	 * The default is a simple regex that excludes all code markup characters.
	 * 
	 * Validation against a more specific regex is done by calling setValidationRegex() before
	 * validation.
	 * 
	 * Server-side validation must also be done; it is not sufficient to rely on browser-side validation.
	 * This will normally be done when the whole-form payload is submitted to the form's own servlet. 
	 * 
	 * @return
	 */
    public abstract boolean validate();


	/**
	 * @return the enabled*/
	
	public  boolean isEnabled() {
		return enabled;
	}


	/**
	 * @param enabled the enabled to set*/
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		widget.setEnabled(enabled);
	}
}
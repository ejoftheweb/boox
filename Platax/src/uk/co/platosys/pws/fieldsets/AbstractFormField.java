package uk.co.platosys.pws.fieldsets;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;

import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.inputfields.AbstractValueField;
import uk.co.platosys.pws.labels.FieldInfoLabel;
import uk.co.platosys.pws.labels.FieldLabel;


/**
 * This is the base class that covers all PWS form field triples: label/widget/infolabel, normally 
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



public abstract class AbstractFormField<T> implements FormField<T>, HasEnabled, Focusable {
    
	FieldLabel label=new FieldLabel();
	FieldInfoLabel infoLabel = new FieldInfoLabel();
	Form parent;
	AbstractValueField<T> widget;
	int position=0;
	String errorInfoLabel="";
	String validationRegex;
	boolean enabled=false;
	boolean required=true;
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
	protected AbstractFormField (String[] labelText,  int position, Form parent, boolean required) throws IllegalArgumentException{
		if(labelText.length!=4){throw new IllegalArgumentException("label array must be size 4");}
		label.setText(labelText[0]);
		infoLabel.setText(labelText[1]);
		this.errorInfoLabel=(labelText[3]);
		infoLabel.setErrorText(errorInfoLabel);
		this.validationRegex=labelText[2];
		this.position=position;
		this.parent=parent;
		this.required=required;
	}
	protected void start(){
		widget.setEnabled(enabled);
		widget.addValueChangeHandler(new ValueChangeHandler<T>(){
			@Override
			public void onValueChange(ValueChangeEvent<T> event) {
				if(validate()){
					setOK(true);
					moveNext();
				}else{
					setOK(false);
				};
			}
			
		});
		if(!required){
			widget.addKeyDownHandler(new KeyDownHandler(){
				@Override
				public void onKeyDown(KeyDownEvent event) {
					if((event.getNativeKeyCode()==KeyCodes.KEY_ENTER)||(event.getNativeKeyCode()==KeyCodes.KEY_TAB)){
						setOK(true);
						moveNext();
					}
				}
			});
		}
		try {
			parent.addField(this);
		} catch (Exception e) {
			
		}
	}
	public void moveNext(){
		@SuppressWarnings("rawtypes")
		FormField nextField = parent.getNextField(this);
		//Window.alert("next field position="+nextField.getPosition());
		nextField.setEnabled(true);
		nextField.setFocus(true);
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
	

    public void setFocus(boolean focus){
    	widget.setFocus(focus);
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
	public T getValue(){
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
	
	protected void setWidget(AbstractValueField<T>widget) {
		this.widget = widget;
	}
     protected String getLabelText(){
    	 return label.getText();
     }
     protected String getLabelInfoText(){
    	 return infoLabel.getText();
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
 * 
 */
	public void setOK(boolean OK){
		infoLabel.setOK(OK);
	}
	/**
	 * @param enabled the enabled to set*/
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		widget.setEnabled(enabled);
	}
	/**
	 * unless overridden in a subclass, this method will always return 0
	 */
	@Override
	public int getTabIndex() {return 0;}
	/**
	 * unless overriden in a subclass, this method does nothing
	 */
	@Override
	public void setAccessKey(char key) {}
	/**
	 * unless overridden in a subclass, this method does nothing
	 */
	@Override
	public void setTabIndex(int index) {}
	@Override
	public abstract HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) ;		 
	public void setValue(T value){
		
	}

	@Override
	public void setValue(T value, boolean fireEvents) {
		// TODO Auto-generated method stub
		
	}
}
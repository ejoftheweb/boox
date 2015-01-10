package uk.co.platosys.pws.inputfields;


import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;

/**
 * implementing subclasses add a GWT field such as a TextBox that returns a value to the flow panel and must implement the 
 * abstract methods of this class by passing the results to that object.
 * @author edward
 *
 * @param <T>
 * @param <D>
 */
public abstract  class AbstractValueField<T> extends FlowPanel implements  HasValue<T>, HasFieldValue<T>, HasValueChangeHandlers<T>, HasKeyDownHandlers, Focusable, HasEnabled {
	protected AbstractValueField() {}
    public abstract HandlerRegistration addKeyDownHandler(KeyDownHandler handler);
	public abstract HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler);
	@Override
	public abstract T getValue();
	@Override
	public abstract void setFocus(boolean focused);
	@Override
	public abstract void setEnabled (boolean enabled);
	@Override
	public abstract boolean isEnabled();
	
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
	public void setValue(T value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(T value, boolean fireEvents) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void fireEvent(GwtEvent<?> event) {
		// TODO Auto-generated method stub
		
	}
}

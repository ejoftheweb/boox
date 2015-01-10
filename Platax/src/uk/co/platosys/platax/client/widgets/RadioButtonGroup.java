package uk.co.platosys.platax.client.widgets;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;




import uk.co.platosys.pws.values.ValuePair;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget displaying a group of radio buttons (only one radio button can be selected inside a group)
 * 
 * @author Anthony Birembaut
 */
public class RadioButtonGroup extends Composite implements HasClickHandlers, ClickHandler, HasValueChangeHandlers<Boolean>, ValueChangeHandler<Boolean> {

    protected static int radioButtonGroupIndex = 0;
    
    /**
     * the flow panel used to display the widget
     */
    protected FlowPanel flowPanel;
    
    /**
     * The set of radio buttons in the group
     */
    protected Set<RadioButton> radioButtons = new HashSet<RadioButton>();
    
    /**
     * items style
     */
    protected String itemsStyle;
    
    /**
     * The radio buttons group name
     */
    protected String radioButtonGroupName;

    /**
     * click handlers registered for the widget
     */
    protected List<ClickHandler> clickHandlers;

    /**
     * value change handlers registered for the widget
     */
    protected List<ValueChangeHandler<Boolean>> valueChangeHandlers;
    
    /**
     * indicates if HTML is allowed as value of the widget
     */
    protected boolean allowHTML;
    
    /**
     * indicates if the widget instance is a child of a multiple element
     */
    protected boolean isElementOfMultipleWidget;
    
    /**
     * Constructor
     * 
     * @param radioButtonGroupId Id of the radio button group
     * @param availableValues available values of the group
     * @param initialValue initial value
     * @param itemsStyle the css classes of each radio button
     * @param allowHTML allow HTML in the radio buttons labels
     *            
     */
    public RadioButtonGroup(final String radioButtonGroupId, final List<ValuePair> values, final String initialValue, final String itemsStyle, final boolean allowHTML) {
        this(radioButtonGroupId, values, initialValue, itemsStyle, allowHTML, false);
    }
    
    /**
     * Constructor
     * 
     * @param radioButtonGroupId Id of the radio button group
     * @param availableValues available values of the group
     * @param initialValue initial value
     * @param itemsStyle the css classes of each radio button
     * @param allowHTML allow HTML in the radio buttons labels
     * @param isElementOfMultipleWidget indicates if the widget instance is a child of a multiple element
     *            
     */
    public RadioButtonGroup (final String radioButtonGroupId, final List<ValuePair> values, final String initialValue, final String itemsStyle, final boolean allowHTML, final boolean isElementOfMultipleWidget) {
        this.itemsStyle = itemsStyle;
        this.allowHTML = allowHTML;
        this.isElementOfMultipleWidget = isElementOfMultipleWidget;
        flowPanel = new FlowPanel();
        radioButtonGroupName = getRadioButtonGroupName(radioButtonGroupId);
         for (final ValuePair value : values) {
            final RadioButton radioButton = new RadioButton(radioButtonGroupName, value.getLabel(), allowHTML);
            radioButton.addClickHandler(this);
            radioButton.addValueChangeHandler(this);
            radioButton.setFormValue(value.getValue());
            if (initialValue != null && initialValue.equals(value.getValue())){
                radioButton.setValue(true);
            }
            radioButton.setStyleName("_radio");
            if (itemsStyle != null && itemsStyle.length() > 0) {
                radioButton.addStyleName(itemsStyle);
            }
            radioButtons.add(radioButton);
            flowPanel.add(radioButton);
        }
        
        initWidget(flowPanel);
    }
    
    /**
     * Useful in case the groups of radio buttons is displayed in several widget group instances
     * @param radioButtonGroupId the radio button definition Id
     * @return a name that can be used for the radio button group instance
     */
    protected String getRadioButtonGroupName(final String radioButtonGroupId) {
        
        String radioButtonGroupName =  null;
        if (isElementOfMultipleWidget) {
            if (radioButtonGroupIndex == 0) {
                radioButtonGroupName = radioButtonGroupId;
            } else {
                radioButtonGroupName = radioButtonGroupId + Integer.toString(radioButtonGroupIndex);
            }
            radioButtonGroupIndex++;
        } else {
            radioButtonGroupName = radioButtonGroupId;
        }
        return radioButtonGroupName;
    }
    
    /**
     * @return the String value of the slected radio button of the group (null if no radio button is selected)
     */
    public String getValue(){
        
        String value = null;
        
        final Iterator<Widget> iterator = flowPanel.iterator();
        while (iterator.hasNext()) {
            final RadioButton radioButton = (RadioButton) iterator.next();
            if (radioButton.getValue()) {
                value = radioButton.getFormValue();
                break;
            }
        }
        
        return value;
    }
    
    /**
     * Set the value of the widget
     * @param value
     */
    public void setValue(final String value, boolean fireEvents) {
        if ((getValue() != null && getValue().equals(value)) || (value != null && value.equals(getValue()))) {
            fireEvents = false;
        }
        for (final RadioButton radioButton : radioButtons) {
            if (value != null && value.equals(radioButton.getFormValue())) {
                radioButton.setValue(true);
            } else {
                radioButton.setValue(false);
            }
            if (fireEvents) {
            	ValueChangeEvent.fire(radioButton, true);
            }
        }
    }
    
    /**
     * Set the wigdet available values
     * @param availableValues
     */
    public void setAvailableValues(final List<ValuePair> values, final boolean fireEvents) {
        radioButtons.clear();
        flowPanel.clear();
        for (final ValuePair value : values) {
            final RadioButton radioButton = new RadioButton(radioButtonGroupName, value.getLabel(), allowHTML);
            radioButton.addClickHandler(this);
            radioButton.addValueChangeHandler(this);
            radioButton.setFormValue(value.getValue());
            radioButton.setStyleName("_radio");
            if (itemsStyle != null && itemsStyle.length() > 0) {
                radioButton.addStyleName(itemsStyle);
            }
            radioButton.addClickHandler(this);
            radioButton.addValueChangeHandler(this);
            radioButton.setFormValue(value.getValue());
            radioButtons.add(radioButton);
            flowPanel.add(radioButton);
        }
        if (fireEvents) {
            ValueChangeEvent.fire(this, true);
        }
    }
    
    /**
     * Enable or disable the radiobuttons group
     * @param isEnabled
     */
    public void setEnabled(final boolean isEnabled) {
        for (final RadioButton radioButton : radioButtons) {
            radioButton.setEnabled(isEnabled);
        }
    }

    /**
     * {@inheritDoc}
     */
    public HandlerRegistration addClickHandler(final ClickHandler clickHandler) {
        if (clickHandlers == null) {
            clickHandlers = new ArrayList<ClickHandler>();
        }
        clickHandlers.add(clickHandler);
        return new EventHandlerRegistration(clickHandler);
    }
    
    /**
     * {@inheritDoc}
     */
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Boolean> valueChangeHandler) {
        if (valueChangeHandlers == null) {
            valueChangeHandlers = new ArrayList<ValueChangeHandler<Boolean>>();
        }
        valueChangeHandlers.add(valueChangeHandler);
        return new EventHandlerRegistration(valueChangeHandler);
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(final ClickEvent clickEvent) {
        for (final ClickHandler clickHandler : clickHandlers) {
            clickHandler.onClick(clickEvent);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void onValueChange(final ValueChangeEvent<Boolean> valueChangeEvent) {
        for (final ValueChangeHandler<Boolean> valueChangeHandler : valueChangeHandlers) {
            valueChangeHandler.onValueChange(valueChangeEvent);
        }
    }
    
    /**
     * Custom Handler registration
     */
    protected class EventHandlerRegistration implements HandlerRegistration {

        protected EventHandler eventHandler;
        
        public EventHandlerRegistration(final EventHandler eventHandler) {
            this.eventHandler = eventHandler;
        }
        
        public void removeHandler() {
            if (eventHandler instanceof ClickHandler) {
                clickHandlers.remove(eventHandler);
            } else if (eventHandler instanceof ValueChangeHandler<?>) {
                valueChangeHandlers.remove(eventHandler);
            }
        }
    }}
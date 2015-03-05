package uk.co.platosys.pws.fieldsets;

import java.util.Date;





import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.inputfields.PDateBox;
import uk.co.platosys.pws.values.GWTAddress;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;

public class DateField extends AbstractFormField<Date> {
PDateBox dateBox;
	public DateField(String[] labelText,  int position,	Form parent, boolean required) throws IllegalArgumentException {
		super(labelText,  position, parent, required);
		dateBox=new PDateBox();
		setWidget(dateBox);
		start();
	}

	@Override
	public boolean validate() {
		
		return true;
	}
	/*@Override
	protected void start(){
		widget.setEnabled(enabled);
		widget.addValueChangeHandler(new ValueChangeHandler<Date>(){
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				Window.alert("DF vc event received");
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
	}*/
	@Override
	public void fireEvent(GwtEvent<?> event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Date> handler) {
		return dateBox.addValueChangeHandler(handler);
	}

}

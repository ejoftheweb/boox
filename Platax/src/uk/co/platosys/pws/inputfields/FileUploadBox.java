package uk.co.platosys.pws.inputfields;

import uk.co.platosys.pws.widgets.ValueFileUpload;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Focusable;

public class FileUploadBox extends AbstractValueField<String> {
 ValueFileUpload fileUpload=new ValueFileUpload();
 
 public FileUploadBox (){
	 
	 add(fileUpload);
 }
 
 	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return ((HasKeyDownHandlers) fileUpload).addKeyDownHandler(handler);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return fileUpload.addValueChangeHandler(handler);
	}

	@Override
	public String getValue() {
		 return fileUpload.getFilename();
	}

	@Override
	public void setFocus(boolean focused) {
		((Focusable) fileUpload).setFocus(focused);
		
	}

	@Override
	public void setEnabled(boolean enabled) {
		fileUpload.setEnabled(enabled);
		
	}

	@Override
	public boolean isEnabled() {
		return fileUpload.isEnabled();
	}
	
}

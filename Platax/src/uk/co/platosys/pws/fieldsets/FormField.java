package uk.co.platosys.pws.fieldsets;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;

public interface FormField<T> extends HasValue<T>{
	 public int getPosition();
	 public IsWidget getLabel();
	 public IsWidget getWidget();
	 public IsWidget getInfoLabel();
	 public void setEnabled(boolean enabled);
	 public void setFocus(boolean focus);
}

package uk.co.platosys.pws.inputfields;

import com.google.gwt.user.client.ui.IsWidget;

public interface HasFieldValue<T> extends IsWidget{
 T getValue();
}

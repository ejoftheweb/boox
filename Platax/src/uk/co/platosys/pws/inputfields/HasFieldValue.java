package uk.co.platosys.pws.inputfields;

import uk.co.platosys.pws.values.IsFieldValue;

import com.google.gwt.user.client.ui.IsWidget;

public interface HasFieldValue<T> extends IsWidget{
 IsFieldValue<T> getValue();
}

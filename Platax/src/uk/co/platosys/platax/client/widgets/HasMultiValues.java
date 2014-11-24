package uk.co.platosys.platax.client.widgets;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.IsWidget;

public interface HasMultiValues extends IsWidget {
 public ArrayList<String> getValues();
}

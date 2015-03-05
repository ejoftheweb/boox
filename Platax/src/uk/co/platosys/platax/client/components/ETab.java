package uk.co.platosys.platax.client.components;

import java.util.Iterator;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.Styles;
import uk.co.platosys.platax.client.widgets.labels.TabEnterpriseLabel;
import uk.co.platosys.platax.client.widgets.labels.TabPageTitleLabel;
import uk.co.platosys.platax.client.widgets.labels.TabPageSubtitleLabel;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * An ETab is a component tab in a PlataxTabPanel.
 * 
 * It inherits from UTab and adds a label to the status panel.  
 * 
 * An ETab is always associated with a single enterprise.
 * 
 * 
 * @author edward
 *
 */

public abstract class ETab extends UTab {
 protected Platax platax;
 private int index=-2; //the index of this PTab in its parent. 
 private GWTEnterprise enterprise;
 

 public ETab(GWTEnterprise enterprise){
	 super();
	// Window.alert("Etab constructor");
	 this.enterprise=enterprise;
	 platax = Platax.getCurrentInstance();
	 platax.setCurrentEnterprise(enterprise);
	 setup(platax);
 }
 public ETab(){
	 super();
	 //Window.alert("Etab constructor");
	 platax = Platax.getCurrentInstance();
	 this.enterprise=platax.getCurrentEnterprise();
	 setup(platax);
 }
 private void setup(Platax platax){
	 statusPanel.add(new TabEnterpriseLabel(enterprise.getName()));
	 
	
}

 


//if you want these methods to do anything, override them in the subclass.
public void clear(){}

public Iterator<Widget> iterator(){ return null;}
public  boolean remove(Widget widget){return false;}
public  void refresh(){}




public void select(){
	platax.setCurrentEnterprise(this.enterprise);
}
/**
 * @return the enterprise*/

public GWTEnterprise getEnterprise() {
	return enterprise;
}
/**
 * @param enterprise the enterprise to set*/

public void setEnterprise(GWTEnterprise enterprise) {
	this.enterprise = enterprise;
}

}
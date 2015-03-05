package uk.co.platosys.platax.client.components;

import java.util.Iterator;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.Styles;
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
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A PTab is a component tab in a PlataxTabPanel.
 * 
 * It is the root tab in the in the class hierarchy; all the tabs in the panel inherit from this class.
 * 
 * 
 * It consists of two basic components: 
 * the tabItem which is a FlowPanel consisting of a label and an icon that goes in the tab top; and
 * 
 * the  tabRootPanel panel which is another FlowPanel and which goes in the page part of the tab. 
 * 
 * 
 
 * 
 * A PTab is not necessarily associated with a single enterprise - e.g. the user tab references all the user's enterprises.
 * 
 * 
 * 
 * @author edward
 *
 */

public abstract class PTab implements IsWidget, HasWidgets {
 protected Platax platax;
 private String title="blank pTab";	
 private String header="";
 private FlowPanel tabItem;
 private InlineLabel tabItemTitle= new InlineLabel();
 private InlineLabel counter=new InlineLabel();
 private Anchor closeTag=new Anchor("X");
 private boolean closeEnabled=true;
 private boolean closeConfirm=true;
 private String closeConfirmMessage=StringText.REALLY_CLOSE_TAB;
 protected LayoutPanel tabRootPanel;
 private int index=-2; //the index of this PTab in its parent. 
 
 
 public PTab(){
	 try{
		 platax = Platax.getCurrentInstance();
	 }catch(Exception x){
		 Window.alert(x.getMessage());
	 }
	 setup(platax);
 }
 @Deprecated
 public  PTab(Platax platax){
	 setup(platax);
 }
 private void setup(Platax platax){
	 tabItem = new FlowPanel();
	 tabItem.setStyleName(Styles.PTAB_TABITEM_PANEL);
	 tabRootPanel=new LayoutPanel();
	 tabRootPanel.setStyleName(Styles.PTAB_ROOT_PANEL);
	 closeTag.setStyleName(Styles.PTAB_CLOSE_TAG);
	 tabItemTitle.setText(title);
     tabItem.add(tabItemTitle);
     tabItem.add(counter);
     tabItem.add(closeTag);
     
     closeTag.addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			if(closeEnabled){
			if(!(closeConfirm)){
				close();
			}else{
				if(Window.confirm(closeConfirmMessage)){
				 close();
				}
			}
		}}
    	 
     });
     
}

 public void add(Widget widget){
	 tabRootPanel.add(widget);
 }
 public void close(){
	 platax.removeTab(this);
 }
 public String getHeader(){
	 return header;
	 
 }
 public Widget getTabItem(){
	 return tabItem;
 }
 public Widget getTabRootPanel(){
	 return tabRootPanel;
 }
/**
 * enables or disables closing the tab from the close icon.
 * 
 * As a matter of good design, in general closing should be enabled only when there is provision for reopening 
 * a tab of the same type. 
 * 
 * The default behaviour however is for tabs to be close-enabled.
 * 
 * @param closeEnabled
 */
 
 public void setCloseEnabled(boolean closeEnabled){
	 this.closeEnabled=closeEnabled;
	 closeTag.setEnabled(closeEnabled);
	 if(closeEnabled){
		  closeTag.setStyleName(Styles.PTAB_CLOSE_TAG);
	 }else{
		 closeTag.setStyleName(Styles.PTAB_CLOSE_TAG_DISABLED);
	 }
 }
 /**
  * sets the closeConfirm parameter. If true, a Window.confirm dialog
  * intercepts the tab closing.
  * 
  * @param closeConfirm
  */
 public void setCloseConfirm(boolean closeConfirm){
	 this.closeConfirm=closeConfirm;
 }
 /**
  * Sets the message displayed in the close-confirm dialog.
  * 
  * @param closeConfirmMessage
  */
 public void setCloseConfirmMessage(String closeConfirmMessage){
	 this.closeConfirmMessage=closeConfirmMessage;
 }
 /**
  * Sets the parent. The PTab must already have been added to the parent tab panel.
  * This method is called by PlataxTabPanel addTab methods.
  * @param parent
  * @return the index it was added at, or -1 if the parent returns an error, or -3 if an error is thrown.
  */
public int setParent(PTabPanel parent){
	try{
		this.index=parent.getWidgetIndex(this);
		return index;
	}catch(Exception x){
		return -3;
	}
}

@Deprecated
public int setTabHeaderText(String header) {
	this.header = header;
	tabItemTitle.setText(header);
	return index;
}
public int setTabHead(SafeHtml header) {
	this.header = header.asString();
	tabItemTitle.setText(header.asString());
	return index;
}
/**
 * Gets the index position of this pTab in its parent.
 * @return an int being the index position, or indication of an error if the index<0. 
 */
public int getIndex(){
	return index;
}

public Widget asWidget(){
	return tabRootPanel;
}

//if you want these methods to do anything, override them in the subclass.
public abstract void clear();
public  void add(IsWidget widget){
	tabRootPanel.add(widget);
}
public abstract Iterator<Widget> iterator();
public abstract boolean remove(Widget widget);
public abstract void refresh();



public InlineLabel getCounter(){
	return counter;
}
public void setStyleName( String styleName){
	tabRootPanel.setStyleName(styleName);
}
public void setHeadStyleName(String styleName){
	tabItem.setStyleName(styleName);
}

/**This method should be overridden in subclasses.
 * 
 */
public void select(){
	platax.setCurrentEnterprise(null);
}
}

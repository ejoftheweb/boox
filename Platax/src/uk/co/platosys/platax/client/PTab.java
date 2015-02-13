package uk.co.platosys.platax.client;

import java.util.Iterator;

import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.Styles;
import uk.co.platosys.platax.client.widgets.PlataxTabPanel;
import uk.co.platosys.platax.client.widgets.html.AnchorHTML;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A PTab is a component tab in a PlataxTabPanel.
 * 
 * It consists of two basic components: 
 * the tabItem which is a FlowPanel consisting of a label and an icon that goes in the tab top; and
 * the panel which is a DockLayoutPanel which goes in the page part of the tab. 
 * the panel is split into two parts: the page, which contains the business part of the app; and the shareCol, 
 * which contains the sharing information about the page - who gets to see it. 
 * 
 * These are set in subclasses.
 * 
 * @author edward
 *
 */

public abstract class PTab implements IsWidget, HasWidgets {
 private String title="blank pTab";	
 private String header="";
 private String content="";
 private PlataxTabPanel parent;
 private FlowPanel tabItem;
 private InlineLabel tabItemTitle= new InlineLabel();
 private InlineLabel counter=new InlineLabel();
 private Anchor closeTag=new Anchor("X");
 private boolean closeEnabled=true;
 private boolean closeConfirm=true;
 private String closeConfirmMessage=StringText.REALLY_CLOSE_TAB;
 private FlowPanel page;
 private FlowPanel shareCol;
 private DockLayoutPanel panel;
 private int index=-2; //the index of this PTab in its parent. 
 private GWTEnterprise enterprise; 
 private Platax pPlatax;
 
 public PTab(){
	 final Platax platax = Platax.getCurrentInstance();
	 setup(platax);
 }
 public  PTab(final Platax platax){
	 setup(platax);
 }
 private void setup(final Platax platax){
	 this.pPlatax=platax;
	 tabItem = new FlowPanel();
	 page=new FlowPanel();
	 panel=new DockLayoutPanel(Unit.PCT);
	 shareCol=new FlowPanel();
	
	 panel.addWest(page, 90);
	 panel.addEast(shareCol, 9.9);
	 page.setStyleName(Styles.PTAB_CONTENT_STYLE);
	 shareCol.setStyleName(Styles.PTAB_SHARE_STYLE);
	 closeTag.setStyleName(Styles.PTAB_CLOSE_TAG);
	 shareCol.add(new Label(LabelText.SHARE));
     tabItemTitle.setText(title);
     tabItem.add(tabItemTitle);
     tabItem.add(counter);
     tabItem.add(closeTag);
     
     closeTag.addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			if(closeEnabled){
			if(!(closeConfirm)){
				platax.removeTab(PTab.this);
			}else{
				if(Window.confirm(closeConfirmMessage)){
					platax.removeTab(PTab.this);
				}
			}
		}}
    	 
     });
}

 public void add(Widget widget){
	 page.add(widget);
 }

 public String getHeader(){
	 return header;
	 
 }
 public Widget getTabItem(){
	 return tabItem;
 }
 public Widget getPage(){
	 return panel;
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
public int setParent(PlataxTabPanel parent){
	this.parent=parent;
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

public Widget asWidget(){return page;}

//if you want these methods to do anything, override them in the subclass.
public abstract void clear();
public abstract void add(IsWidget widget);
public abstract Iterator<Widget> iterator();
public abstract boolean remove(Widget widget);
public abstract void refresh();



public InlineLabel getCounter(){
	return counter;
}
public void setStyleName( String styleName){
	page.setStyleName(styleName);
}
public void setHeadStyleName(String styleName){
	tabItem.setStyleName(styleName);
}
public GWTEnterprise getEnterprise() {
	return enterprise;
}
public void setEnterprise(GWTEnterprise enterprise) {
	this.enterprise = enterprise;
}
public void select(){
	pPlatax.setCurrentEnterprise(this.enterprise);
}
}

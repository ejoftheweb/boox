package uk.co.platosys.platax.client.widgets;

import java.util.Iterator;

import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.Styles;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
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
 * the page which is a VerticalPanel which goes in the page part of the tab. 
 * 
 * These are set in subclasses.
 * 
 * @author edward
 *
 */

public abstract class PTab implements IsWidget, HasWidgets, HasCloseHandlers<PTab>{
 private String header="";
 private String content="";
 private PlataxTabPanel parent;
 private FlowPanel tabItem;
 private InlineLabel tabItemTitle;
 private Image image;
 private VerticalPanel page;
 private VerticalPanel shareCol;
 private FlowPanel panel;
 private int index=-2; //the index of this PTab in its parent. 
 public PTab(){
	 tabItem = new FlowPanel();
	 page=new VerticalPanel();
	 panel=new FlowPanel();
	 page.setWidth("85%");
	 shareCol=new VerticalPanel();
	 shareCol.setWidth("14%");
	 panel.add(page);
	 panel.add(shareCol);
	 page.setStyleName(Styles.PTAB_CONTENT_STYLE);
	 shareCol.setStyleName(Styles.PTAB_SHARE_STYLE);
	 shareCol.add(new Label(LabelText.SHARE));
     tabItemTitle = new InlineLabel();
     tabItemTitle.setText("Blank pTab");
     tabItem.add(tabItemTitle);
     //image = new Image("../icons/close_tab.png");
     //tabItem.add(image);
     addHandlers();
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

public int setTabHeaderText(String header) {
	this.header = header;
	tabItemTitle.setText(header);
	return index;
}
/**
 * Gets the index position of this pTab in its parent.
 * @return an int being the index position, or indication of an error if the index<0. 
 */
public int getIndex(){
	return index;
}
@Override
public  HandlerRegistration addCloseHandler(CloseHandler<PTab> handler) {
    return addHandler(handler, CloseEvent.getType());
}

private HandlerRegistration addHandler(CloseHandler<PTab> handler,
		Type<CloseHandler<?>> type) {
	// TODO Auto-generated method stub
	return null;
}

private void addHandlers() {
    /*image.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            CloseEvent.fire(PTab.this, PTab.this);
        }
    }); */
 
}
public void fireEvent(GwtEvent<?> event){
	//todo add the code here
	 
}
public Widget asWidget(){return page;}
//if you want these methods to do anything, override them in the subclass.
public void clear(){}
public void add(IsWidget widget){}

public Iterator<Widget> iterator(){return null;}
public boolean remove(Widget widget){return false;}
}

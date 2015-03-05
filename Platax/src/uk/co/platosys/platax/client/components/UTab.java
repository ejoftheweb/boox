package uk.co.platosys.platax.client.components;

import java.util.Iterator;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.Styles;
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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A UTab is a component tab in a PlataxTabPanel.
 * 
 * It inherits from PTab and adds a DockLayoutPanel which divides the business part of the tab into two - 
 * 
 * the page, which contains the business part of the app; 
 * and the shareCol, 
 * which contains the sharing information about the page - who gets to see it. 
 * 
 * The page itself is divided into two: the titlePanel, and the contentPage.
 * The title panel is also divided into two: the titlesPanel, and the statusPanel (which should not be confused with the application StatusBox). 
 * 
 * 
 * These are set in subclasses.
 * 
 * A PTab is not necessarily associated with a single enterprise - e.g. the user tab references all the user's enterprises.
 * 
 * 
 * 
 * @author edward
 *
 */

public abstract class UTab extends PTab {
 protected Platax platax;
 //private FlowPanel tabRootPanel; //specified in PTab
 	private DockLayoutPanel uTabRootPanel=new DockLayoutPanel(Unit.PCT);//the container for everything indexed by the tab, except the tab itself.
 	private FlowPanel shareCol=new FlowPanel();//contains the share information for the page. 
 	private FlowPanel page=new FlowPanel();//the container for the business contents of the page
	    private HorizontalPanel titlePanel=new HorizontalPanel();
	    	private FlowPanel titlesPanel=new FlowPanel();
	    		private TabPageTitleLabel title=new TabPageTitleLabel();
	    		private TabPageSubtitleLabel subTitle=new TabPageSubtitleLabel();
	    	protected FlowPanel statusPanel=new FlowPanel();
	    protected FlowPanel contentPage= new FlowPanel();
 
 
 private int index=-2; //the index of this PTab in its parent. 
 
 
 public UTab(){
	 platax = Platax.getCurrentInstance();
	 setup();
 }
 @Deprecated
 public  UTab(Platax platax){
	 setup();
 }
 private void setup(){
	 tabRootPanel.add(uTabRootPanel);
	 
	 uTabRootPanel.setStyleName(Styles.UTAB_ROOT_PANEL);
	 uTabRootPanel.addWest(page, 90);
	 	page.setStyleName(Styles.PTAB_PAGE_STYLE);
	 uTabRootPanel.addEast(shareCol, 9.9);
	 	shareCol.setStyleName(Styles.PTAB_SHARE_STYLE);
	 	shareCol.add(new Label(LabelText.SHARE));
     // now divide up the page part //
	 page.add(titlePanel);
	 page.add(contentPage);
	 	contentPage.setStyleName(Styles.UTAB_CONTENT_PAGE);
	 titlePanel.setWidth("100%");	
	 titlePanel.add(titlesPanel);
	 	titlePanel.setStyleName(Styles.UTAB_TITLE_PANEL);
	 	titlesPanel.setStyleName(Styles.UTAB_TITLES_PANEL);
	 	titlesPanel.add(title);
	 	titlesPanel.add(subTitle);
	 titlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);	
	 titlePanel.add(statusPanel);
	 //Window.alert("done setting up utab");
	
}

 public void add(Widget widget){
	 contentPage.add(widget);
 }

 

//if you want these methods to do anything, override them in the subclass.
public abstract void clear();
@Override
public  void add(IsWidget widget){
	contentPage.add(widget);
}
public abstract Iterator<Widget> iterator();
public abstract boolean remove(Widget widget);
public abstract void refresh();

public void setStyleName( String styleName){
	page.setStyleName(styleName);
}


/**This method should be overridden in subclasses.
 * 
 */
public void select(){
	platax.setCurrentEnterprise(null);
}

public void setTitle(String title){
	this.title.setText(title);
}
public void setSubTitle(String subTitle){
	this.subTitle.setText(subTitle);
}
}
package uk.co.platosys.platax.client.widgets;

import java.util.Iterator;
import java.util.List;





import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * This class extends GWT TabLayoutPanel by allowing the addition of multiple
 * PTabs (which should really be the only tabs we add to this, although it will
 * in fact support the addition of any implementation of isWidget)
 * @author edward
 *
 */


public class PlataxTabPanel extends TabLayoutPanel {
	public  PlataxTabPanel(double barHeight, Style.Unit barUnit){
	super(barHeight, barUnit);
	
	}
	public void addTabs(List<PTab> content){
	Iterator<PTab> tit = content.iterator();
		while(tit.hasNext()){
			PTab pTab = tit.next();
			this.addTab(pTab);
		}
	}
	public void removeTabs(List<PTab> content){
		Iterator<PTab> tit = content.iterator();
			while(tit.hasNext()){
				PTab pTab = tit.next();
				this.remove(pTab);
			}
		}
	
	public void remove(PTab pTab) {
		remove(getWidgetIndex(pTab));
	}
	/**
	 * adds a tab at the end.
	 * @param pTab
	 */
	public void addTab(PTab pTab){
		Widget page = pTab.getPage();
		Widget tabItem = pTab.getTabItem();
	
		add(page,tabItem);
		pTab.setParent(this);
	}
	 /** adds a tab at the end.
	  * selects it if true
	 * @param pTab
	 */
	public void addTab(PTab pTab, boolean select){
		Widget page = pTab.getPage();
		Widget tabItem = pTab.getTabItem();
	
		add(page,tabItem);
		if(select){
			selectTab(page);
		}
		pTab.setParent(this);
	}
	/**
	 * adds a tab at a specified index.
	 * @param pTab
	 * @param index
	 */
	public void addTab(PTab pTab, int index){
		Widget page = pTab.getPage();
		Widget tabItem = pTab.getTabItem();
		insert(page,tabItem, index);
		pTab.setParent(this);
	}
}

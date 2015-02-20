package uk.co.platosys.platax.client.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;











import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * This class extends GWT TabLayoutPanel by allowing the addition of multiple
 * PTabs (which should really be the only tabs we add to this, although it will
 * in fact support the addition of any implementation of isWidget)
 * @author edward
 *
 */


public class PTabPanel extends TabLayoutPanel {
	List<PTab> tabs = new ArrayList<PTab>();
	public  PTabPanel(double barHeight, Style.Unit barUnit){
	super(barHeight, barUnit);
	addSelectionHandler(new SelectionHandler<Integer>(){
     	@Override
		public void onSelection(SelectionEvent<Integer> event) {
     		//Window.alert("Selection event triggered on ptp");
			int selectedIndex = event.getSelectedItem();
			PTab selectedTab=tabs.get(selectedIndex);
			selectedTab.select();
		}
		
	});
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
		tabs.remove(pTab);
		remove(pTab.getPage());
	}
	/**
	 * adds a tab at the end.
	 * @param pTab
	 */
	public void addTab(PTab pTab){
		int index=0;
		try{
			tabs.add(pTab);
			index=tabs.indexOf(pTab);
			//Window.alert("PTP-at tab added at "+index);
			
		}catch(Exception x){
			Window.alert("PTP-atp"+x.getMessage());
		
		}addTab(pTab, index);
	}
	 /** adds a tab at the end.
	  * selects it if true
	 * @param pTab
	 */
	public void addTab(PTab pTab, boolean select){
		Widget page = pTab.getPage();
		addTab(pTab);
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
		try{
			insert(page,tabItem, index);
		}catch(Exception x){
			//Window.alert("PTP-ati "+x.getMessage());
		}
		pTab.setParent(this);
	}
}

/*
 * MenuItem.java
 *
 * Created on 23 September 2007, 09:56
 *
 * This code is copyright PLATOSYS,
 * to the extent that copyright subsists.
 *
 *
 */

package uk.co.platosys.webplates;

import org.apache.ecs.xhtml.a;
import org.apache.ecs.xhtml.div;

/**
 *
 * @author edward
 */
public class MenuItem extends div {
    
    /** Creates a new instance of MenuItem */
    public MenuItem(String label, String link) {
        super();
        this.setClass("menuItem");
        //add the anchor element inside the div.
        this.addElement(new a(link, label));
   }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.xservlets;

import java.io.File;
import java.util.Vector;

import uk.co.platosys.xservlets.Notice;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.PlatosysProperties;
/**
 * A NoticeBoard is an object that stores notices for use by servlets in a webapplication.
 * 
 * An xservlets webapp would normally have noticeboards around for the application, session and request contexts.
 * 
 *
 * NoticeBoard implements List and its iterator will return notices in reverse order of addition (most recent first) if they have been added using the addNotice method.
 * 
 * 
 * @author edward
 */
public class NoticeBoard extends Vector<Notice>{
    public static final String APPLICATION_TYPE="applicationNotice";
    public static final String SESSION_TYPE="sessionNotice";
    public static final Logger logger = XservletProperties.XSERVLET_LOGGER;
    private String type;
    public NoticeBoard(String type){
        super();
        this.type=type;
    }
    public NoticeBoard(File file){
        super();
        this.type=type;
    }
    
    /**
     * A notice is always added at index position 0;
     * @param notice
     */
    public void addNotice(String content){
        this.insertElementAt(new Notice(type,content), 0);
    }

    /**
     * returns a name for the noticeboard.
     * @return
     */
    public String getType(){
        return type;
    }         
    
    /**
     * Returns the most recent notice added.
     */
    public Notice getLastNotice(){
        return this.get(0);
    }
    
}

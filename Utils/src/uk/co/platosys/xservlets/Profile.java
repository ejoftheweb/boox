/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.xservlets;
import uk.co.platosys.util.*;
/**
 * The profile object attaches to the session and can be extended to hold many 
 * attributes required by the application
 *
 * @author edward
 */
public abstract class Profile {
Logger logger= XservletProperties.XSERVLET_LOGGER;
private String subjectID; //the unique string that identifies the owner of this profile.

public abstract String getProfileID();
}
 
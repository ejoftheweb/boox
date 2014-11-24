/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.xservlets;

/**
 *
 * @author edward
 */
public class XservletException extends Exception {

    /**
     * Creates a new instance of <code>XservletException</code> without detail message.
     */
    public XservletException() {
    }


    /**
     * Constructs an instance of <code>XservletException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public XservletException(String msg) {
        super(msg);
    }
    
    public XservletException(String msg, Exception e) {
        super(msg, e);
    }
}

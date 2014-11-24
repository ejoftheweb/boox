/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.xservlets;

/**
 *
 * @author edward
 */
public class AbsentParameterException extends Exception {

    /**
     * Creates a new instance of <code>AbsentParameterException</code> without detail message.
     */
    public AbsentParameterException() {
    }


    /**
     * Constructs an instance of <code>AbsentParameterException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public AbsentParameterException(String msg) {
        super(msg);
    }
}

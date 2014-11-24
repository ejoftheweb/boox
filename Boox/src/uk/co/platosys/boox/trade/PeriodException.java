/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.trade;

/**
 *
 * @author edward
 */
public class PeriodException extends Exception {

    /**
     * Creates a new instance of <code>PeriodException</code> without detail message.
     */
    public PeriodException() {
    }


    /**
     * Constructs an instance of <code>PeriodException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public PeriodException(String msg) {
        super(msg);
    }
}

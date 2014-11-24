/*
 * This code is copyright Platosys, to the extent that copyright subsists.
 * Use, modification and distribution are subject to the consent of Platosys.
Please see the documentation that accompanied the distribution of this code for the licence terms
applicable to you. If you cannot find the licence documentation, or you would like to discuss alternative
terms and conditions, please contact Platosys at licensing@platosys.co.uk.
 */

package uk.co.platosys.boox.core.exceptions;

/**
 *
 * @author edward
 */
public class PermissionsException extends Exception {

    /**
     * Creates a new instance of <code>PermissionsException</code> without detail message.
     */
    public PermissionsException() {
    }


    /**
     * Constructs an instance of <code>PermissionsException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public PermissionsException(String msg) {
        super(msg);
    }
}

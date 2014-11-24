/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.core.exceptions;

/**
 *
 * @author edward
 */
public class CredentialsException extends Exception {

    /**
     * Creates a new instance of <code>CredentialsException</code> without detail message.
     */
    public CredentialsException() {
        super("Username/Password Mismatched");
    }


    /**
     * Constructs an instance of <code>CredentialsException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public CredentialsException(String msg) {
        super(msg);
    }
}

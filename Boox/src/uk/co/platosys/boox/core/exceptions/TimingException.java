
package uk.co.platosys.boox.core.exceptions;

/**
 *
 * @author edward
 */
public class TimingException extends BooxException {

    /**
     * Constructs an instance of <code>TimingException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public TimingException(String msg) {
        super(msg);
    }
      /**
     * Constructs an instance of <code>TimingException</code> with the specified detail message and the given
    * cause.
     * @param msg the detail message.
     */
    public TimingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

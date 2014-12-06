package uk.co.platosys.xmail;

public class XmailException extends Exception {

	/**
	 * the class for xceptions thrown by the xmail system.
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public  XmailException(){
		super();
	}
	public XmailException(String msg){
		super(msg);
	}
	public XmailException(Exception x) {
		super(x);
	}
}

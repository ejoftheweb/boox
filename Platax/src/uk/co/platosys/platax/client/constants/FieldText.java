package uk.co.platosys.platax.client.constants;

import uk.co.platosys.platax.shared.FieldVerifier;

/**
 * in this package for now but to refactor to Constants.
 * @author edward
 *
 */

public class FieldText {
	private static final String ERROR="Sorry, that won't do, please try again";
	private static final String ANYALPHA=FieldVerifier.USERNAME_REGEX;
	public static final String[] EMAIL={"Email", "Email Address", FieldVerifier.EMAIL_REGEX, "Sorry that's an invalid email address, please try again"};
	public static final String[] GIVEN_NAME={"Given Name", "First/Given name", ANYALPHA, ERROR};
	public static final String[] FAMILY_NAME={"Family Name", "Last/Family name", ANYALPHA, ERROR};
	public static final String[] PHONE={"Phone", "Phone Number", FieldVerifier.PHONE_REGEX, "Sorry, that's not a valid phone number"};
	public static final String[] POSTCODE={"Postcode", "Post Code", FieldVerifier.POSTCODE_REGEX, "Sorry that's not a valid postcode"};
	public static final String[] NAT_INS={"NI Number", "National Insurance Number", FieldVerifier.NI_REGEX, "Sorry that's not a valid NI number"};
	public static final String[] DOB={"DOB", "Date of Birth", null,ERROR};
	public static final String[] ADDRESS={"Address", "Home Address", ANYALPHA, ERROR};
	public static final String[] NATIONALITY={"Nationality", "Nationality", ANYALPHA, ERROR};
	
	
	public static final String[] NAME={"Name", "Handy short name", ANYALPHA, ERROR};
	public static final String[] LEGALNAME={"Legal Name", "Full legal name", ANYALPHA, ERROR};
    public static final String[] IS_TRADE_CUSTOMER={"Trade?", "Check if this is a trade customer", ANYALPHA, ERROR};
    
    public static final String[] PAY_RATE={"Starting Pay", "Starting rate of pay", null, ERROR};
    public static final String[] PAY_FREQ={"Paid per", "Payment period", null, ERROR};
}

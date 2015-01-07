package uk.co.platosys.platax.client.constants;

import uk.co.platosys.platax.shared.FieldVerifier;

/**
 * in this package for now but to refactor to Constants.
 * @author edward
 *
 */

public class FieldText {
	
	public static final String[] EMAIL={"Email", "Email Address", FieldVerifier.EMAIL_REGEX, "Sorry that's an invalid email address, please try again"};
	public static final String[] GIVEN_NAME={"Given Name", "First/Given name", null, "sorry that won't do, please try again"};
	public static final String[] FAMILY_NAME={"Family Name", "Last/Family name", null, "sorry that won't do, please try again"};
	public static final String[] PHONE={"Phone", "Phone Number", FieldVerifier.PHONE_REGEX, "Sorry, that's not a valid phone number"};
	public static final String[] POSTCODE={"Postcode", "Post Code", FieldVerifier.POSTCODE_REGEX, "Sorry that's not a valid postcode"};

}

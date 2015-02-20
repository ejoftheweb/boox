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
	
	public static final String[] PETTYCASH={"Cash Tin", "Select which cash tin", ANYALPHA, ERROR};
	public static final String[] PETTYCASH_BALANCE={"Cash in Tin", "Count the cash remaining", null, ERROR};
	
	public static final String[] NAME={"Name", "Handy short name", ANYALPHA, ERROR};
	public static final String[] LEGALNAME={"Legal Name", "Full legal name", "^[A-Z]([a-zA-Z0-9]|[- @\\.#&!])*$", ERROR};
	
	public static final String[] ENTERPRISE_TYPE={"Capital Structure", "Please select your organisation's capital type", ANYALPHA, ERROR};
	public static final String[] ENTERPRISE_ROLE={"Your Role in it", "Please select your role in it", ANYALPHA, ERROR};
	public static final String[] ENTERPRISE_STARTUP={"Startup?", "Please tick if it's a startup", ANYALPHA, ERROR};
	public static final String[] ENTERPRISE_STARTDATE={"Start date", "Enter the accounting reference date", ANYALPHA, ERROR};
	
	
	
    public static final String[] IS_TRADE_CUSTOMER={"Trade?", "Check if this is a trade customer", ANYALPHA, ERROR};
    
    public static final String[] PAY_RATE={"Starting Pay", "Starting rate of pay", null, ERROR};
    public static final String[] PAY_FREQ={"Paid per", "Payment period", null, ERROR};
    //payment form
    public static final String[] PAYMENT_FROM={"From", "Source of payment", null, ERROR};
    public static final String[] PAYMENT_TO={"To", "Destination of payment", null, ERROR};
    public static final String[] PAYMENT_AMOUNT={"Amount", "Amount paid", null, ERROR};
    //Bank accounts
    public static final String[] NEW_BANKAC={"Add Account", "Click to add a new bank account", null, ERROR};
    //new bank account
    public static final String[] BANKAC_NAME={"Name", "Convenient short name (e.g. Current, Deposit)", ANYALPHA, ERROR};
    public static final String[] BANKAC_HOLDER={"Holder", "Account holder\'s name", null, ERROR};
    public static final String[] BANKAC_NUMBER={"Account Number", "Bank account number ", "^(\\d){8}$", "Must be 8 numeric digits, no spaces"};
    public static final String[] BANCAC_SORTCODE={"Sort Code", "Bank Branch Sort Code", "^(\\d){2}-(\\d){2}-(\\d){2}$", "3 sets of 2 digits separated by hyphens"};
    public static final String[] BANCAC_IBAN={"IBAN", "International Bank Account Number", "[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}", ERROR};
    public static final String[] BANKAC_ADDRESS={"Address", "Postal Address", null, ERROR};
    		
}

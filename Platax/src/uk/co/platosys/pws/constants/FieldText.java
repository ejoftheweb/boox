package uk.co.platosys.pws.constants;

import uk.co.platosys.platax.shared.FieldVerifier;
/**
**building: including number in building 23 International House
**street: including number in street 52 Rodean Road
**district Blickarly
**town LITTLEHAMPTON - maps to us City
**county snants
**postcode LH34 0BD
**country UK*/
public class FieldText {
	private static final String ERROR="Sorry, that won't do, please try again";
	public static final String[] BUILDING={"Building", "e.g. 23 International House", null, ERROR};
	public static final String[] STREET={"Street", "e.g. 52 Rodean Road", null, ERROR};
	public static final String[] DISTRICT={"District", "District, locality or village name", null, ERROR};
	public static final String[] TOWN={"Town", "Post town", null, ERROR};
	public static final String[] POSTCODE={"Postcode", "Post Code", FieldVerifier.POSTCODE_REGEX, "Sorry that's not a valid postcode"};
	public static final String[] COUNTY={"County", "County or region", null, ERROR};
	public static final String[] COUNTRY={"Country", "Country", null,ERROR};
	public static final String[] SUBMIT={"Submit", "Click Submit when done or Cancel to start again", null, ERROR};
}

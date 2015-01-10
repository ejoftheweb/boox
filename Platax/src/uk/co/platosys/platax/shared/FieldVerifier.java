package uk.co.platosys.platax.shared;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is not translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {

	
	public static final String EMAIL_REGEX = "^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$";
	public static final String PASSWORD_REGEX="^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$";
	public static final String USERNAME_REGEX="^([a-zA-Z '-]+)$";
	public static final String POSTCODE_REGEX="^(GIR 0AA)|(((A[BL]|B[ABDHLNRSTX]?|C[ABFHMORTVW]|D[ADEGHLNTY]|E[HNX]?|F[KY]|G[LUY]?|H[ADGPRSUX]|I[GMPV]|JE|K[ATWY]|L[ADELNSU]?|M[EKL]?|N[EGNPRW]?|O[LX]|P[AEHLOR]|R[GHM]|S[AEGKLMNOPRSTY]?|T[ADFNQRSW]|UB|W[ADFNRSV]|YO|ZE)[1-9]?[0-9]|((E|N|NW|SE|SW|W)1|EC[1-4]|WC[12])[A-HJKMNPR-Y]|(SW|W)([2-9]|[1-9][0-9])|EC[1-9][0-9]) [0-9][ABD-HJLNP-UW-Z]{2})$";
	public static final String PRICE_REGEX="^(0\\.|[1-9]\\d*\\.)d{2}";
	public static final String NI_REGEX="^(?!BG|GB|NK|KN|TN|NT|ZZ)[ABCEGHJ-PRSTW-Z][ABCEGHJ-NPRSTW-Z]\\d{6}[A-D]$";
	public static final String PHONE_REGEX="^(\\+44\\s?7\\d{3}|\\(?07\\d{3}\\)?)\\s?\\d{3}\\s?\\d{3}$";
	  public static boolean isValid(String test, String regex) {
		if (test == null) {
			return false;
		}
		return test.matches(regex);
	}
	public static boolean confirms(String password, String confirm){
		if (password==null) {
			return false;
		}
		return password.equals(confirm);
	}
	public static boolean isValidEmail(String email){
		return isValid(email, EMAIL_REGEX);
	}
	public static boolean isValidPassword(String password){
		return isValid(password, PASSWORD_REGEX);
	}
	public static boolean isValidUsername(String username){
		return isValid(username, USERNAME_REGEX);
	}
	public static boolean isValidPrice(String price){
		return isValid(price, PRICE_REGEX);
	}
	public static boolean isValidName(String string){
		return true;
	}
	public static boolean isValidPhone(String phone){
		return isValid(phone,  PHONE_REGEX);
	}
	public static boolean isValidNI(String ni){
		return isValid(ni, NI_REGEX);
	}
}

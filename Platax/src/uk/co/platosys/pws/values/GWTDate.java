package uk.co.platosys.pws.values;

import java.util.Date;

/**
 * Wrapper class for java.util.Date to implement IsFieldValue
 * @author edward
 *
 */
public class GWTDate extends Date implements IsFieldValue<Date>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	public GWTDate(Date value) {
		super(value.getTime());
	}
    public GWTDate(){}
}

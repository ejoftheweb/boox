package uk.co.platosys.pws.values;


import java.io.Serializable;

/**
 * A wrapper for label-value pairs
 */
public class BasicValuePair implements Serializable, ValuePair{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String label;

    private String value;    
    
    /**
     * Constructor
     * @param label
     * @param value
     */
    public BasicValuePair(final String label, final String value) {
        this.label = label;
        this.value = value;
    }

    /**
     * Default constructor
     */
    public BasicValuePair() {}
     

    public String getLabel() { return label;}

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getValue() { return value;}
    public void setValue(final String value) {this.value = value;}
}

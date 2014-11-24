package uk.co.platosys.platax.client.widgets;


import java.io.Serializable;

/**
 * A wrapper for label-value pairs
 */
public class FieldValue implements Serializable{

    private String label;

    private String value;    
    
    /**
     * Constructor
     * @param label
     * @param value
     */
    public FieldValue(final String label, final String value) {
        this.label = label;
        this.value = value;
    }

    /**
     * Default constructor
     */
    public FieldValue() {}
     

    public String getLabel() { return label;}

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getValue() { return value;}
    public void setValue(final String value) {this.value = value;}
}

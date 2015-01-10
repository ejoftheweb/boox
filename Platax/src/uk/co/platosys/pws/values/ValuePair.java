package uk.co.platosys.pws.values;

/**
 * A wrapper for label-value pairs
 */
public interface ValuePair {

    public String getLabel();

    public void setLabel(final String label); 

    public String getValue(); 
}

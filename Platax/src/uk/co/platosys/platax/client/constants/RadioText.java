package uk.co.platosys.platax.client.constants;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.pws.values.BasicValuePair;
import uk.co.platosys.pws.values.ValuePair;

public class RadioText {
	public static final String[] PAY_PER_VALUES={"Hour", "Week","Year"};
	public static final String[] PAY_PER_LABELS=PAY_PER_VALUES;
    public static final String PAY_PER_DEFAULT="Hour";
    public static final List<ValuePair> PAY_PER_LIST=fillList(PAY_PER_LABELS, PAY_PER_VALUES);
    
    
    private static List<ValuePair>fillList(String[] labels, String[] values){
    	ArrayList<ValuePair> list = new ArrayList<ValuePair>();
    	for(int i=0; i<labels.length; i++){
    		ValuePair pair = new BasicValuePair(labels[i], values[i]);
    		list.add(pair);
    	}
    	return list;
    }
}

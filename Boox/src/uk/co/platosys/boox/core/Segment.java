package uk.co.platosys.boox.core;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
/**
 *  Segments and Modules explained
 *  
 *  These are a feature of Boox and allow for the creation of specific accounting structures (a chart of accounts, etc)
 *  to suit various use-cases.
 *  
 *  For conventional accounting applications, segments refer to parts of the system. Thus, there could be segments for:
 *  - capital, -operations, -overhead, -sector, and -tax. 
 *  
 *  Within each segment are modules. Depending on the segment, zero, one or more of these modules can be selected to make up the 
 *  accounting system. 
 *  
 *  Thus, the "capital" segment would include a choice of modules depending on the capital structure of the enterprise, whether it is a partnership,
 *  a sole trader, a company limited by shares, by guarantee, or a trust. These would be mutually-exclusive, so only one could be selected.
 *  
 *  Then, for the "sector" segment there would be industry-based modules, e.g. for catering, or for construction. These are non-exclusive since an
 *  enterprise could operate in several sectors.
 *  
 *  
 * 
 * 
 * @author edward
 *
 */
public class Segment {
	private String name;
	private String description;
	private List<Module> modules;
	private boolean multipleselection;
	public static final String ELEMENT_NAME="segment";
	public static final String NAME_ATTNAME="name";
	public static final String DESC_ATTNAME="description";
	public static final String SEL_ATTNAME="selection";
	public static final String SEL_ATTVALUE="single";

	public Segment(Element segmentElement) {
		this.modules=new ArrayList<Module>();
		this.name=segmentElement.getAttributeValue(NAME_ATTNAME);
		this.description=segmentElement.getAttributeValue(DESC_ATTNAME);
		if (segmentElement.getAttributeValue(SEL_ATTNAME).equals(SEL_ATTVALUE)){
			this.multipleselection=false;
		}else{
			this.multipleselection=true;
		}
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isMultipleselection() {
		return multipleselection;
	}
    public void addModule(Module module){
    	modules.add(module);
    }
    public List<Module> getModules(){
    	return modules;
    
    }
}

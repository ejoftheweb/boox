package uk.co.platosys.boox.core;

import java.io.File;

import org.jdom2.Element;
import org.jdom2.Namespace;

import uk.co.platosys.boox.core.exceptions.BooxException;
/**Segments and Modules explained
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
*  A module consists of an XML file with two elements under its root element: Ledgers and Tasks. Ledgers sets up the hierarchical accounting structure
*  of Ledgers and Accounts, together with a default set of permissions.
*  Tasks sets up the associated tasks that need to be done to keep the books up-to-date. 
*  */

public class Module {
 private String name;
 private String description;
 private String filename;
 private String segment;
 private boolean multipleSelection=false;
 private File file;//the file containing the module definition
 public static final String ELEMENT_NAME="module";
 public static final Namespace ns = Namespace.getNamespace("http://www.platosys.co.uk/boox");
 public static final String NAME_ATTNAME="name";
 public static final String DESC_ATTNAME="description";
 public static final String SEGMENT_ATTNAME="segment";
 public static final String FILENAME_ATTNAME="file";
 public static final String SELECTION_ATTNAME="selection";
 public static final String MULTISELECT_ATTVALUE="multiple";
 public static final File MODULE_DIR=new File("/etc/platosys/booxtemplates/");//TODO read from a property.
 public static final File MODULE_FILE=new File (MODULE_DIR, "boox.xml");
 
	public Module(String name, String description, String segment, String filename) {
		this.name=name;
		this.description=description;
		this.filename=filename;
		this.segment=segment;
		this.file = new File(new File(MODULE_DIR, segment), filename);
		// TODO Auto-generated constructor stub
	}
	public Module (Element moduleElement) throws BooxException{
		if (!(moduleElement.getName().equals(ELEMENT_NAME))){throw new BooxException("Element is not a Module element");}
		this.name=moduleElement.getAttributeValue(NAME_ATTNAME);
		this.description=moduleElement.getAttributeValue(DESC_ATTNAME);
		this.segment=moduleElement.getAttributeValue(SEGMENT_ATTNAME);
		this.filename=moduleElement.getAttributeValue(FILENAME_ATTNAME);
		this.file = new File(new File(MODULE_DIR, segment), filename);
		this.multipleSelection=(moduleElement.getAttributeValue(SELECTION_ATTNAME).equals(MULTISELECT_ATTVALUE));
	}
	public File getFile() {
		return file;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getFilename() {
		return filename;
	}
	public String getSegment() {
		return segment;
	}
	/**
	 * @return the multipleSelection
	 */
	public boolean isMultipleSelection() {
		return multipleSelection;
	}
	/**
	 * @param multipleSelection the multipleSelection to set
	 */
	public void setMultipleSelection(boolean multipleSelection) {
		this.multipleSelection = multipleSelection;
	}

}

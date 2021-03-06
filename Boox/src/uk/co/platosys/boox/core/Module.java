package uk.co.platosys.boox.core;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.util.DocMan;
import uk.co.platosys.util.Logger;
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
*  A module consists of an XML file with three elements under its root element: Ledgers, Tasks and Menu. Ledgers sets up the hierarchical accounting structure
*  of Ledgers and Accounts, together with a default set of permissions.
*  Tasks sets up the associated tasks that need to be done to keep the books up-to-date. 
*  Menu provides module-specific menu entries for use by the front-end application.
*  
*  Note that Boox doesn't need to know anything about the menu. It's just that menu entries are usually module-specific.
*  so the module file is the sensible place to put this information. 
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
 public static  Logger logger = Boox.logger;
 
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
	/**
	    * This returns a Map of the available Modules, indexed by the module name, reading from the boox.xml config
	    * file which lists them all. 
	    * @return
	    */
	   public static Map<String, Module> getModules(){
		   try{ 
			   logger.log("Module-getting the modules");
			   Map<String, Module> modules = new HashMap<String, Module>();
			   Document moduleDoc = DocMan.build(Module.MODULE_FILE);
			   Element rtel = moduleDoc.getRootElement();
			   List<Element> modelements = rtel.getChildren(Module.ELEMENT_NAME, ns);
			   Iterator<Element> it = modelements.iterator();
			   while (it.hasNext()){
				   Module module = new Module(it.next());
				   modules.put(module.getName(), module);
			   }
			   return modules;
		   }catch(Exception e){
			   logger.log("Module- problem parsing the modules file", e);
			   return null;
		   }
	   }

}

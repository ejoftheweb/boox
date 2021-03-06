package uk.co.platosys.platax.server.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import uk.co.platosys.boox.compliance.Role;
import uk.co.platosys.boox.core.Boox;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Module;
import uk.co.platosys.boox.core.Segment;
import uk.co.platosys.boox.core.Directory;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.boox.trade.Customer;
import uk.co.platosys.platax.client.services.EnterpriseService;
import uk.co.platosys.platax.server.core.Booxlet;
import uk.co.platosys.platax.server.core.PXConstants;
import uk.co.platosys.platax.server.core.PlataxServer;
import uk.co.platosys.platax.server.core.PlataxUser;
import uk.co.platosys.platax.server.core.SystemMessages;
import uk.co.platosys.platax.shared.Constants;
import uk.co.platosys.platax.shared.Message;
import uk.co.platosys.platax.shared.boox.GWTBankDetails;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTDirectoryEntry;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTModule;
import uk.co.platosys.platax.shared.boox.GWTRole;
import uk.co.platosys.platax.shared.boox.GWTSegment;
import uk.co.platosys.platax.shared.exceptions.PlataxException;
import uk.co.platosys.util.ISODate;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.RandomString;


public class EnterpriseServiceImpl extends Booxlet implements EnterpriseService {
/**
	 * 
	 */
	private static final long serialVersionUID = 5953214743330925557L;
static Logger logger = Logger.getLogger("platax");
	

	public static ArrayList<GWTCustomer> getGWTCustomers(GWTEnterprise gwtEnterprise, Clerk clerk){
		ArrayList<GWTCustomer> gwtCustomers = new ArrayList<GWTCustomer>();
		try{
			Enterprise enterprise = Enterprise.getEnterprise(gwtEnterprise.getEnterpriseID());
			List<Customer> customers = Customer.getCustomers(enterprise, clerk, Customer.SELECTION_ALL);
			Iterator<Customer>cit=customers.iterator();
			while(cit.hasNext()){
				Customer customer=cit.next();
				logger.log("PSvr-getGWTcustomers: adding customer:" +customer.getName()+", sysname "+customer.getSysname());
				gwtCustomers.add(new GWTCustomer(customer.getName(), customer.getSysname()));
			}
		}catch (Exception x){
			//the permissions exception will be caught here for now
			
			PlataxServer.logger.log("plataxSVR exception getting customer list", x);
		}
		return gwtCustomers;
	}




	/**
	 * Takes a Boox Enterprise object as its argument and creates a GWTserializable 
	 * analogue (a GWTEnterprise).
	 * @param enterprise
	 * @return
	 */
	public static GWTEnterprise convert(Enterprise enterprise, Clerk clerk){
		try{
		GWTEnterprise gwtEnterprise = new GWTEnterprise(enterprise.getSysname(), enterprise.getName(), enterprise.getLegalName()) ;
		//set the privileges held by this platax user??
		gwtEnterprise.setCustomers(getGWTCustomers(gwtEnterprise, clerk));
		gwtEnterprise.setSegments(getSegments(true));
		gwtEnterprise=readRatios(enterprise, clerk, gwtEnterprise);
		logger.log("ESI has converted Enterprise "+enterprise.getName()+" to GWT");
		
		return gwtEnterprise;
		}catch (Exception x){
			logger.log("ESI convert error", x);
			return null;
		}
	}
	
	/**
	 * method to get an enterprise to read its current ratios.
	 * @param enterprise
	 * @param user
	 * @param gwtEnterprise
	 * @return
	 */
	public static GWTEnterprise readRatios (Enterprise enterprise, Clerk clerk, GWTEnterprise gwtEnterprise){
		try {
			
			//Read Net Current Assets:
			Money netAssets = enterprise.getNetAssetValue(clerk);
			gwtEnterprise.addRatio("Net Assets", PlataxServer.convert(netAssets), "Book value of the enterprise's capital");
			Money profit = enterprise.getCurrentProfit(clerk);
			gwtEnterprise.addRatio("Operating Profit", PlataxServer.convert(profit), "Non-adjusted operating profit in the current period");
			Money currentAssets = enterprise.getNetCurrentAssets(clerk);
			gwtEnterprise.addRatio("Net Current Assets", PlataxServer.convert(currentAssets), "Approximately how solvent we are");
			return gwtEnterprise;
		} catch (Exception e) {
			PlataxServer.logger.log("problem reading ratios for enterprise "+enterprise.getName(), e);
			e.printStackTrace();
		}
		return null;
	    
	}

	public TreeMap<String, String> getOrgTypes() {
		TreeMap<String, String> orgTypes = new TreeMap<String, String>();
		orgTypes.put("please select", "");
		Map<String, Module> modules = Boox.getModules();
		Iterator<Entry<String, Module>> mit = modules.entrySet().iterator();
		while (mit.hasNext()){
			Entry<String, Module> entry = mit.next();
			Module module = entry.getValue();
			if(module.getSegment().equals("capital")){
				orgTypes.put(module.getDescription(),module.getName());
			}
		}
		return orgTypes;
	}

	public TreeMap<String, String> getSectors() {
		TreeMap<String, String> orgTypes = new TreeMap<String, String>();
		orgTypes.put("please select", "");
		Map<String, Module> modules = Boox.getModules();
		Iterator<Entry<String, Module>> mit = modules.entrySet().iterator();
		while (mit.hasNext()){
			Entry<String, Module> entry = mit.next();
			Module module = entry.getValue();
			if(module.getSegment().equals("sector")){
				orgTypes.put(module.getDescription(),module.getName());
			}
		}
		
		return orgTypes;
	}

	public ArrayList<GWTModule> getModules(){
	 try{
		 logger.log("ESIgm trying to get the modules");
	 ArrayList<GWTModule> gwmodules = new ArrayList<GWTModule>();
		Map<String, Module> modules = Boox.getModules();
		Iterator<Entry<String, Module>> mit = modules.entrySet().iterator();
		while (mit.hasNext()){
			Entry<String, Module> entry = mit.next();
			Module module = entry.getValue();
			GWTModule gModule = new GWTModule();
			gModule.setName(module.getName());
			gModule.setDescription(module.getDescription());
			gModule.setSegment(module.getSegment());
			gModule.setMultiSelect(module.isMultipleSelection());
			gwmodules.add(gModule);
		}
		logger.log("ESIgm returning AL of "+gwmodules.size()+" modules");
		return gwmodules;
	 }catch(Exception x){
		 logger.log("ESIgm threw exception ", x);
		 return null;
	 }
   }


	@Override
	public Message getMessage(String key) {
		// TODO trap message not found
		return SystemMessages.getMessage(key);
	}
	
	@Override
	public GWTEnterprise addEnterpriseModules(String sysname, ArrayList<String> modulenames) {
		PlataxUser puser=null;
		Enterprise enterprise=null;
		Clerk clerk=null;
		File accountsTemplateFile=null;
		try {
			puser = (PlataxUser) getSession().getAttribute(PXConstants.USER);
			if (puser==null){
				throw new PlataxException("RESI no user in session to create enterprise");
			}
			enterprise=puser.getEnterprise(sysname);
			clerk=puser.getClerk(enterprise);
			Map<String, Module> modules=Module.getModules();
			for(String modulename:modulenames){
			   Module module = modules.get(modulename);
			   accountsTemplateFile=module.getFile();				
			   Boox.createLedgersAndAccounts(enterprise, clerk, accountsTemplateFile);
			}
		}catch(Exception x){
			logger.log("ESI-AEM problem adding modules");
		}
		return convert(enterprise, clerk);
	}
	 
	public GWTEnterprise setDirectoryEntry(String sysname,
			GWTDirectoryEntry entry) {
		// TODO Auto-generated method stub
		return null;
	}
	 
	public GWTEnterprise addBankDetails(String sysname, GWTBankDetails details) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public  ArrayList<GWTSegment> getSegments() {
		try{
			 logger.log("ESIgm trying to get the segments");
		 ArrayList<GWTSegment> gSegments = new ArrayList<GWTSegment>();
			Map<String, Segment> segments = Boox.getSegments();
			Iterator<Entry<String, Segment>> mit = segments.entrySet().iterator();
			while (mit.hasNext()){
				Entry<String, Segment> entry = mit.next();
				Segment segment = entry.getValue();
				GWTSegment gSegment = new GWTSegment();
				gSegment.setName(segment.getName());
				gSegment.setDescription(segment.getDescription());
				gSegment.setInstructions(segment.getInstructions());
				for(Module module:segment.getModules()){
					gSegment.addModule(getGWTModule(module));
				}
				
				gSegment.setMultiSelect(segment.isMultipleselection());
				gSegments.add(gSegment);
			}
			logger.log("ESIgm returning AL of "+gSegments.size()+" segments");
			return gSegments;
		 }catch(Exception x){
			 logger.log("ESIgm threw exception ", x);
			 return null;
		 }
	}
	
	public static ArrayList<GWTSegment> getSegments(boolean test) {
		try{
			 logger.log("ESIgm trying to get the segments");
		 ArrayList<GWTSegment> gSegments = new ArrayList<GWTSegment>();
			Map<String, Segment> segments = Boox.getSegments();
			Iterator<Entry<String, Segment>> mit = segments.entrySet().iterator();
			while (mit.hasNext()){
				Entry<String, Segment> entry = mit.next();
				Segment segment = entry.getValue();
				GWTSegment gSegment = new GWTSegment();
				gSegment.setName(segment.getName());
				gSegment.setDescription(segment.getDescription());
				gSegment.setInstructions(segment.getInstructions());
				for(Module module:segment.getModules()){
					gSegment.addModule(getGWTModule(module));
				}
				
				gSegment.setMultiSelect(segment.isMultipleselection());
				gSegments.add(gSegment);
			}
			logger.log("ESIgm returning AL of "+gSegments.size()+" segments");
			return gSegments;
		 }catch(Exception x){
			 logger.log("ESIgm threw exception ", x);
			 return null;
		 }
	}
    public static GWTModule getGWTModule(Module module){
    	GWTModule gModule = new GWTModule();
    	gModule.setName(module.getName());
    	gModule.setDescription(module.getDescription());
    	gModule.setSegment(module.getSegment());
    	return gModule;
    }
    /**
     * This is the main method for registering an enterprise with Boox/Platax. The enterprise is created in Boox, 
     * the current pxUser is established as the supervising Clerk, passwords are created and safely stored, the basic
     * accounts framework is created followed by the capital accounts structure (according to the orgtype parameter)
     * 
     * @param name
     * @param legalName
     * @param orgtype
     * @param role
     * @param isStartup
     * @param startDate
     * 
     */
	@Override
	public GWTEnterprise registerEnterprise(String name, String legalName,
		String orgtype, String role, boolean isStartup, Date startDate) {
		PlataxUser puser=null;
		Enterprise enterprise=null;
		Clerk supervisor=null;
		String supervisorpassword=null;
		File accountsTemplateFolder=null;
		File accountsTemplateFile=null;
		try {
			puser = (PlataxUser) getSession().getAttribute(PXConstants.USER);
			if (puser==null){
				throw new PlataxException("RESI no user in session to create enterprise");
			}
			//TODO: Check for Duplicates!
			enterprise = Enterprise.createEnterprise(name, legalName, null, new ISODate(startDate.getTime()));
		}catch(Exception e){
			logger.log("RESI register enterprise failed at Stage I", e);
			return null;
		}try{
			//TODO manage the role
			supervisorpassword = RandomString.getRandomKey();
			supervisor = Boox.createSupervisor(enterprise, puser.getXuserID(), supervisorpassword);
		}catch(Exception e){
			logger.log("RESI register enterprise failed at Stage II", e);
			return null;
		}try{	
		    //STAGE III: create the basic accounts structure
			accountsTemplateFolder = new File(Constants.SYSTEM_CONFIG_DIR, PXConstants.ACCTS_TEMPLATE_FOLDER);
			accountsTemplateFile = new File(accountsTemplateFolder, PXConstants.ACCTS_TEMPLATE_FILE );
			logger.log("RESI now to create LandAcs for enterprise:"+enterprise.getName());
			Boox.createLedgersAndAccounts(enterprise, supervisor, accountsTemplateFile);
		}catch(Exception e){
			logger.log("RESI register enterprise failed at Stage III", e);
			return null;
		}try{		
			//STAGE IV: create the capital accounts structure
				File capitalAccountsFolder = new File(accountsTemplateFolder, PXConstants.CAPITAL_TEMPLATES_FOLDER);
				File capitalAccountsFile=new File(capitalAccountsFolder, orgtype+".xml");
				Boox.createLedgersAndAccounts(enterprise, supervisor, capitalAccountsFile);
		}catch(Exception e){
			logger.log("RESI problem creating enterprise at stage IV", e);
			return null;
		}try{	
			//TODO manage the date
			puser.addEnterprise(enterprise, supervisor, supervisorpassword);
			logger.log("RESI enterprise "+enterprise.getName()+" created");
			return convert(enterprise, supervisor);
		}catch(Exception e){
			logger.log("RESI problem creating enterprise at stage V", e);
			return null;
		}
	}

	@Override
	public ArrayList<GWTRole> getRoles() {
		List<Role> roles = Role.getRoles();
		ArrayList<GWTRole> gRoles = new ArrayList<GWTRole>();
		for(Role role: roles){
			gRoles.add(new GWTRole(role.getName(), role.getLocalisedName()));
		}
		return gRoles;
	}

	@Override
	public Boolean isNameOK(String name) {
		try {
			Boolean nameOK = new Boolean (Directory.isNameOK(name, true));
			return nameOK;
		}catch(Exception x){
			logger.log("ESI-isNameOK error: ", x);
			return null;
		}
	}


	
	
   
}

package uk.co.platosys.platax.server.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Element;

import uk.co.platosys.boox.compliance.Role;
import uk.co.platosys.boox.core.Boox;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.Module;
import uk.co.platosys.boox.core.Segment;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.boox.trade.Customer;
import uk.co.platosys.platax.client.services.EnterpriseService;
import uk.co.platosys.platax.client.services.UserService;
import uk.co.platosys.platax.server.core.PXConstants;
import uk.co.platosys.platax.server.core.PlataxServer;
import uk.co.platosys.platax.server.core.PlataxUser;
import uk.co.platosys.platax.server.core.SystemMessages;
import uk.co.platosys.platax.shared.Constants;
import uk.co.platosys.platax.shared.FieldVerifier;
import uk.co.platosys.platax.shared.Message;
import uk.co.platosys.platax.shared.boox.GWTBankDetails;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTDirectoryEntry;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTModule;
import uk.co.platosys.platax.shared.boox.GWTRole;
import uk.co.platosys.platax.shared.boox.GWTSegment;
import uk.co.platosys.platax.shared.boox.GWTSelectable;
import uk.co.platosys.platax.shared.exceptions.PlataxException;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.RandomString;
import uk.co.platosys.xservlets.Xservlet;
import uk.co.platosys.xuser.XuserCredentialsException;
import uk.co.platosys.xuser.XuserException;
import uk.co.platosys.xuser.XuserExistsException;


public class EnterpriseServiceImpl extends Xservlet implements
		EnterpriseService {
/**
	 * 
	 */
	private static final long serialVersionUID = 5953214743330925557L;
static Logger logger = Logger.getLogger("platax");
	



 public GWTEnterprise registerEnterprise(String name, String legalName) {
	PlataxUser puser=null;
	Enterprise enterprise=null;
	Clerk supervisor=null;
	String supervisorpassword=null;
	
	try {
		puser = (PlataxUser) getSession().getAttribute(PXConstants.USER);
		if (puser==null){
			logger.log("RESI no user in session to create enterprise");
			throw new PlataxException("RESI no user in session to create enterprise");
		}
		logger.log("RESI puser is "+puser.getUsername());
	    logger.log("RESI puser has xuserid "+puser.getXuserID());
		//TODO revisit this process
		//
		logger.log("registering enterprise "+name);
		//STAGE I: create the enterprise!
		//TODO: Check for Duplicates!
		enterprise = Enterprise.createEnterprise(name, legalName, null);
		logger.log("RESI enterprise "+enterprise.getName()+" created");
		logger.log("RESI: enterprise "+enterprise.getName()+" has dBname:"+enterprise.getDatabaseName());
	}catch(Exception e){
		logger.log("RESI register enterprise failed at Stage I", e);
		return null;
	}try{
		logger.log("RESI now getting a supervisor password");
		supervisorpassword = RandomString.getRandomKey();
		logger.log("RESI supervisor password is "+supervisorpassword);
		logger.log("RESI now creating supervisor "+puser.getXuserID()+" with password "+supervisorpassword);
		supervisor = Boox.createSupervisor(enterprise, puser.getXuserID(), supervisorpassword);
		logger.log("RESI supervisor" + supervisor.getName()+ " created");
	}catch(Exception e){
		logger.log("RESI register enterprise failed at Stage II", e);
		return null;
		
   
	}try{	
	    //STAGE III: create the basic accounts structure
		Map<String, Module> modules = Boox.getModules();
		for(String modulename: modules.keySet()){
			Module module = modules.get(modulename);
			Boox.createLedgersAndAccounts(enterprise, supervisor, module.getFile());
		}
		
		puser.addEnterprise(enterprise, supervisor, supervisorpassword);
		return convert(enterprise, supervisor);
	}catch(Exception e){
		logger.log("RESI problem creating enterprise at stage V", e);
		return null;
	}
	
}

 @Override
	public GWTEnterprise registerEnterprise(String name, String legalName,
			ArrayList<String> modulenames) {
		PlataxUser puser=null;
		Enterprise enterprise=null;
		Clerk supervisor=null;
		String supervisorpassword=null;
		File accountsTemplateFolder=null;
		File accountsTemplateFile=null;
		
		try {
			puser = (PlataxUser) getSession().getAttribute(PXConstants.USER);
			if (puser==null){
				logger.log("RESI no user in session to create enterprise");
				throw new PlataxException("RESI no user in session to create enterprise");
			}
			logger.log("RESI puser is "+puser.getUsername());
		    logger.log("RESI puser has xuserid "+puser.getXuserID());
			//TODO revisit this process
			//
			logger.log("registering enterprise "+name);
			//STAGE I: create the enterprise!
			//TODO: Check for Duplicates!
			enterprise = Enterprise.createEnterprise(name, legalName, null);
			logger.log("RESI enterprise "+enterprise.getName()+" created");
			logger.log("RESI: enterprise "+enterprise.getName()+" has dBname:"+enterprise.getDatabaseName());
		}catch(Exception e){
			logger.log("RESI register enterprise failed at Stage I", e);
			return null;
		}try{
			logger.log("RESI now getting a supervisor password");
			supervisorpassword = RandomString.getRandomKey();
			logger.log("RESI supervisor password is "+supervisorpassword);
			logger.log("RESI now creating supervisor "+puser.getXuserID()+" with password "+supervisorpassword);
			supervisor = Boox.createSupervisor(enterprise, puser.getXuserID(), supervisorpassword);
			logger.log("RESI supervisor" + supervisor.getName()+ " created");
		}catch(Exception e){
			logger.log("RESI register enterprise failed at Stage II", e);
			return null;
			
	   
		}try{	
		    //STAGE III: create the basic accounts structure
			Map<String, Module> modules = Boox.getModules();
			for(String modulename: modulenames){
				Module module = modules.get(modulename);
				Boox.createLedgersAndAccounts(enterprise, supervisor, module.getFile());
			}
			
			puser.addEnterprise(enterprise, supervisor, supervisorpassword);
			return convert(enterprise, supervisor);
		}catch(Exception e){
			logger.log("RESI problem creating enterprise at stage V", e);
			return null;
		}
		
	}


	
	public GWTEnterprise registerEnterprise(String name, String legalName, String type, String sector) {
		PlataxUser puser=null;
		Enterprise enterprise=null;
		Clerk supervisor=null;
		String supervisorpassword=null;
		File accountsTemplateFolder=null;
		File accountsTemplateFile=null;
		
		try {
			puser = (PlataxUser) getSession().getAttribute(PXConstants.USER);
			if (puser==null){
				logger.log("RESI no user in session to create enterprise");
				throw new PlataxException("RESI no user in session to create enterprise");
			}
			logger.log("RESI puser is "+puser.getUsername());
		    logger.log("RESI puser has xuserid "+puser.getXuserID());
			//TODO revisit this process
			//
			logger.log("registering enterprise "+name);
			//STAGE I: create the enterprise!
			//TODO: Check for Duplicates!
			enterprise = Enterprise.createEnterprise(name, legalName, null);
			logger.log("RESI enterprise "+enterprise.getName()+" created");
			logger.log("RESI: enterprise "+enterprise.getName()+" has dBname:"+enterprise.getDatabaseName());
		}catch(Exception e){
			logger.log("RESI register enterprise failed at Stage I", e);
			return null;
		}try{
			logger.log("RESI now getting a supervisor password");
			supervisorpassword = RandomString.getRandomKey();
			logger.log("RESI supervisor password is "+supervisorpassword);
			logger.log("RESI now creating supervisor "+puser.getXuserID()+" with password "+supervisorpassword);
			supervisor = Boox.createSupervisor(enterprise, puser.getXuserID(), supervisorpassword);
			logger.log("RESI supervisor" + supervisor.getName()+ " created");
		}catch(Exception e){
			logger.log("RESI register enterprise failed at Stage II", e);
			return null;
			
	   //TODO: rejig this to reflect the modules approach		
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
				File capitalAccountsFile=new File(capitalAccountsFolder, type+".xml");
				Boox.createLedgersAndAccounts(enterprise, supervisor, capitalAccountsFile);
				puser.addEnterprise(enterprise, supervisor, supervisorpassword);
				
		}catch(Exception e){
			logger.log("RESI problem creating enterprise at stage IV", e);
			return null;
		}try{		
			//STAGE V: create the sector accounts structure
			File sectorAccountsFolder = new File(accountsTemplateFolder, PXConstants.SECTOR_TEMPLATES_FOLDER);
			File sectorAccountsFile=new File(sectorAccountsFolder, sector+".xml");
			Boox.createLedgersAndAccounts(enterprise, supervisor, sectorAccountsFile);
			puser.addEnterprise(enterprise, supervisor, supervisorpassword);
			return convert(enterprise, supervisor);
		}catch(Exception e){
			logger.log("RESI problem creating enterprise at stage V", e);
			return null;
		}
		
	}




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
		GWTEnterprise gwtEnterprise = new GWTEnterprise(enterprise.getEnterpriseID(), enterprise.getName(), enterprise.getLegalName()) ;
		logger.log("ESI has created new gwtEnterprise "+enterprise.getName());
		//set the privileges held by this platax user??
		//gwtEnterprise = readRatios(enterprise, user, gwtEnterprise);
		gwtEnterprise.setCustomers(getGWTCustomers(gwtEnterprise, clerk));
		//gwtEnterprise.setProducts(getProducts(gwtEnterprise, user));
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
	public static GWTEnterprise readRatios (Enterprise enterprise, PlataxUser user, GWTEnterprise gwtEnterprise){
		try {
			Clerk clerk = user.getClerk(enterprise);
			//Read Net Current Assets:
			Money currentAssets =Boox.openLedger(enterprise, "CurrentAssets", clerk).getBalance(enterprise, clerk);
			Money currentLiabilities=Boox.openLedger(enterprise, "CurrentLiabilities", clerk).getBalance(enterprise, clerk);
			Money netCurrentAssets = currentAssets.subtract(currentLiabilities);
			gwtEnterprise.addRatio("Net Current Assets", netCurrentAssets.toPrefixedString(), "Shows how solvent the enterprise is");
			//Read Owner's Capital
			Money equity = Boox.openLedger(enterprise, "Equity", clerk).getBalance(enterprise, clerk);
			gwtEnterprise.addRatio("Equity Capital", equity.toPrefixedString(), "Book value of the company");
			return gwtEnterprise;
		} catch (Exception e) {
			PlataxServer.logger.log("problem reading ratios for enterprise "+enterprise.getName(), e);
			e.printStackTrace();
		}
		return null;
	    
	}


	public static void createLedgersAndAccounts(Enterprise enterprise, Clerk clerk, Element element, Ledger ledger) throws BooxException, PermissionsException{
	    Boox.createLedgersAndAccounts(enterprise, clerk, element, ledger);
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
	public GWTEnterprise registerEnterprise(String name, String legalName,
			String templateFileName) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public GWTEnterprise addEnterpriseModules(String sysname,
			ArrayList<String> modulenames) {
		// TODO Auto-generated method stub
		return null;
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
	public ArrayList<GWTSegment> getSegments() {
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
    public GWTModule getGWTModule(Module module){
    	GWTModule gModule = new GWTModule();
    	gModule.setName(module.getName());
    	gModule.setDescription(module.getDescription());
    	gModule.setSegment(module.getSegment());
    	return gModule;
    }

	@Override
	public GWTEnterprise registerEnterprise(String name, String legalName,
			String orgtype, String role, boolean isStartup, Date startDate) {
		PlataxUser puser=null;
		Enterprise enterprise=null;
		Clerk supervisor=null;
		String supervisorpassword=null;
		File accountsTemplateFolder=null;
		File accountsTemplateFile=null;
		//TODO do something with the 
		try {
			puser = (PlataxUser) getSession().getAttribute(PXConstants.USER);
			if (puser==null){
				logger.log("RESI no user in session to create enterprise");
				throw new PlataxException("RESI no user in session to create enterprise");
			}
			logger.log("RESI puser is "+puser.getUsername());
		    logger.log("RESI puser has xuserid "+puser.getXuserID());
			//TODO revisit this process
			//
			logger.log("registering enterprise "+name);
			//STAGE I: create the enterprise!
			//TODO: Check for Duplicates!
			enterprise = Enterprise.createEnterprise(name, legalName, null);
			logger.log("RESI enterprise "+enterprise.getName()+" created");
			logger.log("RESI: enterprise "+enterprise.getName()+" has dBname:"+enterprise.getDatabaseName());
		}catch(Exception e){
			logger.log("RESI register enterprise failed at Stage I", e);
			return null;
		}try{
			logger.log("RESI now getting a supervisor password");
			supervisorpassword = RandomString.getRandomKey();
			logger.log("RESI supervisor password is "+supervisorpassword);
			logger.log("RESI now creating supervisor "+puser.getXuserID()+" with password "+supervisorpassword);
			supervisor = Boox.createSupervisor(enterprise, puser.getXuserID(), supervisorpassword);
			logger.log("RESI supervisor" + supervisor.getName()+ " created");
		}catch(Exception e){
			logger.log("RESI register enterprise failed at Stage II", e);
			return null;
			
	   //TODO: rejig this to reflect the modules approach		
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
				puser.addEnterprise(enterprise, supervisor, supervisorpassword);
				
		}catch(Exception e){
			logger.log("RESI problem creating enterprise at stage IV", e);
			return null;
		}try{		
			
			puser.addEnterprise(enterprise, supervisor, supervisorpassword);
			return convert(enterprise, supervisor);
		}catch(Exception e){
			logger.log("RESI problem creating enterprise at stage V", e);
			return null;
		}
		
	}

	@Override
	public ArrayList<GWTRole> getRoles() {
		// TODO Auto-generated method stub
		List<Role> roles = Role.getRoles();
		ArrayList<GWTRole> gRoles = new ArrayList<GWTRole>();
		for(Role role: roles){
			gRoles.add(new GWTRole(role.getName(), role.getLocalisedName()));
		}
		return gRoles;
	}


	
	
   
}

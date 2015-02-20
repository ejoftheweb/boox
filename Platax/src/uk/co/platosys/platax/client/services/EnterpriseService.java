package uk.co.platosys.platax.client.services;

import java.util.ArrayList;
import java.util.Date;
import uk.co.platosys.platax.shared.Message;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTModule;
import uk.co.platosys.platax.shared.boox.GWTRole;
import uk.co.platosys.platax.shared.boox.GWTSegment;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("enterpriseService")
public interface EnterpriseService extends RemoteService {

   public GWTEnterprise registerEnterprise(String name, String legalName, String orgtype, String role, boolean isStartup, Date startDate);//ArrayList<String> modulenames);
   
   
   public GWTEnterprise addEnterpriseModules(String sysname, ArrayList<String> modulenames );
  // public GWTEnterprise setDirectoryEntry(String sysname, GWTDirectoryEntry entry);
   //public GWTEnterprise addBankDetails(String sysname, GWTBankDetails details);
    public ArrayList<GWTModule> getModules();
   public ArrayList<GWTSegment> getSegments();
   public ArrayList<GWTRole> getRoles();
   public Message getMessage(String key);
   
   public Boolean isNameOK(String name);
  
}

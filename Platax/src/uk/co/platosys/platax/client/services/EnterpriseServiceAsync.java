package uk.co.platosys.platax.client.services;

import java.util.ArrayList;
import java.util.Date;

import uk.co.platosys.platax.shared.Message;
import uk.co.platosys.platax.shared.boox.GWTBankDetails;
import uk.co.platosys.platax.shared.boox.GWTDirectoryEntry;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTModule;
import uk.co.platosys.platax.shared.boox.GWTRole;
import uk.co.platosys.platax.shared.boox.GWTSegment;
import uk.co.platosys.platax.shared.boox.GWTSelectable;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EnterpriseServiceAsync {
	
	public void registerEnterprise(String name, String legalName, AsyncCallback<GWTEnterprise> callback);
	public void registerEnterprise(String name, String legalName, String type, String sector, AsyncCallback<GWTEnterprise> callback);
	public void registerEnterprise(String name, String legalName, ArrayList<String> modulenames, AsyncCallback<GWTEnterprise> callback);
	public void registerEnterprise(String name, String legalName, String orgType, String role, boolean isStartup, Date startDate, AsyncCallback<GWTEnterprise> callback);
	public void addEnterpriseModules(String sysname, ArrayList<String> modulenames , AsyncCallback<GWTEnterprise> callback );
	//public void setDirectoryEntry(String sysname, GWTDirectoryEntry enry, AsyncCallback<GWTEnterprise> callback);
	void getModules(AsyncCallback<ArrayList<GWTModule>> callback);
	void getRoles(AsyncCallback<ArrayList<GWTRole>> callback);
	//public void addBankDetails(String sysname, GWTBankDetails details, AsyncCallback<GWTEnterprise> callback);
	public void getSegments(AsyncCallback<ArrayList<GWTSegment>> callback);
	void getMessage(String key, AsyncCallback<Message> callback);
 
}

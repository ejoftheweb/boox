package uk.co.platosys.platax.shared;
/**
 * PXUser is a serializable container for user data. It attaches to
 * a serverside plataxUser object. 
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.platax.shared.boox.GWTEnterprise;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PXUser implements Serializable, IsSerializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String username;
  private String lastLogin;
  private String lastLogout;
  private String lastLoginfrom;
  private List<GWTEnterprise> enterprises=new ArrayList<GWTEnterprise>();
  private boolean authenticated=false;
  
  public static final int VISITOR_PRIVILEGE=0;
  public static final int INVESTOR_PRIVILEGE=2;
  public static final int AUDITOR_PRIVILEGE=4;
  public static final int CLERICAL_PRIVILEGE=8;
  public static final int DIRECTOR_PRIVILEGE=16;
  
  public PXUser(){}

public void setUsername(String username) {
	this.username = username;
}

public String getUsername() {
	return username;
}

public void setLastLogin(String lastLogin) {
	this.lastLogin = lastLogin;
}

public String getLastLogin() {
	return lastLogin;
}

public void setLastLogout(String lastLogout) {
	this.lastLogout = lastLogout;
}

public String getLastLogout() {
	return lastLogout;
}

public void setLastLoginfrom(String lastLoginfrom) {
	this.lastLoginfrom = lastLoginfrom;
}

public String getLastLoginfrom() {
	return lastLoginfrom;
}
public int getNoOfEnterprises(){
	return enterprises.size();
}
public void addEnterprise(GWTEnterprise enterprise){
	enterprises.add(enterprise);
}
public List<GWTEnterprise> getEnterprises(){
	return enterprises;
}

public boolean isAuthenticated() {
	return authenticated;
}

public void setAuthenticated(boolean authenticated) {
	this.authenticated = authenticated;
}
}

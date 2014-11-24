package uk.co.platosys.platax.client.services;



import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("userService")
public interface UserService extends RemoteService {
	
	public String registerUser(String email, String username, String password, String confirm, boolean accept, boolean investor);
	
}

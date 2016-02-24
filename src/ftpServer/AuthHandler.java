package ftpServer;

import java.util.HashMap;

public class AuthHandler {
	protected HashMap<String, String> clients ;
	protected HashMap<String, Boolean> clientsAuth ;

	public AuthHandler(){
		clients = new HashMap<String, String>();
		clientsAuth = new HashMap<String, Boolean>();
	}
	
	public void addIdentifiant(String id, String password) {
		clients.put(id, password);
		clientsAuth.put(id, false);
	}

	public boolean userExist(String id) {
		return clients.containsKey(id);
	}

	public boolean isAuthenticated(String id) {
		return clientsAuth.get(id);
	}

	public boolean connect(String id, String password) {
		if (clients.get(id).equals(password)) {
			clientsAuth.put(id, true);
			return true;
		} else {
			return false;
		}
	}

	public boolean disconnect(String id) {
		if (clients.containsKey(id)) {
			clientsAuth.put(id, false);
			return true;
		} else {
			return false;
		}
	}

}

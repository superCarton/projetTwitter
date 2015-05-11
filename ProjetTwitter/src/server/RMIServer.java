package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class RMIServer extends UnicastRemoteObject implements ITwitter {

	private Map<String, String> userPassword;
	
	protected RMIServer() throws RemoteException {
		
		super();
		userPassword = new HashMap<String, String>();
		
		// on ajoute des utilisateurs
		userPassword.put("user", "user");
	}

	@Override
	public boolean connect(String user, String password) throws RemoteException {
		
		if (userPassword.containsKey(user) && userPassword.get(user).equals(password)){
			System.out.println("Connexion acceptee : " + user + "/" + password);
			return true;
		}
		System.out.println("Connexion refusee : " + user + "/" + password);
		return false;
		
	}

	@Override
	public void twitter(String user, String message) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reTwitter(String user, String message) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	

}

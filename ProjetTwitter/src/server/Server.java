package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {

	public static void main(String[] args) {
		  
	    try {

	      LocateRegistry.createRegistry(1993);

	      String url = "rmi://localhost:1993/TwitterServer";
	      System.out.println("Enregistrement de l'objet avec l'url : " + url);
	      
	      RMIServer objet = new RMIServer(); 
	      Naming.rebind(url, objet);

	      System.out.println("Serveur lancé");
	      
	    } catch (RemoteException e) {
	      e.printStackTrace();
	    } catch (MalformedURLException e) {
			e.printStackTrace();
		}
}
	
}

package server;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.*;

public class Server {

	public static void main(String[] args) {
		  
	    try {

	      Registry registry = LocateRegistry.createRegistry(1993);

	      String url = "rmi://localhost:1993/TwitterServer";
	      System.out.println("Enregistrement de l'objet avec l'url : " + url);
	      
	      RMIServer objet = new RMIServer(); 
	      registry.bind("TwitterServer", objet);

	      System.out.println("Serveur lance");
	      
	    } catch (RemoteException e) {
	      e.printStackTrace();
		}catch(AlreadyBoundException ex){
    ex.printStackTrace();
    }
}
	
}

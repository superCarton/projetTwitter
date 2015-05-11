package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import server.ITwitter;

public class Client {

	
	public static void main(String[] args) {
		
	    ITwitter instanceServeur;
		BufferedReader buff;
		String userName = "";
		boolean connected = false;

		buff = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Demarrage du client");
		
	    try {
	    	
	    String url = "rmi://localhost:1993/TwitterServer";
	       Remote r = Naming.lookup(url);
	       
	       instanceServeur = ((ITwitter) r);
	       if (r instanceof ITwitter) {
	    	   
	    	   instanceServeur = ((ITwitter) r);
	    	   System.out.println("Connexion reussie au serveur : " + url);

	    	  
	    	   /* PARTIE CONNEXION */
	    	   while (!connected){
	    		   System.out.println("\n------------ Connexion a twitter ----------");
	    		   System.out.println("Entrez votre nom d'utilisateur :");
	    		   userName = buff.readLine();
	    		   System.out.println("Entrez votre mot de passe :");
	    		   String password = buff.readLine();
	    		   connected = instanceServeur.connect(userName, password);
	    		   if (!connected) System.out.println("Mauvais user/mdp");
	    	   }
	    	   
	    	   System.out.println("\n-------- Bienvenue sur Twitter Mr. " + userName + " -----\n");
	    	   
	    	   /* PARTIE ECHANGES */
	    	   int choice = 0;
	    	   while (connected){
	    		   
	    		   System.out.println("Commandes disponibles : \n"
	    		   		+ "1 : twitter\n"
	    		   		+ "2 : retwitter\n"
	    		   		+ "3 : deconnecter\n");
	    		   
	    		   choice = Integer.parseInt(buff.readLine());
	    		   
	    		   switch (choice){
		    		   case 1 : {
		    			   System.out.println("twitter");
		    			   instanceServeur.twitter(userName, "");
		    			   break;
		    		   	}
		    		   case 2 : {
		    			   System.out.println("ReTwitter");
		    			   instanceServeur.reTwitter(userName, "");
		    			   break;
		    		   }
		    		   case 3 : {
		    			   System.out.println("Deconnexion");
		    			   connected = false;
		    			   break;
		    		   }
		    		   default : System.err.println("Valeur interdite");
	    		   }
	    		   
	    	   }
	    	   
	    	   System.out.println("\n------------ A bientot sur Twitter ----------\n");
	    	   
	    	   
	       } else {
	    	   System.err.println("Connexion echouee au serveur : " + url);
	    	   System.exit(0);
	       }
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	       
	}
}

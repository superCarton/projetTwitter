package client;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.*;
import java.rmi.registry.*;
import server.ITwitter;

public class Client {


  public static void main(String[] args) {

    ITwitter instanceServeur;
    BufferedReader buff;
    String userName = "";
    UUID identifiant = null;
    List<Sub> subscripter = new ArrayList<Sub>();

    buff = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Demarrage du client");

    try {
      if(System.getSecurityManager() == null) {
        System.setSecurityManager(new RMISecurityManager());
      }
      Registry registry = LocateRegistry.getRegistry("localhost", 1993);
      String url = "rmi://localhost:1993/TwitterServer";
      Remote r = registry.lookup("TwitterServer");

      instanceServeur = ((ITwitter) r);
      if (r instanceof ITwitter) {

        instanceServeur = ((ITwitter) r);
        System.out.println("Connexion reussie au serveur : " + url);


        /* PARTIE CONNEXION */
        while (identifiant == null){
          System.out.println("\n------------ Connexion a twitter ----------");
          System.out.println("Entrez votre nom d'utilisateur :");
          userName = buff.readLine();
          System.out.println("Entrez votre mot de passe :");
          String password = buff.readLine();
          identifiant = instanceServeur.connect(userName, password);
          if (identifiant == null) System.out.println("Mauvais user/mdp");
        }

        System.out.println("\n-------- Bienvenue sur Twitter Mr. " + userName + " -----\n");

        /* PARTIE ECHANGES */
        int choice = 0;
        while (identifiant != null){

          System.out.println("Commandes disponibles : \n"
              + "1 : twitter\n"
              + "2 : retwitter\n"
              + "3 : deconnecter\n"
              + "4 : abonnement tag\n"
              + "5 : liste tag\n");

          try {
            choice = Integer.parseInt(buff.readLine());
          } catch (java.lang.NumberFormatException e){
            choice = -1;
          }

          switch (choice){
            case 1 : {
                       System.out.println("entrez le message a twitter: ");
                       String message = buff.readLine();
                       instanceServeur.twitter(identifiant, message);
                       break;
            }
            case 2 : {
                       System.out.println("ReTwitter");
                       instanceServeur.reTwitter(identifiant, "");
                       break;
            }
            case 3 : {
                       System.out.println("Deconnexion");
                       identifiant = null;
                       break;
            }
            case 4: {
                     System.out.println("entrez le nom du tag auquel vous voulez etre abonne");
                     String tagName = buff.readLine();
                     subscripter.add(new Sub(tagName));
                     break;
                     
            }
            case 5: {
                  System.out.println("Liste des tags disponibles:");
                  List<String> tags = instanceServeur.getTags();
                  for(int i = 0; i < tags.size(); i++){
                  System.out.println(tags.get(i));
                  }
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

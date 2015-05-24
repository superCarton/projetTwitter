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

        /* recuperation des abonnements */
          List<String> tags = instanceServeur.getMyTags(identifiant);
          for(int i = 0; i < tags.size(); i++){
              subscripter.add(new Sub(tags.get(i)));
          }


        System.out.println("\n-------- Bienvenue sur Twitter Mr. " + userName + " -----\n");

        /* PARTIE ECHANGES */
        int choice = 0;
        while (identifiant != null){

            System.out.println("\n--------------------------------------------");
          System.out.println("\nCommandes disponibles : \n"
              + "1 : twitter\n"
              + "2 : retwitter\n"
              + "3 : s'abonner\n"
                  + "4 : mes abonnements\n"
              + "5 : tous les tags\n"
                  + "6 : deconnecter");
            System.out.println("--------------------------------------------\n");

          try {
            choice = Integer.parseInt(buff.readLine());
          } catch (java.lang.NumberFormatException e){
            choice = -1;
          }

          switch (choice){
            case 1 : {
                       System.out.println("Entrez le message a twitter : ");
                       String message = buff.readLine();
                       instanceServeur.twitter(identifiant, message);
                       break;
            }
            case 2 : {
                       System.out.println("ReTwitter avec l'id du tweet");
                       instanceServeur.reTwitter(identifiant, "");
                       break;
            }
            case 3: {
                  System.out.println("Entrez le nom du tag auquel vous voulez vous abonner : ");
                  String tagName = buff.readLine();
                  subscripter.add(new Sub(tagName));
                  instanceServeur.subscribe(identifiant, tagName);
                  break;
              }
              case 4: {
                  System.out.println("Les tags auquels je suis abonné :");
                  List<String> tags2 = instanceServeur.getMyTags(identifiant);
                  for(int i = 0; i < tags2.size(); i++){
                      System.out.println(tags2.get(i));
                  }
                  break;
              }
            case 5: {
                  System.out.println("Tous les tags disponibles :");
                  List<String> tags3 = instanceServeur.getTags();
                  for(int i = 0; i < tags3.size(); i++){
                  System.out.println(tags3.get(i));
                  }
                  break;
            }
              case 6 : {
                  System.out.println("Deconnexion");
                  instanceServeur.deconnect(identifiant);
                  identifiant = null;
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

package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.*;
import java.util.regex.*;

public class RMIServer extends UnicastRemoteObject implements ITwitter {

  private static final long serialVersionUID=1L;
  private List<String> tags  = new ArrayList<String>();
  private Map<String, String> userPassword;
  private Pub publisher = new Pub();
  protected RMIServer() throws RemoteException {

    super();
    userPassword = new HashMap<String, String>();

    // on ajoute des utilisateurs
    userPassword.put("user", "user");
  }

  @Override
  public List<String> getTags() throws RemoteException{
    return tags;
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

  /**
   * Je considere que "Twitter" correspond a pour chaque tag contenu dans le message, 
   * le message est rajouté a la queue correspondant a chaque tag.
   * Le message est egalement rajouté a la queue de l'utilisation qui envoie ce message. 
   * Exemple: 
   * utilisation = "didier"
   * message = "Bonjour @polytechnicois comment allez vous apres ces @partiels?"
   * le "message" sera ajouté a la queue "didier", "polytechnicois" et "partiels"
   *
   * Si le tag existe deja, le message sera ajouté a la queue correspondante, sinon, la queue sera cree puis
   * le message ajouté a la queue.
   *
   */
  @Override
  public void twitter(String user, String message) throws RemoteException {
    /*
     * Parse les tag dans les message, et oui, dans notre "Twitter" nous n'utilison pas les "#" mais les "@"
     */
    Pattern p = Pattern.compile("@(\\w+)");
    Matcher m = p.matcher(message);
    while(m.find()){
      try{
        publisher.publier(m.group(1),message); 
      }catch(Exception e){
        System.out.println(e.getMessage());
      }
      System.out.println(m.group(1));
    }
    reTwitter(user, message);
  }

  /**
   * Je considere que "retwitter" correspond a mettre un message uniquement dans la queue de l'utilisateur
   * qui le publie.
   * Exemple: 
   * Utilisation =  "didier"
   * Retwitter du message "Bonjour @polytechnicois comment allez vous apres ces @partiels?"
   * le "message" sera ajouté a la queue "didier" uniquement.
   *
   */
  @Override
  public void reTwitter(String user, String message) throws RemoteException {
    try{
      publisher.publier(user,message);
    }catch(Exception e){
      System.out.println(e.getMessage());
    }

  }

}

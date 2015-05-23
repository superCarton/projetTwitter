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
  private Map<String, String> userPassword = new HashMap<String,String>();
  private Map<UUID,String> connectedUser = new HashMap<UUID,String>();
  private Pub publisher = new Pub();
  protected RMIServer() throws RemoteException {
    super();
    // on ajoute des utilisateurs
    userPassword.put("user", "user");
  }

  @Override
  public List<String> getTags() throws RemoteException{
    return tags;
  }

  @Override
  public UUID connect(String user, String password) throws RemoteException {

    if (userPassword.containsKey(user) && userPassword.get(user).equals(password)){
      System.out.println("Connexion acceptee : " + user + "/" + password);
       UUID identifiantUnique = UUID.randomUUID();
       connectedUser.put(identifiantUnique, user);
      return identifiantUnique;
    }
    System.out.println("Connexion refusee : " + user + "/" + password);
    return null;

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
  public boolean twitter(UUID identifiant, String message) throws RemoteException {
    if(!connectedUser.containsKey(identifiant)){
      System.out.println("Vous n'etes pas autorisé a tweeter");
      return false;
    }
    
    /*
     * Parse les tag dans les message, et oui, dans notre "Twitter" nous n'utilison pas les "#" mais les "@"
     */
    Pattern p = Pattern.compile("@(\\w+)");
    Matcher m = p.matcher(message);
    while(m.find()){
      try{
        tags.add(m.group(1));
        publisher.publier(m.group(1),message); 
      }catch(Exception e){
        System.out.println(e.getMessage());
      }
      System.out.println(m.group(1));
    }
    return reTwitter(identifiant, message);
    
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
  public boolean reTwitter(UUID identifiant, String message) throws RemoteException {
     if(!connectedUser.containsKey(identifiant)){
      System.out.println("Vous n'etes pas autorisé a tweeter");
      return false;
    }

    tags.add(connectedUser.get(identifiant));
    try{
      publisher.publier(connectedUser.get(identifiant),message);
    }catch(Exception e){
      System.out.println(e.getMessage());
    }

    return true;
  }


}

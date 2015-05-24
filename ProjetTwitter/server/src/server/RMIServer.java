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
    private Map<String, List<String>> tagsForUsers = new HashMap<String, List<String>>();
  private Pub publisher = new Pub();
    private int id_courant = 0;
    private Map<Integer, Tweet> listeTweets = new HashMap<Integer, Tweet>();
  
  protected RMIServer() throws RemoteException {
    super();
    // on ajoute des utilisateurs
    userPassword.put("user1", "user1");
	userPassword.put("user2", "user2");
	userPassword.put("user3", "user3");
	userPassword.put("user4", "user4");
	userPassword.put("user5", "user5");
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

    public boolean deconnect(UUID identifiant) throws RemoteException{
        if(!connectedUser.containsKey(identifiant)){
            System.out.println("Vous n'etes pas connecté");
            return false;
        }

        connectedUser.remove(identifiant);
        System.out.println("Deconnexion recue");
        return true;
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

      String author = connectedUser.get(identifiant);
    /*
     * Parse les tag dans les message, et oui, dans notre "Twitter" nous n'utilison pas les "#" mais les "@"
     */
    Pattern p = Pattern.compile("@(\\w+)");
    Matcher m = p.matcher(message);
    while(m.find()){
      try{
        String tag = m.group(1);
        if(tags.indexOf(tag) == -1) {
          tags.add(tag);
        }

        publisher.publier(author, tag, message, id_courant);
      }catch(Exception e){
        System.out.println(e.getMessage());
      }
    }
      listeTweets.put(id_courant, new Tweet(author, message));
      id_courant++;

    return reTwitter(identifiant, id_courant-1);
    
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
  public boolean reTwitter(UUID identifiant, int id_tweet) throws RemoteException {

     if(!connectedUser.containsKey(identifiant)){
      System.out.println("Vous n'etes pas autorisé a tweeter");
      return false;
    }

      String tag = connectedUser.get(identifiant);
        if(tags.indexOf(tag) == -1) {
          tags.add(tag);
        }

      if (!listeTweets.containsKey(id_tweet)){
          System.out.println("L'id de ce tweet est introuvable");
          return false;
      }

      Tweet t = listeTweets.get(id_tweet);

    try{
      publisher.publier(t.getAuthor(), tag, t.getMessage(), id_tweet);
    }catch(Exception e){
      System.out.println(e.getMessage());
    }

    return true;
  }


    /*
    * On enregistre les abonnements de tags pour chaque utilisateur
     */
    @Override
    public boolean subscribe(UUID identifiant, String nomHashtag) throws RemoteException {

        if(!connectedUser.containsKey(identifiant)){
            System.out.println("Vous n'etes pas autorisé a souscrire");
            return false;
        }

        List<String> tags;
        String pseudo = connectedUser.get(identifiant);

        if(!tagsForUsers.containsKey(pseudo)){

            tags = new ArrayList<String>();
        } else {
            tags = tagsForUsers.get(pseudo);
        }

        if (tags.contains(nomHashtag)){
            System.out.println("Vous etes deja abonnes a ce tag");
            return false;
        }

        tags.add(nomHashtag);
        tagsForUsers.put(pseudo, tags);

        return true;
    }

    /* Obtenir la liste des tags auquels on est abonnés
     */
    @Override
    public List<String> getMyTags(UUID identifiant) throws RemoteException{

        if(!connectedUser.containsKey(identifiant)) {
            System.out.println("Vous n'etes pas autorisé ");
            return new ArrayList<String>();
        }

        String pseudo = connectedUser.get(identifiant);

        if(!tagsForUsers.containsKey(pseudo)){
            return new ArrayList<String>();
        } else {
            return tagsForUsers.get(pseudo);
        }
    }

}

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

  @Override
  public void twitter(String user, String message) throws RemoteException {
    /*
     * Parse les tag dans les message
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
  }

  @Override
  public void reTwitter(String user, String message) throws RemoteException {
    // TODO Auto-generated method stub

  }



}

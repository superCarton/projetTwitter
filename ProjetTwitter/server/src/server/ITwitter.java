package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;
public interface ITwitter extends Remote {

	public UUID connect(String user, String password) throws RemoteException;
    public boolean deconnect(UUID identifiant) throws RemoteException;
	public boolean twitter(UUID identifiant, String message) throws RemoteException;
	public boolean reTwitter(UUID identifiant, String message) throws RemoteException;
  public List<String> getTags() throws RemoteException;
    public List<String> getMyTags(UUID identifiant) throws RemoteException;
  	public boolean subscribe(UUID identifiant, String nomHashtag) throws RemoteException;
}

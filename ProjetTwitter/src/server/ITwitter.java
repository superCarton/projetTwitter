package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITwitter extends Remote {

	public boolean connect(String user, String password) throws RemoteException;
	public void twitter(String user, String message) throws RemoteException;
	public void reTwitter(String user, String message) throws RemoteException;
}

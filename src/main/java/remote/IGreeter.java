package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGreeter extends Remote
{
	public String Greet() throws RemoteException;
}

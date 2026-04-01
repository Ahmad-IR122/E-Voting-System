package servers;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VotingService extends Remote {
    String castVote(String voterId, String candidate) throws RemoteException;

}

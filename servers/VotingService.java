package servers;

import model.Vote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VotingService extends Remote {
    String castVote(Vote vote) throws RemoteException;

}

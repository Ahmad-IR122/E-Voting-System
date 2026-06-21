package servers;
import model.Vote;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VerificationService extends Remote {
    boolean verifyVote(Vote vote) throws RemoteException;

}

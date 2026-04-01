package servers;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VerificationService extends Remote {
    boolean verifyVote(String voteData, String hash) throws RemoteException;

}

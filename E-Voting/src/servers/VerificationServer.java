package servers;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class VerificationServer implements VerificationService {

    @Override
    public boolean verifyVote(String voteData, String hash) throws RemoteException {
        return false;
    }

    public static void main(String[] args) {


    }

}

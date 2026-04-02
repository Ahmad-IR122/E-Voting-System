package servers;

import model.Vote;
import util.HashUtil;
import util.KeyManager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;

public class VerificationServer extends UnicastRemoteObject implements VerificationService {
    public VerificationServer() throws RemoteException {
        super();
    }

    @Override
    public boolean verifyVote(Vote vote) throws RemoteException {
        try {
            String voteData = vote.getVoterID() + "|" + vote.getCandidateName() + "|" + vote.getTimestamp();
            String newHash = HashUtil.generateHash(voteData);
            if (!newHash.equals(vote.getHash())) {
                System.out.println("Hash mismatch for vote from voter ID: " + vote.getVoterID());
                return false;
            }
            PublicKey publicKey = KeyManager.getPublicKey();
            boolean isSignatureValid = util.SignatureUtil.verifySignature(
                    voteData,
                    vote.getSignature(),
                    publicKey
            );
            if (!isSignatureValid) {
                System.out.println("Invalid signature!");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error verifying vote: " + e.getMessage());
            return false;
        }


    }

    public static void main(String[] args) {
        try {
            VerificationServer server = new VerificationServer();

            Registry registry = LocateRegistry.createRegistry(3000);
            registry.rebind("VerificationService", server);

            System.out.println("Verification Server running on port 3000...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

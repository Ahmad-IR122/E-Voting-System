package servers;

import model.Vote;
import util.HashUtil;
import util.KeyManager;
import util.SignatureUtil;

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
            System.out.println("Verification server received vote");
            System.out.println("Voter ID: " + vote.getVoterID());

            String voteData = vote.getVoterID() + "|" + vote.getCandidateName() + "|" + vote.getTimestamp();
            System.out.println("Verifying hash...");
            String newHash = HashUtil.generateHash(voteData);
            if (!newHash.equals(vote.getHash())) {
                System.out.println("Hash mismatch for vote from voter ID: " + vote.getVoterID());
                return false;
            }
            System.out.println("Verifying signature...");
            PublicKey publicKey = KeyManager.getPublicKey();
            boolean isSignatureValid = SignatureUtil.verifySignature(
                    voteData,
                    vote.getSignature(),
                    publicKey
            );
            if (!isSignatureValid) {
                System.out.println("Invalid signature!");
                return false;
            }
            System.out.println("Vote verification passed.");
            return true;
        } catch (Exception e) {
            System.out.println("Error verifying vote: " + e.getMessage());
            return false;
        }


    }

    public static void main(String[] args) {
        try {
            KeyManager.generateAndSaveKeysIfNotExist();
            KeyManager.printKeyPaths();

            VerificationServer server = new VerificationServer();

            Registry registry = LocateRegistry.createRegistry(2000);
            registry.rebind("VerificationService", server);

            System.out.println("Verification Server running on port 2000...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

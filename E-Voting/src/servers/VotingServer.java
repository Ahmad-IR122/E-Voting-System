package servers;

import model.Vote;
import util.FileUtil;
import util.HashUtil;
import util.KeyManager;
import util.SignatureUtil;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;

public class VotingServer implements VotingService {

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    public String castVote(Vote vote) throws RemoteException {
        try{

            if (vote == null) {
                return "Invalid vote payload.";
            }

            if (isBlank(vote.getVoterID()) || isBlank(vote.getCandidateName())
                    || isBlank(vote.getTimestamp()) || isBlank(vote.getHash())
                    || isBlank(vote.getSignature())) {
                return "Incomplete vote data.";
            }

            String voteID = vote.getVoterID();
            if(!FileUtil.voterExists(voteID)) {
                return "Invalid voter ID";
            }

            if(FileUtil.hasAlreadyVoted(voteID)){
                return "You have already voted.";
            }

            String voteData = vote.getVoterID() + "|" + vote.getCandidateName() + "|" + vote.getTimestamp();
            String newHash = HashUtil.generateHash(voteData);
            if(!newHash.equals(vote.getHash())){
                return "Vote data integrity compromised.";
            }
            PublicKey publicKey = KeyManager.getPublicKey();

            boolean isSignatureValid = SignatureUtil.verifySignature(
                    voteData,
                    vote.getSignature(),
                    publicKey
            );

            if (!isSignatureValid) {
                FileUtil.writeAuditLog("Invalid signature for voter: " + vote.getVoterID());
                return "Invalid signature!";
            }


            Registry registry = LocateRegistry.getRegistry("localhost", 3000);
            VerificationService verificationService =
                    (VerificationService) registry.lookup("VerificationService");

            boolean isValid = verificationService.verifyVote(vote);

            if (!isValid) {
                FileUtil.writeAuditLog("Verification failed for voter: " + voteID);
                return "Vote rejected (verification failed)";
            }
            FileUtil.saveVote(vote);

            FileUtil.markVoterAsVoted(voteID);

            return "Vote recorded successfully";

        } catch (NotBoundException e) {
            FileUtil.writeAuditLog("Verification service is unavailable: " + e.getMessage());
            throw new RemoteException("Verification service is unavailable.", e);
        } catch (Exception e) {
            FileUtil.writeAuditLog("Error processing vote: " + e.getMessage());
            throw new RemoteException("Error processing vote.", e);
        }

    }

    public static void main(String[] args ) {

        try {
            VotingServer server = new VotingServer();
            VotingService stub = (VotingService) UnicastRemoteObject.exportObject(server, 3000);
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(3000);
            } catch (RemoteException e) {
                registry = LocateRegistry.getRegistry(3000);
            }
            registry.rebind("VotingService", stub);
            System.out.println("Voting Server is running...");

        } catch (Exception e) {
            System.err.println("Failed to start VotingServer:");
            e.printStackTrace();
            throw new RuntimeException("Failed to start VotingServer", e);
        }


    }


}

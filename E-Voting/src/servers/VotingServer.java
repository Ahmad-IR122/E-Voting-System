package servers;

import model.Vote;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class VotingServer implements VotingService {

    @Override
    public String castVote(Vote vote) throws RemoteException {
        System.out.println("Received vote from voter ID: " + vote.getVoterID()
                + " \nfor candidate: " + vote.getCandidateName() +
                "\n at " + vote.getTimestamp() + " \nwith hash: " +
                vote.getHash() + "\n and signature: " + vote.getSignature());
        return "vote received successfully";
    }

    public static void main(String[] args ) {

        try {
            VotingServer server = new VotingServer();
            VotingService stub = (VotingService) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.createRegistry(3000);
            registry.bind("VotingService", stub);
            System.out.println("Voting Server is running...");

        } catch (Exception e) {
            System.err.println("Failed to verify or generate signature in SignatureUtil:");
            e.printStackTrace();
            throw new RuntimeException("Failed to verify or generate signature", e);
        }


    }


}

package servers;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class VotingServer implements VotingService {

    @Override
    public String castVote(String voterId, String candidate) throws RemoteException {
        return "";
    }
    public static void main(String[] args) {

        try{
            VotingServer server = new VotingServer();
            VotingService stub = (VotingService) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.createRegistry(3000);
            registry.bind("VotingService", stub);
            System.out.println("Voting Server is running...");

        } catch (Exception e) {
            System.out.println("Voting Server exception: " + e.toString());
            e.printStackTrace();
        }


    }



}

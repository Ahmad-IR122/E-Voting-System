package client;

import model.Vote;
import servers.VotingService;
import util.HashUtil;
import util.SignatureUtil;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class VotingClient {
    public static void main(String[] args) {
        System.out.println("Welcome to the E-Voting System!");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your Voter ID: ");
        String voterId = scanner.nextLine().trim();
        String regex = "^[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(voterId);

        while (true) {
            if (matcher.matches()) {
                System.out.println("Voter ID is valid.");
                break;
            } else {
                System.out.println("Invalid Voter ID. Please enter a 4-digit number.");
                System.out.print("Enter your Voter ID: ");
                voterId = scanner.nextLine().trim();
                matcher = pattern.matcher(voterId);
            }

        }
        System.out.print("Enter the candidate you want to vote for: ");
        String candidate = scanner.nextLine().trim();
        String timestamp = LocalDateTime.now().toString();
        String VoteData = voterId + "|" + candidate + "|" + timestamp;
        String hash = HashUtil.generateHash(VoteData);
        String signature = SignatureUtil.generateSignature(VoteData);

        Vote vote = new Vote(voterId, candidate, timestamp, hash, signature);
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 3000);
            VotingService votingService = (VotingService) registry.lookup("VotingService");
            String response = votingService.castVote(vote);
            System.out.println("Response from server: " + response);

        } catch (java.rmi.NotBoundException e) {
            System.out.println("Service 'VotingService' is not bound in the registry.");
            e.printStackTrace();
        } catch (RemoteException e) {
            System.out.println("RMI connection error: " + e.getMessage());
            e.printStackTrace();
        }
        scanner.close();



    }
}

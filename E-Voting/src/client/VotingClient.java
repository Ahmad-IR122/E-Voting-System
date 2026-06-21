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

public class VotingClient {

    private static final String HOST = "localhost";
    private static final int PORT = 3000;
    private static final String SERVICE_NAME = "VotingService";
    private static final Pattern VOTER_ID_PATTERN = Pattern.compile("^\\d{4}$");

    public static void main(String[] args) {
        System.out.println("Welcome to the E-Voting System!");

        try (Scanner scanner = new Scanner(System.in)) {
            String voterId = getValidVoterId(scanner);
            String candidate = getValidCandidate(scanner);

            Vote vote = buildVote(voterId, candidate);
            submitVote(vote);
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getValidVoterId(Scanner scanner) {
        while (true) {
            System.out.print("Enter your Voter ID: ");
            String voterId = scanner.nextLine().trim();

            if (VOTER_ID_PATTERN.matcher(voterId).matches()) {
                System.out.println("Voter ID is valid.");
                return voterId;
            }

            System.out.println("Invalid Voter ID. Please enter a 4-digit number.");
        }
    }

    private static String getValidCandidate(Scanner scanner) {
        while (true) {
            System.out.print("Enter the candidate you want to vote for: ");
            String candidate = scanner.nextLine().trim();
            if (!candidate.isEmpty()) {
                return candidate;
            }

            System.out.println("Candidate name cannot be empty.");
        }
    }

    private static Vote buildVote(String voterId, String candidate) {
        String timestamp = LocalDateTime.now().toString();
        String voteData = voterId + "|" + candidate + "|" + timestamp;
        String hash = HashUtil.generateHash(voteData);
        String signature = SignatureUtil.generateSignature(voteData);

        return new Vote(voterId, candidate, timestamp, hash, signature);
    }

    private static void submitVote(Vote vote) {
        try {
            Registry registry = LocateRegistry.getRegistry(HOST, PORT);
            VotingService votingService = (VotingService) registry.lookup(SERVICE_NAME);

            String response = votingService.castVote(vote);
            System.out.println("Response from server: " + response);

        } catch (java.rmi.NotBoundException e) {
            System.out.println("Service '" + SERVICE_NAME + "' is not bound in the registry.");
        } catch (RemoteException e) {
            System.out.println("RMI connection error: " + e.getMessage());
        }
    }
}
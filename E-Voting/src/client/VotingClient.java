package client;

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
        System.out.println("Casting vote for " + candidate + " at " + timestamp);
        String VoteData = voterId + "|" + candidate + "|" + timestamp;
        // Here you would call the VotingService to cast the vote
        // For example: votingService.castVote(voterId, candidate);
        System.out.println("Vote data: " + VoteData + "\n");
        System.out.println("Vote cast successfully!");


    }
}

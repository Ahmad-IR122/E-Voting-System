package util;
import model.Vote;
import java.io.*;
import java.time.LocalDateTime;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
public class FileUtil {

    private static final String votesFilePath = "util/votes.txt";
    private static final String votersFilePath = "util/voters.txt";
    private static final String logFilePath = "util/audit_log.txt";

    public static boolean voterExists(String voterId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(votersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(voterId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean hasAlreadyVoted(String voterId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(votersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(voterId)) {
                    return parts[1].equalsIgnoreCase("true");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void markVoterAsVoted(String voterId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(votersFilePath));
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts[0].equals(voterId)) {
                    updatedLines.add(parts[0] + ",true");
                } else {
                    updatedLines.add(line);
                }
            }

            Files.write(Paths.get(votersFilePath), updatedLines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveVote(Vote vote) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(votesFilePath, true))) {
            writer.write(vote.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeAuditLog(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            String time = LocalDateTime.now().toString();
            writer.write("[" + time + "] " + message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

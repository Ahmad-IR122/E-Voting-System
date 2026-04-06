package util;
import model.Vote;
import java.io.*;
import java.time.LocalDateTime;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class FileUtil {

    private static final Path dataDirectory = resolveDataDirectory();
    private static final Path votesFilePath = dataDirectory.resolve("votes.txt");
    private static final Path votersFilePath = dataDirectory.resolve("voters.txt");
    private static final Path logFilePath = dataDirectory.resolve("audit_log.txt");

    private static Path resolveDataDirectory() {
        List<Path> candidates = Arrays.asList(
                Paths.get("src", "data"),
                Paths.get("E-Voting", "src", "data"),
                Paths.get("data")
        );

        for (Path candidate : candidates) {
            Path absoluteCandidate = candidate.toAbsolutePath().normalize();
            if (Files.isDirectory(absoluteCandidate)) {
                return absoluteCandidate;
            }
        }

        Path fallback = Paths.get("E-Voting", "src", "data").toAbsolutePath().normalize();
        try {
            Files.createDirectories(fallback);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to create data directory: " + fallback, e);
        }
        return fallback;
    }

    private static void ensureDataFilesExist() {
        try {
            Files.createDirectories(dataDirectory);
            createFileIfMissing(votesFilePath);
            createFileIfMissing(votersFilePath);
            createFileIfMissing(logFilePath);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to initialize data files.", e);
        }
    }

    private static void createFileIfMissing(Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
    }

    static {
        ensureDataFilesExist();
    }

    public static boolean voterExists(String voterId) {
        try (BufferedReader reader = Files.newBufferedReader(votersFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("[|]");
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
        try (BufferedReader reader = Files.newBufferedReader(votersFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("[|]");
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
            List<String> lines = Files.readAllLines(votersFilePath);
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split("[|]");
                if (parts[0].equals(voterId)) {
                    updatedLines.add(parts[0] + ",true");
                } else {
                    updatedLines.add(line);
                }
            }

            Files.write(votersFilePath, updatedLines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveVote(Vote vote) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                votesFilePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {
            writer.write(vote.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeAuditLog(String message) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                logFilePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {
            String time = LocalDateTime.now().toString();
            writer.write("[" + time + "] " + message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

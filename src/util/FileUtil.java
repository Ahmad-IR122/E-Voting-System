package util;

import model.Vote;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileUtil {

    private static final Path DATA_DIR = resolveDataDirectory();
    private static final Path votesFilePath = DATA_DIR.resolve("votes.txt");
    private static final Path votersFilePath = DATA_DIR.resolve("voters.txt");
    private static final Path logFilePath = DATA_DIR.resolve("audit_log.txt");

    private static Path resolveDataDirectory() {
        List<Path> candidates = Arrays.asList(
                Paths.get("E-Voting", "src", "data"),
                Paths.get("src", "data"),
                Paths.get("data")
        );

        for (Path candidate : candidates) {
            Path absoluteCandidate = candidate.toAbsolutePath().normalize();
            if (Files.exists(absoluteCandidate) && Files.isDirectory(absoluteCandidate)) {
                return absoluteCandidate;
            }
        }

        Path fallback = Paths.get("src", "data").toAbsolutePath().normalize();
        try {
            Files.createDirectories(fallback);
        } catch (IOException e) {
            throw new RuntimeException("Could not create data directory: " + fallback, e);
        }
        return fallback;
    }

    private static void ensureFileExists(Path filePath) {
        try {
            if (!Files.exists(filePath)) {
                if (filePath.getParent() != null) {
                    Files.createDirectories(filePath.getParent());
                }
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create file: " + filePath.toAbsolutePath(), e);
        }
    }

    public static boolean voterExists(String voterId) {
        ensureFileExists(votersFilePath);

        try {
            System.out.println("Looking for voter ID: [" + voterId + "]");
            System.out.println("Reading voters file from: " + votersFilePath.toAbsolutePath());

            String normalizedVoterId = voterId == null ? "" : voterId.trim();
            if (normalizedVoterId.isEmpty()) {
                return false;
            }

            List<String> voterIds = new ArrayList<>();
            for (String line : Files.readAllLines(votersFilePath, StandardCharsets.UTF_8)) {
                String cleanedLine = line.trim();
                if (cleanedLine.isEmpty()) {
                    continue;
                }

                String[] parts = cleanedLine.split("\\|");
                if (parts.length >= 1) {
                    voterIds.add(parts[0].trim());
                }
            }

            Collections.sort(voterIds);

            if (Collections.binarySearch(voterIds, normalizedVoterId) >= 0) {
                System.out.println("MATCH FOUND");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("NO MATCH FOUND");
        return false;
    }

    public static boolean hasAlreadyVoted(String voterId) {
        ensureFileExists(votersFilePath);

        try (BufferedReader reader = Files.newBufferedReader(votersFilePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String cleanedLine = line.trim();
                if (cleanedLine.isEmpty()) {
                    continue;
                }

                String[] parts = cleanedLine.split("\\|");
                if (parts.length >= 2 && parts[0].trim().equals(voterId.trim())) {
                    return parts[1].trim().equalsIgnoreCase("true");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void markVoterAsVoted(String voterId) {
        ensureFileExists(votersFilePath);

        try {
            List<String> lines = Files.readAllLines(votersFilePath, StandardCharsets.UTF_8);
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                String cleanedLine = line.trim();

                if (cleanedLine.isEmpty()) {
                    continue;
                }

                String[] parts = cleanedLine.split("\\|");
                if (parts.length >= 2 && parts[0].trim().equals(voterId.trim())) {
                    updatedLines.add(parts[0].trim() + "|true");
                } else {
                    updatedLines.add(cleanedLine);
                }
            }

            Files.write(
                    votersFilePath,
                    updatedLines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveVote(Vote vote) {
        ensureFileExists(votesFilePath);

        try {
            Files.write(
                    votesFilePath,
                    (vote.toString() + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeAuditLog(String message) {
        ensureFileExists(logFilePath);

        String logEntry = "[" + LocalDateTime.now() + "] " + message + System.lineSeparator();

        try {
            Files.write(
                    logFilePath,
                    logEntry.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
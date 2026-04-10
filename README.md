# 🗳️ E-Voting System

> A Java-based electronic voting system focused on vote integrity, verification, and secure record handling.

![Java](https://img.shields.io/badge/Java-22-orange?style=for-the-badge&logo=openjdk)
![Platform](https://img.shields.io/badge/Platform-Console%20App-blue?style=for-the-badge)
![Architecture](https://img.shields.io/badge/Architecture-Java%20RMI-green?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Academic%20Project-lightgrey?style=for-the-badge)

## 📖 Overview

This project is a console-based Java e-voting application built with **Java RMI**. A voter submits a vote through a client application; the vote is forwarded to a voting server, which validates it and forwards it to a separate verification server before the vote is permanently saved.

The system is designed around a few core principles:

- Validate the voter's identity before accepting a vote
- Prevent duplicate voting
- Check vote integrity using **SHA-256 hashing**
- Verify authenticity using **RSA digital signatures** (2048-bit, auto-generated on first run)
- Store votes and audit logs in local text files

## ✨ Features

- Secure vote submission through a Java RMI client
- Separate **Voting Server** and **Verification Server** communicating over RMI
- Voter ID validation against a local registry before vote processing
- Duplicate vote prevention
- Vote integrity check using SHA-256 hashing
- Digital signature generation and verification using RSA
- Audit logging for failed verifications and server errors
- Flat-file data storage for voters, votes, and logs

## 🛠️ Tech Stack

| Area | Technology |
|------|-----------|
| Language | Java 22 |
| Architecture | Java RMI |
| Security | SHA-256, RSA 2048-bit, Base64 |
| Data Storage | Plain text files |
| Build | Manual `javac` / `java` commands |

## 📦 Installation

### Prerequisites

- Java JDK 22 or newer
- A terminal or IDE that supports running Java applications

### Clone the repository

```bash
git clone https://github.com/Ahmad-IR122/E-Voting-System.git
cd E-Voting-System
```

### Compile the project

First, create the output directory:

```bash
mkdir -p out          # Linux / macOS
mkdir out             # Windows
```

Then compile all sources:

**Linux / macOS**
```bash
find E-Voting/src -name "*.java" | xargs javac -d out
```

**Windows (PowerShell)**
```powershell
javac -d out (Get-ChildItem -Recurse E-Voting\src -Filter *.java | ForEach-Object { $_.FullName })
```

**Windows (Command Prompt)**
```cmd
for /r E-Voting\src %f in (*.java) do @set SOURCES=%SOURCES% "%f"
javac -d out %SOURCES%
```

> **Note:** RSA key pairs are generated automatically in `src/data/keys/` the first time either server starts. No manual key setup is required.

## 🚀 Usage

Run the three components **in this order** — each in its own terminal:

### 1. Start the Verification Server

```bash
java -cp out servers.VerificationServer
```

### 2. Start the Voting Server

```bash
java -cp out servers.VotingServer
```

### 3. Start the Voting Client

```bash
java -cp out client.VotingClient
```

### Vote submission flow

1. Enter a **voter ID** (must exist in `voters.txt`)
2. Enter the candidate name
3. The client builds a vote record with a timestamp, then hashes and signs it
4. The voting server checks voter validity and prevents duplicate voting
5. The voting server independently verifies the hash and signature
6. The vote is forwarded to the verification server for a second check
7. If both checks pass, the vote is saved to `votes.txt`

## 🗂️ Project Structure

```text
E-Voting-System/
├── E-Voting/
│   └── src/
│       ├── client/
│       │   └── VotingClient.java        # Entry point for voters
│       ├── model/
│       │   └── Vote.java                # Vote data model
│       ├── servers/
│       │   ├── VerificationServer.java  # Verification server (port 2000)
│       │   ├── VerificationService.java # RMI interface for verification
│       │   ├── VotingServer.java        # Voting server (port 3000)
│       │   └── VotingService.java       # RMI interface for voting
│       ├── util/
│       │   ├── FileUtil.java            # File I/O helpers
│       │   ├── HashUtil.java            # SHA-256 hashing
│       │   ├── KeyManager.java          # RSA key generation & loading
│       │   └── SignatureUtil.java       # RSA sign & verify
│       └── data/
│           ├── audit_log.txt            # Timestamped failure log
│           ├── voters.txt               # Voter registry (format: ID|hasVoted)
│           └── votes.txt                # Accepted vote records
├── out/                                 # Compiled .class files (generated)
└── README.md
```

> **voters.txt format:** Each line follows `<voterID>|<hasVoted>`, for example `1234|false`. The `hasVoted` flag is updated to `true` once a vote is accepted.

## 🔌 RMI Services

This project uses **Java RMI** instead of REST endpoints.

| Service | Method | Description |
|---------|--------|-------------|
| `VotingService` | `castVote(Vote vote)` | Accepts a vote from the client, validates it, and coordinates with the verification server |
| `VerificationService` | `verifyVote(Vote vote)` | Re-checks the vote hash and digital signature |

### Default ports

| Component | Port |
|-----------|------|
| Verification Server | `2000` |
| Voting Server | `3000` |

## 🔮 Future Improvements

- Add a graphical user interface
- Replace text-file storage with a database
- Add admin controls and election management
- Strengthen voter authentication beyond a simple ID
- Encrypt stored vote data at rest
- Add automated tests
- Add result tallying and reporting
- Package the application with Maven or Gradle

## 🤝 Contributing

Contributions are welcome.

1. Fork the repository
2. Create a new branch
3. Make your changes
4. Test your changes
5. Open a pull request

## 👤 Author / Contact

- **Name:** Ahmad Irshaid
- **Email:** irsheidahmad094@gmail.com

---

## 📘 Course Information

| Field | Value |
|-------|-------|
| Course | Web Services Security |
| Semester | Second Semester 2024/2025 |
| Assignment Type | Homework Project |
| Language | Java |
| Instructor | Amjad W. Hawash |

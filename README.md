# 🗳️ E-Voting System

> A simple Java-based electronic voting system focused on vote integrity, verification, and secure record handling.

![Java](https://img.shields.io/badge/Java-22-orange?style=for-the-badge&logo=openjdk)
![Platform](https://img.shields.io/badge/Platform-Console%20App-blue?style=for-the-badge)
![Architecture](https://img.shields.io/badge/Architecture-Java%20RMI-green?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Academic%20Project-lightgrey?style=for-the-badge)

## 📖 Overview

This project is a Java-based e-voting application built with **Java RMI**. It allows a voter to submit a vote through a client application, sends that vote to a voting server, and validates the vote through a separate verification server before saving it.

The system is designed around a few core ideas:

- Validate the voter before accepting a vote
- Prevent duplicate voting
- Check vote integrity using **SHA-256 hashing**
- Verify authenticity using **RSA digital signatures**
- Store votes and audit logs in local text files

This repository appears to be a console-based Java application rather than a web or mobile app.

## ✨ Features

- Secure vote submission through a Java client
- Separate **Voting Server** and **Verification Server**
- Voter ID validation before vote processing
- Duplicate vote prevention
- Vote integrity check using SHA-256 hashing
- Digital signature generation and verification using RSA
- Audit logging for failed verification and server issues
- Flat-file data storage for voters, votes, and logs

## 🛠️ Tech Stack

- **Language:** Java
- **Architecture:** Java RMI
- **Security:** SHA-256, RSA, Base64
- **Data Storage:** Text files
- **Build/Run Style:** Manual `javac` / `java` commands
- **IDE:** [Add IDE name if you want to mention it]

## 📦 Installation

### Prerequisites

- Java JDK 22 or newer
- A terminal or IDE that can run Java applications

### Clone the repository

```bash
git clone [Add repository URL]
cd E-Voting-System
```

### Compile the project

```bash
javac -d out (Get-ChildItem -Recurse E-Voting\src -Filter *.java | ForEach-Object { $_.FullName })
```

## 🚀 Usage

Run the services in this order:

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

### Basic flow

- Enter a **4-digit voter ID**
- Enter the candidate name
- The client creates vote data with a timestamp
- The system hashes and signs the vote
- The voting server checks the voter and prevents duplicate voting
- The verification server validates the hash and signature
- The vote is stored if all checks pass

## 🗂️ Project Structure

```text
E-Voting-System/
├── E-Voting/
│   └── src/
│       ├── client/
│       │   └── VotingClient.java
│       ├── model/
│       │   └── Vote.java
│       ├── servers/
│       │   ├── VerificationServer.java
│       │   ├── VerificationService.java
│       │   ├── VotingServer.java
│       │   └── VotingService.java
│       ├── util/
│       │   ├── FileUtil.java
│       │   ├── HashUtil.java
│       │   ├── KeyManager.java
│       │   └── SignatureUtil.java
│       └── data/
│           ├── audit_log.txt
│           ├── voters.txt
│           └── votes.txt
├── out/
├── src/
│   └── data/
│       └── keys/
│           ├── private.key
│           └── public.key
└── README.md
```

## 🔌 API Endpoints / Main Functionality

This project does not expose REST API endpoints. It uses **Java RMI remote services** instead.

### Main remote services

- `VotingService.castVote(Vote vote)`
  Accepts a vote from the client and processes it through validation and verification.

- `VerificationService.verifyVote(Vote vote)`
  Checks the vote hash and digital signature before the vote is accepted.

### Default ports

- `VerificationServer`: `2000`
- `VotingServer`: `3000`

## 🔮 Future Improvements

- Add a graphical user interface
- Replace text-file storage with a database
- Add admin controls and election management
- Improve voter authentication beyond a 4-digit ID
- Add encryption for stored vote data
- Add automated tests
- Add result tallying and reporting
- Package the application with a proper build tool like Maven or Gradle

## 🤝 Contributing

Contributions are welcome.

To contribute:

1. Fork the repository
2. Create a new branch
3. Make your changes
4. Test your changes
5. Open a pull request

## 👤 Author / Contact

- **Name:** `Ahmad Irshaid`
- **Email:** `irsheidahmad094@gmail.com`

---
## 📘 Course Information
- **Course:** `Web Services Security`
- **Semester:** `Second Semester 2025/2022`
- **University Assignment:** `Home Work Project`
- **Programming language** `JAVA`
- **Instructor :** `Amjad W. Hawash`

package model;
import java.io.Serializable;

public class Vote implements Serializable {
    private String voterID;
    private String candidateName;
    private String timestamp;
    private String hash;
    private String signature;

    public Vote() {
        System.out.println("Vote object Created........... ");
    }

    public Vote(String voterID, String candidateName, String timestamp, String hash, String signature) {

        System.out.println("Vote object Created........... ");
        this.voterID = voterID;
        this.candidateName = candidateName;
        this.timestamp = timestamp;
        this.hash = hash;
        this.signature = signature;
    }



    public String getVoterID() {
        return voterID;
    }

    public void setVoterID(String voterID) {
        this.voterID = voterID;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return voterID + "," + candidateName + "," + timestamp + "," + hash + "," + signature;
    }
}

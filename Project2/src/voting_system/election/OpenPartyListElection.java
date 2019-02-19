package voting_system.election;

import voting_system.Candidate;
import voting_system.Party;
import voting_system.ballot.OpenPartyListBallot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.System.exit;

/**
 * A class representing an open party list election.
 * By: Michael Birk, John Caspers, Mukesh Pragaram Choudhary, Daniel Rockcastle
 */
public class OpenPartyListElection extends Election {

    private final List<OpenPartyListBallot> ballotList;
    private final int numSeats;

    public OpenPartyListElection() {
        super();
        ballotList = new ArrayList<>();
        numSeats = 0;
    } // unused default constructor

    /**
     * Constructor.
     *
     * @param candidateList A list of Candidate objects.
     * @param ballotList    A list of OpenPartyListBallot objects.
     * @param numCandidates The total number of candidates in the election.
     * @param numBallots    The total number of ballots in the election.
     * @param numSeats      The total number of seats in the election.
     */
    public OpenPartyListElection(List<Candidate> candidateList,
                                 List<OpenPartyListBallot> ballotList,
                                 int numCandidates,
                                 int numBallots,
                                 int numSeats) {
        super(candidateList, numCandidates, numBallots);
        this.ballotList = ballotList;
        this.numSeats = numSeats;
    }

    /**
     * Runs the election by iterating through each ballot and counting the votes. The winner is the candidate with the majority of the votes.
     * Audit file is created in the directory you choose that shows how the election progressed.
     *
     * @param outputPath The path to the directory  the audit file will be outputted to.
     */
    @Override
    public void runElection(Path outputPath) {
        Map<Party, Integer> partyVoteMap = new HashMap<>();
        for (Party party : Party.values()) {
            partyVoteMap.put(party, 0);
        }
        Path outputFile = Paths.get(outputPath.toString(), System.currentTimeMillis() + "_audit");
        Path outputFileSummary = Paths.get(outputPath.toString(), System.currentTimeMillis() + "_summary");
        StringBuilder summaryFileBuilder = new StringBuilder();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        summaryFileBuilder.append("Date is " + dateFormat.format(localDate) + "\n");
        summaryFileBuilder.append("Type of voting is OPL.\n");
        // String which will hold all information outputted into the audit file
        StringBuilder auditFileBuilder = new StringBuilder();
        auditFileBuilder.append("Type of voting is OPL.\nNumber of candidates is " + getNumCandidates() + ".\nNumber of ballots/votes is " + getNumBallots() + ".\nNumber of seats is " + numSeats + "\n");
        summaryFileBuilder.append("Candidates are ");
        for (Candidate candidate : getCandidateList()) {
            auditFileBuilder.append(candidate.getName() + "\n");
            summaryFileBuilder.append(candidate.getName() + ",");
        }
        summaryFileBuilder.append("\n");
        summaryFileBuilder.append("Number of seats is " + numSeats + ".\n");
        // Iterates through the ballots and adds the votes for each candidate
        ballotList.forEach(ballot ->
                ballot.getVoteList().forEach(vote -> {
                    vote.getCandidate().addVote();
                    partyVoteMap.put(vote.getCandidate().getParty(), partyVoteMap.get(vote.getCandidate().getParty()) + 1);
                    auditFileBuilder.append("Vote added to candidate " + vote.getCandidate().getName() + " who now has " + vote.getCandidate().getNumberOfVotes() +
                            ". Vote added to party " + vote.getCandidate().getParty() + " who now has " + partyVoteMap.get(vote.getCandidate().getParty()) + " votes.\n");
                }));

        int numVotesRequired = getNumBallots() / numSeats;
        auditFileBuilder.append("Number of votes required to win a seat is " + numVotesRequired + "\n");
        int numSeatsRemaining = numSeats;
        int numPartySeats = 0;
        Set<Candidate> winnerSet = new HashSet<>();
        Candidate tempCandidate = new Candidate();
        for (Party party : Party.values()) {  //iterate through the party and determine number of seats won by each party
            tempCandidate = new Candidate();
            numPartySeats = partyVoteMap.get(party) / numVotesRequired;
            auditFileBuilder.append(party + " has " + numPartySeats + " seats that they won outright.\n");
            while (numPartySeats > 0) {
                for (Candidate candidate : getCandidateList()) {  // Iterate through the canadidates to determine who won a seat
                    if (candidate.getParty().equals(party) && !winnerSet.contains(candidate) && candidate.getNumberOfVotes() > tempCandidate.getNumberOfVotes()) {
                        tempCandidate = candidate;
                    }
                }
                winnerSet.add(tempCandidate);  // Store candidate who won a seat
                partyVoteMap.put(party, partyVoteMap.get(party) - numVotesRequired);
                numSeatsRemaining--;
                numPartySeats--;
                auditFileBuilder.append("Candidate " + tempCandidate.getName() + " won a seat with " + tempCandidate.getNumberOfVotes() + " votes. The " + party + " party has " + numPartySeats + " seats remaining.\n");
            }
        }

        auditFileBuilder.append("There are currently " + numSeatsRemaining + " seats remaining for remainder elections.\n");
        Party tempParty = Party.DEMOCRAT;
        while (numSeatsRemaining > 0) {  // Assign the remaining seats to the parties with the most remaining votes
            for (Party party : Party.values()) {
                if (partyVoteMap.get(party) > partyVoteMap.get(tempParty)) {
                    tempParty = party;
                }
            }
            tempCandidate = new Candidate();
            for (Candidate candidate : getCandidateList()) { // Determine candidates who won remaining seats
                if (candidate.getParty().equals(tempParty) && !winnerSet.contains(candidate) && candidate.getNumberOfVotes() > tempCandidate.getNumberOfVotes()) {
                    tempCandidate = candidate;
                }
            }
            winnerSet.add(tempCandidate);
            partyVoteMap.put(tempParty, 0);
            numSeatsRemaining--;
            auditFileBuilder.append("Candidate " + tempCandidate.getName() + " won a remainder seat with " + tempCandidate.getNumberOfVotes() + " votes and the " + tempCandidate.getParty() + " party\n");
        }

        for (Candidate winner : winnerSet) {  // Add winners to output string
            auditFileBuilder.append("A winner is " + winner.getName() + " with the party " + winner.getParty() + "\n");
            summaryFileBuilder.append("A winner is " + winner.getName() + " with the party " + winner.getParty() + "\n");
        }
        // Write information about how the election progressed into the audit file
        try {
            Files.write(outputFile, Arrays.asList(auditFileBuilder.toString()), StandardCharsets.UTF_8,
                    Files.exists(outputFile) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        } catch (IOException fileWriteFailure) {
            System.out.println("File write failure" + fileWriteFailure.toString());
            exit(6);
        }
        try {
            Files.write(outputFileSummary, Arrays.asList(summaryFileBuilder.toString()), StandardCharsets.UTF_8,
                    Files.exists(outputFileSummary) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        } catch (IOException fileWriteFailure) {
            System.out.println("File write failure" + fileWriteFailure.toString());
            exit(6);
        }

    }

    /**
     * Checks equality between two objects by comparing list of candidates; number of candidates, seats, and ballots;
     * and list of ballots.
     *
     * @param o The object to be compared against.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        OpenPartyListElection that = (OpenPartyListElection) o;
        return this.getCandidateList().equals(that.getCandidateList())
                && this.getNumCandidates() == that.getNumCandidates()
                && this.getNumBallots() == that.getNumBallots()
                && this.ballotList.equals(that.ballotList)
                && this.numSeats == that.numSeats;
    }

    /**
     * @return String representing this instance of an OpenPartyListElection object.
     */
    @Override
    public String toString() {
        return "OpenPartyListElection{" +
                "candidateList=" + getCandidateList() +
                ", numCandidates=" + getNumCandidates() +
                ", numBallots=" + getNumBallots() +
                ", ballotList=" + ballotList +
                ", numSeats=" + numSeats +
                '}';
    }

    public List<OpenPartyListBallot> getBallotList() {
        return ballotList;
    }

    public int getNumSeats() {
        return numSeats;
    }

}

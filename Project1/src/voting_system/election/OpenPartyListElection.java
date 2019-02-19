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
import java.util.*;

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
     * Runs the election by iterating through each ballot and counting the votes.
     * The winner is the candidate with the majority of the votes.
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
        Path outputFile = Paths.get(outputPath.toString(), Long.toString(System.currentTimeMillis()) + "_audit");

        // string which will hold all information outputted into the audit file
        StringBuilder auditFileBuilder = new StringBuilder();
        auditFileBuilder.append("Type of voting is OPL.\nNumber of candidates is ")
                .append(getNumCandidates())
                .append(".\nNumber of ballots/votes is ")
                .append(getNumBallots())
                .append(".\nNumber of seats is ")
                .append(numSeats)
                .append("\n");
        for (Candidate candidate : getCandidateList()) {
            auditFileBuilder.append(candidate.getName()).append("\n");
        }

        // iterates through the ballots and adds the votes for each candidate
        ballotList.forEach(ballot ->
                ballot.getVoteList().forEach(vote -> {
                    vote.getCandidate().addVote();
                    partyVoteMap.put(vote.getCandidate().getParty(),
                            partyVoteMap.get(vote.getCandidate().getParty()) + 1);
                    auditFileBuilder.append("Vote added to candidate ")
                            .append(vote.getCandidate().getName())
                            .append(" who now has ")
                            .append(vote.getCandidate().getNumberOfVotes())
                            .append(". Vote added to party ")
                            .append(vote.getCandidate().getParty())
                            .append(" who now has ")
                            .append(partyVoteMap.get(vote.getCandidate().getParty()))
                            .append(" votes.\n");
                }));

        int numVotesRequired = getNumBallots() / numSeats;
        auditFileBuilder.append("Number of votes required to win a seat is ")
                .append(numVotesRequired)
                .append("\n");
        int numSeatsRemaining = numSeats;
        int numPartySeats;
        Set<Candidate> winnerSet = new HashSet<>();
        Candidate tempCandidate;

        // iterate through the party and determine number of seats won by each party
        for (Party party : Party.values()) {
            tempCandidate = new Candidate();
            numPartySeats = partyVoteMap.get(party) / numVotesRequired;
            auditFileBuilder.append(party)
                    .append(" has ")
                    .append(numPartySeats)
                    .append(" seats that they won outright.\n");
            while (numPartySeats > 0) {
                // iterate through the candidates to determine who won a seat
                for (Candidate candidate : getCandidateList()) {
                    if (candidate.getParty().equals(party)
                            && !winnerSet.contains(candidate)
                            && candidate.getNumberOfVotes() > tempCandidate.getNumberOfVotes()) {
                        tempCandidate = candidate;
                    }
                }

                winnerSet.add(tempCandidate); // store candidate who won a seat
                partyVoteMap.put(party, partyVoteMap.get(party) - numVotesRequired);
                numSeatsRemaining--;
                numPartySeats--;
                auditFileBuilder.append("Candidate ")
                        .append(tempCandidate.getName())
                        .append(" won a seat with ")
                        .append(tempCandidate.getNumberOfVotes())
                        .append(" votes. The ")
                        .append(party)
                        .append(" party has ")
                        .append(numPartySeats)
                        .append(" seats remaining.\n");
            }
        }

        auditFileBuilder.append("There are currently ")
                .append(numSeatsRemaining)
                .append(" seats remaining for remainder elections.\n");
        Party tempParty = Party.DEMOCRAT;

        // assign the remaining seats to the parties with the most remaining votes
        while (numSeatsRemaining > 0) {
            for (Party party : Party.values()) {
                if (partyVoteMap.get(party) > partyVoteMap.get(tempParty)) {
                    tempParty = party;
                }
            }

            // determine candidates who won remaining seats
            tempCandidate = new Candidate();
            for (Candidate candidate : getCandidateList()) {
                if (candidate.getParty().equals(tempParty)
                        && !winnerSet.contains(candidate)
                        && candidate.getNumberOfVotes() > tempCandidate.getNumberOfVotes()) {
                    tempCandidate = candidate;
                }
            }
            winnerSet.add(tempCandidate);
            partyVoteMap.put(tempParty, 0);
            numSeatsRemaining--;
            auditFileBuilder.append("Candidate ")
                    .append(tempCandidate.getName())
                    .append(" won a remainder seat with ")
                    .append(tempCandidate.getNumberOfVotes())
                    .append(" votes and the ")
                    .append(tempCandidate.getParty())
                    .append(" party\n");
        }

        // add winners to output string
        for (Candidate winner : winnerSet) {
            auditFileBuilder.append("A winner is ")
                    .append(winner.getName())
                    .append(" with the party ")
                    .append(winner.getParty())
                    .append("\n");
        }

        // write information about how the election progressed into the audit file
        try {
            Files.write(outputFile, Collections.singletonList(auditFileBuilder.toString()), StandardCharsets.UTF_8,
                    Files.exists(outputFile) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
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
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OpenPartyListElection that = (OpenPartyListElection) o;
        return this.getCandidateList().equals(that.getCandidateList())
                && this.getNumCandidates() == that.getNumCandidates()
                && this.getNumBallots() == that.getNumBallots()
                && this.ballotList.equals(that.ballotList)
                && this.numSeats == that.numSeats;
    }

    @Override
    public String toString() {
        return "Election{" +
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

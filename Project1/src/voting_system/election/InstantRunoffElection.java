package voting_system.election;

import voting_system.Candidate;
import voting_system.ballot.InstantRunoffBallot;
import voting_system.vote.InstantRunoffVote;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.exit;

/**
 * A class representing an instant runoff election.
 * By: Michael Birk, John Caspers, Mukesh Pragaram Choudhary, Daniel Rockcastle
 */
public class InstantRunoffElection extends Election {

    private final List<InstantRunoffBallot> ballotList;

    public InstantRunoffElection() {
        super();
        ballotList = new ArrayList<>();
    } // unused default constructor

    /**
     * Constructor.
     *
     * @param candidateList A list of Candidate objects.
     * @param ballotList    A list of InstantRunoffBallot objects.
     * @param numCandidates The total number of candidates in the election.
     * @param numBallots    The total number of ballots in the election.
     */
    public InstantRunoffElection(List<Candidate> candidateList,
                                 List<InstantRunoffBallot> ballotList,
                                 int numCandidates,
                                 int numBallots) {
        super(candidateList, numCandidates, numBallots);
        this.ballotList = ballotList;
    }

    /**
     * Runs the election by iterating through each ballot and counting the votes.
     * The winner is the first candidate to reach 50 percent of the vote.
     * Audit file is created in the directory you choose that shows how the election progressed.
     *
     * @param outputPath The path to the directory  the audit file will be outputted to.
     */
    @Override
    public void runElection(Path outputPath) {
        List<Candidate> removedList = new ArrayList<>();
        Path outputFile = Paths.get(outputPath.toString(), Long.toString(System.currentTimeMillis()) + "_audit");

        // string which will hold all information outputted into the audit file
        StringBuilder auditFileBuilder = new StringBuilder();
        auditFileBuilder.append("Type of voting is IRV.\nNumber of candidates is ")
                .append(getNumCandidates())
                .append(".\nNumber of ballots/votes is ")
                .append(getNumBallots())
                .append(".\n");
        for (Candidate candidate : getCandidateList()) {
            auditFileBuilder.append(candidate.getName()).append("\n");
        }

        while (true) {
            // iterates through the ballot list to find the highest ranked candidate
            for (InstantRunoffBallot ballot : ballotList) {
                Candidate highestRankedCandidate = null;
                int maxRank = Integer.MAX_VALUE;

                for (InstantRunoffVote vote : ballot.getVoteList()) {
                    if (!removedList.contains(vote.getCandidate()) && vote.getRank() < maxRank) {
                        highestRankedCandidate = vote.getCandidate();
                        maxRank = vote.getRank();
                    }
                }

                if (highestRankedCandidate != null) {
                    highestRankedCandidate.addVote();
                    auditFileBuilder.append("Vote added to candidate ")
                            .append(highestRankedCandidate.getName())
                            .append(" who now has ")
                            .append(highestRankedCandidate.getNumberOfVotes())
                            .append(" votes.\n");
                }
            }

            // iterates through Candidate list and checks if the candidate has majority of votes else gets the losingCandidate
            Candidate losingCandidate = null;
            int minVotes = Integer.MAX_VALUE;
            for (Candidate candidate : getCandidateList()) {
                if (!removedList.contains(candidate)) {
                    if (((double) candidate.getNumberOfVotes() / (double)(getNumBallots()) > 0.5)) {
                        auditFileBuilder.append(candidate.getName())
                                .append(" wins! They had ")
                                .append(candidate.getNumberOfVotes())
                                .append(" total votes!\n");

                        // write information about how the election progressed into the audit file
                        try {
                            Files.write(outputFile, Collections.singletonList(auditFileBuilder.toString()), StandardCharsets.UTF_8,
                                    Files.exists(outputFile) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
                        } catch (IOException fileWriteFailure) {
                            System.out.println("File write failure" + fileWriteFailure.toString());
                            exit(6);
                        }
                        return;
                    } else if (candidate.getNumberOfVotes() < minVotes) {
                        losingCandidate = candidate;
                        minVotes = candidate.getNumberOfVotes();
                    }

                    candidate.resetNumberOfVotes();
                }
            }

            if (losingCandidate != null) {
                removedList.add(losingCandidate);
                auditFileBuilder.append(losingCandidate.getName())
                        .append(" is eliminated! They only had ")
                        .append(minVotes)
                        .append(" first place votes!\n");
            }
        }
    }

    /**
     * Checks equality between two objects by comparing list of candidates, number of candidates and ballots, and list
     * of ballots.
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

        InstantRunoffElection that = (InstantRunoffElection) o;
        return this.getCandidateList().equals(that.getCandidateList())
                && this.getNumCandidates() == that.getNumCandidates()
                && this.getNumBallots() == that.getNumBallots()
                && this.ballotList.equals(that.ballotList);
    }

    @Override
    public String toString() {
        return "Election{" +
                "candidateList=" + getCandidateList() +
                ", numCandidates=" + getNumCandidates() +
                ", numBallots=" + getNumBallots() +
                ", ballotList=" + ballotList +
                '}';
    }

    public List<InstantRunoffBallot> getBallotList() {
        return ballotList;
    }

}

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
import java.util.Arrays;
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

        List<List<Candidate>> roundsList = new ArrayList<>(); // votes candidates had each round
        StringBuilder outputTable = new StringBuilder();

        List<Candidate> removedList = new ArrayList<>();
        Path outputFile = Paths.get(outputPath.toString(), System.currentTimeMillis() + "_audit");
        // String which will hold all information outputted into the audit file
        StringBuilder auditFileBuilder = new StringBuilder();
        auditFileBuilder.append("Type of voting is IRV.\nNumber of candidates is " + getNumCandidates() + ".\nNumber of ballots/votes is " + getNumBallots() + ".\n");
        for (Candidate candidate : getCandidateList()) {
            auditFileBuilder.append(candidate.getName() + "\n");
        }
        while (true) {
            // iterates through the ballot list to find the highest ranked candidate.
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
                    auditFileBuilder.append("Vote added to candidate " + highestRankedCandidate.getName() + " who now has " + highestRankedCandidate.getNumberOfVotes() + " votes.\n");
                }
            }
            List<Candidate> currentCandidateList = new ArrayList<>();
            for (Candidate newcandidate : getCandidateList()) {
                Candidate copycandidate = new Candidate(newcandidate.getName(), newcandidate.getParty(), newcandidate.getNumberOfVotes());
                currentCandidateList.add(copycandidate);
            }
            roundsList.add(currentCandidateList); // adds the canadidates who got votes each round

            // Iterates through Candidate list and checks if the candidate has majority of votes else gets the losingCandidate
            Candidate losingCandidate = null;
            int minVotes = Integer.MAX_VALUE;
            for (Candidate candidate : getCandidateList()) {
                if (!removedList.contains(candidate)) {
                    if (((double) candidate.getNumberOfVotes() / (double) (getNumBallots()) > 0.5)) {
                        auditFileBuilder.append(candidate.getName() + " wins! They had " + candidate.getNumberOfVotes() + " total votes!\n");
                        // Write information about how the election progressed into the audit file
                        try {
                            Files.write(outputFile, Arrays.asList(auditFileBuilder.toString()), StandardCharsets.UTF_8,
                                    Files.exists(outputFile) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
                        } catch (IOException fileWriteFailure) {
                            System.out.println("File write failure" + fileWriteFailure.toString());
                            exit(6);
                        }
                        int currentCan = 0;
                        int currentRound = 0;
                        List<Integer> exhaust = new ArrayList<>();
                        for (int i = 0; i < (2 + getNumCandidates()); i++) {
                            if (i == 0) {
                                int longestcan = 0;
                                for (Candidate maxcan : getCandidateList()) {
                                    if (maxcan.getName().length() > longestcan) {
                                        longestcan = maxcan.getName().length();
                                    }
                                }
                                for (int j = 0; j < (1 + roundsList.size()); j++) {
                                    if (j == 0) {
                                        outputTable.append("       Candidates");
                                        if (longestcan > 18) {
                                            for (int a = 0; a < (longestcan - 18) + 1; a++) {
                                                outputTable.append(" ");
                                            }
                                            outputTable.append("| ");
                                        } else {
                                            for (int a = 0; a < (18 - longestcan) + 1; a++) {
                                                outputTable.append(" ");
                                            }
                                            outputTable.append("| ");
                                        }
                                    } else {
                                        outputTable.append("  Round " + (j) + "   | ");
                                    }
                                }
                                outputTable.append("\n");
                            } else if (i == 1) {
                                int longestcan = 0;
                                for (Candidate maxcan : getCandidateList()) {
                                    if (maxcan.getName().length() > longestcan) {
                                        longestcan = maxcan.getName().length();
                                    }
                                }
                                for (int x = 0; x < (2 + (2 * roundsList.size())); x++) {
                                    if (x == 0) {
                                        outputTable.append("Candidate");
                                        if (longestcan > 9) {
                                            for (int a = 0; a < (longestcan - 9) + 1; a++) {
                                                outputTable.append(" ");
                                            }
                                            outputTable.append("| ");
                                        } else {
                                            for (int a = 0; a < (9 - longestcan) + 1; a++) {
                                                outputTable.append(" ");
                                            }
                                            outputTable.append("| ");
                                        }
                                    } else if (x == 1) {
                                        outputTable.append("  Party    | ");
                                    } else {
                                        if ((x % 2) == 0) {
                                            outputTable.append("Votes | ");
                                        } else {
                                            outputTable.append("+/- | ");
                                        }
                                    }
                                }
                                outputTable.append("\n");
                            } else {
                                int longestcan = 0;
                                for (Candidate maxcan : getCandidateList()) {
                                    if (maxcan.getName().length() > longestcan) {
                                        longestcan = maxcan.getName().length();
                                    }
                                }
                                for (int x = 0; x < (2 + (2 * roundsList.size())); x++) {
                                    if (x == 0) {
                                        outputTable.append(roundsList.get(0).get(currentCan).getName());
                                        for (int a = 0; a < (longestcan - roundsList.get(0).get(currentCan).getName().length()) + 1; a++) {
                                            outputTable.append(" ");
                                        }
                                        outputTable.append("| ");
                                    } else if (x == 1) {
                                        outputTable.append(roundsList.get(0).get(currentCan).getParty());
                                        String length1 = roundsList.get(0).get(currentCan).getParty() + "";
                                        if (length1.length() > 11) {
                                            for (int a = 0; a < (length1.length() - 11); a++) {
                                                outputTable.append(" ");
                                            }
                                            outputTable.append("|  ");
                                        } else {
                                            for (int a = 0; a < 11 - length1.length(); a++) {
                                                outputTable.append(" ");
                                            }
                                            outputTable.append("|  ");
                                        }
                                    } else {
                                        if ((x % 2) == 0) {
                                            outputTable.append(" " + roundsList.get((x - 2) / 2).get(currentCan).getNumberOfVotes() + "   | ");
                                        } else {
                                            if (currentRound == 0) { // first round
                                                outputTable.append(" +" + roundsList.get((x - 2) / 2).get(currentCan).getNumberOfVotes() + "  | ");
                                                exhaust.add(roundsList.get((x - 2) / 2).get(currentCan).getNumberOfVotes());
                                                currentRound++;
                                            } else { // 2nd round and on
                                                if (currentRound <= removedList.size() && roundsList.get((x - 2) / 2).get(currentCan).getName().equals(removedList.get(currentRound - 1).getName())) { // loser of last round
                                                    outputTable.append(" -" + roundsList.get(((x - 2) / 2) - 1).get(currentCan).getNumberOfVotes() + "  | ");
                                                    exhaust.add(-1 * roundsList.get(((x - 2) / 2) - 1).get(currentCan).getNumberOfVotes());
                                                    currentRound++;
                                                } else {
                                                    outputTable.append(" +" + (roundsList.get((x - 2) / 2).get(currentCan).getNumberOfVotes() - roundsList.get(((x - 2) / 2) - 1).get(currentCan).getNumberOfVotes()) + "  | ");
                                                    int value = roundsList.get((x - 2) / 2).get(currentCan).getNumberOfVotes() - roundsList.get(((x - 2) / 2) - 1).get(currentCan).getNumberOfVotes();
                                                    exhaust.add(value);
                                                    currentRound++;
                                                }
                                            }

                                        }
                                    }
                                }
                                currentRound = 0;
                                currentCan++;
                                outputTable.append("\n");
                                //System.out.println(currentCan);

                            }
                        }// total num of ints divided by num canadits
                        outputTable.append("EXHAUSTED PILE           |  ");
                        int rounds = exhaust.size() / getNumCandidates();
                        int sum = 0;
                        for (int i = 0; i < rounds; i++) {
                            int pile = 0;
                            for (int j = 0; j < exhaust.size(); j++) {
                                if ((j + rounds) % rounds == i) {
                                    pile = exhaust.get(j) + pile;

                                }
                            }
                            if (i == 0) {
                                pile = 0;
                            }
                            sum = sum + pile;
                            outputTable.append(Math.abs(sum) + "   |   ");
                            outputTable.append("+" + Math.abs(pile) + "  | ");
                        }
                        outputTable.append("\n");
                        outputTable.append("Total | " + getNumBallots());
                        System.out.println(outputTable);
                        return;
                    } else if (candidate.getNumberOfVotes() < minVotes) {
                        losingCandidate = candidate;
                        minVotes = candidate.getNumberOfVotes();
                    }

                    candidate.resetNumberOfVotes();
                }
            }

            removedList.add(losingCandidate);
            auditFileBuilder.append(losingCandidate.getName() + " is eliminated! They only had " + minVotes + " first place votes!\n");
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
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        InstantRunoffElection that = (InstantRunoffElection) o;
        return this.getCandidateList().equals(that.getCandidateList())
                && this.getNumCandidates() == that.getNumCandidates()
                && this.getNumBallots() == that.getNumBallots()
                && this.ballotList.equals(that.ballotList);
    }

    /**
     * @return String representing this instance of an InstantRunoffElection object.
     */
    @Override
    public String toString() {
        return "InstantRunoffElection{" +
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

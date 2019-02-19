package voting_system.election;

import voting_system.Candidate;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing an election.
 * By: Michael Birk, John Caspers, Mukesh Pragaram Choudhary, Daniel Rockcastle
 */
abstract class Election {

    private final List<Candidate> candidateList;
    private final int numCandidates; // TODO: do we need this? it's the same as candidateList.size()
    private final int numBallots; // TODO: or this?

    Election() {
        this.candidateList = new ArrayList<>();
        this.numCandidates = 0;
        this.numBallots = 0;
    }

    /**
     * Constructor.
     *
     * @param candidateList A list of Candidate objects
     * @param numCandidates The number of candidates
     * @param numBallots    The number of ballots
     */
    Election(List<Candidate> candidateList, int numCandidates, int numBallots) {
        this.candidateList = candidateList;
        this.numCandidates = numCandidates;
        this.numBallots = numBallots;
    }

    public List<Candidate> getCandidateList() {
        return candidateList;
    }

    public int getNumCandidates() {
        return numCandidates;
    }

    public int getNumBallots() {
        return numBallots;
    }

    public abstract void runElection(Path outputPath);

}

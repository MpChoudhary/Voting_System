package voting_system.vote;

import voting_system.Candidate;

/**
 * An individual instant runoff vote that is contained on a Ballot.
 * By: Michael Birk, John Caspers, Mukesh Pragaram Choudhary, Daniel Rockcastle
 */
public class InstantRunoffVote extends Vote {

    private final int rank;

    public InstantRunoffVote() {
        super();
        rank = 0;
    } // unused default constructor

    /**
     * Constructor.
     *
     * @param candidate A Candidate object.
     * @param rank      The Candidate's rank on the instant runoff ballot.
     */
    public InstantRunoffVote(Candidate candidate, int rank) {
        super(candidate);
        this.rank = rank;
    }

    /**
     * Checks equality between two objects by comparing candidate and rank.
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

        InstantRunoffVote that = (InstantRunoffVote) o;
        return this.getCandidate().equals(that.getCandidate())
                && this.rank == that.rank;
    }

    @Override
    public String toString() {
        return "InstantRunoffVote{" +
                "candidate=" + getCandidate() +
                "rank=" + rank +
                '}';
    }

    public int getRank() {
        return rank;
    }

}

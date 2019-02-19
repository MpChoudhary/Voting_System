package voting_system.vote;

import voting_system.Candidate;

/**
 * An individual open party list vote that is contained on a Ballot.
 * By: Michael Birk, John Caspers, Mukesh Pragaram Choudhary, Daniel Rockcastle
 */
public class OpenPartyListVote extends Vote {

    public OpenPartyListVote() {
        super();
    } // unused default constructor

    /**
     * Constructor.
     *
     * @param candidate A Candidate object.
     */
    public OpenPartyListVote(Candidate candidate) {
        super(candidate);
    }

    /**
     * Checks equality between two objects by comparing candidate.
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

        OpenPartyListVote that = (OpenPartyListVote) o;
        return this.getCandidate().equals(that.getCandidate());
    }

    @Override
    public String toString() {
        return "InstantRunoffVote{" +
                "candidate=" + getCandidate() +
                '}';
    }

}

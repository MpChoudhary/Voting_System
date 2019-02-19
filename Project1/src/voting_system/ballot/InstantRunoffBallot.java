package voting_system.ballot;

import voting_system.vote.InstantRunoffVote;

import java.util.ArrayList;
import java.util.List;

/**
 * An individual ballot for an instant runoff election. Contains a list of InstantRunoffVote objects.
 * By: Michael Birk, John Caspers, Mukesh Pragaram Choudhary, Daniel Rockcastle
 */
public class InstantRunoffBallot implements Ballot {

    private final List<InstantRunoffVote> voteList;

    public InstantRunoffBallot() {
        this.voteList = new ArrayList<>();
    } // unused default constructor

    /**
     * Constructor.
     *
     * @param voteList A list of InstantRunoffVote objects contained on an instant runoff ballot.
     */
    public InstantRunoffBallot(List<InstantRunoffVote> voteList) {
        this.voteList = voteList;
    }

    /**
     * Checks equality between two objects by comparing list of votes.
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

        InstantRunoffBallot that = (InstantRunoffBallot) o;
        return this.voteList.equals(that.voteList);
    }

    @Override
    public String toString() {
        return "InstantRunoffBallot{" +
                "voteList=" + voteList +
                '}';
    }

    public List<InstantRunoffVote> getVoteList() {
        return voteList;
    }

}

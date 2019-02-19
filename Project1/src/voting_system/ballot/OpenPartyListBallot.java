package voting_system.ballot;

import voting_system.vote.OpenPartyListVote;

import java.util.ArrayList;
import java.util.List;

/**
 * An individual ballot for an instant runoff election. Contains a list of OpenPartyListVote objects.
 * By: Michael Birk, John Caspers, Mukesh Pragaram Choudhary, Daniel Rockcastle
 */
public class OpenPartyListBallot implements Ballot {

    private final List<OpenPartyListVote> voteList;

    public OpenPartyListBallot() {
        this.voteList = new ArrayList<>();
    } // unused default constructor

    /**
     * Constructor.
     *
     * @param voteList A list of OpenPartyListVote objects contained on an open party list ballot.
     */
    public OpenPartyListBallot(List<OpenPartyListVote> voteList) {
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

        OpenPartyListBallot that = (OpenPartyListBallot) o;
        return this.voteList.equals(that.voteList);
    }

    @Override
    public String toString() {
        return "InstantRunoffBallot{" +
                "voteList=" + voteList +
                '}';
    }

    public List<OpenPartyListVote> getVoteList() {
        return voteList;
    }

}

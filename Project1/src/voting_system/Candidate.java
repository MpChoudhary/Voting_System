package voting_system;

/**
 * A class to represent a candidate in an election.
 * By: Michael Birk, John Caspers, Mukesh Pragaram Choudhary, Daniel Rockcastle
 */
public class Candidate {

    private final String name;
    private final Party party;
    private int numberOfVotes;

    public Candidate() {
        name = "";
        party = Party.INDEPENDENT;
        numberOfVotes = 0;
    } // unused default constructor

    /**
     * Constructor.
     *
     * @param name          The candidate's name.
     * @param party         The candidate's political party.
     * @param numberOfVotes The candidate's number of votes thus far.
     */
    public Candidate(String name, Party party, int numberOfVotes) {
        this.name = name;
        this.party = party;
        this.numberOfVotes = numberOfVotes; // TODO: do we need this? shouldn't it always be 0?
    }

    public String getName() {
        return name;
    }

    public Party getParty() {
        return party;
    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    /**
     * Iterates the candidate's number of votes by one.
     */
    public void addVote() {
        numberOfVotes++;
    }

    /**
     * Resets the number of votes to 0.
     */
    public void resetNumberOfVotes() {
        numberOfVotes = 0;
    }

    /**
     * Checks equality between two objects by comparing name, party, and number of votes.
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

        Candidate that = (Candidate) o;
        return this.name.equals(that.name)
                && this.party == that.party
                && this.numberOfVotes == that.numberOfVotes;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "name='" + name + '\'' +
                ", party=" + party +
                ", numberOfVotes=" + numberOfVotes +
                '}';
    }

}

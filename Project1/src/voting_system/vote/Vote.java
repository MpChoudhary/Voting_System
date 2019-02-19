package voting_system.vote;

import voting_system.Candidate;

/**
 * Abstract class for a Vote object.
 * By: Michael Birk, John Caspers, Mukesh Pragaram Choudhary, Daniel Rockcastle
 */
abstract class Vote {

    private final Candidate candidate;

    Vote() {
        candidate = new Candidate();
    }

    /**
     * Constructor.
     *
     * @param candidate A Candidate object.
     */
    Vote(Candidate candidate) {
        this.candidate = candidate;
    }

    public Candidate getCandidate() {
        return candidate;
    }

}

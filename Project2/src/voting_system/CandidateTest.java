package voting_system;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CandidateTest {

    public static Candidate createDefaultCandidate() {
        return new Candidate();
    }

    public static Candidate createCandidate(String name, Party party) {
        return new Candidate(name, party, 0);
    }

    @Test
    public void testDefaultConstructor() {
        String name = "";
        Party party = Party.INDEPENDENT;
        Candidate candidate = createDefaultCandidate();
        assertEquals(name, candidate.getName());
        assertEquals(party.name(), candidate.getParty().name());
        assertEquals(0, candidate.getNumberOfVotes());
    }

    @Test
    public void testConstructor() {
        String name = "Can1";
        Party party = Party.DEMOCRAT;
        Candidate candidate = createCandidate(name, party);
        assertEquals(name, candidate.getName());
        assertEquals(party.name(), candidate.getParty().name());
        assertEquals(0, candidate.getNumberOfVotes());
    }

    @Test
    public void testGetName() {
        String name = "Can1";
        Candidate candidate = createCandidate(name, Party.DEMOCRAT);
        assertEquals(name, candidate.getName());
    }

    @Test
    public void testGetParty() {
        Party party = Party.DEMOCRAT;
        Candidate candidate = createCandidate("Can1", party);
        assertEquals(party.name(), candidate.getParty().name());
    }

    @Test
    public void testGetNumberOfVotes() {
        Candidate candidate = createCandidate("Can1", Party.DEMOCRAT);
        assertEquals(0, candidate.getNumberOfVotes());
    }

    @Test
    public void testAddVote() {
        Candidate candidate = createCandidate("Can1", Party.DEMOCRAT);
        candidate.addVote();
        candidate.addVote();
        candidate.addVote();
        candidate.addVote();
        assertEquals(4, candidate.getNumberOfVotes());
    }

    @Test
    public void testEquals() {
        Candidate candidate1 = new Candidate("Can1", Party.DEMOCRAT, 0);
        Candidate candidate2 = new Candidate("Can1", Party.DEMOCRAT, 0);
        Candidate candidate3 = new Candidate("Can2", Party.DEMOCRAT, 0);
        Candidate candidate4 = new Candidate("Can1", Party.REPUBLICAN, 0);
        Candidate candidate5 = new Candidate("Can1", Party.DEMOCRAT, 1);
        assertEquals(candidate1, candidate1);
        assertEquals(candidate1, candidate2);
        assertNotEquals(candidate1, candidate3);
        assertNotEquals(candidate1, candidate4);
        assertNotEquals(candidate1, candidate5);
    }

}

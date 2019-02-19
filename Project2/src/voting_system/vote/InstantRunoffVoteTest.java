package voting_system.vote;

import org.junit.jupiter.api.Test;
import voting_system.CandidateTest;
import voting_system.Party;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class InstantRunoffVoteTest {

    public static InstantRunoffVote createDefaultInstantRunoffVote() {
        return new InstantRunoffVote();
    }

    public static InstantRunoffVote createInstantRunoffVote(String name, Party party, int rank) {
        return new InstantRunoffVote(CandidateTest.createCandidate(name, party), rank);
    }

    @Test
    public void testDefaultConstructor() {
        String name = "";
        Party party = Party.INDEPENDENT;
        InstantRunoffVote vote = createDefaultInstantRunoffVote();
        assertEquals(CandidateTest.createCandidate(name, party), vote.getCandidate());
        assertEquals(0, vote.getRank());
    }

    @Test
    public void testConstructor() {
        String name = "Can1";
        Party party = Party.DEMOCRAT;
        InstantRunoffVote vote = createInstantRunoffVote(name, party, 1);
        assertEquals(CandidateTest.createCandidate(name, party), vote.getCandidate());
        assertEquals(1, vote.getRank());
    }

    @Test
    public void testGetCandidate() {
        String name = "Can1";
        Party party = Party.DEMOCRAT;
        InstantRunoffVote vote = createInstantRunoffVote(name, party, 1);
        assertEquals(CandidateTest.createCandidate(name, party), vote.getCandidate());
    }

    @Test
    public void testGetRank() {
        String name = "Can1";
        Party party = Party.DEMOCRAT;
        InstantRunoffVote vote = createInstantRunoffVote(name, party, 1);
        assertEquals(1, vote.getRank());
    }

    @Test
    public void testEquals() {
        InstantRunoffVote vote1 = createInstantRunoffVote("Can1", Party.DEMOCRAT, 1);
        InstantRunoffVote vote2 = createInstantRunoffVote("Can1", Party.DEMOCRAT, 1);
        InstantRunoffVote vote3 = createInstantRunoffVote("Can2", Party.DEMOCRAT, 1);
        InstantRunoffVote vote4 = createInstantRunoffVote("Can1", Party.REPUBLICAN, 1);
        InstantRunoffVote vote5 = createInstantRunoffVote("Can1", Party.DEMOCRAT, 2);
        assertEquals(vote1, vote1);
        assertEquals(vote1, vote2);
        assertNotEquals(vote1, vote3);
        assertNotEquals(vote1, vote4);
        assertNotEquals(vote1, vote5);
    }

}

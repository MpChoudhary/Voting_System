package voting_system.vote;

import org.junit.jupiter.api.Test;
import voting_system.CandidateTest;
import voting_system.Party;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class OpenPartyListVoteTest {

    public static OpenPartyListVote createDefaultOpenPartyListVote() {
        return new OpenPartyListVote();
    }

    public static OpenPartyListVote createOpenPartyListVote(String name, Party party) {
        return new OpenPartyListVote(CandidateTest.createCandidate(name, party));
    }

    @Test
    public void testDefaultConstructor() {
        String name = "";
        Party party = Party.INDEPENDENT;
        OpenPartyListVote vote = createDefaultOpenPartyListVote();
        assertEquals(CandidateTest.createCandidate(name, party), vote.getCandidate());
    }

    @Test
    public void testConstructor() {
        String name = "Can1";
        Party party = Party.DEMOCRAT;
        OpenPartyListVote vote = createOpenPartyListVote(name, party);
        assertEquals(CandidateTest.createCandidate(name, party), vote.getCandidate());
    }

    @Test
    public void testGetCandidate() {
        String name = "Can1";
        Party party = Party.DEMOCRAT;
        OpenPartyListVote vote = createOpenPartyListVote(name, party);
        assertEquals(CandidateTest.createCandidate(name, party), vote.getCandidate());
    }

    @Test
    public void testEquals() {
        OpenPartyListVote vote1 = createOpenPartyListVote("Can1", Party.DEMOCRAT);
        OpenPartyListVote vote2 = createOpenPartyListVote("Can1", Party.DEMOCRAT);
        OpenPartyListVote vote3 = createOpenPartyListVote("Can2", Party.DEMOCRAT);
        OpenPartyListVote vote4 = createOpenPartyListVote("Can1", Party.REPUBLICAN);
        assertEquals(vote1, vote1);
        assertEquals(vote1, vote2);
        assertNotEquals(vote1, vote3);
        assertNotEquals(vote1, vote4);
    }

}

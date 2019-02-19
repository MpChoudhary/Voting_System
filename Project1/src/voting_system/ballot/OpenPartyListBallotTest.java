package voting_system.ballot;

import org.junit.jupiter.api.Test;
import voting_system.Party;
import voting_system.vote.OpenPartyListVote;
import voting_system.vote.OpenPartyListVoteTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class OpenPartyListBallotTest {

    public static ArrayList<OpenPartyListVote> createVoteList(int candidateNum, Party party) {
        ArrayList<OpenPartyListVote> temp = new ArrayList<>();
        temp.add(OpenPartyListVoteTest.createOpenPartyListVote("Can" + candidateNum, party));
        return temp;
    }

    public static OpenPartyListBallot createDefaultOpenPartyListBallot() {
        return new OpenPartyListBallot();
    }

    public static OpenPartyListBallot createOpenPartyListBallot(int candidateNum, Party party) {
        return new OpenPartyListBallot(OpenPartyListBallotTest.createVoteList(candidateNum, party));
    }

    @Test
    public void testDefaultConstructor() {
        OpenPartyListBallot ballot = createDefaultOpenPartyListBallot();
        assertEquals(new ArrayList<>(), ballot.getVoteList());
    }

    @Test
    public void testConstructor() {
        int candidateNum = 1;
        Party party = Party.DEMOCRAT;
        OpenPartyListBallot ballot = createOpenPartyListBallot(candidateNum, party);
        assertEquals(createVoteList(candidateNum, party), ballot.getVoteList());
    }


    @Test
    public void testGetVoteList() {
        int candidateNum = 1;
        Party party = Party.DEMOCRAT;
        OpenPartyListBallot ballot = createOpenPartyListBallot(candidateNum, party);
        assertEquals(createVoteList(candidateNum, party), ballot.getVoteList());
    }

    @Test
    public void testEquals() {
        OpenPartyListBallot ballot1 = createOpenPartyListBallot(1, Party.DEMOCRAT);
        OpenPartyListBallot ballot2 = createOpenPartyListBallot(1, Party.DEMOCRAT);
        OpenPartyListBallot ballot3 = createOpenPartyListBallot(2, Party.DEMOCRAT);
        OpenPartyListBallot ballot4 = createOpenPartyListBallot(1, Party.REPUBLICAN);
        assertEquals(ballot1, ballot1);
        assertEquals(ballot1, ballot2);
        assertNotEquals(ballot1, ballot3);
        assertNotEquals(ballot1, ballot4);
    }

}

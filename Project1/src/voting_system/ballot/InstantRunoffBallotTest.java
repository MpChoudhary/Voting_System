package voting_system.ballot;

import org.junit.jupiter.api.Test;
import voting_system.CandidateTest;
import voting_system.Party;
import voting_system.vote.InstantRunoffVote;
import voting_system.vote.InstantRunoffVoteTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class InstantRunoffBallotTest {

    public static ArrayList<InstantRunoffVote> createVoteList() {
        ArrayList<InstantRunoffVote> voteList = new ArrayList<>();
        int i = 0;
        for (; i < 3; i++) {
            voteList.add(InstantRunoffVoteTest.createInstantRunoffVote("Can" + i, Party.DEMOCRAT, i + 1));
        }
        for (; i < 6; i++) {
            voteList.add(InstantRunoffVoteTest.createInstantRunoffVote("Can" + i, Party.REPUBLICAN, i + 1));
        }
        for (; i < 8; i++) {
            voteList.add(InstantRunoffVoteTest.createInstantRunoffVote("Can" + i, Party.INDEPENDENT, i + 1));
        }
        return voteList;
    }

    public static InstantRunoffBallot createDefaultInstantRunoffBallot() {
        return new InstantRunoffBallot();
    }

    public static InstantRunoffBallot createInstantRunoffBallot() {
        return new InstantRunoffBallot(InstantRunoffBallotTest.createVoteList());
    }

    @Test
    public void testDefaultConstructor() {
        InstantRunoffBallot ballot = createDefaultInstantRunoffBallot();
        assertEquals(new ArrayList<>(), ballot.getVoteList());
    }

    // TODO: these next 2 are basically the same tests, do we need both?
    @Test
    public void testConstructor() {
        InstantRunoffBallot ballot = createInstantRunoffBallot();
        assertEquals(createVoteList(), ballot.getVoteList());
    }

    @Test
    public void testGetVoteList() {
        InstantRunoffBallot ballot = createInstantRunoffBallot();
        assertEquals(createVoteList(), ballot.getVoteList());
    }

    @Test
    public void testEquals() {
        InstantRunoffBallot ballot1 = createInstantRunoffBallot();
        InstantRunoffBallot ballot2 = createInstantRunoffBallot();
        ArrayList<InstantRunoffVote> temp = new ArrayList<>();
        temp.add(new InstantRunoffVote(CandidateTest.createCandidate("Can1", Party.DEMOCRAT), 1));
        InstantRunoffBallot ballot3 = new InstantRunoffBallot(temp);
        assertEquals(ballot1, ballot1);
        assertEquals(ballot1, ballot2);
        assertNotEquals(ballot1, ballot3);
    }

}

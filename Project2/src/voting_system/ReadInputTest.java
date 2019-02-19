package voting_system;

import org.junit.jupiter.api.Test;
import voting_system.ballot.InstantRunoffBallot;
import voting_system.ballot.OpenPartyListBallot;
import voting_system.election.InstantRunoffElection;
import voting_system.election.OpenPartyListElection;
import voting_system.vote.InstantRunoffVote;
import voting_system.vote.OpenPartyListVote;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ReadInputTest {

    private static List<String> makeOPLFileLines() {
        return new ArrayList<>(Arrays.asList(
                "OPL",
                "6",
                "[Pike,D],[Foster,D],[Deutsch,R],[Borg,R],[Jones,R],[Smith,I]",
                "3",
                "9",
                "1,,,,,",
                "1,,,,,",
                ",1,,,,",
                ",,,,1,",
                ",,,,,1",
                ",,,1,,",
                ",,,1,,",
                "1,,,,,",
                ",1,,,,"));
    }

    private static List<String> makeIRVFileLines() {
        return new ArrayList<>(Arrays.asList(
                "IR",
                "4",
                "Rosen (D),Kleinberg (R),Chou (I),Royce (L)",
                "6",
                "1,3,4,2",
                "1,,2,",
                "1,2,3,",
                "3,2,1,4",
                ",,1,2",
                ",,,1"));
    }

    private static List<String> makeIRVFileLinesNoRemove() {
        return new ArrayList<>(Arrays.asList(
                "IR",
                "4",
                "Rosen (D),Kleinberg (R),Chou (I),Royce (L)",
                "5",
                "1,3,4,2",
                "1,,2,",
                "1,2,3,",
                "3,2,1,4",
                ",,1,2"));
    }

    @Test
    public void testReadInputFile() {
        assertEquals(makeOPLFileLines(), ReadInput.readInputFile(Paths.get("testing/testOPLInput.csv")));
    }

    @Test
    public void testParseInputLinesIRVRemoveBallot() {
        Candidate can1 = new Candidate("Rosen (D)", null, 0);
        Candidate can2 = new Candidate("Kleinberg (R)", null, 0);
        Candidate can3 = new Candidate("Chou (I)", null, 0);
        Candidate can4 = new Candidate("Royce (L)", null, 0);

        List<Candidate> expectedCandidates = new ArrayList<>(Arrays.asList(can1, can2, can3, can4));
        List<InstantRunoffBallot> expectedBallots = new ArrayList<>(Arrays.asList(
                new InstantRunoffBallot(new ArrayList<>(Arrays.asList(
                        new InstantRunoffVote(can1, 1),
                        new InstantRunoffVote(can2, 3),
                        new InstantRunoffVote(can3, 4),
                        new InstantRunoffVote(can4, 2)
                ))),
                new InstantRunoffBallot(new ArrayList<>(Arrays.asList(
                        new InstantRunoffVote(can1, 1),
                        new InstantRunoffVote(can3, 2)
                ))),
                new InstantRunoffBallot(new ArrayList<>(Arrays.asList(
                        new InstantRunoffVote(can1, 1),
                        new InstantRunoffVote(can2, 2),
                        new InstantRunoffVote(can3, 3)
                ))),
                new InstantRunoffBallot(new ArrayList<>(Arrays.asList(
                        new InstantRunoffVote(can1, 3),
                        new InstantRunoffVote(can2, 2),
                        new InstantRunoffVote(can3, 1),
                        new InstantRunoffVote(can4, 4)
                ))),
                new InstantRunoffBallot(new ArrayList<>(Arrays.asList(
                        new InstantRunoffVote(can3, 1),
                        new InstantRunoffVote(can4, 2)
                )))));

        InstantRunoffElection actualElection = ReadInput.parseInputLinesIRV(makeIRVFileLines(), Paths.get(""));
        assertEquals(new InstantRunoffElection(expectedCandidates, expectedBallots, 4, 5),
                actualElection);
        assertNotEquals(new InstantRunoffElection(), actualElection);
    }

    @Test
    public void testParseInputLinesNoRemove() {
        Candidate can1 = new Candidate("Rosen (D)", null, 0);
        Candidate can2 = new Candidate("Kleinberg (R)", null, 0);
        Candidate can3 = new Candidate("Chou (I)", null, 0);
        Candidate can4 = new Candidate("Royce (L)", null, 0);

        List<Candidate> expectedCandidates = new ArrayList<>(Arrays.asList(can1, can2, can3, can4));
        List<InstantRunoffBallot> expectedBallots = new ArrayList<>(Arrays.asList(
                new InstantRunoffBallot(new ArrayList<>(Arrays.asList(
                        new InstantRunoffVote(can1, 1),
                        new InstantRunoffVote(can2, 3),
                        new InstantRunoffVote(can3, 4),
                        new InstantRunoffVote(can4, 2)
                ))),
                new InstantRunoffBallot(new ArrayList<>(Arrays.asList(
                        new InstantRunoffVote(can1, 1),
                        new InstantRunoffVote(can3, 2)
                ))),
                new InstantRunoffBallot(new ArrayList<>(Arrays.asList(
                        new InstantRunoffVote(can1, 1),
                        new InstantRunoffVote(can2, 2),
                        new InstantRunoffVote(can3, 3)
                ))),
                new InstantRunoffBallot(new ArrayList<>(Arrays.asList(
                        new InstantRunoffVote(can1, 3),
                        new InstantRunoffVote(can2, 2),
                        new InstantRunoffVote(can3, 1),
                        new InstantRunoffVote(can4, 4)
                ))),
                new InstantRunoffBallot(new ArrayList<>(Arrays.asList(
                        new InstantRunoffVote(can3, 1),
                        new InstantRunoffVote(can4, 2)
                )))));

        InstantRunoffElection actualElection = ReadInput.parseInputLinesIRV(makeIRVFileLinesNoRemove(), Paths.get(""));
        assertEquals(new InstantRunoffElection(expectedCandidates, expectedBallots, 4, 5),
                actualElection);
        assertNotEquals(new InstantRunoffElection(), actualElection);
    }

    @Test
    public void testParseInputLinesOPL() {
        Candidate can1 = new Candidate("Pike", Party.DEMOCRAT, 0);
        Candidate can2 = new Candidate("Foster", Party.DEMOCRAT, 0);
        Candidate can3 = new Candidate("Deutsch", Party.REPUBLICAN, 0);
        Candidate can4 = new Candidate("Borg", Party.REPUBLICAN, 0);
        Candidate can5 = new Candidate("Jones", Party.REPUBLICAN, 0);
        Candidate can6 = new Candidate("Smith", Party.INDEPENDENT, 0);

        List<Candidate> expectedCandidates = new ArrayList<>(Arrays.asList(can1, can2, can3, can4, can5, can6));
        List<OpenPartyListBallot> expectedBallots = new ArrayList<>(Arrays.asList(
                new OpenPartyListBallot(new ArrayList<>(Collections.singletonList(new OpenPartyListVote(can1)))),
                new OpenPartyListBallot(new ArrayList<>(Collections.singletonList(new OpenPartyListVote(can1)))),
                new OpenPartyListBallot(new ArrayList<>(Collections.singletonList(new OpenPartyListVote(can2)))),
                new OpenPartyListBallot(new ArrayList<>(Collections.singletonList(new OpenPartyListVote(can5)))),
                new OpenPartyListBallot(new ArrayList<>(Collections.singletonList(new OpenPartyListVote(can6)))),
                new OpenPartyListBallot(new ArrayList<>(Collections.singletonList(new OpenPartyListVote(can4)))),
                new OpenPartyListBallot(new ArrayList<>(Collections.singletonList(new OpenPartyListVote(can4)))),
                new OpenPartyListBallot(new ArrayList<>(Collections.singletonList(new OpenPartyListVote(can1)))),
                new OpenPartyListBallot(new ArrayList<>(Collections.singletonList(new OpenPartyListVote(can2))))
        ));

        OpenPartyListElection actualElection = ReadInput.parseInputLinesOPL(makeOPLFileLines());
        assertEquals(new OpenPartyListElection(expectedCandidates, expectedBallots, 6, 9, 3),
                actualElection);
        assertNotEquals(new OpenPartyListElection(), actualElection);
    }

}

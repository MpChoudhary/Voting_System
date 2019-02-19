package voting_system;

import voting_system.ballot.InstantRunoffBallot;
import voting_system.ballot.OpenPartyListBallot;
import voting_system.election.InstantRunoffElection;
import voting_system.election.OpenPartyListElection;
import voting_system.vote.InstantRunoffVote;
import voting_system.vote.OpenPartyListVote;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;

/**
 * Class made for reading input file and parsing it.
 * By: Michael Birk, John Caspers, Mukesh Pragaram Choudhary, Daniel Rockcastle
 */
public class ReadInput {

    private static final Map<String, Party> partyMap = createMap();

    /**
     * Builds a static map that contains the representation of parties in the input file as the key and the Party in our
     * party enum as the value.
     *
     * @return Map of Strings/Parties as represented by the input file and our Party enum. Used for easy adding of party.
     */
    private static Map<String, Party> createMap() {
        Map<String, Party> partyMap = new HashMap<>();
        partyMap.put("D", Party.DEMOCRAT);
        partyMap.put("I", Party.INDEPENDENT);
        partyMap.put("R", Party.REPUBLICAN);
        return partyMap;
    }

    /**
     * Function that reads in an input file and makes a list of its lines.
     *
     * @param fileName This is the name of the input file as inputted from the user.
     * @return A List of strings that represent the lines in the file.
     */
    public static List<String> readInputFile(Path fileName) {
        List<String> fileLines = new ArrayList<>();
        try {
            Files.lines(fileName).forEachOrdered(fileLines::add);
        } catch (IOException fileReadError) {
            System.out.println("Error reading or opening input file " + fileReadError.toString());
            exit(5);
        }
        return fileLines;
    }

    /**
     * This function parses the input file lines according to the outline showed for the Open Party Election.
     *
     * @param inputLines lines of file stored as an ordered list.
     * @return A new OpenPartyListElection object containing the candidates, ballots, number of candidates, number of ballots, and number of seats.
     */
    public static OpenPartyListElection parseInputLinesOPL(List<String> inputLines) {
        //record initial data from the file
        int numCandidates = Integer.parseInt(inputLines.get(1));
        int numBallots = Integer.parseInt(inputLines.get(4));
        int numSeats = Integer.parseInt(inputLines.get(3));

        // remove all brackets and split the string by commas
        String[] candidatesAndParties = inputLines.get(2)
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .split(",");
        List<Candidate> candidateList = new ArrayList<>();
        String candidateName = "";

        // create a new candidate for every 2 values in our CSV, first being the name and second being the party
        for (int i = 0; i < candidatesAndParties.length; i++) {
            if (i % 2 == 0) {
                candidateName = candidatesAndParties[i];
            } else {
                candidateList.add(new Candidate(candidateName, partyMap.get(candidatesAndParties[i]), 0));
            }
        }
        List<OpenPartyListBallot> ballotList = new ArrayList<>();

        // record votes for each candidate to a ballot
        for (int i = 5; i < 5 + numBallots; i++) {
            String[] voteList = inputLines.get(i).split(",");
            List<OpenPartyListVote> OPLVoteList = new ArrayList<>();
            for (int j = 0; j < voteList.length; j++) {
                if (!voteList[j].equals("")) {
                    OPLVoteList.add(new OpenPartyListVote(candidateList.get(j)));
                }
            }
            ballotList.add(new OpenPartyListBallot(OPLVoteList));
        }
        return new OpenPartyListElection(candidateList, ballotList, numCandidates, numBallots, numSeats);
    }

    /**
     * This function parses the input file lines according to the outline showed for the Instant Runoff Voting.
     *
     * @param inputLines lines of input file stored as an ordered list.
     * @return A new InstantRunoffElection object containing the candidates, ballots, number of candidates, and number of ballots.
     */
    public static InstantRunoffElection parseInputLinesIRV(List<String> inputLines, Path outputPath) {
        // record initial values from the input file
        int numCandidates = Integer.parseInt(inputLines.get(1));
        int numBallots = Integer.parseInt(inputLines.get(3));
        int numInvalidBallots = 0;

        // split the file by commas
        // String[] candidates = inputLines.get(2).split(",");
        // List<Candidate> candidateList = new ArrayList<>();
        StringBuilder invalidBallots = new StringBuilder();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.now();
        Path outputFile = Paths.get(outputPath.toString(), "invalidated_" + dateFormat.format(localDate));

        String[] candidatesAndParties = inputLines.get(2)
                .replaceAll("\\(", ",")
                .replaceAll("\\)", "")
                .split(",");

        String candidateName = "";
        //split the file by commas.
        //String[] candidates = inputLines.get(2).split(",");
        List<Candidate> candidateList = new ArrayList<>();
        //add all candidates to our candidate list. We don't care about party, so just call it null.
        // for (String candidate : candidates) {
        //     candidateList.add(new Candidate(candidate, null, 0));
        // }
        for (int i = 0; i < candidatesAndParties.length; i++) {
            if (i % 2 == 0) {
                candidateName = candidatesAndParties[i];
            } else {
                if (partyMap.get(candidatesAndParties[i]) == null) {
                    candidateList.add(new Candidate(candidateName, partyMap.get("I"), 0));
                } else {
                    candidateList.add(new Candidate(candidateName, partyMap.get(candidatesAndParties[i]), 0));
                }
            }
        }

        List<InstantRunoffBallot> ballotList = new ArrayList<>();

        // for all ballots, add the ranks of each respective candidate to it
        for (int i = 4; i < 4 + numBallots; i++) {
            String[] voteList = inputLines.get(i).split(",");
            List<InstantRunoffVote> IRVVoteList = new ArrayList<>();
            for (int j = 0; j < voteList.length; j++) {
                if (!voteList[j].equals("")) {
                    IRVVoteList.add(new InstantRunoffVote(candidateList.get(j), Integer.parseInt(voteList[j])));
                }
            }
            if (!(IRVVoteList.size() < .5 * numCandidates)) {
                ballotList.add(new InstantRunoffBallot(IRVVoteList));
            } else {
                invalidBallots.append("Ballot invalidated with votes " + inputLines.get(i) + "\n");
                numInvalidBallots++;
            }
        }
        try {
            Files.write(outputFile, Collections.singletonList(invalidBallots.toString()), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE);
        } catch (IOException fileWriteFailure) {
            System.out.println("File write failure" + fileWriteFailure.toString());
            exit(6);
        }
        numBallots -= numInvalidBallots;
        return new InstantRunoffElection(candidateList, ballotList, numCandidates, numBallots);
    }

}

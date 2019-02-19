package voting_system;

import voting_system.election.InstantRunoffElection;
import voting_system.election.OpenPartyListElection;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Main driver class that is used to call the run election and grab input from the user.
 * By: Michael Birk, John Caspers, Mukesh Pragaram Choudhary, Daniel Rockcastle
 */
public class Driver {

    /**
     * The main method of the driver class.
     * Grabs filename for input and output directory from user via command line.
     * Also detects what type of voting is used and calls respective function.
     *
     * @param args Any command line arguments. They don't do anything as of now.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please type the path to your input file:");
        Path inputPath = Paths.get(scanner.nextLine());

        System.out.println("Please type the path to your output directory:");
        Path outputPath = Paths.get(scanner.nextLine());
        scanner.close();

        List<String> inputLines = ReadInput.readInputFile(inputPath);
        if (inputLines != null) {
            // detect OPL versus IRV. if OPL, run OPL. else run IRV.
            if (inputLines.get(0).equals("OPL")) {
                OpenPartyListElection OPLElection = ReadInput.parseInputLinesOPL(inputLines);
                OPLElection.runElection(outputPath);
            } else {
                InstantRunoffElection IRVElection = ReadInput.parseInputLinesIRV(inputLines, outputPath);
                IRVElection.runElection(outputPath);
            }
        }
    }

}

package voting_system;

import voting_system.election.InstantRunoffElection;
import voting_system.election.OpenPartyListElection;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.io.File;
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
     * The main method of the driver class. Grabs filename for input and output directory. Also detects what type of voting is used and calls respective function.
     *
     * @param args Command line arguments. This should be either blank or a path to an election input file.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Path inputPath = Paths.get("");
        Path outputPath = Paths.get("");
        System.out.println("Would you like to use the command line or a GUI?");
        int setFlag = 0;
        while (true) {
            System.out.println("Type one: cmd, gui");
            switch (scanner.nextLine()) {
                case "cmd":
                    System.out.println("Please provide the path of your election file:");
                    if (args.length <= 0) {
                        inputPath = Paths.get(scanner.nextLine());
                    } else {
                        inputPath = Paths.get(args[0]);
                        System.out.println("Input file taken from command line argument\n");
                    }
                    System.out.println("Please provide the path of the directory you'd like your audit file to be placed in:");
                    outputPath = Paths.get(scanner.nextLine());
                    setFlag = 1;
                    break;
                case "gui":
                    System.out.println("Type fe for file explorer or sp to type in a path:");
                    switch (scanner.nextLine()) {
                        case "sp":
                            JTextField input = new JTextField();
                            JTextField output = new JTextField();
                            JPanel panel = new JPanel();
                            panel.setLayout(new GridLayout(5, 0, 0, 0));
                            panel.add(new JLabel("Please provide the path of your election file.\n"));
                            panel.add(input);
                            panel.add(new JLabel("Please provide the path of the directory you'd like your audit file to be placed in.\n"));
                            panel.add(output);
                            int result = JOptionPane.showConfirmDialog(
                                    null,
                                    panel,
                                    "Election Input and Output",
                                    JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE);
                            if (result == JOptionPane.OK_OPTION) {
                                inputPath = Paths.get(input.getText());
                                outputPath = Paths.get(output.getText());
                                setFlag = 1;
                                break;
                            } else {
                                System.out.println("Input canceled, please try again or type ^C to exit.");
                                continue;
                            }
                        case "fe":
                            System.out.println("Please select your input file. Note that your output file will be saved in the same folder your input file is found in.");
                            FileDialog getInputFile = new FileDialog(new JFrame());
                            getInputFile.setVisible(true);
                            if (getInputFile.getFiles().length != 0) {
                                File inputFile = getInputFile.getFiles()[0];
                                inputPath = Paths.get(inputFile.getAbsolutePath());
                                outputPath = Paths.get(inputFile.getParent());
                                setFlag = 1;
                                break;
                            } else {
                                System.out.println("Input canceled, please try again or type ^C to exit.");
                                continue;
                            }
                    }
                default:
                    if (setFlag != 1) {
                        System.out.println("Invalid input, please try again or type ^C to exit.");
                    }
            }
            if (setFlag == 1) {
                break;
            }
        }
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
        System.exit(0);
    }

}

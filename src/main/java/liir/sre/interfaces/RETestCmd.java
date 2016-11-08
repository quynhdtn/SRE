package liir.sre.interfaces;
import liir.sre.regex.RE;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by quynhdo on 08/11/16.
 * An interface to test the code
 *
 */
public class RETestCmd {
    /***
     * Test matching the data in a given file
     * @param in_path path to input file
     * @param out_path path to write output
     * @throws IOException
     */
    private static void testFile(String in_path, String out_path) throws IOException {
        HashMap<String,RE> matchers = new HashMap<>();
        ArrayList<String> output_strings= new ArrayList<>();
        Files.lines(Paths.get(in_path)).forEach(s->{
            String[] temps = s.split("\\s+");
            String re = temps[0];
            if (temps.length>=2){
                for (int i=1;i<temps.length;i++) {

                    String str = temps[i];
                    RE myr = null;

                    if (!matchers.containsKey(re)) {
                        myr = new RE(re);
                        matchers.put(re, myr);
                    } else
                        myr = matchers.get(re);

                    try {
                        boolean rs = myr.matches(str);
                        output_strings.add(re + " " + str + " " + String.valueOf(rs));

                    } catch (Exception e) {
                        System.out.println("There is an Invalid regular expression: "+re);

                    }
                }

            }


        });

        Files.write(Paths.get(out_path), output_strings);
    }
    public static void main(String[] args) throws IOException {

        CommandLine commandLine;
        Option option_re = OptionBuilder.withArgName("r").hasArg().withDescription("Regular expression").create("r");
        Option option_str= OptionBuilder.withArgName("s").hasArg().withDescription("Test string").create("s");
        Option option_inputfile = OptionBuilder.withArgName("f").hasArg().withDescription("Test file").create("f");
        Option option_outputfile = OptionBuilder.withArgName("o").hasArg().withDescription("Output file").create("o");
        Option option_help = new Option("help", "The help option");
        Options options = new Options();
        CommandLineParser parser = new GnuParser();
        String help_str = "java liir.sre.interfaces.RETestCmd  -r RegularExpression -s TestString \n" +
                "java liir.sre.interfaces.RETestCmd  -f TestFile -o Outfile";



        options.addOption(option_re);
        options.addOption(option_str);
        options.addOption(option_inputfile);
        options.addOption(option_outputfile);
        options.addOption(option_help);

        try
        {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption("r")&&(commandLine.hasOption("s")))
            {

                try {
                    boolean rs = RE.matches(commandLine.getOptionValue("r"), commandLine.getOptionValue("s"));
                    System.out.println(commandLine.getOptionValue("s") + " matches " + commandLine.getOptionValue("r") + "?");
                    System.out.println(String.valueOf(rs));

                } catch (Exception e) {
                    System.out.println("Invalid regular expression.");
                }



            }
            else if (commandLine.hasOption("f")&&(commandLine.hasOption("o"))){
                testFile(commandLine.getOptionValue("f"), commandLine.getOptionValue("o"));

            }
            else{
                System.out.println("Wrong input. Input format is as follows:");
                System.out.println(help_str);
                System.exit(1);

            }

            if (commandLine.hasOption("help"))
                System.out.println(help_str);




        }
        catch (ParseException  exception)
        {
            System.out.println("Wrong input.");
            System.out.println(exception.getMessage());
            System.out.println("Help: Input format is as follows:");
            System.out.println(help_str);

        }

    }
}

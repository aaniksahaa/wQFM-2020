package wqfm.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import wqfm.interfaces.Status;
import wqfm.main.Main;

/**
 *
 * @author mahim
 */
public class Helper {

    //https://www.journaldev.com/878/java-write-to-file#:~:text=FileWriter%3A%20FileWriter%20is%20the%20simplest,number%20of%20writes%20is%20less.
    //Use FileWriter when number of write operations are less
    public static void writeToFile(String tree, String outputfileName) {
        File file = new File(outputfileName);
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(tree);
        } catch (IOException e) {
            System.out.println("Error in writingFile to "
                    + outputfileName + ", [Helper.writeToFile]. Exiting system.");
            System.out.println("Tree:\n" + tree);
            System.exit(-1);
        } finally {
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                System.out.println("Error in closing file resource in [Helper.writeToFile]. to outputfile = " + outputfileName);
            }
        }
        System.out.println(">-> Successfully written to output-file " + outputfileName);
    }

    public static void printUsageAndExitSystem() {
//        System.out.println("java -jar wQFM.jar -i <input-file-name> -o <output-file-name> [-p 0/1 <partition-score-mode>]");
        System.out.println("USAGE: java -jar wQFM.jar <input-file> <output-file> <partition-score-alpha> <partition-score-beta>\n"
                + "Or, java -jar wQFM.jar <input-file> <output-file> <partition-score-mode>\n"
                + "Partition Score modes are: (0:s-v, 1:s-0.5*v, 2:s-v-d, 3:3s-2d)");
        System.out.println("Exiting System (arguments not used according to usage)");
        System.exit(-1);
    }

    public static void findOptionsUsingCommandLineArgs(String[] args) {
        System.out.println("Command line args are -> " + Arrays.toString(args));
        if (args.length == 0) {
            if (Main.DEBUG_MODE_TESTING == false) {
                Helper.printUsageAndExitSystem();
            }
            System.out.println("-->>Using default params. ");
            return;
        }
        if (args.length == 1) {
            System.out.println("No output file, using default output file <" + Main.OUTPUT_FILE_NAME + ">");
            Main.INPUT_FILE_NAME = args[0];
            return;
        }
        if (args.length > 4) {
            printUsageAndExitSystem();
        }
        Main.INPUT_FILE_NAME = args[0];
        Main.OUTPUT_FILE_NAME = args[1];
        if (args.length == 3) {
            //partition-score argument given.
            int partition_score_argument = Integer.parseInt(args[2]);
            Main.PARTITION_SCORE_MODE = partition_score_argument;
        }
        if (args.length == 4) {
            //partition-score argument not given. [to do feature selection] //default is [s] - [v]
            double alpha = Double.parseDouble(args[2]);
            double beta = Double.parseDouble(args[3]);
            WeightedPartitionScores.ALPHA_PARTITION_SCORE = alpha;
            WeightedPartitionScores.BETA_PARTITION_SCORE = beta;
        }
    }

    public static int sumArray(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }

    public static int sumList(List<Integer> list) {
        int sum = 0;
        sum = list.stream().map((x) -> x).reduce(sum, Integer::sum);
        return sum;
    }

    public static int sumMapValuesInteger(Map<String, Integer> mapInitialBip) {
        int sum = 0;
        sum = mapInitialBip.keySet().stream().map((key) -> mapInitialBip.get(key)).reduce(sum, Integer::sum);
        return sum;
    }

    public static boolean checkAllValuesIFSame(List<Boolean> list, boolean val) {
        return list.stream().noneMatch((x) -> (x != val)); //if at least one is different wrt val, then return false
    }

    public static boolean checkAllValuesIFSame(Map<String, Boolean> map, boolean val) {
        if (map.isEmpty()) {
            return true;
        }
        return map.keySet().stream().noneMatch((key) -> (map.get(key) != val));
    }

    public static String getReadableMap(Map<String, Integer> map_bipartitions) {
        String s = ("LEFT: ");
        for (String key : map_bipartitions.keySet()) {
            if (map_bipartitions.get(key) == Status.LEFT_PARTITION) {
                s += (key + ", ");
            }
        }
//        s += (" ||| ");
        s += ("\nRIGHT: ");
        for (String key : map_bipartitions.keySet()) {
            if (map_bipartitions.get(key) == Status.RIGHT_PARTITION) {
                s += (key + ", ");
            }
        }
        s += "\n";
        return s;
    }

}

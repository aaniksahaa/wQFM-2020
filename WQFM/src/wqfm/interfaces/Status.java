package wqfm.interfaces;

import wqfm.utils.WeightedPartitionScores;

/**
 *
 * @author mahim
 */
public interface Status {

    // Use this for logical biparititoning
    public static int LEFT_PARTITION = -1; //-1 : left
    public static int UNASSIGNED_PARTITION = 0;
    public static int RIGHT_PARTITION = 1; //+1 : right

    // ALL ARE ARBITRARY VALUES FOR NOW ...
    public static int UNKNOWN = 75;
    public static int SATISFIED = 73;
    public static int VIOLATED = 72;
    public static int DEFERRED = 71;
    public static int BLANK = 70;

    //Free or locked [arbitrary values]
    public static int FREE = 21;
    public static int LOCKED = 22;

    // reroot mode [jar, python, to add perl]
    public static int REROOT_USING_JAR = 41;
    public static int REROOT_USING_PYTHON = 42;
    public static int REROOT_USING_PERL = 43;

    //Any undefined values used throughout
    public static int UNDEFINED = -1000;

    //Quartet's each taxon indices
    public static int LEFT_SISTER_1_IDX = 0;
    public static int LEFT_SISTER_2_IDX = 1;
    public static int RIGHT_SISTER_1_IDX = 2;
    public static int RIGHT_SISTER_2_IDX = 3;

    //Partition score modes ... 0->[s]-[v], 1->[s]-0.5[v], 2->[s]-[v]-[d], 3->3[s]-2[v]
    public static int PARTITION_SCORE_MODE_0 = 0;
    public static int PARTITION_SCORE_MODE_1 = 1;
    public static int PARTITION_SCORE_MODE_2 = 2;
    public static int PARTITION_SCORE_MODE_3 = 3;
    public static int PARTITION_SCORE_MODE_4 = 4;
    public static int PARTITION_SCORE_MODE_5 = 5;
    public static int PARTITION_SCORE_MODE_6 = 6;
    public static int PARTITION_SCORE_MODE_7 = 7;
    public static int PARITTION_SCORE_COMMAND_LINE = 8;

    public static int TOTAL_THREADS = 4;
    
    public static int WHETHER_DYNAMICALLY_COMPUTE_PARTITION_SCORE_EVERY_LEVEL = 1;
    //Helper method for printing quartet's status
    public static String GET_QUARTET_STATUS(int status) {
        switch (status) {
            case SATISFIED:
                return "SATISFIED";
            case VIOLATED:
                return "VIOLATED";
            case DEFERRED:
                return "DEFERRED";
            case BLANK:
                return "BLANK";
            default:
                break;
        }
        return "NULL";
    }

    public static String GET_PARTITION_SCORE_PRINT(int mode) {
        switch (mode) {
            case Status.PARTITION_SCORE_MODE_0:
                return "mode = " + mode + ", [ws - wv]";
            case Status.PARTITION_SCORE_MODE_1:
                return "mode = " + mode + ", [ws - 0.5*wv]";
            case Status.PARTITION_SCORE_MODE_2:
                return "mode = " + mode + ", [ws - wv - wd]";
            case Status.PARTITION_SCORE_MODE_3:
                return "mode = " + mode + ", [3*ws - 2*wv]";
            case Status.PARTITION_SCORE_MODE_4:
                return "mode = " + mode + ", [5*ws - 4*wv]";
            case Status.PARTITION_SCORE_MODE_5:
                return "mode = " + mode + ", [ws]";
            case Status.PARTITION_SCORE_MODE_6:
                return "mode = " + mode + ", [ws - 0.5*wv - 0.25*wd]";
            case Status.PARTITION_SCORE_MODE_7:
                return "mode = " + mode + ", [11*ws - 1*wv]";
            case Status.PARITTION_SCORE_COMMAND_LINE:
                return "mode = Command line [" + WeightedPartitionScores.ALPHA_PARTITION_SCORE + "*ws - " + WeightedPartitionScores.BETA_PARTITION_SCORE + "*wv]";
            default:
                return "default partition score mode = " + mode + ", [ws - wv]";
        }
    }

}

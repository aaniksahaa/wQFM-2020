package wqfm.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
import wqfm.Status;
import wqfm.bip.Bipartition_8_values;
import wqfm.ds.CustomInitTables;

/**
 *
 * @author mahim
 */
public class Utils {

    //Returns true if there is 1 taxa on either side, OR zero taxa on either side.[for pairwise swapping maybe needed]
    public static boolean isThisSingletonBipartition(List<Integer> logical_bipartition) { //true if this bipartition is a singleton bipartition
        int len = logical_bipartition.size();
        int sum = Helper.sumList(logical_bipartition);
        return (Math.abs(sum) == (len - 2)) || (Math.abs(sum) == len);
        //eg. -1,+1, +1,+1,+1,+1  --> so, two terms will lead to 0, rest sum will be length - 2
    }
    public static boolean isThisSingletonBipartition(Map<String, Integer> mapInitialBip) {
        int len = mapInitialBip.keySet().size();
        int sum = Helper.sumMapValuesInteger(mapInitialBip);
        
        return (Math.abs(sum) == (len - 2)) || (Math.abs(sum) == len);
    }

    public static int findQuartetStatus(int left_sis1_bip, int left_sis2_bip, int right_sis1_bip, int right_sis2_bip) {
        int[] four_bipartitions = {left_sis1_bip, left_sis2_bip, right_sis1_bip, right_sis2_bip};

        int sum_four_bipartitions = Helper.sumArray(four_bipartitions);
        //Blank check: Easier to check if blank quartet (all four are same) [priority wise first]
//        if ((left_sisters_bip[0] == left_sisters_bip[1]) && (right_sisters_bip[0] == right_sisters_bip[1]) && (left_sisters_bip[0] == right_sisters_bip[0])) {

        if (Math.abs(sum_four_bipartitions) == 4) { // -1,-1,-1,-1 or +1,+1,+1,+1 all will lead to sum == 4
            return Status.BLANK;
        }
        //Deferred Check: sum == 2 check [otherwise, permutations will be huge]
        if (Math.abs(sum_four_bipartitions) == 2) { //-1,+1 ,+1,+1  => +2 or +1,-1 , -1,-1 => -2 
            return Status.DEFERRED;
        }
        //Satisfied check: left are equal, right are equal AND left(any one) != right(any one)
        if ((left_sis1_bip == left_sis2_bip) && (right_sis1_bip == right_sis2_bip) && (left_sis1_bip != right_sis1_bip)) {
            return Status.SATISFIED;
        }
        //All check fails, Violated quartet
        return Status.VIOLATED;
    }

    public static int getOppositePartition(int partition) {
        switch (partition) {
            case Status.LEFT_PARTITION:
                return Status.RIGHT_PARTITION;
            case Status.RIGHT_PARTITION:
                return Status.LEFT_PARTITION;
            default:
                return Status.UNASSIGNED;
        }
    }

    public static Map obtainBipartitionMap(List<String> taxaList, List<Integer> bipartitions_list) {
        Map<String, Integer> map_bipartitions_list = new HashMap<>();
        for (int i = 0; i < taxaList.size(); i++) {
            map_bipartitions_list.put(taxaList.get(i), bipartitions_list.get(i));
        }
        return map_bipartitions_list;
    }

    public static Bipartition_8_values obtain8ValsBeforeSwap(CustomInitTables customDS, 
            List<Pair<Integer, Integer>> relevantQuartetsBeforeHypoMoving, List<String> taxa_list,
            Map<String, Integer> mapInitialBipartition) {
        return null;
    }

    

}

//----------------------------------------------------------- NOT USED FOR NOW ---------------------------------------------------------


    /*public static int findQuartetStatus(int[] left_sisters_bip, int[] right_sisters_bip) {
        int[] four_bipartitions = {left_sisters_bip[0], left_sisters_bip[1], right_sisters_bip[0], right_sisters_bip[1]};

        int sum_four_bipartitions = Helper.sumArray(four_bipartitions);
        //Blank check: Easier to check if blank quartet (all four are same) [priority wise first]
//        if ((left_sisters_bip[0] == left_sisters_bip[1]) && (right_sisters_bip[0] == right_sisters_bip[1]) && (left_sisters_bip[0] == right_sisters_bip[0])) {

        if (Math.abs(sum_four_bipartitions) == 4) { // -1,-1,-1,-1 or +1,+1,+1,+1 all will lead to sum == 4
            return Status.BLANK;
        }
        //Deferred Check: sum == 2 check [otherwise, permutations will be huge]
        if (Math.abs(sum_four_bipartitions) == 2) { //-1,+1 ,+1,+1  => +2 or +1,-1 , -1,-1 => -2 
            return Status.DEFERRED;
        }
        //Satisfied check: left are equal, right are equal AND left(any one) != right(any one)
        if ((left_sisters_bip[0] == left_sisters_bip[1]) && (right_sisters_bip[0] == right_sisters_bip[1]) && (left_sisters_bip[0] != right_sisters_bip[0])) {
            return Status.SATISFIED;
        }
        //All check fails, Violated quartet
        return Status.VIOLATED;
    }*/

package wqfm.ds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.util.Pair;
import wqfm.Status;
import wqfm.bip.AggValuesBothBipartitionPerTaxa;
import wqfm.bip.Bipartition_8_values;

/**
 *
 * @author mahim
 */
public class FMComputer {

    private final Bipartition_8_values initialBipartition_8_values;
    private final List<Integer> initial_bipartition_logical_list;
    public List<String> taxa_list;
    public List<Pair<Integer, Integer>> quartets_list_indices;
    private final CustomDS customDS;

//    private List<Integer> bipartition_logical_list_per_pass;
    private List<Boolean> lockedTaxaBooleanList; //true: LOCKED, false: FREE
    private final Map<String, Integer> mapInitialBip;

    //Updated on 23 June, 2020 (Mahim)
    private Map<Double, List<String>> mapCandidateGainsPerListTax; // Map of hypothetical gain vs list of taxa
    private Map<String, Bipartition_8_values> mapCandidateTax_vs_8vals; //after hypothetical swap [i.e. IF this is taken as snapshot, no need to recalculate]
    private Map<Integer, PerPassValue> mapOfPerPassValues;
    
    public FMComputer(CustomDS customDS, List<String> list, List<Pair<Integer, Integer>> qrts,
            List<Integer> initial_bip, Bipartition_8_values initialBip_8_vals) {
//        this.taxa_list = new ArrayList<>(list); //Copy OR direct assignment ?
        this.taxa_list = list;
        this.quartets_list_indices = qrts;
        this.customDS = customDS;
        this.initial_bipartition_logical_list = initial_bip;
        //Initially all the taxa will be FREE
        this.lockedTaxaBooleanList = new ArrayList<>(Collections.nCopies(this.taxa_list.size(), false));
//        this.bipartition_logical_list_per_pass = new ArrayList<>(this.initial_bipartition_logical_list);
        this.mapInitialBip = new HashMap<>();

        for (int i = 0; i < this.taxa_list.size(); i++) {
            this.mapInitialBip.put(this.taxa_list.get(i), this.initial_bipartition_logical_list.get(i));
        }
//        System.out.println(this.mapOfInitialBipartition);
        this.mapCandidateGainsPerListTax = new TreeMap<>(Collections.reverseOrder());
        this.mapCandidateTax_vs_8vals = new HashMap<>();
        this.initialBipartition_8_values = initialBip_8_vals;
        this.mapOfPerPassValues = new HashMap<>();
    }

    public void run_FM_singlepass_hypothetical_swap() {
        //per pass or step [per num taxa of steps].
        //Test hypothetically ...

        for (int taxa_iter = 0; taxa_iter < this.taxa_list.size(); taxa_iter++) { // iterate over whole set of taxa
            if (this.lockedTaxaBooleanList.get(taxa_iter) == false) { // this is a free taxon, hypothetically test it ....
                String taxToConsider = this.taxa_list.get(taxa_iter); // WHICH taxa to consider for hypothetical move.

                //First check IF moving this will lead to a singleton bipartition ....
                if (Utils.isSingletonBipartition_doesReversal(initial_bipartition_logical_list, taxa_iter, true) == true) {
                    //THIS hypothetical movement of taxToConsider leads to singleton bipartition so, continue ...
                    continue;
                }
                //DOESN'T lead to singleton bipartition [add to map, and other datastructures]
                //Calculate hypothetical Gain ... [using discussed short-cut]
                List<Pair<Integer, Integer>> relevantQuartetsBeforeHypoMoving = customDS.map_taxa_relevant_quartet_indices.get(taxToConsider);
                Bipartition_8_values _8_vals_before_swap = Utils.obtain8ValsBeforeSwap(customDS, relevantQuartetsBeforeHypoMoving, taxa_list, this.initial_bipartition_logical_list);
                
                List<Pair<Integer, Integer>> deferredQuartetsBeforeHypoMoving = new ArrayList<>(); //keep deferred quartets for later checking ...
                for (int quartets_itr = 0; quartets_itr < relevantQuartetsBeforeHypoMoving.size(); quartets_itr++) {
                    Pair<Integer, Integer> pair = relevantQuartetsBeforeHypoMoving.get(quartets_itr);
                    Quartet quartet = customDS.table1_quartets_double_list.get(pair.getKey()).get(pair.getValue());
                    int status_quartet_before_hyp_swap = Utils.findQuartetStatus(mapInitialBip.get(quartet.taxa_sisters_left[0]),
                            mapInitialBip.get(quartet.taxa_sisters_left[1]), mapInitialBip.get(quartet.taxa_sisters_right[0]), mapInitialBip.get(quartet.taxa_sisters_right[1]));
//                    System.out.println("Before hypo swap, tax considered = " + taxaToConsider + " , Qrt = " + quartet.toString() + " , Status = " + Status.PRINT_STATUS_QUARTET(status_quartet_before_hyp_swap));
                    if (status_quartet_before_hyp_swap == Status.DEFERRED) {
                        deferredQuartetsBeforeHypoMoving.add(pair);
                    }
                }

//                System.out.println("");
            } //end if
        }//end outer for

    }

    public void find_best_taxa_of_single_pass() {
        /*
        1.  Check if mapCandidateGainsPerListTax.size == 0 (any of the two maps) THEN all are singleton ... LOCK all taxaToMove
        2.  OTHERWISE, Use the two maps to find bestTaxaToMove [maxGain OR highestGain_with_max_num_satisfied_qrts]
        3.  LOCK the bestTaxaToMove
         */
    }

    public void run_FM_single_iteration() {
        //per iteration ... has many passes. [will have rollback]
        
    }

    //Whole FM ALGORITHm
    public FMResultObject run_FM_Algorithm_Whole() {
        //Constructor FMResultObject(List<Integer> logical_bipartition, List<String> taxa_list_initial, List<Pair<Integer, Integer>> quartets_list_initial)
        FMResultObject object = new FMResultObject(null, null, null);

//        System.out.println("-->>Inside FMComputer.run_FM_Algorithm_Whole()");
        System.out.println("TESTING runFMSinglePass()");
        run_FM_singlepass_hypothetical_swap();
        find_best_taxa_of_single_pass();
        double max_hypothetical_gain_of_this_pass = Integer.MIN_VALUE;
        String taxa_with_max_hypothetical_gain = "NONE_CHECK_NONE";

        return object;
    }

}

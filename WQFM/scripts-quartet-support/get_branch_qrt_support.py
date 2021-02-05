import dendropy
import sys


global LEFT, RIGHT

LEFT  = -1
RIGHT = 1

#####################################################################################################################



""" Get sorted quartets a,b|c,d: w
"""
def getSortedQuartet(line):
    stringsReplace = ["\n", ";", "(", ")"]
    for s in stringsReplace:
        line = line.replace(s, "") ## remove these chars
        
    line = line.replace(" ", ",")  ## replace WHITESPACE with COMMA    
    arr = line.split(",") ## split by COMMA

    # arr[0:1].sort() ## Maybe not necessary
    # arr[2:3].sort() ## Maybe not necessary

    return (arr[0], arr[1], arr[2], arr[3], float(arr[4]))


""" Process quartets
    1. Form a list of quartets
    2. Form a list of taxa
    3. Form a Map of <taxa: [list of qrt indices]>
"""
def processQuartets(inputQrtFile):
    list_quartets = []
    map_taxa_quartet = {}

    with open(inputQrtFile, 'r') as fin:
        line = fin.readline()
        while line:
            quartet = getSortedQuartet(line) # (t0, t1, t2, t3, w) = getSortedQuartet(line)
            # print(quartet)
            list_quartets.append(quartet)
            (t0, t1, t2, t3, w) = quartet
            for i in range(0, 4):
                if quartet[i] not in map_taxa_quartet:
                    map_taxa_quartet[quartet[i]] = [] ## initialize a list for the taxon NOT in map.
                idx_quartet = len(list_quartets) - 1
                ## Append this INDEX for each taxon.
                map_taxa_quartet[quartet[i]].append(idx_quartet)


            line = fin.readline()

    return list_quartets, map_taxa_quartet

""" Read tree from the Species Tree file
"""
def read_tree(inputStreeFile):
    taxa = dendropy.TaxonNamespace()
    tree = dendropy.Tree.get_from_path(inputStreeFile, "newick", taxon_namespace=taxa, rooting="force-rooted") ## rooting should be done
    return tree


""" Function to return as a map, the bitmap of bipartition
"""
def getPartitionMap(bipartition, taxon_namespace):
    map_bipartition = {}
    for i in range(0, len(taxon_namespace)):
        bit_idx = len(taxon_namespace) - 1 - i
        partition = LEFT if (bipartition[bit_idx] == '0') else RIGHT
        map_bipartition[taxon_namespace[i]] = partition
        #### map_bipartition[str(taxon_namespace[i])] = partition

    return map_bipartition

""" Function to compute Quartet Support for EACH bipartition
"""
def get_support_value(bipartition, taxon_namespace, list_quartets, map_taxa_quartet):
    bipartitionSTR = str(bipartition)
    map_bipartition = getPartitionMap(bipartitionSTR, taxon_namespace)


    return float(100)


""" Function to compute Quartet Support and return new tree with quartet support.
"""
def compute_tree_QSupport(tree, list_quartets, map_taxa_quartet):
    tree.encode_bipartitions() ## encode bipartitions to bitmaps
    support_values = {} ## Map for support values

    for nd in tree:
        if nd.bipartition.is_trivial() == False:
            # print(f"\nBip = {nd.bipartition.leafset_as_newick_string(taxon_namespace=tree.taxon_namespace)}, is_trivial = {nd.bipartition.is_trivial()}")

            # support_values[nd.bipartition] = get_support_value(nd.bipartition) if nd.label is not None else 1.0
            support_values[nd.bipartition] = get_support_value(nd.bipartition, tree.taxon_namespace, list_quartets, map_taxa_quartet) if nd.label is None else -1.0

    tree.encode_bipartitions()

    TOTAL_SATISFIED_QUARTETS = -1

    for nd in tree:
        nd.label = support_values.get(nd.bipartition, float(TOTAL_SATISFIED_QUARTETS))
    return tree




""" Main function
"""
def run(inputQrtFile, inputStreeFile, outputFile):

    list_quartets, map_taxa_quartet = processQuartets(inputQrtFile)
    
    tree = read_tree(inputStreeFile)
    treeStr = tree.as_string("newick").strip()
    
    print(treeStr, "\n")

    output_tree = compute_tree_QSupport(tree, list_quartets, map_taxa_quartet)
    output_tree = output_tree.as_string("newick").strip()

    print("\n")
    print(output_tree)
    # print(f"\n In run(), output_tree = {output_tree}")

#####################################################################################################################

def printUsageExit():
    print(f"Usage: python3 {sys.argv[0]} <input-qrt-file> <input-STree-file> <output-file>")
    exit()




if __name__ == '__main__':
    if len(sys.argv) != 4:
        printUsageExit()
    
    inputQrtFile = sys.argv[1]
    inputStreeFile = sys.argv[2]
    outputFile = sys.argv[3]

    run(inputQrtFile, inputStreeFile, outputFile)


















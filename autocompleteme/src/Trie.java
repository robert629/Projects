
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * The is the framework for a Trie, includes traversal, insertion and getting
 * nodes
 *
 * @author Robert Feldman
 */
public class Trie {

    private TrieNode root; //The root node
    static TrieNodeComparator trieNodeComp; //The object that compares trie nodes

    /**
     * Creates a new Trie with a default trie node and instantiates the
     * comparator
     */
    public Trie() {
        root = new TrieNode();
        trieNodeComp = new TrieNodeComparator();
    }

    /**
     * Gets the root node of the Trie
     *
     * @return the root node
     */
    public TrieNode getRoot() {
        return root;
    }

    /**
     * Adds a node to the Trie. Creates blank nodes if necessary. Updates node
     * properties if they already exist
     *
     * @param word the word of the node
     * @param weight the weight of the node
     */
    public void addTrieNode(String word, long weight) {
        if (word.length() == 1) {
            root.addChild(word, weight, (word.charAt(0)), weight);
        } else {
            int processLength = word.length();
            int subIndex = 1;
            String prefix = "";
            char key;
            String suffix = "";
            TrieNode currentNode = root;
            while (processLength > 0) { //Traverses until the word is completely processed (prefix = word, suffix = "")
                prefix = word.substring(0, subIndex);
                key = prefix.charAt(prefix.length() - 1);
                suffix = word.substring(subIndex);
                if (suffix.equals("")) { //The word has been processed
                    if (prefix.equals(word) & currentNode.getChild(key) != null) { //If the node has a child, then the child node has already been made, so the child node's weights and max child weights must be updated
                        ((TrieNode) currentNode.getChild(key)).setWeight(weight);
                        ((TrieNode) currentNode.getChild(key)).setMaxChildWeight(weight);
                    } else {
                        currentNode.addChild(prefix, weight, key, weight);
                        currentNode.getChild(key).setMaxChildWeight(weight); //Remove or figure out!!
                        break;
                    }
                }
                if (currentNode.getChild(key) == null) { // If there is no letter path, it makes one with a default trie node
                    currentNode.addChild(prefix, -1, key, weight);
                } else { //Moves to the next child/prefix
                    currentNode.setMaxChildWeight(weight);
                    currentNode = currentNode.getChild(key);
                    processLength--;
                    subIndex++;
                }
            }

        }

    }

    /**
     * Gets the node that corresponds to the desired input word
     *
     * @param wordToFind the word of the node to be returned
     * @return the trie node of the specified word
     * @throws Exception if the word cannot be found
     */
    public TrieNode getNode(String wordToFind) throws Exception {
        int subIndex = 0;
        wordToFind = wordToFind.toLowerCase();
        TrieNode currentNode = root;
        while (subIndex < wordToFind.length()) {
            if (currentNode.getChild(wordToFind.charAt(subIndex)) == null) {
                throw new Exception("Word not found");
            }
            currentNode = currentNode.getChild(wordToFind.charAt(subIndex));
            subIndex++;
        }
        return currentNode;
    }

    /**
     * Gets the k (or less than k best words) based on weights
     *
     * @param k the desired number of words
     * @param word the word being searched
     * @return an array list of trie nodes in order of descending weight
     */
    public ArrayList<TrieNode> getKBestWords(int k, String word) throws Exception {

        if (k <= 0) {
            throw new Exception("Number of search results must be positive");
        }
        if (word.length() == 0) {
            throw new Exception("Word cannot be empty string");
        }

        PriorityQueue traversalQueue = new PriorityQueue<TrieNode>(trieNodeComp);
        ArrayList<TrieNode> outputList = new ArrayList<TrieNode>();
        TrieNode currentNode;
        TrieNode dummyNode;
        try {
            traversalQueue.offer(getNode(word)); //Make sure the is in the trie
            if (getNode(word).getWeight() != getNode(word).getMaxChildWeight()) {
                dummyNode = new TrieNode(getNode(word).getWord(), getNode(word).getWeight());
                dummyNode.setMaxChildWeight(dummyNode.getWeight());
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        while (traversalQueue.size() > 0) {
            currentNode = (TrieNode) traversalQueue.poll();
            for (int i = 0; i < currentNode.getChildren().keySet().toArray().length; i++) {
                traversalQueue.offer(currentNode.getChild((Character) currentNode.getChildren().keySet().toArray()[i])); //Traverse based on highest max child weights
                if (currentNode.getWeight() != currentNode.getMaxChildWeight() && currentNode.getWeight() != -1) { //Add fake node with max weight = actual weight if word and child has higher priority
                    dummyNode = new TrieNode(currentNode.getWord(), currentNode.getWeight());
                    dummyNode.setMaxChildWeight(dummyNode.getWeight());
                    traversalQueue.offer(dummyNode);
                }
            }
            if (currentNode.getWeight() > -1 && currentNode.getWeight() == currentNode.getMaxChildWeight()) {
                outputList.add(getNode(currentNode.getWord()));//Consider returning a node if it actually a word (weight  > -1)
                if (outputList.size() == k) {  // Return list if we find K words
                    return outputList;
                }
            }
        }
         //We have traversed any valid node options, but did not find K words. Return less than K words
        return outputList;
    }

}

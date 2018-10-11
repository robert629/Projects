
import java.util.Hashtable;

/**
 * A trie node holds all of the properties of a node in a trie (weight, max
 * child weight, children, etc.)
 *
 * @author Robert Feldman
 */
public class TrieNode {

    private Hashtable children; //hashtable of children of the trie node (nodes accessible by letter)
    private long weight; //Weight of the specific node
    private long maxChildWeight; //max childweight of the node
    private String wordKey; //The word for the specific node

    /**
     * Creates a trie node with a specified word and weight
     *
     * @param word the specific word of the trie node
     * @param w the specific weight of the trie node
     */
    public TrieNode(String word, long w) {
        weight = w;
        wordKey = word;
        maxChildWeight = -1;
        children = new Hashtable<Character, TrieNode>();
    }

    /**
     * Creates a trie node with wordKey = ROOT (intended for root of trie)
     */
    public TrieNode() {
        //Optional for Root
        weight = -1;
        wordKey = "ROOT";
        maxChildWeight = -1;
        //Mandatory for Root
        children = new Hashtable<Character, TrieNode>();

    }

    /**
     * Gets the children of a trie node
     *
     * @return the hashtable that holds the children nodes of this trie node
     */
    public Hashtable getChildren() {
        return (Hashtable<Character, TrieNode>) children;
    }

    /**
     * Gets the weight of this trie node
     *
     * @return the weight
     */
    public long getWeight() {
        return weight;
    }

    /**
     * Gets the max child weight of a trie node
     *
     * @return the max child weight
     */
    public long getMaxChildWeight() {
        return maxChildWeight;
    }

    /**
     * Sets the max child weight to a new max child weight is the new weight is
     * greater than the current max child weight
     *
     * @param wordWeight the desired new max child weight
     */
    public void setMaxChildWeight(long wordWeight) {
        if (wordWeight > maxChildWeight) {
            maxChildWeight = wordWeight;
        }
    }

    /**
     * Gets a child node from the current trie node
     *
     * @param key the character corresponding to the next child (The next letter
     * in the word)
     * @return the trie node at the specified child
     */
    public TrieNode getChild(Character key) {
        return (TrieNode) children.get(key);
    }

    /**
     * Gets the word for the trie node
     *
     * @return the word for the trie node
     */
    public String getWord() {
        return wordKey;
    }

    /**
     * Sets the weight of a trie node. Only used for leaf nodes
     *
     * @param w The desired weight of the trie node
     */
    public void setWeight(long w) {
        weight = w;
    }

    /**
     * Adds a child node to the trie node
     *
     * @param wordK the word of the cnew node
     * @param w the weight of the new word (if it isn't a word: -1
     * @param key the letter that gets to the new trie node word
     * @param wordWeight the weight of the new word as a max child weight
     */
    public void addChild(String wordK, long w, Character key, long wordWeight) {
        children.put(key, new TrieNode(wordK, w));
        if (wordWeight > maxChildWeight) {
            maxChildWeight = wordWeight;
        }
    }

}

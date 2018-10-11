
import java.util.Comparator;

/**
 * The TrieNode Comparator creates a way to compare trie nodes based on their
 * max child weight.
 *
 * @author Robert Feldman
 */
public class TrieNodeComparator implements Comparator<TrieNode> {

    /**
     * Compares the max child weights of two trie nodes.
     *
     * @param t1 a trie node to be compared
     * @param t2 a trie node to be compared
     * @return 1 if the max child weight of the first trie node is less than the
     * max child weight of the second trie node. Otherwise -1
     */
    @Override
    public int compare(TrieNode t1, TrieNode t2) {
        if (t1.getMaxChildWeight() < t2.getMaxChildWeight()) {
            return 1;
        } else {
            return -1;
        }
    }

}

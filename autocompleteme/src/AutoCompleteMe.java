
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Builds a trie, and autocompletes word endings for given user input
 *
 * @author Robert Feldman
 */
public class AutoCompleteMe {

    /**
     * Runs the main program with user input
     *
     * @param args the command line arguments inputed by the user: prefix, file,
     * #results
     */
    public static void main(String[] args) throws Exception {
        try {
            if (args.length != 3) {
                throw new Exception("Input is not of form: word filePath #Results");
            }
            String wordInput = args[0];
            String fileName = args[1];
            int k = Integer.parseInt(args[2]);

            AutoCompleteMe auto = new AutoCompleteMe();
            Trie newTrie = auto.buildAutoCompleteTrie(fileName);
            ArrayList<TrieNode> myArrayList = newTrie.getKBestWords(k, wordInput);
            while (!myArrayList.isEmpty()) {
                TrieNode node = (TrieNode) myArrayList.remove(0);
//Modify print  System.out.printf("%-10s %-15s %-15s %-1s", node.getWeight(), node.getWord(), node.getMaxChildWeight(), "\n");
                System.out.printf("%-15s %-15s %-1s", node.getWeight(), node.getWord(), "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
 
    }

    /**
     * Read in a file name and returns a trie
     *
     * @param fileName the file name of the word list to be read in. It must be
     * in the resources folder
     * @return a Trie filled by the text file with a weight word format
     * @throws Exception If there is an issue with the file
     */
    Trie buildAutoCompleteTrie(String fileName) throws Exception {
        Trie newTrie = new Trie();
        ArrayList<String> inputLines = new ArrayList<String>();
        URL wordListResource = getClass().getClassLoader().getResource("resources/" + fileName);
        assert wordListResource != null;
        try {
            Stream<String> lines = Files.lines(Paths.get(wordListResource.toURI()));
            lines.forEach(inputLines::add);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long weights;
        String words;
        for (int i = 0; i < inputLines.size(); i++) {
            String[] dataRow = inputLines.get(i).split("\\t");
            if (dataRow.length == 2) {            
                weights = Long.parseLong(dataRow[0].trim());
                words = dataRow[1].trim().toLowerCase();
                newTrie.addTrieNode(words, weights);
            }
        }
        return newTrie;
    }
}

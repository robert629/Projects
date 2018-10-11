
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 *
 * @author Robert Feldman
 */
public class TestAutoCompleteMe {

    public static void main(String[] args) throws Exception {
        TestAutoCompleteMe auto = new TestAutoCompleteMe();

        //Time trie loading for movies.txt
        System.out.println("Start reading in movies.txt");
        long startTime = System.nanoTime();
        Trie movieTrie = auto.testBuildAutoCompleteTrie("movies.txt", false);
        long endTime = System.nanoTime();
        System.out.println("It takes " + (endTime - startTime) + " nanoseconds to load movies.txt into a Trie!\n");

        System.out.println("Test functionality using pokemon.txt\n");
        Trie pokemonTrie = auto.testBuildAutoCompleteTrie("pokemon.txt", false);

        //Check that empty string throws error
        try {
            pokemonTrie.getKBestWords(3, "");
        } catch (Exception ex) {
            System.out.println("Correct Error Message for empty string"); //correct error message
            System.out.println(ex);
                    
        }
        //Check that empty string throws error
        try {
            pokemonTrie.getKBestWords(0, "char");
        } catch (Exception ex) {
           
            System.out.println("Correct Error Message for non-positive K"); //Correct error message
            System.out.println(ex);
               
        }
        System.out.println();
        //Test that valid words with children of higher weights return correct order
        System.out.println("(1) Word: Rotom -> rotom + 6 children");
        ArrayList<TrieNode> rotom = pokemonTrie.getKBestWords(8, "rotom");
        printKBestWords(rotom);
        System.out.println("(2) Word: pa -> 12 children. Check that parasect come before paras, and paras is not near parasect (possible maxChildWeight use)");
        ArrayList<TrieNode> pa = pokemonTrie.getKBestWords(1000, "pa");
        printKBestWords(pa);
        System.out.println("(3) Word: me -> 12 children. Check that mew comes before mewtwo");
        ArrayList<TrieNode> me = pokemonTrie.getKBestWords(1000, "me");
        printKBestWords(me);

        //Check that randomizing input words doesn't alter Trie results
        System.out.println("Check that randomizing word list doesn't change output of autocomplete");
        System.out.println("Rotom");
        Trie randomPokemonTrie = auto.testBuildAutoCompleteTrie("pokemon.txt", true);
        ArrayList<TrieNode> rotomRandom = randomPokemonTrie.getKBestWords(8, "rotom");
        printKBestWords(rotomRandom);
        KBestWordsListEqual(rotom, rotomRandom);

        System.out.println("Pa");
        ArrayList<TrieNode> paRandom = randomPokemonTrie.getKBestWords(1000, "pa");
        printKBestWords(paRandom);
        KBestWordsListEqual(pa, paRandom);

        System.out.println("Me");
        ArrayList<TrieNode> meRandom = randomPokemonTrie.getKBestWords(1000, "me");
        printKBestWords(meRandom);
        KBestWordsListEqual(me, meRandom);

        // Randomized Tests
        randomTestsKBestWords(pokemonTrie, randomPokemonTrie);
    }

    Trie testBuildAutoCompleteTrie(String fileName, boolean randomize) throws Exception {
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
        if (randomize) {
            inputLines = randomizeWordList(inputLines);
        }
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

    static ArrayList<String> randomizeWordList(ArrayList wordList) {
        int rand;
        String temp;
        int listSize = wordList.size();
        for (int j = 0; j < listSize; j++) {
            rand = (int) (Math.random() * listSize);
            temp = (String) wordList.get(rand);
            wordList.remove(rand);
            wordList.add(temp);
        }
        return wordList;
    }

    static void printKBestWords(ArrayList<TrieNode> nodesToPrint) {
        for (int j = 0; j < nodesToPrint.size(); j++) {
            TrieNode node = (TrieNode) nodesToPrint.get(j);
            System.out.printf("%-15s %-15s %-1s", node.getWeight(), node.getWord(), "\n");
//Modify print System.out.printf("%-10s %-15s %-15s %-1s", node.getWeight(), node.getWord(), node.getMaxChildWeight(), "\n");
            if (j < nodesToPrint.size() - 1) {
                if (node.getWeight() < ((TrieNode) nodesToPrint.get(j + 1)).getWeight()) {
                    throw new AssertionError("The nodes must be in order of decreasing weight");
                }
            }
        }
        System.out.println();
    }

    static void KBestWordsListEqual(ArrayList<TrieNode> tn1, ArrayList<TrieNode> tn2) throws Exception {
        if (tn1.size() != tn2.size()) {
            throw new Exception("Autocomplete failed. Results not same with randomizing inputs: Output lists are of incorrect size");
        }
        for (int y = 0; y < tn1.size(); y++) {
            if (tn1.get(y).getWeight() != tn2.get(y).getWeight()) {
                throw new Exception("Autocomplete failed. Results not same with randomizing inputs: Weight of words incorrect");
            }
        }
    }

    static void randomTestsKBestWords(Trie trie, Trie randomTrie) throws Exception {
        int numTests = 50;
        String randomString;
        int stringLength;
        int charToAdd;
        int numWords;
        long startTime = 0;
        long endTime = 0;
        ArrayList<TrieNode> testList = new ArrayList<TrieNode>();
        for (int y = 0; y < numTests; y++) {
            System.out.println();
            System.out.println("Test #" + (y + 1));
            randomString = "";
            stringLength = (int) (5 * Math.random() + 1);
            numWords = (int) (Math.random() * 15 + 1);
            for (int k = 0; k < stringLength; k++) {
                charToAdd = (int) (26 * Math.random()) + 97;
                randomString = randomString + (char) (charToAdd);
            }
            System.out.println(randomString + " K = " + numWords);
            try {
                startTime = System.nanoTime();
                endTime = 0;
                testList = trie.getKBestWords(numWords, randomString);
                endTime = System.nanoTime();

            } catch (Exception ex) {
                if (!ex.getMessage().equals("java.lang.Exception: Word not found")) {
                    //   System.out.println(randomString);
                    throw new Exception("Test failed!!!");
                }
            }
            if (testList.size() > numWords) {
                throw new Exception("Test failed: Returned too many words");
            }
            System.out.println("Number of words found = " + testList.size());
            if (endTime != 0) {
                System.out.println("It took " + (endTime - startTime) + " nanoseconds to autocomplete");
            }
            for (int q = 0; q < testList.size(); q++) {
                System.out.println(testList.get(q).getWord() + "  " + randomTrie.getNode(testList.get(q).getWord()).getWeight() + "   " + testList.get(q).getMaxChildWeight());
                if (!testList.get(q).getWord().equals(randomTrie.getNode(testList.get(q).getWord()).getWord())
                        || testList.get(q).getWeight() != randomTrie.getNode(testList.get(q).getWord()).getWeight()
                        || testList.get(q).getMaxChildWeight() != randomTrie.getNode(testList.get(q).getWord()).getMaxChildWeight()) {
                    throw new Exception("Test failed");
                }
            }
        }
    }

}

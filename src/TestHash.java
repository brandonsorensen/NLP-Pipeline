import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import edu.stanford.nlp.process.*;

public class TestHash {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("src/tweets.csv");
        Scanner csvScanner = new Scanner(file);
        csvScanner.useDelimiter(",");
        LinkedList<String> list = new LinkedList<>();
        Set<String> exclusionList = new HashSet<>();
        Set alphabet = new HashSet<String>(
                Arrays.asList("aäbcdeëfghïejklmnoöpqrsßtüuvwxyz".split(""))
        );

        DocumentPreprocessor dp = new DocumentPreprocessor("src/tweets.csv");
        for (List sent : dp) {
            System.out.println(sent);
        }
    }
}

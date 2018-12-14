import java.io.FileNotFoundException;
import java.util.LinkedList;

public class TestHash {
    public static void main(String[] args) throws FileNotFoundException {
        InvertedIndex<String, LinkedList> index = new InvertedIndex<>();
        index.index("src/tweets.csv");
    }
}

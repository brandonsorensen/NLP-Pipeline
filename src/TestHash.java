import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;

public class TestHash {
    public static void main(String[] args) throws FileNotFoundException {
        InvertedIndex<String, ArrayList> index = new InvertedIndex<>();
        ArrayList<Integer> array = new ArrayList<>(100);
        for (int i = 0; i < 10; i += 3) {
            array.add(i);
        }
        index.put("Hello", array);
    }
}

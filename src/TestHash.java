import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TestHash {
    public static void main(String[] args) throws FileNotFoundException {
        InvertedIndex<String, LinkedList> index = new InvertedIndex<>();
        LinkedList<Integer> array = new LinkedList<>();
        for (int i = 0; i < 10; i += 3) {
            array.add(i);
        }
        index.put("Hello", array);
        System.out.println(index.freqDist());
    }
}

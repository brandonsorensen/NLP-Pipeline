import java.util.List;
import java.util.Set;

public class Corpus {
    private final double corpusID;
    private Set<Document> collection;
    private int tokenCount, lineCount, docCount;
    private

    Corpus() {
        corpusID = 1;
    }

    Corpus(double corpusID) {
        this.corpusID = corpusID;
    }
}

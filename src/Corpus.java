import java.util.List;
import java.util.Set;

abstract class Corpus {
    private final int corpusID;
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

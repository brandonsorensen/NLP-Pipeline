import java.util.Collection;
import java.util.List;
import java.util.Set;

abstract class Corpus<d extends Document> implements Collection<Document> {
    private final int corpusID;
    private Collection<Document> collection;
    private int tokenCount, lineCount, docCount;
    private

    Corpus() {
        corpusID = 1;
    }

    Corpus(double corpusID) {
        this.corpusID = corpusID;
    }
}

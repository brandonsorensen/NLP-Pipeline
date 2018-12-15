import java.util.List;

public class Corpus {
    private final double corpusID;
    private List<Document> collection;
    private int tokenCount, lineCount, docCount;

    Corpus() {
        corpusID = 1;
    }

    Corpus(double corpusID) {
        this.corpusID = corpusID;
    }
}

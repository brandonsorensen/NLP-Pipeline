import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Document {
    private final int docID;
    private Set<Token> tokens;
    private ArrayList<Token[]> lines;
    private Set<String> types;
    private Tokenizer tokenizer;
    private String rawText; // TODO: Get raw text for all constructors
    // TODO: Maybe add the ability to name a document

    Document() {
        tokenizer = new Tokenizer();
        clear();
    }
    Document(File file) throws FileNotFoundException {
        this(file, new Tokenizer());
    }

    Document(File file, Tokenizer tokenizer) throws FileNotFoundException {
        this.tokenizer = tokenizer;
        clear();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            tokenize(scanner.next());
        }
    }

    Document(String docText) throws FileNotFoundException {
        this(docText, new Tokenizer());
    }

    Document(String docText, Tokenizer tokenizer) throws FileNotFoundException {
        this.tokenizer = tokenizer;
        clear();
        String[] rawLines = docText.split("\n");
        for (String line : rawLines) {
            tokenize(line);
        }
    }

    private void tokenize(String text) {
        List<String> tokenized = tokenizer.tokenize(text);
        int lineLength = tokenized.size();
        Token[] line = new Token[lineLength];
        int linePosition = 0; // To avoid using get on our linked list so many times
        for (String str : tokenized) {
            Token token = new Token(str, this, getLineCount(), linePosition);
            types.add(str);
            tokens.add(token);
            line[linePosition] = token;
            linePosition++;
        }
        lines.add(line);
    }

    public Token[] getLineForToken(Token token) {
        return lines.get(token.getLineNumber());
    }

    public void clear() {
        tokens = new HashSet<>();
        types = new HashSet<>();
        rawText = "";
    }

    /**
     * Gets an array containing all tokens
     * @return an array of all tokens
     */
    public Set<Token> getTokens() {
        return tokens;
    }

    /**
     * Returns the <\code>tokens</\code> array as a <\code>List</\code>
     * @return a <\code>List</\code> of all tokens
     */
    public Token[] getTokensAsArray() {
        return (Token[]) tokens.toArray();
    }

    /**
     * Returns the all tokens found that matches a given string.
     * @param str a given string
     * @return the first token that matches a given string
     */
    private List<Token> getTokensFromString(String str) {
        if (!contains(str)) {
            throw new NoSuchElementException();
        }

        LinkedList<Token> retVal = new LinkedList<>();
        for (Token tok : tokens) {
            if (tok.equals(str)) {
                retVal.add(tok);
            }
        }
        if (retVal.isEmpty()) {
            throw new NoSuchElementException();
        }
        return retVal;
    }

    /**
     * Gets the term frequency for a given term in this document.
     * @param term a given term
     * @return the frequency of <\code>term</\code> within this document
     */
    public float termFrequency(String term) {
        if (!contains(term)) {
            throw new NoSuchElementException();
        }
        List<Token> matchingTokens = getTokensFromString(term);
        return (float) matchingTokens.size() / tokens.size();
    }

    public int getDocID() {
        return docID;
    }

    public int getLineCount() {
        return lines.size();
    }

    public int getTokenCount() {
        return tokens.size();
    }

    public int getTypeCount() {
        return types.size();
    }

    /**
     * Determines if a term is in the document by checking whether
     * it is in the <\code>types</\code> set.
     * @param term a given term
     * @return whether that term is in the document
     */
    public boolean contains(String term) {
        return types.contains(term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docID, tokens);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Document)) {
            return false;
        }
        return ((Document) other).getDocID() == docID;
    }

    /**
     * Returns a set of all types in the document
     * @return a set of all types in the document
     */
    public Set<String> getTypes() {
        return types;
    }
}

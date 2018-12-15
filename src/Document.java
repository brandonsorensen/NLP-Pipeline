import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Document {
    private final int docID;
    private int lineCount, tokenCount, typeCount;
    private LinkedList<Token> tokens;
    private ArrayList<Token[]> lines;
    private Set<String> types;
    // TODO: Allow optional naming of documents
    private String docName;

    Document(int docID) {
        this.docID = docID;
        clear();
    }
    Document(String path, int docID) throws FileNotFoundException {
        this.docID = docID;
        clear();
        Tokenizer tokenizer = new Tokenizer();
        Scanner scanner = new Scanner(new File(path));
        while (scanner.hasNext()) {
            int priorTypeCount = types.size();
            List<Token> tokenized = tokenizer.tokenize(scanner.next());
            // TODO: Tokenizer returns String, create Tokens here
            for (Token token : tokenized) {
                types.add(token.toString());
                tokens.add(token);
            }
            int lineLength = tokenized.size();
            lineCount++;
            tokenCount += lineLength;
            typeCount = types.size() - priorTypeCount;
        }
    }

    private void tokenize(String path) throws FileNotFoundException {
        Tokenizer tokenizer = new Tokenizer();
        Scanner scanner = new Scanner(new File(path));
        while (scanner.hasNext()) {
            List<String> tokenized = tokenizer.tokenize(scanner.next());
            int lineLength = tokenized.size();
            Token[] line = new Token[lineLength];
            int priorTypeCount = types.size();
            lineCount++;
            int linePosition = 0; // To avoid using get on our linked list so many times
            for (String str : tokenized) {
                Token token = new Token(str, this, lineCount, linePosition);
                types.add(str);
                tokens.add(token);
                line[linePosition] = token;
                linePosition++;
            }
            tokenCount += lineLength;
            typeCount = types.size() - priorTypeCount;
            lines.add(line);
        }
    }

    public Token[] getLineForToken(Token token) {
        return lines.get(token.)
    }

    public void clear() {
        lineCount = 0;
        tokenCount = 0;
        typeCount = 0;
        tokens = new LinkedList<>();
        types = new HashSet<>();
    }

    /**
     * Gets an array containing all tokens
     * @return an array of all tokens
     */
    public List<Token> getTokens() {
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
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public int getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(int tokenCount) {
        this.tokenCount = tokenCount;
    }

    public int getTypeCount() {
        return typeCount;
    }

    public void setTypeCount(int typeCount) {
        this.typeCount = typeCount;
    }

    public void setTokens(LinkedList<Token> tokens) {
        this.tokens = tokens;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
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

    /**
     * Returns a set of all types in the document
     * @return a set of all types in the document
     */
    public Set<String> getTypes() {
        return types;
    }
}

import java.util.Objects;

public class Token {
    private String token;
    private Document doc;
    private int docID, lineNumber, linePosition;
    private final int tokenID;

    Token(String token, Document doc) {
        this.token = token;
        this.doc = doc;
        this.docID = doc.getDocID();
        tokenID = hashCode();
    }

    Token(String token, Document doc, int lineNumber, int linePosition) {
        this.token = token;
        this.doc = doc;
        this.lineNumber = lineNumber;
        this.linePosition = linePosition;
        tokenID = hashCode();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public int getDocID() {
        return docID;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLinePosition() {
        return linePosition;
    }

    public void setLinePosition(int linePosition) {
        this.linePosition = linePosition;
    }

    public double getTokenID() {
        return tokenID;
    }

    @Override
    public boolean equals(Object other) {
        return token.equals(other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docID, token, lineNumber, linePosition);
    }

    @Override
    public String toString() {
        return token;
    }
}

public class Token {
    private String token;
    private Document doc;
    private int docID, lineNumber, linePosition;
    private final double tokenID;

    Token(String token, Document doc) {
        this.token = token;
        this.doc = doc;
        this.docID = doc.getDocID();
    }

    Token(String token, Document doc, int lineNumber, int linePosition) {

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

    public void setTokenID(double tokenID) {
        this.tokenID = tokenID;
    }

    @Override
    public boolean equals(Object other) {
        return token.equals(other);
    }

    @Override
    public String toString() {
        return token;
    }
}

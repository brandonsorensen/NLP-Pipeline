public class Token {
    private String token;
    private Document doc;
    private int docID, lineNumber, linePosition;
    private double tokenID;

    Token(String token, Document doc) {

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

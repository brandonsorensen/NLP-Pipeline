import java.util.*;

class HashNode<String, Postings extends SortedSet<Document>>
        implements Map.Entry<String, Postings> {
    private String key;
    private Postings value;
    private HashNode<String, Postings> next;

    HashNode(String key, Postings value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Postings getValue() {
        return value;
    }

    @Override
    public Postings setValue(Postings value) {
        this.value = value;
        return value;
    }

    public HashNode<String, Postings> next() {
        return next;
    }

    public void setNext(HashNode<String, Postings> next) {
        this.next = next;
    }

    public Postings append(HashNode<String, Postings> newEntry) {
        setNext(newEntry);
        return newEntry.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashNode)) return false;
        HashNode<?, ?> entry = (HashNode<?, ?>) o;
        return key.equals(entry.key) &&
                Objects.equals(value, entry.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}

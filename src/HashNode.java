import java.util.Map;
import java.util.Objects;

class HashNode<String, PostingNode> implements Map.Entry<String, PostingNode> {
    private String key;
    private PostingNode value;
    private HashNode<String, PostingNode> next;

    HashNode(String key, PostingNode value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public PostingNode getValue() {
        return value;
    }

    @Override
    public PostingNode setValue(PostingNode value) {
        this.value = value;
        return value;
    }

    public HashNode<String, PostingNode> next() {
        return next;
    }

    public void setNext(HashNode<String, PostingNode> next) {
        this.next = next;
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

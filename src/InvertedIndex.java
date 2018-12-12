import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.LinkedList;

public class InvertedIndex<String, PostingNode> implements Map<String, PostingNode> {
    private HashNode<String, PostingNode>[] buckets;
    private int size;
    private LinkedList<String> docContent;
    private boolean indexing;
    private int capacity;
    private double loadFactor;

    public static final int DEFAULT_CAPACITY = 16;

    InvertedIndex() {
        clear();
    }

    InvertedIndex(int initialCapacity) {
        clear(initialCapacity);
    }

    InvertedIndex(int initialCapacity, String path) {
        clear(initialCapacity);
        index(path);
    }

    public void index(String path) {return;}

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    public boolean containsPostingNodealue(Object value) {
        return false;
    }

    @Override
    public PostingNode get(Object key) {
        return null;
    }

    @Override
    public PostingNode put(String key, PostingNode value) {
        if (key == null) {
            return putNullKey(value);
        }

        HashNode<String, PostingNode> newEntry = new HashNode<>(key, value);
        int bucket = newEntry.hashCode() % capacity;
        
        HashNode<String, PostingNode> existing =

        return null;
    }

    private PostingNode putNullKey(PostingNode value) {return null;}

    @Override
    public PostingNode remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends PostingNode> m) {

    }

    @Override
    public void clear() {
        clear(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public void clear(int capacity) {
        this.capacity = capacity;
        buckets = new HashNode[capacity];
        indexing = false;
        docContent = new LinkedList<>();
        size = 0;
        loadFactor = capacity * .75;
    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<PostingNode> values() {
        return null;
    }

    @Override
    public Set<Entry<String, PostingNode>> entrySet() {
        return null;
    }
}

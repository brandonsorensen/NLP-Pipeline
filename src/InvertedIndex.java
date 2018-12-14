import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.LinkedList;
import java.util.List;

public class InvertedIndex<Term extends String, Postings extends List>
        implements Map<Term, Postings> {
    private HashNode<Term, Postings>[] buckets;
    private int size;
    private LinkedList<Term> docContent;
    private boolean indexing;
    private int capacity;
    private double loadFactor;
    private double expansionRate;
    private Set<Term> keySet;

    public static final int DEFAULT_CAPACITY = 16;
    public static final double DEFAULT_EXPANSION_RATE = 1.5;

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
        // TODO
        return (size == 0);
    }

    @Override
    public boolean containsValue(Object value) {
        // TODO
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        // TODO
        return false;
    }

    public boolean containsPostingNodealue(Object value) {
        // TODO
        return false;
    }

    @Override
    public Postings get(Object key) {
        // TODO
        return null;
    }

    @Override
    public Postings put(Term key, Postings value) {
        if (key == null) {
            try {
                throw new NullKeyException("Null keys are not allowed.");
            } catch (NullKeyException ex) {
                System.out.println(ex);
            }
        }

        HashNode<Term, Postings> newEntry = new HashNode<>(key, value);
        int bucketIndex = newEntry.hashCode() % capacity;
        Postings retVal;

        HashNode<Term, Postings> existing = buckets[bucketIndex];
        if (existing == null) {
            retVal = addToEmptyBucket(newEntry, bucketIndex);
        } else {
            while (existing.next() != null) existing = existing.next();
            size++;
            retVal = existing.append(newEntry);
        }

        if (size >= loadFactor) {
            expandCapacity(expansionRate);
        }
        return retVal;
    }

    private Postings addToEmptyBucket(HashNode<Term, Postings> newEntry, int index) {
        buckets[index] = newEntry;
        size++;
        keySet.add(newEntry.getKey());
        return newEntry.getValue();
    }

    private void expandCapacity(double rate) {
        capacity = (int) (capacity * rate);
        System.arraycopy(buckets, 0, buckets, 0, capacity);
        loadFactor = capacity * loadFactor;
    }

    @Override
    public Postings remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Term, ? extends Postings> m) {
        for (Term term : m.keySet()) {
            put(term, m.get(term));
        }
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
        expansionRate = 1.5;
    }

    @Override
    public Set<Term> keySet() {
        return keySet;
    }

    @Override
    public Collection<Postings> values() {
        LinkedList<Postings> retVal = new LinkedList<>();
        for (Term term : keySet) {
            retVal.add(get(term));
        }
        return retVal;
    }

    public void setExpansionRate(double rate) {
        expansionRate = rate;
    }

    public double getExpansionRate() {
        return expansionRate;
    }

    @Override
    public Set<Entry<Term, Postings>> entrySet() {
        // TODO
        return null;
    }
}

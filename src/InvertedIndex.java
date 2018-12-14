import java.util.*;

public class InvertedIndex<Term extends String, Postings extends List>
        implements Map<Term, Postings> {
    private ArrayList<HashNode<Term, Postings>> buckets;
    private int size;
    private LinkedList<Term> docContent;
    private boolean indexing;
    private int capacity;
    private double loadFactor;
    private double expansionRate;
    // TODO Remove keySet
    private Set<Term> keySet;
    private Set<Entry<Term, Postings>> entrySet;

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
        return (size == 0);
    }

    @Override
    public boolean containsValue(Object value) {
        for (Term t : keySet) {
            if (value.equals(get(t))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        try {
            get(key);
        } catch (NoSuchElementException e) {
            return false;
        }

        return true;
    }

    @Override
    public Postings get(Object key) {
        HashNode<Term, Postings> entry = getNode(key);
        if (entry == null) {
            throw new NoSuchElementException();
        }
        while (entry != null) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
            entry = entry.next();
        }
        throw new NoSuchElementException();
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

        HashNode<Term, Postings> existing = buckets.get(bucketIndex);
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
        buckets.set(index, newEntry);
        size++;
        keySet.add(newEntry.getKey());
        return newEntry.getValue();
    }

    private void expandCapacity(double rate) {
        capacity = (int) (capacity * rate);
        loadFactor *= capacity;
        buckets = new ArrayList<>(capacity);
        for (Entry<Term, Postings> entry : entrySet) {
            put(entry.getKey(), entry.getValue());
        }
    }

    private HashNode<Term, Postings> getNode(Object key) {
        if (!(key instanceof String)) {
            throw new NoSuchElementException();
        }

        int bucketIndex = getBucketIndex((Term) key);
        return buckets.get(bucketIndex);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Postings remove(Object key) {
        HashNode currentNode = getNode(key);
        if (currentNode.getKey().equals(key)) {
            buckets.remove(getBucketIndex((Term) key));
            return (Postings) currentNode.getValue();
        }
        while (currentNode.next() != null || !currentNode.next().getKey().equals(key)) {
            currentNode = currentNode.next();
        }
        if (currentNode.next() == null) {
            throw new NoSuchElementException();
        }
        HashNode rightNode = currentNode.next().next();
        HashNode objectNode;
        if (rightNode != null) {
            // If the rightNode exists
            objectNode = currentNode.next();
            currentNode.setNext(rightNode);
        } else {
            // If the objectNode is the last node in the list
            objectNode = rightNode;
            currentNode.setNext(null);
        }
        return (Postings) objectNode.getValue();
    }

    @Override
    public void putAll(Map<? extends Term, ? extends Postings> m) {
        for (Term term : m.keySet()) {
            put(term, m.get(term));
        }
    }

    private int getBucketIndex(Term key) {
        return key.hashCode() % capacity;
    }

    @Override
    public void clear() {
        clear(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public void clear(int capacity) {
        this.capacity = capacity;
        buckets = new ArrayList<>(capacity);
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
        return entrySet;
    }
}

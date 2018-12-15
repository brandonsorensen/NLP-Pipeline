import java.io.FileNotFoundException;
import java.util.*;

/**
 * A class to represent an inverted index of terms and their respective documents.
 * In implementation it is a HashMap with a view tweaks. Most notably, attempting to
 * put a term that already exists in the <\code>entrySet</\code> does not result in an
 * exception or override the value but instead adds the term's document ID to the
 * postings list.
 *
 * Each term is mapped to a postings list, which is itself just a list of document IDs.
 * The value-collection need only implement Java's List interface.
 * @param <Term> a term in the collection
 * @param <Postings> an ordered collection of documents that the term appears in
 */
public class InvertedIndex<Term extends String, Postings extends SortedSet<Document>>
        implements Map<Term, Postings> {
    private ArrayList<HashNode<Term, Postings>> buckets;
    private int size, capacity;
    private double loadFactor, expansionRate;
    private LinkedList<Term> docContent;
    private Set<Term> keySet;
    private Set<Entry<Term, Postings>> entrySet;
    private Indexer indexer;

    public static final int DEFAULT_CAPACITY = 16;
    public static final double DEFAULT_EXPANSION_RATE = 1.5;

    InvertedIndex() {
        clear();
    }

    InvertedIndex(int initialCapacity) {
        clear(initialCapacity);
    }

    InvertedIndex(int initialCapacity, String path) throws FileNotFoundException {
        clear(initialCapacity);
        index(path);
    }

    public void index(String path) throws FileNotFoundException {
        // FIXME
        return;
    }

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
        for (Entry entry: entrySet) {
            if (value.equals(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return keySet.contains(key);
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
        // TODO: If key exists, add to its postings list
        // TODO: Check if key exists using keySet
        if (key == null) {
            try {
                throw new NullKeyException("Null keys are not allowed.");
            } catch (NullKeyException ex) {
                System.out.println(ex);
            }
        }

        HashNode<Term, Postings> newEntry = new HashNode<>(key, value);
        if (entrySet.contains(newEntry)) {
            Postings toExtend = get(newEntry);
            toExtend.add(value.first());
            return value;
        }
        return putIfAbsent(key, value);
    }

    @Override
    public Postings putIfAbsent(Term key, Postings value) {
        HashNode<Term, Postings> newEntry = new HashNode<>(key, value);
        int bucketIndex = getBucketIndex(newEntry);
        HashNode<Term, Postings> startNode = buckets.get(bucketIndex);

        if (startNode == null) {
            return addToEmptyBucket(newEntry, bucketIndex);
        }
        // If we make it this far, we know the bucket is active
        return addToActiveBucket(newEntry, startNode);
    }

    private void updateIndex(HashNode<Term, Postings> newEntry) {
        size++;
        entrySet.add(newEntry);
        keySet.add(newEntry.getKey());
        if (size >= loadFactor) {
            expandCapacity(expansionRate);
        }
    }

    private Postings addToEmptyBucket(HashNode<Term, Postings> newEntry, int index) {
        buckets.set(index, newEntry);
        updateIndex(newEntry);
        return newEntry.getValue();
    }

    private Postings addToActiveBucket(HashNode<Term, Postings> newEntry,
                                       HashNode<Term, Postings> startNode) {
        HashNode<Term, Postings> currentNode = startNode;
        while (currentNode.next() != null) currentNode = currentNode.next();
        updateIndex(newEntry);
        return currentNode.append(newEntry);
    }

    private void expandCapacity(double rate) {
        capacity = (int) (capacity * rate);
        loadFactor *= capacity;
        buckets = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            buckets.add(null);
        }
        for (Entry<Term, Postings> entry : entrySet) {
            put(entry.getKey(), entry.getValue());
        }
    }

    private HashNode<Term, Postings> getNode(Object key) {
        if (!(key instanceof String) || !entrySet.contains(key)) {
            throw new NoSuchElementException();
        }

        int bucketIndex = getBucketIndex(getNode(key));
        return buckets.get(bucketIndex);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Postings remove(Object key) {
        HashNode<Term, Postings> currentNode = getNode(key);
        if (currentNode.getKey().equals(key)) {
            buckets.remove(getBucketIndex(currentNode));
            return (Postings) currentNode.getValue();
        }
        while (currentNode.next() != null || !currentNode.next().getKey().equals(key)) {
            currentNode = currentNode.next();
        }
        if (currentNode.next() == null) {
            throw new NoSuchElementException();
        }
        HashNode<Term, Postings> rightNode = currentNode.next().next();
        HashNode<Term, Postings> objectNode;
        if (rightNode != null) {
            // If the rightNode exists
            objectNode = currentNode.next();
            currentNode.setNext(rightNode);
        } else {
            // If the objectNode is the last node in the list
            objectNode = rightNode;
            currentNode.setNext(null);
        }
        size--;
        return objectNode.getValue();
    }

    @Override
    public void putAll(Map<? extends Term, ? extends Postings> m) {
        for (Term term : m.keySet()) {
            put(term, m.get(term));
        }
    }

    private int getBucketIndex(HashNode<Term, Postings> entry) {
        int remainder = entry.hashCode() % capacity;
        if (remainder < 0) {
            remainder += capacity;
        }
        return remainder;
    }

    @Override
    public void clear() {
        clear(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public void clear(int capacity) {
        this.capacity = capacity;
        buckets = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            buckets.add(null);
        }
        docContent = new LinkedList<>();
        size = 0;
        loadFactor = capacity * .75;
        expansionRate = DEFAULT_EXPANSION_RATE;
        indexer = new Indexer();
        keySet = new HashSet<>();
        entrySet = new HashSet<>();
    }

    @Override
    public Set<Term> keySet() {
        return keySet;
    }

    @Override
    public Collection<Postings> values() {
        LinkedList<Postings> retVal = new LinkedList<>();
        for (Entry term : entrySet) {
            retVal.add(get(term.getValue()));
        }
        return retVal;
    }

    public void setExpansionRate(double rate) {
        expansionRate = rate;
    }

    public double getExpansionRate() {
        return expansionRate;
    }

    public Term getDocAtIndex(int index) {
        return docContent.get(index);
    }

    @Override
    public Set<Entry<Term, Postings>> entrySet() {
        return entrySet;
    }

    public Map<Term, Integer> freqDist() {
        HashMap<Term, Integer> freqDist = new HashMap<>();
        for (Entry<Term, Postings> entry : entrySet) {
            freqDist.put(entry.getKey(), entry.getValue().size());
        }
        return freqDist;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        int counter = 0;
        for (Entry<Term, Postings> entry : entrySet) {
            builder.append(entry.getKey());
            builder.append(": ");
            builder.append(entry.getValue());
            if (counter >= 10) {
                break;
            }
            counter++;
            builder.append(",\n");
        }
        builder.append("}");
        return builder.toString();
    }

    private class Indexer {
    }
}

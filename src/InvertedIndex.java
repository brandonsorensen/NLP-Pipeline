import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class InvertedIndex<Term extends String, Postings extends List>
        implements Map<Term, Postings> {
    private ArrayList<HashNode<Term, Postings>> buckets;
    private int size;
    private LinkedList<Term> docContent;
    private int capacity;
    private double loadFactor;
    private double expansionRate;
    private Set<Entry<Term, Postings>> entrySet;
    private Indexer indexer;
    private boolean indexing;

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
        indexer.index(path);
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

        HashNode<Term, Postings> node = buckets.get(bucketIndex);
        if (node == null) {
            retVal = addToEmptyBucket(newEntry, bucketIndex);
        } else {
            retVal = addToActiveBucket(newEntry, node);
        }
        size++;
        entrySet.add(newEntry);
        if (size >= loadFactor) {
            expandCapacity(expansionRate);
        }

        return retVal;
    }

    private Postings addToEmptyBucket(HashNode<Term, Postings> newEntry, int index) {
        buckets.set(index, newEntry);
        return newEntry.getValue();
    }

    private Postings addToActiveBucket(HashNode<Term, Postings> newEntry,
                                       HashNode<Term, Postings> startNode) {
        HashNode<Term, Postings> currentNode = startNode;
        while (currentNode.next() != null) currentNode = currentNode.next();
        return currentNode.append(newEntry);
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
        size--;
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
        expansionRate = DEFAULT_EXPANSION_RATE;
        indexer = new Indexer();
    }

    @Override
    public Set<Term> keySet() {
        Set<Term> retVal = new HashSet<>();
        for (Entry<Term, Postings> entry : entrySet) {
            retVal.add(entry.getKey());
        }
        return retVal;
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

    public String getDocAtIndex(int index) {
        return docContent.get(index);
    }

    @Override
    public Set<Entry<Term, Postings>> entrySet() {
        return entrySet;
    }

    private class Indexer {
        private String[] clean(String line) {
            return line.split("\t");
        }

        LinkedList index(String filePath) throws FileNotFoundException {
            indexing = true;
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(",");
            LinkedList<String> retVal = new LinkedList<>();

            while (scanner.hasNext()) {
                String tweet = scanner.next().split("\t")[0];
                System.out.println(tweet);
                retVal.add(tweet);
            }

            indexing = false;
            return retVal;
        }
    }
}

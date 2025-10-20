package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;
    private final float loadFactor;

    public MyHashMap() {
        this.size = 0;
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int) (capacity * loadFactor);
        this.table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        int hash = calculateHash(key);
        int index = indexForBucket(hash, capacity);
        for (Node<K, V> currentNode = table[index]; currentNode != null;
                currentNode = currentNode.next) {
            if (currentNode.hash == hash && (currentNode.key == key
                    || (key != null && key.equals(currentNode.key)))) {
                currentNode.value = value;
                return;
            }
        }
        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        size++;
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = calculateHash(key);
        int index = indexForBucket(hash, capacity);
        for (Node<K, V> currentNode = table[index]; currentNode != null;
                currentNode = currentNode.next) {
            if (currentNode.hash == hash && (currentNode.key == key
                    || (key != null && key.equals(currentNode.key)))) {
                return currentNode.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int calculateHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int indexForBucket(int hash, int capacity) {
        int helper = capacity - 1;
        return hash & helper;
    }

    private void resize() {
        int newCapacity = capacity * GROW_FACTOR;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndexForBucket = indexForBucket(node.hash, newCapacity);
                node.next = newTable[newIndexForBucket];
                newTable[newIndexForBucket] = node;
                node = next;
            }
        }
        table = newTable;
        capacity = newCapacity;
        threshold = (int) (capacity * loadFactor);
    }

    private static class Node<K, V> {
        private final int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

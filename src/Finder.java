import java.io.BufferedReader;
import java.io.IOException;

/**
 * Finder
 * A puzzle written by Zach Blick
 * for Adventures in Algorithms
 * At Menlo School in Atherton, CA
 *
 * Completed by: Landon Moceri
 **/

public class Finder {

    private static final String INVALID = "INVALID KEY";

    public Finder() {}

    // Pair class to store keys and values
    public class Pair {
        String key;
        String val;

        // Constructor
        Pair(String key, String val) {
            this.key = key;
            this.val = val;
        }
    }

    // Hashmap class using linear probing
    public class HashMap {
        // Constants for initial size, radix, and capacity
        private final int INITIAL_SIZE = 16553;
        private final int RADIX = 256;
        private final double CAPACITY = 0.5;

        // Variables to keep track of filled size and max table size
        // Used to calculate load factor
        private int filledSize = 0;
        private int tableSize = INITIAL_SIZE;

        Pair[] table;

        // Constructor
        public HashMap() {
            table = new Pair[tableSize];
        }

        // Hash function using polynomial rolling hash
        public int hash(String key, int p) {
            int hash = 0;
            for (int i = 0; i < key.length(); i++) {
                hash = (hash * RADIX + key.charAt(i)) % p;
            }
            // Add p to ensure positive hash for indexing
            hash = (hash + p) % p;
            return hash;
        }

        // Resize function to double the table size
        public void resize() {
            // Double the table size and create a new table
            tableSize *= 2;
            Pair[] newTable = new Pair[tableSize];

            // Rehash all elements in the old table and insert into new table
            for (Pair pair : table) {
                if (pair != null) {
                    insertHelper(newTable, pair.key, pair.val);
                }
            }

            // Set the new table as the table
            table = newTable;
        }

        // Insert function to insert a key value pair into the table
        public void insert(String key, String val) {
            // If the load factor is greater than the max capacity, resize the table
            if (filledSize >= CAPACITY * tableSize) {
                resize();
            }
            // Insert the key value pair into the table and increment filled size
            insertHelper(table, key, val);
            filledSize++;
        }

        // Helper function to insert a key value pair into the table
        public void insertHelper(Pair[] t, String key, String val) {
            // Get the hash of the key
            int hash = hash(key, tableSize);
            // Linear probing to find an empty spot in the table
            while (t[hash] != null) {
                hash = (hash + 1) % tableSize;
            }
            // Insert the key value pair into the table
            t[hash] = new Pair(key, val);
        }

        // Query function to find whether a value exists given a key and return the value
        public String query(String key) {
            // Get the hash of the key
            int hash = hash(key, tableSize);
            // Linear probing to find the key in the table
            while (table[hash] != null) {
                // If the key is found, return the value
                if (table[hash].key.equals(key)) {
                    return table[hash].val;
                }
                hash = (hash + 1) % tableSize;
            }
            // Otherwise, return INVALID
            return INVALID;
        }
    }

    // Create the hashmap object
    private HashMap map = new HashMap();

    // Build the table from its csv file
    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {
        // Read each line of the csv file
        String line = br.readLine();
        while (line != null) {
            // Find the correct key and value
            String[] columns = line.split(",");
            String key = columns[keyCol];
            String val = columns[valCol];
            // Insert the key value pair into the hashmap
            map.insert(key, val);
            line = br.readLine();
        }
        br.close();
    }

    // Query the hashmap for a key's value
    public String query(String key) {
        return map.query(key);
    }
}
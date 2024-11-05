import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

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

    // Pair class to store key-value pairs
    // This is needed, as keys and values are needed to resolve collisions
    public class Pair {
        String key;
        String val;

        Pair (String key, String val) {
            this.key = key;
            this.val = val;
        }
    }

    public class HashMap {
        // 16553, which will cause the total size to be 16 B * 16443 = 263 KB
        private final int INITIAL_SIZE = 16553;
        private final int RADIX = 256;
        private final double LOAD_FACTOR = 0.5;


        private int filledSize = 0;
        private int tableSize = INITIAL_SIZE;

        Pair[] table;

        public HashMap() {
            table = new Pair[tableSize];
        }

        // Hash function to hash a string using polynomial rolling hash
        public int hash(String key, int p) {
            int hash = 0;
            for (int i = 0; i < key.length(); i++) {
                hash = (hash * RADIX + key.charAt(i)) % p;
            }
            // Add p to ensure the hash is positive
            hash = (hash + p) % p;
            return hash;
        }

        public void resize() {
            tableSize *= 2;
            filledSize = 0;
            Pair[] newTable = new Pair[tableSize];

            for (Pair pair : table) {
                if (pair != null) {
                    insertPrimitive(newTable, pair.key, pair.val);
                    filledSize++;
                }
            }

            table = newTable;
        }

        public void insert(String key, String val) {
            if (LOAD_FACTOR * 0.5 <= filledSize) {
                resize();
            }
            insertPrimitive(table, key, val);
            filledSize++;
        }

        public void insertPrimitive(Pair[] t, String key, String val) {
            int hash = hash(key, tableSize);
            if (t[hash] == null) {
                t[hash] = new Pair(key, val);
            } else {
                int i = hash;
                while (t[i] != null) {
                    i++;
                }
                t[i] = new Pair(key, val);
            }
        }

        public String query(String key) {
            int hash = hash(key, tableSize);
            if (table[hash] != null && table[hash].key.equals(key)) {
                return table[hash].val;
            } else {
                int i = hash;
                while (table[i] != null && !table[i].key.equals(key)) {
                    i++;
                }
                if (table[i] != null) {
                    return table[i].val;
                }
            }
            return INVALID;
        }
    }

    private HashMap map = new HashMap();

    // This function should build a hash table from the given BufferedReader
    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {
        // Read the first line
        String line = br.readLine();

        // Iterate over each subsequent line
        while (line != null) {
            // Grab the specified columns to get the key and value
            String[] columns = line.split(",");
            String key = columns[keyCol];
            String val = columns[valCol];

            // Insert the key-value pair into the hash table
            map.insert(key, val);

            line = br.readLine();
        }
        br.close();
    }

    // Finds whether a value exists for a given key in the hash table
    public String query(String key){
        return map.query(key);
    }
}
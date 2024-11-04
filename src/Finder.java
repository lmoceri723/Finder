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

    // Constants
    private static final String INVALID = "INVALID KEY";
    private static final int p = 100003;
    private static final int radix = 256;

    // Pair class to store key-value pairs
    // This is needed, as keys and values are needed to resolve collisions
    public class Pair {
        String key;
        String val;

        Pair(String key, String val) {
            this.key = key;
            this.val = val;
        }
    }

    // Array of pair ArrayLists to store the hash table
    ArrayList<Pair>[] table = (ArrayList<Pair>[]) new ArrayList[p];

    public Finder() {}

    // Hash function to hash a string using polynomial rolling hash
    public int hash(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash * radix + key.charAt(i)) % p;
        }
        // Add p to ensure the hash is positive
        hash = (hash + p) % p;
        return hash;
    }

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
            insert(key, val);

            line = br.readLine();
        }
        br.close();
    }

    // Function to insert a pair into the hash table
    public void insert(String key, String val){
        // Hash the key
        int hash = hash(key);

        // Create an ArrayList if one does not exist
        if (table[hash] == null) {
            table[hash] = new ArrayList<>();
        }
        // Add the pair to the hash table
        table[hash].add(new Pair(key, val));
    }

    // Finds whether a value exists for a given key in the hash table
    public String query(String key){
        // Hash the key

        int hash = hash(key);
        // Check if the hash table entry is empty
        if (table[hash] != null) {
            // Search for the key in the hash table
            for (Pair pair : table[hash]) {
                // Return the correct value if the key is found
                if (pair.key.equals(key)) {
                    return pair.val;
                }
            }
        }
        return INVALID;
    }
}
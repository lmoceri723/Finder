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
    private static final int p = 249998741;
    private static final int radix = 256;

    public class Pair {
        String key;
        String val;

        Pair(String key, String val) {
            this.key = key;
            this.val = val;
        }
    }

    ArrayList<Pair>[] table = (ArrayList<Pair>[]) new ArrayList[p];

    public Finder() {}

    // This function should build a hash table from the given BufferedReader
    public int hash(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash * radix + key.charAt(i)) % p;
        }
        hash = (hash + p) % p;
        return hash;
    }

    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {
        String line = br.readLine();
        while (line != null) {
            String[] columns = line.split(",");
            String key = columns[keyCol];
            String val = columns[valCol];
            int hash = hash(key);

            if (table[hash] == null) {
                table[hash] = new ArrayList<>();
            }
            table[hash].add(new Pair(key, val));

            line = br.readLine();
        }
        br.close();
    }

    public String query(String key){
        int hash = hash(key);
        if (table[hash] != null) {
            // Search for the key in the hash table
            for (Pair pair : table[hash]) {
                if (pair.key.equals(key)) {
                    return pair.val;
                }
            }
        }
        return INVALID;
    }
}
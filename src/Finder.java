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

    public class Pair {
        String key;
        String val;

        Pair(String key, String val) {
            this.key = key;
            this.val = val;
        }
    }

    public class HashMap {
        private final int INITIAL_SIZE = 16553;
        private final int RADIX = 256;
        private final double CAPACITY = 0.5;

        private int filledSize = 0;
        private int tableSize = INITIAL_SIZE;

        Pair[] table;

        public HashMap() {
            table = new Pair[tableSize];
        }

        public int hash(String key, int p) {
            int hash = 0;
            for (int i = 0; i < key.length(); i++) {
                hash = (hash * RADIX + key.charAt(i)) % p;
            }
            hash = (hash + p) % p;
            return hash;
        }

        public void resize() {
            tableSize *= 2;
            Pair[] newTable = new Pair[tableSize];

            for (Pair pair : table) {
                if (pair != null) {
                    insertHelper(newTable, pair.key, pair.val);
                }
            }

            table = newTable;
        }

        public void insert(String key, String val) {
            if (filledSize >= CAPACITY * tableSize) {
                resize();
            }
            insertHelper(table, key, val);
            filledSize++;
        }

        public void insertHelper(Pair[] t, String key, String val) {
            int hash = hash(key, tableSize);
            while (t[hash] != null) {
                hash = (hash + 1) % tableSize;
            }
            t[hash] = new Pair(key, val);
        }

        public String query(String key) {
            int hash = hash(key, tableSize);
            while (table[hash] != null) {
                if (table[hash].key.equals(key)) {
                    return table[hash].val;
                }
                hash = (hash + 1) % tableSize;
            }
            return INVALID;
        }
    }

    private HashMap map = new HashMap();

    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {
        String line = br.readLine();
        while (line != null) {
            String[] columns = line.split(",");
            String key = columns[keyCol];
            String val = columns[valCol];
            map.insert(key, val);
            line = br.readLine();
        }
        br.close();
    }

    public String query(String key) {
        return map.query(key);
    }
}
package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class UTXOPool {

    // The current collection of UTXOs, with each one mapped to its corresponding transaction output
    // main.UTXO is the key
    // Output is the value
    private HashMap<UTXO, Transaction.Output> H;

    /**
     * constructor
     * get ready for the data structure
     */
    public UTXOPool() {
        H = new HashMap<UTXO, Transaction.Output>();
    }

    /**
     * copy constructor
     * Creates a new main.UTXOPool that is a copy of {@code uPool}
     */
    public UTXOPool(UTXOPool uPool) {
        H = new HashMap<UTXO, Transaction.Output>(uPool.H);
    }

    /** Adds a mapping from main.UTXO {@code utxo} to transaction output @code{txOut} to the pool */
    public void addUTXO(UTXO utxo, Transaction.Output txOut) {
        H.put(utxo, txOut);
    }

    /** Removes the main.UTXO {@code utxo} from the pool */
    public void removeUTXO(UTXO utxo) {
        H.remove(utxo);
    }

    /**
     * get Output by specifying a main.UTXO
     * @param ut
     * @return the Output in specified transaction or null
     * output corresponding to main.UTXO {@code utxo} or null if {@code utxo} is not in the pool.
     */
    public Transaction.Output getTxOutput(UTXO ut) {
        return H.get(ut);
    }

    /**
     * confirm that whether the main.UTXOPool contain some main.UTXO
     * @param utxo
     * @return true if main.UTXO {@code utxo} is in the pool and false otherwise
     */
    public boolean contains(UTXO utxo) {
        return H.containsKey(utxo);
    }

    /**
     * get the key set of H, the Map for (main.UTXO, transaction.Output)
     * @return an {@code ArrayList} of all UTXOs in the pool
     */
    public ArrayList<UTXO> getAllUTXO() {
        Set<UTXO> setUTXO = H.keySet();
        ArrayList<UTXO> allUTXO = new ArrayList<UTXO>();
        for (UTXO ut : setUTXO) {
            allUTXO.add(ut);
        }
        return allUTXO;
    }
}

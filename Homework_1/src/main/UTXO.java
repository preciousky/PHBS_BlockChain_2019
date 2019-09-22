package main;

import java.util.Arrays;

public class UTXO implements Comparable<UTXO> {

    // Hash of the transaction from which this main.UTXO originates
    private byte[] txHash;
    // Index of the corresponding output in said transaction
    private int index;

    /**
     * constructor
     * Creates a new main.UTXO corresponding to the output with index <index> in the transaction whose
     * hash is {@code txHash}
     */
    public UTXO(byte[] txHash, int index) {
        this.txHash = Arrays.copyOf(txHash, txHash.length);
        this.index = index;
    }

    /**
     * Compares this main.UTXO to the one specified by {@code other}, considering them equal if they have
     * {@code txHash} arrays with equal contents
     * and
     * equal {@code index} values
     */
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }

        UTXO utxo = (UTXO) other;
        byte[] hash = utxo.txHash;
        int in = utxo.index;
        if (hash.length != txHash.length || index != in)
            return false;
        for (int i = 0; i < hash.length; i++) {
            if (hash[i] != txHash[i])
                return false;
        }
        return true;
    }

    /**
     * Simple implementation of a main.UTXO hashCode that respects equality of UTXOs
     * i.e., utxo1.equals(utxo2)  =>  utxo1.hashCode() == utxo2.hashCode()
     */
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + index;
        hash = hash * 31 + Arrays.hashCode(txHash);
        return hash;
    }

    /**
     * Compares this main.UTXO to the one specified by {@code utxo}
     * @param utxo another main.UTXO
     * @return if the latter is greater return -1, if the former is greater, return 1, if they equals, return 0
     */
    public int compareTo(UTXO utxo) {
        byte[] hash = utxo.txHash;
        int in = utxo.index;
        // if the index of the latter is greater, return -1
        if (in > index)
            return -1;
        // if the index of the former is greater, return 1
        else if (in < index)
            return 1;
        else {
            // if the txHash of the latter is greater, return -1
            int len1 = txHash.length;
            int len2 = hash.length;
            if (len2 > len1)
                return -1;
            else if (len2 < len1)
                return 1;
            else {
                for (int i = 0; i < len1; i++) {
                    if (hash[i] > txHash[i])
                        return -1;
                    else if (hash[i] < txHash[i])
                        return 1;
                }
                return 0;
            }
        }
    }

    /** @return the transaction hash of this main.UTXO */
    public byte[] getTxHash() {
        return txHash;
    }

    /** @return the index of this main.UTXO */
    public int getIndex() {
        return index;
    }
}

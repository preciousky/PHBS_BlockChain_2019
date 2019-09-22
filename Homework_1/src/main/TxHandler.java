package main;

import java.util.ArrayList;

public class TxHandler {

    private UTXOPool pool;

    /**
     * Creates a public ledger whose current main.UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the main.UTXOPool(main.UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        // IMPLEMENT THIS
        this.pool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current main.UTXO pool,
     * (2) the signatures on each input of {@code tx} are valid,
     * (3) no main.UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     * values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        System.out.println("Get into isValidTx function.");
        if (tx == null) {
            System.out.println("The input tx is null, return false!");
            System.out.println("#######################Finish the isValid function.#######################");
            return false;
        }
        double outputSum = 0;
        double inputSum = 0;
        // A buffer of main.UTXO to confirm there is no multiple claims inputs
        UTXOPool bufferPool = new UTXOPool();
        /*
        make sure all inputs are available
        1. included in the main.UTXO for (1)
        2. get the sum of them for (5)
        3. no multiple claimed main.UTXO for (3)
        4. the sig is valid for (2)
         */
        for (int i = 0; i < tx.numInputs(); i++) {
            Transaction.Input in = tx.getInput(i);
            // package the input into a main.UTXO
            UTXO u = new UTXO(in.prevTxHash, in.outputIndex);
            System.out.println("############====----1----====#############");
            if (!pool.contains(u)) {
                System.out.println("The prevHash in input[" + i + "] is not contained in UTXOPool, return false!");
                System.out.println("#######################Finish the isValid function.#######################");
                return false;
            }
            System.out.println("############====----2----====#############");
            if (bufferPool.contains(u)) {
                System.out.println("Double-spend in the same transaction. return false.");
                System.out.println("#######################Finish the isValid function.#######################");
                return false;
            }
            System.out.println("############====----3----====#############");
            Transaction.Output preOutput = this.pool.getTxOutput(u);
            if (preOutput == null) {
                System.out.println("#######################Finish the isValid function.#######################");
                return false;
            }
            System.out.println("############====----4----====#############");
            /** verifySignature ( PK, data, Sig) */
            if (!Crypto.verifySignature(preOutput.address, tx.getRawDataToSign(i), in.signature)) {
                System.out.println("Signature is not valid, return false!");
                System.out.println("#######################Finish the isValid function.#######################");
                return false;
            }
            System.out.println("############====----5----====#############");
            // key(utxo) and value(preOutput) are both valid, update the bufferPool
            bufferPool.addUTXO(u, preOutput);
            inputSum += preOutput.value;
        }

        System.out.println("############====----6----====#############");
        for (int i = 0; i < tx.numOutputs(); i++) {
            Transaction.Output out = tx.getOutput(i);
            if (out.value < 0) {
                System.out.println("The prevHash in output[" + i + "] is negative, return false!");
                System.out.println("#######################Finish the isValid function.#######################");
                return false;
            }
            outputSum += out.value;
        }
//        System.out.println("Sum of Inputs:" + inputSum);
//        System.out.println("Sum of Outputs:" + outputSum);
        System.out.println("############====----7----====#############");
        if (outputSum > inputSum) {
            System.out.println("OutputSum is greater than InputSum, return false!");
        }
        System.out.println("############====----8----====#############");
        System.out.println("#######################Finish the isValid function.#######################");
        return outputSum <= inputSum;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current main.UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        ArrayList<Transaction> ansList = new ArrayList<Transaction>();
        for (Transaction tx : possibleTxs) {
            // if the tx is not valid, ignore it.
            if (!isValidTx(tx))
                continue;
            // we accept the tx in this set of transactions
            ansList.add(tx);
            /** update UTXOPool */
            // remove the UTXO based on the inputs
            for (Transaction.Input input : tx.getInputs()) {
                UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
                this.pool.removeUTXO(utxo);
            }
            // add the UTXO based on the outputs
            for (int i = 0; i < tx.numOutputs(); i++) {
                Transaction.Output output = tx.getOutput(i);
                this.pool.addUTXO(new UTXO(tx.getHash(), i), output);
            }
        }
        Transaction[] ans = new Transaction[ansList.size()];
        for (int i = 0; i < ansList.size(); i++) {
            ans[i] = ansList.get(i);
        }
        return ans;
    }
}

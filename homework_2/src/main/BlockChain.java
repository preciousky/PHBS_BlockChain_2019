package main;

// main.Block Chain should maintain only limited block nodes to satisfy the functions
// You should not have all the blocks added to the block chain in memory 
// as it would cause a memory overflow.


import java.util.ArrayList;
import java.util.HashMap;

public class BlockChain {
    public static final int CUT_OFF_AGE = 10;
    private HashMap<byte[], BlockNode> blocks = new HashMap<byte[], BlockNode>();
    private TransactionPool transactionPool;
    private int counter; // count for the block in each layer

    public int getMaxHeight() {
        return maxHeight;
    }

    private int maxHeight;


    /**
     * create an empty block chain with just a genesis block. Assume {@code genesisBlock} is a valid
     * block
     */
    public BlockChain(Block genesisBlock) {
        UTXOPool genesisUTXOPool = new UTXOPool();
        Transaction.Output baseOutput = genesisBlock.getCoinbase().getOutput(0);
        genesisUTXOPool.addUTXO(new UTXO(genesisBlock.getCoinbase().getHash(), 0), baseOutput);
        for (Transaction tx : genesisBlock.getTransactions()) {
            for (int i = 0; i < tx.numOutputs(); i++) {
                Transaction.Output output = tx.getOutput(i);
                genesisUTXOPool.addUTXO(new UTXO(tx.getHash(), i), output);
            }
        }
        BlockNode bn = new BlockNode(genesisBlock, genesisUTXOPool, 0, 0);
        blocks.put(genesisBlock.getHash(), bn);
        counter = 0;
        maxHeight = 0;
        transactionPool = new TransactionPool();
    }

    /**
     * Get the maximum height block
     * If there are multiple blocks at the same height, return the oldest block in getMaxHeightBlock() function.
     */
    public Block getMaxHeightBlock() {
        for (HashMap.Entry<byte[], BlockNode> entry : blocks.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
            if (entry.getValue().getHeight() == this.maxHeight && entry.getValue().getOrder() == 0) {
                return entry.getValue().getBlock();
            }
        }
        return null;
    }

    /**
     * Get the main.UTXOPool for mining a new block on top of max height block
     */
    public UTXOPool getMaxHeightUTXOPool() {
        for (HashMap.Entry<byte[], BlockNode> entry : blocks.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
            if (entry.getValue().getHeight() == this.maxHeight && entry.getValue().getOrder() == 0) {
                return entry.getValue().getUTXOPool();
            }
        }
        return null;
    }

    /**
     * Get the transaction pool to mine a new block
     */
    public TransactionPool getTransactionPool() {
        return this.transactionPool;
    }

    /**
     * Add {@code block} to the block chain if it is valid. For validity, all transactions should be
     * valid and block should be at {@code height > (maxHeight - CUT_OFF_AGE)}.
     *
     * <p>
     * For example, you can try creating a new block over the genesis block (block height 2) if the
     * block chain height is {@code <=
     * CUT_OFF_AGE + 1}. As soon as {@code height > CUT_OFF_AGE + 1}, you cannot create a new block
     * at height 2.
     * <p>
     * A new genesis block won’t be mined.
     * If you receive a block which claims to be a genesis block (parent is a null hash)
     * in the addBlock(Block b) function, you can return false.
     *
     * @return true if block is successfully added
     */
    public boolean  addBlock(Block block) {
        if (block.getPrevBlockHash() == null)
            return false;
        BlockNode bn = blocks.get(block.getPrevBlockHash());
        if (bn != null) {
            UTXOPool utxoPool = bn.getUTXOPool();
            utxoPool.addUTXO(new UTXO(block.getCoinbase().getHash(),0), block.getCoinbase().getOutput(0));
            for (Transaction tx : block.getTransactions()) {
                /** update main.UTXOPool */
                // remove the main.UTXO based on the inputs
                for (Transaction.Input input : tx.getInputs()) {
                    UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
                    utxoPool.removeUTXO(utxo);
                }
                // add the main.UTXO based on the outputs
                for (int i = 0; i < tx.numOutputs(); i++) {
                    Transaction.Output output = tx.getOutput(i);
                    utxoPool.addUTXO(new UTXO(tx.getHash(), i), output);
                }
                this.transactionPool.removeTransaction(tx.getHash());
            }
            int height = bn.getHeight();
            if (height == this.maxHeight) {
                this.counter = 0;
                this.maxHeight++;
                BlockNode newBn = new BlockNode(block, utxoPool, height + 1, counter);
                this.freeMemory();
                blocks.put(block.getHash(),newBn);
                return true;
            } else if (height == this.maxHeight - 1) {
                BlockNode newBn = new BlockNode(block, utxoPool, height + 1, ++this.counter);
                blocks.put(block.getHash(),newBn);
                return true;
            } else {
                BlockNode newBn = new BlockNode(block, utxoPool, height + 1, -1);
                blocks.put(block.getHash(),newBn);
                return true;
            }
        }
        return false;
    }

    /**
     * Add a transaction to the transaction pool
     */
    public void addTransaction(Transaction tx) {
        transactionPool.addTransaction(tx);
    }

    /**
     * free the node can be abandoned.
     */
    public void freeMemory() {
        int temp = this.maxHeight - CUT_OFF_AGE - 1;
        ArrayList<byte[]> list = new ArrayList<byte[]>();
        for (byte[] key : blocks.keySet()){
            if (blocks.get(key).getHeight() == temp) {
                list.add(key);
            }
        }
        for(byte[] key: list){
            blocks.remove(key);
        }
    }
}
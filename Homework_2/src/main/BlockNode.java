package main;

public class BlockNode {
    private Block block;
    private int height;
    private int order;
    private UTXOPool utxoPool;

    public BlockNode(Block block, UTXOPool utxoPool, int height, int order) {
        this.block = block;
        this.height = height;
        this.order = order;
        this.utxoPool = utxoPool;
    }

    public Block getBlock() {
        return block;
    }

    public int getHeight() {
        return height;
    }

    public int getOrder() {
        return order;
    }

    public UTXOPool getUTXOPool() {
        return utxoPool;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setUtxoPool(UTXOPool utxoPool) {
        this.utxoPool = utxoPool;
    }
}

package test.main;

import main.*;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import test.main.CreateKeys;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/** 
* BlockChain Tester. 
* 
* @author <Authors name> 
* @since <pre>10ÔÂ 17, 2019</pre> 
* @version 1.0 
*/ 
public class BlockChainTest {
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    private byte[] genesisHashCode;
    private PublicKey initPK;
    private PrivateKey initSK;
    private BlockChain blockChain;
    private Block genesisBlock;
@Before
public void before() throws Exception {
    Map<String, Object> keyMap;
    keyMap = CreateKeys.initKey();
    initPK = (PublicKey) keyMap.get(PUBLIC_KEY);
    initSK = (PrivateKey) keyMap.get(PRIVATE_KEY);
    this.genesisBlock = new Block(null, initPK);
    this.genesisBlock.finalize();
    this.genesisHashCode = genesisBlock.getCoinbase().getHash();
    this.blockChain = new BlockChain(genesisBlock);
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: getMaxHeightBlock() 
* 
*/ 
@Test
public void testGetMaxHeightBlock() throws Exception {
    Block testb = blockChain.getMaxHeightBlock();
    assertEquals(genesisBlock.getHash(),testb.getHash());
} 

/** 
* 
* Method: getMaxHeightUTXOPool() 
* 
*/ 
@Test
public void testGetMaxHeightUTXOPool() throws Exception {
    UTXOPool utxoPool = blockChain.getMaxHeightUTXOPool();
    assertEquals(true,utxoPool.contains(new UTXO(genesisHashCode,0)));
}

/** 
* 
* Method: getTransactionPool() 
* 
*/ 
@Test
public void testGetTransactionPool() throws Exception { 
this.testAddTransaction();
TransactionPool pool = this.blockChain.getTransactionPool();
    assertEquals(5, pool.getTransactions().size());
} 

/** 
* 
* Method: addBlock(Block block) 
* 
*/ 
@Test
public void testAddBlock() throws Exception {
    /**
     * generate a block here and add it to the blockchain
     */
    Signature sign = Signature.getInstance("SHA256withRSA");
    Map<String, Object> KAs = CreateKeys.initKey();
    Block testBlock = new Block(this.genesisBlock.getHash(), (PublicKey) KAs.get(PUBLIC_KEY));
    testBlock.finalize();
    this.blockChain.addBlock(testBlock);
    assertEquals(1, this.blockChain.getMaxHeight());
} 

/** 
* 
* Method: addTransaction(Transaction tx) 
* 
*/ 
@Test
public void testAddTransaction() throws Exception {
    Signature sign = Signature.getInstance("SHA256withRSA");
    // prepare for 6 key pairs
    Map<String, Object> KAs = CreateKeys.initKey();
    Map<String, Object> KBs = CreateKeys.initKey();
    Map<String, Object> KCs = CreateKeys.initKey();
    Map<String, Object> KDs = CreateKeys.initKey();
    Map<String, Object> KEs = CreateKeys.initKey();
    Map<String, Object> KFs = CreateKeys.initKey();
//        // debugging code
//        System.out.println("SK: " + CreateKeys.getPrivateKey(keypair));
//        System.out.println("PK: " + CreateKeys.getPublicKey(keypair));
    /** generate transaction 0 */
    Transaction tx0 = new Transaction();
    tx0.addInput(genesisHashCode, 0);
    tx0.addOutput(100, (PublicKey) KAs.get(PUBLIC_KEY));
    tx0.addOutput(300, (PublicKey) KBs.get(PUBLIC_KEY));
    tx0.addOutput(600, (PublicKey) KCs.get(PUBLIC_KEY));
    /** add signature 0*/
    sign.initSign(initSK);
    sign.update(tx0.getRawDataToSign(0));
    tx0.addSignature(sign.sign(), 0);
    /** add hash value */
    tx0.finalize();

    /** generate transaction 1 */
    Transaction tx1 = new Transaction();
    tx1.addInput(tx0.getHash(), 0);
    tx1.addOutput(50, (PublicKey) KDs.get(PUBLIC_KEY));
    tx1.addOutput(50, (PublicKey) KFs.get(PUBLIC_KEY));
    /** add signature 0*/
    sign.initSign((PrivateKey) KAs.get(PRIVATE_KEY));
    sign.update(tx1.getRawDataToSign(0));
    tx1.addSignature(sign.sign(), 0);
    /** add hash value */
    tx1.finalize();

    /** generate transaction 2 */
    Transaction tx2 = new Transaction();
    tx2.addInput(tx0.getHash(), 1);
    tx2.addOutput(300, (PublicKey) KDs.get(PUBLIC_KEY));
    /** add signature 0*/
    sign.initSign((PrivateKey) KBs.get(PRIVATE_KEY));
    sign.update(tx2.getRawDataToSign(0));
    tx2.addSignature(sign.sign(), 0);
    /** add hash value */
    tx2.finalize();

    /** generate transaction 3 */
    Transaction tx3 = new Transaction();
    tx3.addInput(tx0.getHash(), 1);
    tx3.addOutput(300, (PublicKey) KEs.get(PUBLIC_KEY));
    /** add signature 0*/
    sign.initSign((PrivateKey) KBs.get(PRIVATE_KEY));
    sign.update(tx3.getRawDataToSign(0));
    tx3.addSignature(sign.sign(), 0);
    /** add hash value */
    tx3.finalize();

    /** generate transaction 4 */
    Transaction tx4 = new Transaction();
    tx4.addInput(tx0.getHash(), 2);
    tx4.addOutput(600, (PublicKey) KFs.get(PUBLIC_KEY));
    /** add signature 0*/
    sign.initSign((PrivateKey) KCs.get(PRIVATE_KEY));
    sign.update(tx4.getRawDataToSign(0));
    tx4.addSignature(sign.sign(), 0);
    tx4.finalize();

    /** generate transaction 5 */
    Transaction tx5 = new Transaction();
    tx5.addInput(tx1.getHash(), 0);
    tx5.addInput(tx1.getHash(), 1);
    tx5.addInput(tx3.getHash(), 0);
    tx5.addInput(tx4.getHash(), 0);
    tx5.addOutput(1000, (PublicKey) KAs.get(PUBLIC_KEY));
    /** add signature 0*/
    sign.initSign((PrivateKey) KDs.get(PRIVATE_KEY));
    sign.update(tx5.getRawDataToSign(0));
    tx5.addSignature(sign.sign(), 0);
    /** add signature 1*/
    sign.initSign((PrivateKey) KFs.get(PRIVATE_KEY));
    sign.update(tx5.getRawDataToSign(1));
    tx5.addSignature(sign.sign(), 1);
    /** add signature 2*/
    sign.initSign((PrivateKey) KEs.get(PRIVATE_KEY));
    sign.update(tx5.getRawDataToSign(2));
    tx5.addSignature(sign.sign(), 2);
    /** add signature 3*/
    sign.initSign((PrivateKey) KFs.get(PRIVATE_KEY));
    sign.update(tx5.getRawDataToSign(3));
    tx5.addSignature(sign.sign(), 3);
    /** add hash value */
    tx5.finalize();

    this.blockChain.addTransaction(tx1);
    this.blockChain.addTransaction(tx2);
    this.blockChain.addTransaction(tx3);
    this.blockChain.addTransaction(tx4);
    this.blockChain.addTransaction(tx5);

    assertEquals(5, blockChain.getTransactionPool().getTransactions().size());
}

} 

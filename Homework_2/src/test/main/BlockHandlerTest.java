package test.main;

import main.*;
import org.junit.*;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Map;

/**
 * BlockHandler Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>10ÔÂ 15, 2019</pre>
 */
public class BlockHandlerTest {
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    private byte[] genesisHashCode;
    private PublicKey initPK;
    private PrivateKey initSK;
    private BlockHandler blockHandler;
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
        this.blockHandler = new BlockHandler(blockChain);
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: processBlock(Block block)
     */
    @Test
    public void testProcessBlock() throws Exception {
//TODO: Test goes here...
        Signature sign = Signature.getInstance("SHA256withRSA");
        Map<String, Object> KAs = CreateKeys.initKey();
        Block testBlock = new Block(this.genesisBlock.getHash(), (PublicKey) KAs.get(PUBLIC_KEY));
        testBlock.finalize();
        this.blockChain.addBlock(testBlock);
        assertEquals(1, this.blockChain.getMaxHeight());
    }

    /**
     * Method: createBlock(PublicKey myAddress)
     */
    @Test
    public void testCreateBlock() throws Exception {
//TODO: Test goes here...
        Signature sign = Signature.getInstance("SHA256withRSA");
        // prepare for 6 key pairs
        Map<String, Object> K1_0s = CreateKeys.initKey();
        Map<String, Object> K1_1s = CreateKeys.initKey();
        Map<String, Object> K2s = CreateKeys.initKey();
        Map<String, Object> K3s = CreateKeys.initKey();
        Map<String, Object> K4s = CreateKeys.initKey();
        Map<String, Object> K5s = CreateKeys.initKey();
        Map<String, Object> K6s = CreateKeys.initKey();
        Map<String, Object> K7s = CreateKeys.initKey();
        Map<String, Object> K8s = CreateKeys.initKey();
        Map<String, Object> K9s = CreateKeys.initKey();
        Map<String, Object> K10s = CreateKeys.initKey();
        Map<String, Object> K11s = CreateKeys.initKey();

        /** generate transaction 1-0 */
        Transaction tx1_0 = new Transaction();
        tx1_0.addInput(genesisHashCode, 0);
        tx1_0.addOutput(25, (PublicKey) K1_0s.get(PUBLIC_KEY));
        /** add signature 0*/
        sign.initSign(initSK);
        sign.update(tx1_0.getRawDataToSign(0));
        tx1_0.addSignature(sign.sign(), 0);
        /** add hash value */
        tx1_0.finalize();

        /** generate transaction 1_1 */
        Transaction tx1_1 = new Transaction();
        tx1_1.addInput(genesisHashCode, 0);
        tx1_1.addOutput(25, (PublicKey) K1_1s.get(PUBLIC_KEY));
        /** add signature 0*/
        sign.initSign(initSK);
        sign.update(tx1_1.getRawDataToSign(0));
        tx1_1.addSignature(sign.sign(), 0);
        /** add hash value */
        tx1_1.finalize();

        /** generate transaction 2 */
        Transaction tx2 = new Transaction();
        tx2.addInput(tx1_0.getHash(), 0);
        tx2.addOutput(25, (PublicKey) K2s.get(PUBLIC_KEY));
        /** add signature 0*/
        sign.initSign((PrivateKey) K1_0s.get(PRIVATE_KEY));
        sign.update(tx2.getRawDataToSign(0));
        tx2.addSignature(sign.sign(), 0);
        /** add hash value */
        tx2.finalize();

        /** generate transaction 3 */
        Transaction tx3 = new Transaction();
        tx3.addInput(tx2.getHash(), 0);
        tx3.addOutput(25, (PublicKey) K3s.get(PUBLIC_KEY));
        /** add signature 0*/
        sign.initSign((PrivateKey) K2s.get(PRIVATE_KEY));
        sign.update(tx3.getRawDataToSign(0));
        tx3.addSignature(sign.sign(), 0);
        /** add hash value */
        tx3.finalize();

        /** generate transaction 4 */
        Transaction tx4 = new Transaction();
        tx4.addInput(tx3.getHash(), 0);
        tx4.addOutput(25, (PublicKey) K4s.get(PUBLIC_KEY));
        /** add signature 0*/
        sign.initSign((PrivateKey) K3s.get(PRIVATE_KEY));
        sign.update(tx4.getRawDataToSign(0));
        tx4.addSignature(sign.sign(), 0);
        tx4.finalize();

        /** generate transaction 5 */
        Transaction tx5 = new Transaction();
        tx5.addInput(tx4.getHash(), 0);
        tx5.addOutput(25, (PublicKey) K5s.get(PUBLIC_KEY));
        /** add signature 0*/
        sign.initSign((PrivateKey) K4s.get(PRIVATE_KEY));
        sign.update(tx5.getRawDataToSign(0));
        tx5.addSignature(sign.sign(), 0);
        tx5.finalize();

        /** generate transaction 6 */
        Transaction tx6 = new Transaction();
        tx6.addInput(tx5.getHash(), 0);
        tx6.addOutput(25, (PublicKey) K6s.get(PUBLIC_KEY));
        /** add signature 0*/
        sign.initSign((PrivateKey) K5s.get(PRIVATE_KEY));
        sign.update(tx6.getRawDataToSign(0));
        tx6.addSignature(sign.sign(), 0);
        tx6.finalize();

        /** generate transaction 7 */
        Transaction tx7 = new Transaction();
        tx7.addInput(tx6.getHash(), 0);
        tx7.addOutput(25, (PublicKey) K7s.get(PUBLIC_KEY));
        /** add signature 0*/
        sign.initSign((PrivateKey) K6s.get(PRIVATE_KEY));
        sign.update(tx7.getRawDataToSign(0));
        tx7.addSignature(sign.sign(), 0);
        tx7.finalize();

        /** generate transaction 8 */
        Transaction tx8 = new Transaction();
        tx8.addInput(tx7.getHash(), 0);
        tx8.addOutput(25, (PublicKey) K8s.get(PUBLIC_KEY));
        /** add signature 0*/
        sign.initSign((PrivateKey) K7s.get(PRIVATE_KEY));
        sign.update(tx8.getRawDataToSign(0));
        tx8.addSignature(sign.sign(), 0);
        tx8.finalize();

        /** generate transaction 9 */
        Transaction tx9 = new Transaction();
        tx9.addInput(tx8.getHash(), 0);
        tx9.addOutput(25, (PublicKey) K9s.get(PUBLIC_KEY));
        /** add signature 0*/
        sign.initSign((PrivateKey) K8s.get(PRIVATE_KEY));
        sign.update(tx9.getRawDataToSign(0));
        tx9.addSignature(sign.sign(), 0);
        tx9.finalize();

        /** generate transaction 10 */
        Transaction tx10 = new Transaction();
        tx10.addInput(tx9.getHash(), 0);
        tx10.addOutput(25, (PublicKey) K10s.get(PUBLIC_KEY));
        /** add signature 0*/
        sign.initSign((PrivateKey) K9s.get(PRIVATE_KEY));
        sign.update(tx10.getRawDataToSign(0));
        tx10.addSignature(sign.sign(), 0);
        tx10.finalize();

        /** generate transaction 11 */
        Transaction tx11 = new Transaction();
        tx11.addInput(tx10.getHash(), 0);
        tx11.addOutput(25, (PublicKey) K11s.get(PUBLIC_KEY));
        /** add signature 0*/
        sign.initSign((PrivateKey) K10s.get(PRIVATE_KEY));
        sign.update(tx11.getRawDataToSign(0));
        tx11.addSignature(sign.sign(), 0);
        tx11.finalize();

        this.blockHandler.processTx(tx1_0);
        assertEquals(1, blockHandler.getBlockChain().getTransactionPool().getTransactions().size());
        this.blockHandler.createBlock((PublicKey) K1_0s.get(PUBLIC_KEY));
        assertEquals(1,this.blockHandler.getBlockChain().getMaxHeight());

        Block testBlock = new Block(this.genesisBlock.getHash(), (PublicKey) K1_1s.get(PUBLIC_KEY));
        testBlock.addTransaction(tx1_1);
        testBlock.finalize();
        this.blockHandler.processBlock(testBlock);

        assertEquals((PublicKey)K1_0s.get(PUBLIC_KEY),this.blockHandler.getBlockChain().getMaxHeightBlock().getTransaction(0).getOutput(0).address);

        this.blockHandler.processTx(tx2);
        this.blockHandler.createBlock((PublicKey) K2s.get(PUBLIC_KEY));

        this.blockHandler.processTx(tx3);
        this.blockHandler.createBlock((PublicKey) K3s.get(PUBLIC_KEY));

        this.blockHandler.processTx(tx4);
        this.blockHandler.createBlock((PublicKey) K4s.get(PUBLIC_KEY));

        this.blockHandler.processTx(tx5);
        this.blockHandler.createBlock((PublicKey) K5s.get(PUBLIC_KEY));

        this.blockHandler.processTx(tx6);
        this.blockHandler.createBlock((PublicKey) K6s.get(PUBLIC_KEY));

        this.blockHandler.processTx(tx7);
        this.blockHandler.createBlock((PublicKey) K7s.get(PUBLIC_KEY));

        this.blockHandler.processTx(tx8);
        this.blockHandler.createBlock((PublicKey) K8s.get(PUBLIC_KEY));

        this.blockHandler.processTx(tx9);
        this.blockHandler.createBlock((PublicKey) K9s.get(PUBLIC_KEY));

        this.blockHandler.processTx(tx10);
        this.blockHandler.createBlock((PublicKey) K10s.get(PUBLIC_KEY));

        this.blockHandler.processTx(tx11);
        this.blockHandler.createBlock((PublicKey) K11s.get(PUBLIC_KEY));

        assertEquals(11,this.blockHandler.getBlockChain().getMaxHeight());

        Block testBlockForFreeMemory = new Block(this.genesisBlock.getHash(), (PublicKey) K1_1s.get(PUBLIC_KEY));
        testBlock.finalize();

        assertEquals(false, this.blockHandler.processBlock(testBlock));
    }
    @Test
    public void testAddGenesisBlock() throws Exception {
        Block testBlock = new Block(null, initPK);
        testBlock.finalize();
        assertEquals(false, this.blockHandler.getBlockChain().addBlock(testBlock));
    }
} 

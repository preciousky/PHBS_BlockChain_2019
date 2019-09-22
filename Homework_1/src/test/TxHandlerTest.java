package test;

import main.*;
import org.junit.Test;
import org.junit.Before;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * main.TxHandler Tester.
 * @author Edward Gao
 * @version 1.0
 * @since <pre>9ÔÂ 17, 2019</pre>
 */

public class TxHandlerTest {
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    private byte[] geniusHashCode;
    private PublicKey initPK;
    private PrivateKey initSK;
    private TxHandler txHandler;
    byte[] unvalidRawData = new byte[]{0, 0, 0, 0, 0, 0, 0};

    private void printTransactions(Transaction tx) {
        for (Transaction.Input input : tx.getInputs()) {
            System.out.println(input);
        }
        for (Transaction.Output output : tx.getOutputs()) {
            System.out.println(output);
        }
    }

    @Before
    public void before() throws Exception {
        Map<String, Object> keyMap;
        /** generate the keys */
        keyMap = CreateKeys.initKey();
//        // debugging code
//        String firstReceiverPK = CreateKeys.getPublicKey(keyMap);
//        System.out.println(publicKey);
//        String firstReceiverSK = CreateKeys.getPrivateKey(keyMap);
//        System.out.println(privateKey);
        initPK = (PublicKey) keyMap.get(PUBLIC_KEY);
        initSK = (PrivateKey) keyMap.get(PRIVATE_KEY);
        /** generate the very first transaction, UTXO, and UTXOPool */
        Transaction initTx = new Transaction();
        initTx.addOutput(1000, initPK);
        // set hash value
        initTx.finalize();
        geniusHashCode = initTx.getHash();
        // generate UTXO and then UTXOPool
        UTXO initUTXO = new UTXO(initTx.getHash(), 0);
        UTXOPool initUTXOPool = new UTXOPool();
        initUTXOPool.addUTXO(initUTXO, initTx.getOutput(0));
        /** Init the ledger (txHandler) ready */
        txHandler = new TxHandler(initUTXOPool);
    }

    /**
     * Method: isValidTx(main.Transaction tx)
     * test for null input
     */
    @Test
    public void testIsValidTx1() throws Exception {
        Transaction testTx = null;
        assertEquals(false, txHandler.isValidTx(testTx));
    }

    /**
     * Method: isValidTx(main.Transaction tx)
     * test for sum of inputs and sum of output
     */
    @Test
    public void testIsValidTx2() throws Exception {
        Transaction testTx = new Transaction();
        testTx.addInput(geniusHashCode, 0);
        /** set data for test */
        testTx.addOutput(1001, initPK); // initPK means that the coins flow return to the owner
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(initSK);
        sign.update(testTx.getRawDataToSign(0));
        testTx.addSignature(sign.sign(), 0);
        assertEquals(false, txHandler.isValidTx(testTx));
    }

    /**
     * Method: isValidTx(main.Transaction tx)
     * test for input not contained in UTXOPool
     */
    @Test
    public void testIsValidTx3() throws Exception {
        Transaction testTx = new Transaction();
        testTx.addInput(geniusHashCode, 0);
        /** set data for test */
        testTx.addInput(geniusHashCode, 1);
        testTx.addOutput(1000, initPK);
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(initSK);
        sign.update(testTx.getRawDataToSign(0));
        testTx.addSignature(sign.sign(), 0);
        testTx.addSignature(sign.sign(), 1);
        assertEquals(false, txHandler.isValidTx(testTx));
    }

    /**
     * Method: isValidTx(main.Transaction tx)
     * test for signature
     */
    @Test
    public void testIsValidTx4() throws Exception {
        Transaction testTx = new Transaction();
        testTx.addInput(geniusHashCode, 0);
        testTx.addOutput(1001, initPK); // initPK means that the coins flows to the owner in the cas
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(initSK);
        /** set data for test */
        sign.update(unvalidRawData);
        testTx.addSignature(sign.sign(), 0);
        assertEquals(false, txHandler.isValidTx(testTx));
    }

    /**
     * Method: isValidTx(main.Transaction tx)
     * test for negative output
     */
    @Test
    public void testIsValidTx5() throws Exception {
        Transaction testTx = new Transaction();
        testTx.addInput(geniusHashCode, 0);
        /** set data for test */
        testTx.addOutput(-1, initPK);
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(initSK);
        sign.update(testTx.getRawDataToSign(0));
        testTx.addSignature(sign.sign(), 0);
        assertEquals(false, txHandler.isValidTx(testTx));
    }

    /**
     * Method: isValidTx(main.Transaction tx)
     * test for double-spending input
     */
    @Test
    public void testIsValidTx6() throws Exception {
        Transaction testTx = new Transaction();
        testTx.addInput(geniusHashCode, 0);
        /** set data for test */
        testTx.addInput(geniusHashCode, 0);
        testTx.addOutput(2000, initPK);
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(initSK);
        sign.update(testTx.getRawDataToSign(0));
        testTx.addSignature(sign.sign(), 0);
        assertEquals(false, txHandler.isValidTx(testTx));
    }


    /**
     * Method: handleTxs(main.Transaction[] possibleTxs)
     */
    @Test
    public void testHandleTxs() throws Exception {
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
        tx0.addInput(geniusHashCode, 0);
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
        Transaction[] testTxs = new Transaction[]{tx0, tx1, tx2, tx3, tx4, tx5};
        Transaction[] expectedResult = new Transaction[]{tx0, tx1, tx2, tx4};
        assertArrayEquals(expectedResult, txHandler.handleTxs(testTxs));
    }
}

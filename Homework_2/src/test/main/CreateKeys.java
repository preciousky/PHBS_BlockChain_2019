package test.main;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import sun.misc.BASE64Encoder;

public class CreateKeys {

    public static final String KEY_ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * get the PK from KeyPair (the function is called usefully in debugging)
     * @param keyMap generated from initKey()
     * @return string of PK
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return (new BASE64Encoder()).encodeBuffer(key.getEncoded());
    }

    /**
     * get the SK from KeyPair (the function is called usefully in debugging)
     * @param keyMap generated from initKey()
     * @return string of SK
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return (new BASE64Encoder()).encodeBuffer(key.getEncoded());
    }

    /**
     * generate Keys and put them into a hash map
     * @return the hash map with PK and SK
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        // instantiate KeyPairGenerator with RSA and 1024 size
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        // get keyPair object
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // get PK and SK
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // put the keys into map
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }
}
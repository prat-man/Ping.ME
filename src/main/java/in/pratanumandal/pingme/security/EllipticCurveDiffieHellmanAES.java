package in.pratanumandal.pingme.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;

public class EllipticCurveDiffieHellmanAES {

    private final SecureRandom random;
    private PublicKey publicKey;
    private KeyAgreement keyAgreement;
    private byte[] sharedSecret;
    private byte[] secretKey;

    public EllipticCurveDiffieHellmanAES() {
        this.random = new SecureRandom();
        generateKeyExchangeParams();
    }

    private void generateKeyExchangeParams() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
            kpg.initialize(256);
            KeyPair kp = kpg.generateKeyPair();
            publicKey = kp.getPublic();
            keyAgreement = KeyAgreement.getInstance("ECDH");
            keyAgreement.init(kp.getPrivate());
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public void setReceiverPublicKey(PublicKey publicKey) {
        try {
            keyAgreement.doPhase(publicKey, true);
            sharedSecret = keyAgreement.generateSecret();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            secretKey = messageDigest.digest(sharedSecret);
        }
        catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public boolean isReady() {
        return sharedSecret != null;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    protected byte[] generateIV() {
        byte[] iv = new byte[12];
        random.nextBytes(iv);
        return iv;
    }

    public SecureObject encrypt(Object object) {
        try {
            // create cipher
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            // GCM parameters
            byte[] iv = generateIV();
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);

            // secret key
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, "AES");

            // initialize cipher
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            // encrypt
            return new SecureObject((Serializable) object, cipher, iv);
        }
        catch (NoSuchPaddingException |
               NoSuchAlgorithmException |
               IOException |
               InvalidKeyException |
               IllegalBlockSizeException |
               InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    public Object decrypt(SecureObject secureObject) {
        try {
            // create cipher
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            // GCM parameters
            byte[] iv = secureObject.getIV();
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);

            // secret key
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, "AES");

            // initialize cipher
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

            // decrypt
            return secureObject.getObject(cipher);
        }
        catch (NoSuchPaddingException |
               NoSuchAlgorithmException |
               IOException |
               InvalidKeyException |
               IllegalBlockSizeException |
               BadPaddingException |
               ClassNotFoundException |
               InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

}

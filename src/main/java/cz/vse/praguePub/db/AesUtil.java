package cz.vse.praguePub.db;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Třída pro dekódování hesel databázových uživatelů uložených v databázi zakódovaných pomocí hesel registrovaných uživatelů.
 * Používán je algoritmus AES.
 */
public class AesUtil {
    private final int keySize;
    private final int iterationCount;
    private Cipher cipher;

    public AesUtil(int keySize, int iterationCount) {
        this.keySize = keySize;
        this.iterationCount = iterationCount;

        try {
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            this.cipher = null;
        }
    }

    /**
     * Metoda decryptuje zakódované heslo databázového uživatele.
     * Toto heslo je zakódované heslem registrovaného uživatele aplikace uloženým v databázi.
     * @param raw neupravený output zakódovaného hesla v databázi
     * @param passPhrase heslo uživatele
     * @return decryptované heslo (heslo pro přihlášení do databáze)
     */
    public String decrypt(String raw, String passPhrase) {
        String[] splitRaw = raw.split("::");

        String salt = splitRaw[1];
        String iv = splitRaw[0];
        String cipherText = splitRaw[2];

        SecretKey key = generateKey(salt, passPhrase);
        byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, Base64.decodeBase64(cipherText));
        if (decrypted == null) return null;
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * Vygeneruje SecretKey
     * @param salt část zakódovaného hesla z databáze (2. položka)
     * @param passPhrase heslo uživatele aplikace
     * @return tajný klíč pro další použití v šifrování
     */
    private SecretKey generateKey(String salt, String passPhrase) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(
                    passPhrase.toCharArray(),
                    Hex.decodeHex(salt.toCharArray()),
                    this.iterationCount,
                    this.keySize
            );
            return new SecretKeySpec(skf.generateSecret(spec).getEncoded(), "AES");
        } catch ( NoSuchAlgorithmException
                | DecoderException
                | InvalidKeySpecException e
        ) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Konečné rozšifrování hesla databázového uživatele
     * @param encryptMode ENCRYPT/DECRYPT - vždy bude DECRYPT
     * @param secretKey vygenerováno metodou AesUtil.generateKey()
     * @param iv inicializační vektor, část neupraveného zakódovaného hesla z databáze (1. položka)
     * @param bytes samotné zašifrované heslo, část neupraveného zakódovaného hesla z databáze (3. položka)
     * @return byte[] s heslem databázového uživatele
     */
    private byte[] doFinal(int encryptMode, SecretKey secretKey, String iv, byte[] bytes) {
        try {
            this.cipher.init(
                    encryptMode,
                    secretKey,
                    new IvParameterSpec(Hex.decodeHex(iv))
            );
            return cipher.doFinal(bytes);
        } catch ( InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | BadPaddingException
                | DecoderException e
        ) {
            e.printStackTrace();
            return null;
        }
    }

    public static String fillTo16Chars(String toFill) {
        StringBuilder sb = new StringBuilder();
        int pswLength = toFill.length();

        for (int i = 0; i < 16; i++) {
            if (i < pswLength) sb.append(toFill.charAt(i));
            else sb.append("0");
        }
        return sb.toString();
    }
}

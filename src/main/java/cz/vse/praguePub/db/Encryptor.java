package cz.vse.praguePub.db;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

/**
 * Třída kódování/dekódování hesel pro přihlašování do databáze
 */
public final class Encryptor {
    /**
     * Zakóduje String pomocí klíče (String)<br>
     * <strong>Je třeba dodělat doplnění do 16 bitů</strong>
     * @param input vstup k zakódování
     * @param key 16bitový klíč pro AES
     * @return zakódovaný String (Optional&lt;String&gt;)
     */
    public static Optional<String> encrypt(String input, String key) {
        byte[] cipherText = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
            cipherText = cipher.doFinal(input.getBytes());
        } catch ( NoSuchPaddingException
                | NoSuchAlgorithmException
                | InvalidKeyException
                | IllegalBlockSizeException
                | BadPaddingException e
        ) {
            e.printStackTrace();
        }
        return Optional.ofNullable(Base64.getEncoder().encodeToString(cipherText));
    }

    /**
     * Dekóduje String pomocí klíče (String)<br>
     * <strong>Je třeba dodělat doplnění do 16 bitů</strong>
     * @param input vstup k dekódování
     * @param key 16bitový klíč pro AES
     * @return dekódovaný String (Optional&lt;String&gt;)
     */
    public static Optional<String> decrypt(String input, String key) {
        byte[] plainText = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"));
            plainText = cipher.doFinal(Base64.getDecoder().decode(input));
        } catch ( NoSuchPaddingException
                | NoSuchAlgorithmException
                | InvalidKeyException
                | IllegalBlockSizeException
                | BadPaddingException e
            ) {
            e.printStackTrace();
        }

        return (plainText != null ? Optional.of(new String(plainText)): Optional.empty());
    }
}

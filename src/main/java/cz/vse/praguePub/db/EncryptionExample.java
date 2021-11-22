package cz.vse.praguePub.db;

import lombok.val;

import static cz.vse.praguePub.db.Encryptor.*;

public class EncryptionExample {
    public static void main(String[] args) {
        val key = "abcd000011112222";
        val encrypted = encrypt("tvoje mama", key).orElse("");
        val decrypted = decrypt(encrypted, key).orElse("");

        System.out.println(encrypted);
        System.out.println(decrypted);

        assert (encrypted.equals(decrypted));
    }
}

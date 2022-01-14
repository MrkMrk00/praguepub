package cz.vse.praguePub;

import cz.vse.praguePub.util.AesUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AesUtilTest {
    private AesUtil au;

    @BeforeEach
    @Test
    public void vytvoreniInstance() {
        this.au = new AesUtil(128, 1000);
        assertNotNull(this.au);
    }

    @Test
    public void fillTo16Chars() {
        String l4 = "1234";
        String l16 = AesUtil.fillTo16Chars(l4);
        assertEquals(l16.length(), 16);
    }

    @Test
    public void decrypt() {
        String decryptovano = this.au.decrypt(
                "cd171fb1ff340bc3fe09fc029efe0879::4954c9e81c76a4d25fb658567b12d842::xJWPnZIRSbWdCj9st4W3oA==",
                AesUtil.fillTo16Chars("123456")
        );
        assertNotNull(decryptovano);
        assertEquals(decryptovano, "spravne_heslo");
    }
}

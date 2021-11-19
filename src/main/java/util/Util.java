package util;

import java.nio.charset.StandardCharsets;

public class Util {
    public static String utf8encode(String toEncode) {
        return new String(toEncode.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }
}

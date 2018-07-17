package utils.codec;

import java.nio.charset.StandardCharsets;

public class Ascii {
    public static byte[] fromAscii(String ascii) {
        return ascii.getBytes();
    }

    public static String toAscii(byte[] bytes) {
        return new String(bytes, StandardCharsets.US_ASCII);
    }
}
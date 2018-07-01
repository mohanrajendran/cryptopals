package utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class XorTest {
    @Test
    public void sameLengthTest() {
        byte[] byte1 = Codec.fromHex("12345678");
        byte[] byte2 = Codec.fromHex("90abcdef");
        byte[] expected = Codec.fromHex("829f9b97");
        byte[] result = Xor.sameLength(byte1, byte2);
        assertArrayEquals(expected, result);
    }
}
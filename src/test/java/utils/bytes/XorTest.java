package utils.bytes;

import utils.codec.Hex;
import org.junit.Test;
import static org.junit.Assert.*;

public class XorTest {
    @Test
    public void sameLengthTest() {
        byte[] byte1 = Hex.fromHex("12345678");
        byte[] byte2 = Hex.fromHex("90abcdef");
        byte[] expected = Hex.fromHex("829f9b97");
        byte[] result = Xor.sameLength(byte1, byte2);
        assertArrayEquals(expected, result);
    }
}
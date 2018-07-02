package utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class CodecTest {
    @Test
    public void fromHexTest() {
        byte[] result = Codec.fromHex("4d616e");
        assertArrayEquals(new byte[] { 77, 97, 110 }, result);
    }

    @Test
    public void toBase64Test() {
        String result1 = Codec.toBase64(new byte[] { 77, 97, 110 });
        String result2 = Codec.toBase64(new byte[] { 77, 97 });
        String result3 = Codec.toBase64(new byte[] { 77 });

        assertEquals("TWFu", result1);
        assertEquals("TWE=", result2);
        assertEquals("TQ==", result3);
    }

    @Test
    public void fromBase64Test() {
        byte[] result1 = Codec.fromBase64("TWFu");
        byte[] result2 = Codec.fromBase64("TWE=");
        byte[] result3 = Codec.fromBase64("TQ==");

        assertArrayEquals(new byte[] { 77, 97, 110 }, result1);
        assertArrayEquals(new byte[] { 77, 97 }, result2);
        assertArrayEquals(new byte[] { 77 }, result3);
    }
}
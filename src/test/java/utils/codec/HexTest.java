package utils.codec;

import org.junit.Test;
import static org.junit.Assert.*;

public class HexTest {
    @Test
    public void fromHexTest() {
        byte[] result = Hex.fromHex("4d616e");
        assertArrayEquals(new byte[] { 77, 97, 110 }, result);
    }
}
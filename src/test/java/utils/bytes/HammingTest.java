package utils.bytes;

import utils.codec.Ascii;
import org.junit.Test;
import static org.junit.Assert.*;

public class HammingTest {
    @Test
    public void hammingDistanceTest() {
        byte[] fst = Ascii.fromAscii("this is a test");
        byte[] snd = Ascii.fromAscii("wokka wokka!!!");

        assertEquals(37, Hamming.distance(fst, snd));
    }
}
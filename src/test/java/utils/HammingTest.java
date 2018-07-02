package utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class HammingTest {
    @Test
    public void hammingDistanceTest() {
        byte[] fst = Codec.fromAscii("this is a test");
        byte[] snd = Codec.fromAscii("wokka wokka!!!");

        assertEquals(37, Hamming.computeDistance(fst, snd));
    }
}
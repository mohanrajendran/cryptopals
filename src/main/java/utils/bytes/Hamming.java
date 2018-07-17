package utils.bytes;

import java.util.stream.IntStream;

public class Hamming {
    public static int distance(byte[] fst, byte[] snd) {
        if (fst.length != snd.length)
            throw new IllegalArgumentException("Two byte arrays must be of same length");

        return IntStream.range(0, fst.length).map(i -> distance(fst[i], snd[i])).sum();
    }

    private static int distance(byte fst, byte snd) {
        int xor = fst ^ snd;

        int count = 0;
        while (xor != 0) {
            count++;
            xor = xor & (xor - 1);
        }
        return count;
    }
}
package utils;

public class Xor {
    public static byte[] xorSameLength(byte[] fst, byte[] snd) {
        if (fst.length != snd.length)
            throw new IllegalArgumentException("Input byte arrays must be of the same length");

        byte[] result = new byte[fst.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (fst[i] ^ snd[i]);
        }

        return result;
    }
}
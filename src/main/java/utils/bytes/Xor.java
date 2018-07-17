package utils.bytes;

public class Xor {
    public static byte[] sameLength(byte[] fst, byte[] snd) {
        if (fst.length != snd.length)
            throw new IllegalArgumentException("Input byte arrays must be of the same length");

        byte[] result = new byte[fst.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (fst[i] ^ snd[i]);
        }

        return result;
    }

    public static byte[] repeated(byte[] notRepeat, byte repeat) {
        return repeated(notRepeat, new byte[] { repeat });
    }

    public static byte[] repeated(byte[] notRepeat, byte[] repeat) {
        byte[] result = new byte[notRepeat.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (notRepeat[i] ^ repeat[i % repeat.length]);
        }

        return result;
    }
}
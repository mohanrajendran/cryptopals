package utils.codec;

public class Hex {
    public static byte[] fromHex(String hex) {
        if (hex.length() % 2 != 0)
            throw new IllegalArgumentException("Input string must be of even length");

        byte[] result = new byte[hex.length() / 2];

        for (int i = 0; i < hex.length() / 2; ++i) {
            int fst = Character.digit(hex.charAt(i * 2), 16) << 4;
            int snd = Character.digit(hex.charAt(i * 2 + 1), 16);

            result[i] = (byte) (fst + snd);
        }

        return result;
    }

    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
package utils;

import java.nio.charset.StandardCharsets;

public class Codec {
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

    public static byte[] fromAscii(String ascii) {
        return ascii.getBytes();
    }

    public static String toAscii(byte[] bytes) {
        return new String(bytes, StandardCharsets.US_ASCII);
    }

    public static byte[] fromBase64(String base64) {
        if (base64.length() % 4 != 0)
            throw new IllegalArgumentException("Input string must be of a length divisible by 4");

        int resultLength = 3 * base64.length() / 4;
        if (base64.charAt(base64.length() - 1) == '=')
            resultLength--;
        if (base64.charAt(base64.length() - 2) == '=')
            resultLength--;
        byte[] result = new byte[resultLength];

        int carry = 0;
        int rIdx = 0;
        for (int i = 0; i < base64.length(); i++) {
            char current = base64.charAt(i);
            if (current == '=')
                break;

            int value = base64Chars.indexOf(current);
            if (i % 4 == 0) {
                carry = value;
            } else if (i % 4 == 1) {
                result[rIdx++] = (byte) ((carry << 2) + (value >> 4));
                carry = value & ((1 << 4) - 1);
            } else if (i % 4 == 2) {
                result[rIdx++] = (byte) ((carry << 4) + (value >> 2));
                carry = value & ((1 << 2) - 1);
            } else {
                result[rIdx++] = (byte) ((carry << 6) + value);
            }
        }

        return result;
    }

    public static String toBase64(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        int carry = 0;
        for (int i = 0; i < bytes.length; ++i) {
            byte current = bytes[i];

            if (i % 3 == 0) {
                int idx = current >> 2;
                sb.append(base64Chars.charAt(idx));
                carry = current & ((1 << 2) - 1);
            } else if (i % 3 == 1) {
                int idx = (carry << 4) + (current >> 4);
                sb.append(base64Chars.charAt(idx));
                carry = current & ((1 << 4) - 1);
            } else {
                int idx = (carry << 2) + (current >> 6);
                sb.append(base64Chars.charAt(idx));
                idx = current & ((1 << 6) - 1);
                sb.append(base64Chars.charAt(idx));
            }
        }

        if (bytes.length % 3 == 1) {
            int idx = carry << 4;
            sb.append(base64Chars.charAt(idx));
            sb.append("==");
        } else if (bytes.length % 3 == 2) {
            int idx = carry << 2;
            sb.append(base64Chars.charAt(idx));
            sb.append('=');
        }

        return sb.toString();
    }

    private static String base64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
}
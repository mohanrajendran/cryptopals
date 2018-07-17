package utils.codec;

public class Base64 {
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
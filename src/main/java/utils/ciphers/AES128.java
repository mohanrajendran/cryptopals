package utils.ciphers;

import java.util.Arrays;

public class AES128 {
    private static final int KEY_BYTES = 16;
    private static final int NUM_ROUNDS = 10;

    private byte[] key;
    public byte[] expandedKey;

    public AES128(byte[] key) {
        if (key.length != KEY_BYTES)
            throw new IllegalArgumentException("Key length should be 128-bits");

        this.key = key;
        this.expandedKey = expandKey();
    }

    private byte[] expandKey() {
        byte[] expandedKey = new byte[KEY_BYTES * (NUM_ROUNDS + 1)];

        for (int i = 0; i < KEY_BYTES; i++) {
            expandedKey[i] = key[i];
        }

        for (int i = 4; i < 44; i++) {
            byte[] word = { expandedKey[(i - 1) * 4], expandedKey[(i - 1) * 4 + 1], expandedKey[(i - 1) * 4 + 2],
                    expandedKey[(i - 1) * 4 + 3] };

            if (i % 4 == 0) {
                byte temp = word[0];
                word[0] = word[1];
                word[1] = word[2];
                word[2] = word[3];
                word[3] = temp;

                for (int j = 0; j < 4; j++)
                    word[j] = sBox[0xFF & word[j]];

                word[0] ^= rCon[i / 4];
            }

            for (int j = 0; j < 4; j++) {
                expandedKey[i * 4 + j] = (byte) (word[j] ^ expandedKey[(i - 4) * 4 + j]);
            }
        }

        return expandedKey;
    }

    public byte[] decrypt(byte[] cipherText) {
        byte[] plainText = new byte[cipherText.length];

        return plainText;
    }

    public byte[] encrypt(byte[] plainText) {
        byte[] cipherText = new byte[plainText.length];

        for (int i = 0; i < plainText.length; i += KEY_BYTES) {
            byte[] block = Arrays.copyOfRange(plainText, i, i + KEY_BYTES);
            byte[] cipherBlock = this.encryptBlock(block);
            System.arraycopy(cipherBlock, 0, cipherText, i, KEY_BYTES);
        }

        return cipherText;
    }

    private byte[] encryptBlock(byte[] block) {
        byte[] state = block;

        this.addRoundKey(state, 0);
        for (int round = 1; round < NUM_ROUNDS; round++) {
            this.subBytes(state);
            this.shiftRows(state);
            this.mixColumns(state);
            this.addRoundKey(state, round * block.length);
        }

        this.subBytes(state);
        this.shiftRows(state);
        this.addRoundKey(state, NUM_ROUNDS * block.length);

        return state;
    }

    private void addRoundKey(byte[] state, int offset) {
        for (int i = 0; i < state.length; i++) {
            state[i] ^= this.expandedKey[offset + i];
        }
    }

    private void subBytes(byte[] state) {
        for (int i = 0; i < state.length; i++) {
            state[i] = sBox[0xFF & state[i]];
        }
    }

    private void shiftRows(byte[] state) {
        byte temp;
        // shift row 1
        temp = state[1];
        state[1] = state[5];
        state[5] = state[9];
        state[9] = state[13];
        state[13] = temp;

        // shift row 2
        temp = state[2];
        state[2] = state[10];
        state[10] = temp;
        temp = state[6];
        state[6] = state[14];
        state[14] = temp;

        // shift row 3
        temp = state[15];
        state[15] = state[11];
        state[11] = state[7];
        state[7] = state[3];
        state[3] = temp;
    }

    private void mixColumns(byte[] state) {
        for (int i = 0; i < 16; i += 4) {
            byte[] a = new byte[4];
            byte[] b = new byte[4];
            byte[] c = new byte[4];

            for (int j = 0; j < 4; j++) {
                a[j] = state[i + j];
                b[j] = xtime(a[j]);
                c[j] = (byte) (a[j] ^ b[j]);
            }

            state[i] = (byte) (b[0] ^ c[1] ^ a[2] ^ a[3]);
            state[i + 1] = (byte) (a[0] ^ b[1] ^ c[2] ^ a[3]);
            state[i + 2] = (byte) (a[0] ^ a[1] ^ b[2] ^ c[3]);
            state[i + 3] = (byte) (c[0] ^ a[1] ^ a[2] ^ b[3]);
            /*
             * state[i] = (byte) ((xtime(state[i + 1])) ^ state[i] ^ temp); state[i + 1] =
             * (byte) ((xtime(state[i + 2])) ^ state[i + 1] ^ temp); state[i + 2] = (byte)
             * ((xtime(state[i + 3])) ^ state[i + 2] ^ temp); state[i + 3] = (byte)
             * ((xtime(state[i])) ^ state[i + 3] ^ temp);
             */
            /*
             * state[i] = (byte) ((xtime(state[i])) ^ (xtime(state[i + 1]) ^ state[i + 1] ^
             * state[i + 2] ^ state[i + 3])); state[i + 1] = (byte) (state[i] ^
             * xtime(state[i + 1]) ^ xtime(state[i + 2]) ^ state[i + 2] ^ state[i + 3]);
             */
        }
    }

    private byte xtime(byte val) {
        return (byte) ((val << 1) ^ (((val & 0xff) >>> 7) * 0x1b));
    }

    private static byte[] rCon = { (byte) 0x8d, (byte) 0x01, (byte) 0x02, (byte) 0x04, (byte) 0x08, (byte) 0x10,
            (byte) 0x20, (byte) 0x40, (byte) 0x80, (byte) 0x1b, (byte) 0x36, (byte) 0x6c, (byte) 0xd8, (byte) 0xab,
            (byte) 0x4d, (byte) 0x9a, (byte) 0x2f, (byte) 0x5e, (byte) 0xbc, (byte) 0x63, (byte) 0xc6, (byte) 0x97,
            (byte) 0x35, (byte) 0x6a, (byte) 0xd4, (byte) 0xb3, (byte) 0x7d, (byte) 0xfa, (byte) 0xef, (byte) 0xc5,
            (byte) 0x91, (byte) 0x39, (byte) 0x72, (byte) 0xe4, (byte) 0xd3, (byte) 0xbd, (byte) 0x61, (byte) 0xc2,
            (byte) 0x9f, (byte) 0x25, (byte) 0x4a, (byte) 0x94, (byte) 0x33, (byte) 0x66, (byte) 0xcc, (byte) 0x83,
            (byte) 0x1d, (byte) 0x3a, (byte) 0x74, (byte) 0xe8, (byte) 0xcb, (byte) 0x8d, (byte) 0x01, (byte) 0x02,
            (byte) 0x04, (byte) 0x08, (byte) 0x10, (byte) 0x20, (byte) 0x40, (byte) 0x80, (byte) 0x1b, (byte) 0x36,
            (byte) 0x6c, (byte) 0xd8, (byte) 0xab, (byte) 0x4d, (byte) 0x9a, (byte) 0x2f, (byte) 0x5e, (byte) 0xbc,
            (byte) 0x63, (byte) 0xc6, (byte) 0x97, (byte) 0x35, (byte) 0x6a, (byte) 0xd4, (byte) 0xb3, (byte) 0x7d,
            (byte) 0xfa, (byte) 0xef, (byte) 0xc5, (byte) 0x91, (byte) 0x39, (byte) 0x72, (byte) 0xe4, (byte) 0xd3,
            (byte) 0xbd, (byte) 0x61, (byte) 0xc2, (byte) 0x9f, (byte) 0x25, (byte) 0x4a, (byte) 0x94, (byte) 0x33,
            (byte) 0x66, (byte) 0xcc, (byte) 0x83, (byte) 0x1d, (byte) 0x3a, (byte) 0x74, (byte) 0xe8, (byte) 0xcb,
            (byte) 0x8d, (byte) 0x01, (byte) 0x02, (byte) 0x04, (byte) 0x08, (byte) 0x10, (byte) 0x20, (byte) 0x40,
            (byte) 0x80, (byte) 0x1b, (byte) 0x36, (byte) 0x6c, (byte) 0xd8, (byte) 0xab, (byte) 0x4d, (byte) 0x9a,
            (byte) 0x2f, (byte) 0x5e, (byte) 0xbc, (byte) 0x63, (byte) 0xc6, (byte) 0x97, (byte) 0x35, (byte) 0x6a,
            (byte) 0xd4, (byte) 0xb3, (byte) 0x7d, (byte) 0xfa, (byte) 0xef, (byte) 0xc5, (byte) 0x91, (byte) 0x39,
            (byte) 0x72, (byte) 0xe4, (byte) 0xd3, (byte) 0xbd, (byte) 0x61, (byte) 0xc2, (byte) 0x9f, (byte) 0x25,
            (byte) 0x4a, (byte) 0x94, (byte) 0x33, (byte) 0x66, (byte) 0xcc, (byte) 0x83, (byte) 0x1d, (byte) 0x3a,
            (byte) 0x74, (byte) 0xe8, (byte) 0xcb, (byte) 0x8d, (byte) 0x01, (byte) 0x02, (byte) 0x04, (byte) 0x08,
            (byte) 0x10, (byte) 0x20, (byte) 0x40, (byte) 0x80, (byte) 0x1b, (byte) 0x36, (byte) 0x6c, (byte) 0xd8,
            (byte) 0xab, (byte) 0x4d, (byte) 0x9a, (byte) 0x2f, (byte) 0x5e, (byte) 0xbc, (byte) 0x63, (byte) 0xc6,
            (byte) 0x97, (byte) 0x35, (byte) 0x6a, (byte) 0xd4, (byte) 0xb3, (byte) 0x7d, (byte) 0xfa, (byte) 0xef,
            (byte) 0xc5, (byte) 0x91, (byte) 0x39, (byte) 0x72, (byte) 0xe4, (byte) 0xd3, (byte) 0xbd, (byte) 0x61,
            (byte) 0xc2, (byte) 0x9f, (byte) 0x25, (byte) 0x4a, (byte) 0x94, (byte) 0x33, (byte) 0x66, (byte) 0xcc,
            (byte) 0x83, (byte) 0x1d, (byte) 0x3a, (byte) 0x74, (byte) 0xe8, (byte) 0xcb, (byte) 0x8d, (byte) 0x01,
            (byte) 0x02, (byte) 0x04, (byte) 0x08, (byte) 0x10, (byte) 0x20, (byte) 0x40, (byte) 0x80, (byte) 0x1b,
            (byte) 0x36, (byte) 0x6c, (byte) 0xd8, (byte) 0xab, (byte) 0x4d, (byte) 0x9a, (byte) 0x2f, (byte) 0x5e,
            (byte) 0xbc, (byte) 0x63, (byte) 0xc6, (byte) 0x97, (byte) 0x35, (byte) 0x6a, (byte) 0xd4, (byte) 0xb3,
            (byte) 0x7d, (byte) 0xfa, (byte) 0xef, (byte) 0xc5, (byte) 0x91, (byte) 0x39, (byte) 0x72, (byte) 0xe4,
            (byte) 0xd3, (byte) 0xbd, (byte) 0x61, (byte) 0xc2, (byte) 0x9f, (byte) 0x25, (byte) 0x4a, (byte) 0x94,
            (byte) 0x33, (byte) 0x66, (byte) 0xcc, (byte) 0x83, (byte) 0x1d, (byte) 0x3a, (byte) 0x74, (byte) 0xe8,
            (byte) 0xcb, (byte) 0x8d };
    private static byte[] sBox = { (byte) 0x63, (byte) 0x7C, (byte) 0x77, (byte) 0x7B, (byte) 0xF2, (byte) 0x6B,
            (byte) 0x6F, (byte) 0xC5, (byte) 0x30, (byte) 0x01, (byte) 0x67, (byte) 0x2B, (byte) 0xFE, (byte) 0xD7,
            (byte) 0xAB, (byte) 0x76, (byte) 0xCA, (byte) 0x82, (byte) 0xC9, (byte) 0x7D, (byte) 0xFA, (byte) 0x59,
            (byte) 0x47, (byte) 0xF0, (byte) 0xAD, (byte) 0xD4, (byte) 0xA2, (byte) 0xAF, (byte) 0x9C, (byte) 0xA4,
            (byte) 0x72, (byte) 0xC0, (byte) 0xB7, (byte) 0xFD, (byte) 0x93, (byte) 0x26, (byte) 0x36, (byte) 0x3F,
            (byte) 0xF7, (byte) 0xCC, (byte) 0x34, (byte) 0xA5, (byte) 0xE5, (byte) 0xF1, (byte) 0x71, (byte) 0xD8,
            (byte) 0x31, (byte) 0x15, (byte) 0x04, (byte) 0xC7, (byte) 0x23, (byte) 0xC3, (byte) 0x18, (byte) 0x96,
            (byte) 0x05, (byte) 0x9A, (byte) 0x07, (byte) 0x12, (byte) 0x80, (byte) 0xE2, (byte) 0xEB, (byte) 0x27,
            (byte) 0xB2, (byte) 0x75, (byte) 0x09, (byte) 0x83, (byte) 0x2C, (byte) 0x1A, (byte) 0x1B, (byte) 0x6E,
            (byte) 0x5A, (byte) 0xA0, (byte) 0x52, (byte) 0x3B, (byte) 0xD6, (byte) 0xB3, (byte) 0x29, (byte) 0xE3,
            (byte) 0x2F, (byte) 0x84, (byte) 0x53, (byte) 0xD1, (byte) 0x00, (byte) 0xED, (byte) 0x20, (byte) 0xFC,
            (byte) 0xB1, (byte) 0x5B, (byte) 0x6A, (byte) 0xCB, (byte) 0xBE, (byte) 0x39, (byte) 0x4A, (byte) 0x4C,
            (byte) 0x58, (byte) 0xCF, (byte) 0xD0, (byte) 0xEF, (byte) 0xAA, (byte) 0xFB, (byte) 0x43, (byte) 0x4D,
            (byte) 0x33, (byte) 0x85, (byte) 0x45, (byte) 0xF9, (byte) 0x02, (byte) 0x7F, (byte) 0x50, (byte) 0x3C,
            (byte) 0x9F, (byte) 0xA8, (byte) 0x51, (byte) 0xA3, (byte) 0x40, (byte) 0x8F, (byte) 0x92, (byte) 0x9D,
            (byte) 0x38, (byte) 0xF5, (byte) 0xBC, (byte) 0xB6, (byte) 0xDA, (byte) 0x21, (byte) 0x10, (byte) 0xFF,
            (byte) 0xF3, (byte) 0xD2, (byte) 0xCD, (byte) 0x0C, (byte) 0x13, (byte) 0xEC, (byte) 0x5F, (byte) 0x97,
            (byte) 0x44, (byte) 0x17, (byte) 0xC4, (byte) 0xA7, (byte) 0x7E, (byte) 0x3D, (byte) 0x64, (byte) 0x5D,
            (byte) 0x19, (byte) 0x73, (byte) 0x60, (byte) 0x81, (byte) 0x4F, (byte) 0xDC, (byte) 0x22, (byte) 0x2A,
            (byte) 0x90, (byte) 0x88, (byte) 0x46, (byte) 0xEE, (byte) 0xB8, (byte) 0x14, (byte) 0xDE, (byte) 0x5E,
            (byte) 0x0B, (byte) 0xDB, (byte) 0xE0, (byte) 0x32, (byte) 0x3A, (byte) 0x0A, (byte) 0x49, (byte) 0x06,
            (byte) 0x24, (byte) 0x5C, (byte) 0xC2, (byte) 0xD3, (byte) 0xAC, (byte) 0x62, (byte) 0x91, (byte) 0x95,
            (byte) 0xE4, (byte) 0x79, (byte) 0xE7, (byte) 0xC8, (byte) 0x37, (byte) 0x6D, (byte) 0x8D, (byte) 0xD5,
            (byte) 0x4E, (byte) 0xA9, (byte) 0x6C, (byte) 0x56, (byte) 0xF4, (byte) 0xEA, (byte) 0x65, (byte) 0x7A,
            (byte) 0xAE, (byte) 0x08, (byte) 0xBA, (byte) 0x78, (byte) 0x25, (byte) 0x2E, (byte) 0x1C, (byte) 0xA6,
            (byte) 0xB4, (byte) 0xC6, (byte) 0xE8, (byte) 0xDD, (byte) 0x74, (byte) 0x1F, (byte) 0x4B, (byte) 0xBD,
            (byte) 0x8B, (byte) 0x8A, (byte) 0x70, (byte) 0x3E, (byte) 0xB5, (byte) 0x66, (byte) 0x48, (byte) 0x03,
            (byte) 0xF6, (byte) 0x0E, (byte) 0x61, (byte) 0x35, (byte) 0x57, (byte) 0xB9, (byte) 0x86, (byte) 0xC1,
            (byte) 0x1D, (byte) 0x9E, (byte) 0xE1, (byte) 0xF8, (byte) 0x98, (byte) 0x11, (byte) 0x69, (byte) 0xD9,
            (byte) 0x8E, (byte) 0x94, (byte) 0x9B, (byte) 0x1E, (byte) 0x87, (byte) 0xE9, (byte) 0xCE, (byte) 0x55,
            (byte) 0x28, (byte) 0xDF, (byte) 0x8C, (byte) 0xA1, (byte) 0x89, (byte) 0x0D, (byte) 0xBF, (byte) 0xE6,
            (byte) 0x42, (byte) 0x68, (byte) 0x41, (byte) 0x99, (byte) 0x2D, (byte) 0x0F, (byte) 0xB0, (byte) 0x54,
            (byte) 0xBB, (byte) 0x16 };

    private static byte[] sBoxInv = { (byte) 0x52, (byte) 0x09, (byte) 0x6A, (byte) 0xD5, (byte) 0x30, (byte) 0x36,
            (byte) 0xA5, (byte) 0x38, (byte) 0xBF, (byte) 0x40, (byte) 0xA3, (byte) 0x9E, (byte) 0x81, (byte) 0xF3,
            (byte) 0xD7, (byte) 0xFB, (byte) 0x7C, (byte) 0xE3, (byte) 0x39, (byte) 0x82, (byte) 0x9B, (byte) 0x2F,
            (byte) 0xFF, (byte) 0x87, (byte) 0x34, (byte) 0x8E, (byte) 0x43, (byte) 0x44, (byte) 0xC4, (byte) 0xDE,
            (byte) 0xE9, (byte) 0xCB, (byte) 0x54, (byte) 0x7B, (byte) 0x94, (byte) 0x32, (byte) 0xA6, (byte) 0xC2,
            (byte) 0x23, (byte) 0x3D, (byte) 0xEE, (byte) 0x4C, (byte) 0x95, (byte) 0x0B, (byte) 0x42, (byte) 0xFA,
            (byte) 0xC3, (byte) 0x4E, (byte) 0x08, (byte) 0x2E, (byte) 0xA1, (byte) 0x66, (byte) 0x28, (byte) 0xD9,
            (byte) 0x24, (byte) 0xB2, (byte) 0x76, (byte) 0x5B, (byte) 0xA2, (byte) 0x49, (byte) 0x6D, (byte) 0x8B,
            (byte) 0xD1, (byte) 0x25, (byte) 0x72, (byte) 0xF8, (byte) 0xF6, (byte) 0x64, (byte) 0x86, (byte) 0x68,
            (byte) 0x98, (byte) 0x16, (byte) 0xD4, (byte) 0xA4, (byte) 0x5C, (byte) 0xCC, (byte) 0x5D, (byte) 0x65,
            (byte) 0xB6, (byte) 0x92, (byte) 0x6C, (byte) 0x70, (byte) 0x48, (byte) 0x50, (byte) 0xFD, (byte) 0xED,
            (byte) 0xB9, (byte) 0xDA, (byte) 0x5E, (byte) 0x15, (byte) 0x46, (byte) 0x57, (byte) 0xA7, (byte) 0x8D,
            (byte) 0x9D, (byte) 0x84, (byte) 0x90, (byte) 0xD8, (byte) 0xAB, (byte) 0x00, (byte) 0x8C, (byte) 0xBC,
            (byte) 0xD3, (byte) 0x0A, (byte) 0xF7, (byte) 0xE4, (byte) 0x58, (byte) 0x05, (byte) 0xB8, (byte) 0xB3,
            (byte) 0x45, (byte) 0x06, (byte) 0xD0, (byte) 0x2C, (byte) 0x1E, (byte) 0x8F, (byte) 0xCA, (byte) 0x3F,
            (byte) 0x0F, (byte) 0x02, (byte) 0xC1, (byte) 0xAF, (byte) 0xBD, (byte) 0x03, (byte) 0x01, (byte) 0x13,
            (byte) 0x8A, (byte) 0x6B, (byte) 0x3A, (byte) 0x91, (byte) 0x11, (byte) 0x41, (byte) 0x4F, (byte) 0x67,
            (byte) 0xDC, (byte) 0xEA, (byte) 0x97, (byte) 0xF2, (byte) 0xCF, (byte) 0xCE, (byte) 0xF0, (byte) 0xB4,
            (byte) 0xE6, (byte) 0x73, (byte) 0x96, (byte) 0xAC, (byte) 0x74, (byte) 0x22, (byte) 0xE7, (byte) 0xAD,
            (byte) 0x35, (byte) 0x85, (byte) 0xE2, (byte) 0xF9, (byte) 0x37, (byte) 0xE8, (byte) 0x1C, (byte) 0x75,
            (byte) 0xDF, (byte) 0x6E, (byte) 0x47, (byte) 0xF1, (byte) 0x1A, (byte) 0x71, (byte) 0x1D, (byte) 0x29,
            (byte) 0xC5, (byte) 0x89, (byte) 0x6F, (byte) 0xB7, (byte) 0x62, (byte) 0x0E, (byte) 0xAA, (byte) 0x18,
            (byte) 0xBE, (byte) 0x1B, (byte) 0xFC, (byte) 0x56, (byte) 0x3E, (byte) 0x4B, (byte) 0xC6, (byte) 0xD2,
            (byte) 0x79, (byte) 0x20, (byte) 0x9A, (byte) 0xDB, (byte) 0xC0, (byte) 0xFE, (byte) 0x78, (byte) 0xCD,
            (byte) 0x5A, (byte) 0xF4, (byte) 0x1F, (byte) 0xDD, (byte) 0xA8, (byte) 0x33, (byte) 0x88, (byte) 0x07,
            (byte) 0xC7, (byte) 0x31, (byte) 0xB1, (byte) 0x12, (byte) 0x10, (byte) 0x59, (byte) 0x27, (byte) 0x80,
            (byte) 0xEC, (byte) 0x5F, (byte) 0x60, (byte) 0x51, (byte) 0x7F, (byte) 0xA9, (byte) 0x19, (byte) 0xB5,
            (byte) 0x4A, (byte) 0x0D, (byte) 0x2D, (byte) 0xE5, (byte) 0x7A, (byte) 0x9F, (byte) 0x93, (byte) 0xC9,
            (byte) 0x9C, (byte) 0xEF, (byte) 0xA0, (byte) 0xE0, (byte) 0x3B, (byte) 0x4D, (byte) 0xAE, (byte) 0x2A,
            (byte) 0xF5, (byte) 0xB0, (byte) 0xC8, (byte) 0xEB, (byte) 0xBB, (byte) 0x3C, (byte) 0x83, (byte) 0x53,
            (byte) 0x99, (byte) 0x61, (byte) 0x17, (byte) 0x2B, (byte) 0x04, (byte) 0x7E, (byte) 0xBA, (byte) 0x77,
            (byte) 0xD6, (byte) 0x26, (byte) 0xE1, (byte) 0x69, (byte) 0x14, (byte) 0x63, (byte) 0x55, (byte) 0x21,
            (byte) 0x0C, (byte) 0x7D };
}
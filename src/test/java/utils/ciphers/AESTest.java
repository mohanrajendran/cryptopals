package utils.ciphers;

import org.junit.Test;

import utils.codec.Hex;

import static org.junit.Assert.*;

public class AESTest {
    @Test
    public void encryptTest() {
        byte[] plainText = Hex.fromHex("3243f6a8885a308d313198a2e0370734");
        byte[] key = Hex.fromHex("2b7e151628aed2a6abf7158809cf4f3c");
        byte[] cipherText = Hex.fromHex("3925841d02dc09fbdc118597196a0b32");

        AES aes = new AES(key);
        assertArrayEquals(cipherText, aes.encrypt(plainText));
    }
}
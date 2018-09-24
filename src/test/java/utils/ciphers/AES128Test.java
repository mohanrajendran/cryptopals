package utils.ciphers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import utils.codec.Hex;

import static org.junit.Assert.*;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class AES128Test {
    @Parameters
    public static Iterable<Object[]> plainCiphers() {
        return Arrays
                .asList(new Object[][] { { "3243f6a8885a308d313198a2e0370734", "3925841d02dc09fbdc118597196a0b32" },
                        { "3243f6a8885a308d313198a2e03707343243f6a8885a308d313198a2e0370734",
                                "3925841d02dc09fbdc118597196a0b323925841d02dc09fbdc118597196a0b32" } });
    }

    public AES128Test(String plain, String cipher) {
        this.plainText = Hex.fromHex(plain);
        this.cipherText = Hex.fromHex(cipher);

        byte[] key = Hex.fromHex("2b7e151628aed2a6abf7158809cf4f3c");
        aes = new AES128(key);
    }

    private byte[] plainText;
    private byte[] cipherText;
    AES128 aes;

    @Test
    public void encryptTest() {
        byte[] encrypted = aes.encrypt(plainText);
        assertArrayEquals(cipherText, encrypted);
    }

    @Test
    public void decryptTest() {
        byte[] decrypted = aes.decrypt(cipherText);
        assertArrayEquals(plainText, decrypted);
    }
}
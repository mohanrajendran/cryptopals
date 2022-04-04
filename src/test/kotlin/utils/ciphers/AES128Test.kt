package utils.ciphers

import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import utils.codec.Hex.fromHex

@ExperimentalUnsignedTypes
class AES128Test {
    private val aes: AES128 = AES128(fromHex("2b7e151628aed2a6abf7158809cf4f3c"))

    @ParameterizedTest
    @MethodSource("testCases")
    fun aesTest(plainText: String, cipherText: String) {
        val plain = fromHex(plainText)
        val cipher = fromHex(cipherText)

        aes.encrypt(plain) shouldBe cipher
        aes.decrypt(cipher) shouldBe plain
    }

    companion object {
        @JvmStatic
        fun testCases() = listOf(
            Arguments.of("3243f6a8885a308d313198a2e0370734", "3925841d02dc09fbdc118597196a0b32"),
            Arguments.of(
                "3243f6a8885a308d313198a2e03707343243f6a8885a308d313198a2e0370734",
                "3925841d02dc09fbdc118597196a0b323925841d02dc09fbdc118597196a0b32"
            )
        )
    }
}

package utils.bytes

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.bytes.Xor.sameLength
import utils.codec.Hex.fromHex

@ExperimentalUnsignedTypes
class XorTest {
    @Test
    fun sameLengthTest() {
        val byte1 = fromHex("12345678")
        val byte2 = fromHex("90abcdef")
        val expected = fromHex("829f9b97")
        val result = sameLength(byte1, byte2)
        result shouldBe expected
    }
}

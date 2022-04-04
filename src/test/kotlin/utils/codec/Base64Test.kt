package utils.codec

import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import utils.codec.Base64.fromBase64
import utils.codec.Base64.toBase64

@ExperimentalUnsignedTypes
class Base64Test {
    @ParameterizedTest
    @MethodSource("testCases")
    fun base64Test(base64: String, bytes: ByteArray) {
        val uBytes = bytes.toUByteArray()
        fromBase64(base64) shouldBe uBytes
        toBase64(uBytes) shouldBe base64
    }

    companion object {
        @JvmStatic
        fun testCases() = listOf(
            Arguments.of("TWFu", byteArrayOf(77, 97, 110)),
            Arguments.of("TWE=", byteArrayOf(77, 97)),
            Arguments.of("TQ==", byteArrayOf(77))
        )
    }
}

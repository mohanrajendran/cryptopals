package utils.codec

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.codec.Hex.fromHex

@ExperimentalUnsignedTypes
class HexTest {
    @Test
    fun fromHexTest() {
        val result = fromHex("4d616e")
        result shouldBe ubyteArrayOf(77u, 97u, 110u)
    }
}

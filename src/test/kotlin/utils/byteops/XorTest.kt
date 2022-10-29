package utils.byteops

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.codec.Hex
import utils.codec.toBytes

@ExperimentalUnsignedTypes
class XorTest : FunSpec({
    test("Given test cases pass") {
        val byte1 = Hex("12345678").toBytes()
        val byte2 = Hex("90abcdef").toBytes()
        val expected = Hex("829f9b97").toBytes()
        val result = byte1 xor byte2
        result shouldBe expected
    }
})

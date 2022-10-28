package utils.byteops

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.codec.fromHex

@ExperimentalUnsignedTypes
class XorTest : FunSpec({
    test("Given test cases pass") {
        val byte1 = "12345678".fromHex()
        val byte2 = "90abcdef".fromHex()
        val expected = "829f9b97".fromHex()
        val result = byte1 xor byte2
        result shouldBe expected
    }
})

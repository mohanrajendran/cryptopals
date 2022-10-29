@file:OptIn(ExperimentalUnsignedTypes::class)

package utils.byteops

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.codec.Ascii
import utils.codec.toBytes

class HammingTest : FunSpec({
    test("Given test cases pass") {
        val fst = Ascii("this is a test").toBytes()
        val snd = Ascii("wokka wokka!!!").toBytes()
        fst hammingDistance snd shouldBe 37
    }
})
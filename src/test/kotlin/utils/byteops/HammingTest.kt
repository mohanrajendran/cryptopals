@file:OptIn(ExperimentalUnsignedTypes::class)

package utils.byteops

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.codec.fromAscii

class HammingTest : FunSpec({
    test("Given test cases pass") {
        val fst = "this is a test".fromAscii()
        val snd = "wokka wokka!!!".fromAscii()
        fst hammingDistance snd shouldBe 37
    }
})
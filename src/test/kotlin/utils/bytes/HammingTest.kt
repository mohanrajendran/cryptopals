package utils.bytes

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.bytes.Hamming.distance
import utils.codec.Ascii.fromAscii

@ExperimentalUnsignedTypes
class HammingTest {
    @Test
    fun hammingDistanceTest() {
        val fst = fromAscii("this is a test")
        val snd = fromAscii("wokka wokka!!!")
        distance(fst, snd) shouldBe 37
    }
}
@file:OptIn(ExperimentalUnsignedTypes::class)

package utils.byteops

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import utils.codec.Ascii
import utils.codec.toBytes

class PaddingTest : FunSpec({
    val testCases = listOf(
        Pair(
            15,
            Ascii("YELLOW SUBMARINE\u000e\u000e\u000e\u000e\u000e\u000e\u000e\u000e\u000e\u000e\u000e\u000e\u000e\u000e")
        ),
        Pair(
            16,
            Ascii("YELLOW SUBMARINE\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010")
        ),
        Pair(17, Ascii("YELLOW SUBMARINE\u0001")),
        Pair(20, Ascii("YELLOW SUBMARINE\u0004\u0004\u0004\u0004")),
    )

    context("Given test cases pass for PKCS#7 padding") {
        withData(testCases) { (blockSize, expected) ->
            val input = Ascii("YELLOW SUBMARINE").toBytes()
            val padded = input.pkcs7Pad(blockSize)

            padded shouldBe expected.toBytes()
        }
    }
})
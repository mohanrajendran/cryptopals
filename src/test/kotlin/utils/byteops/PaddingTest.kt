@file:OptIn(ExperimentalUnsignedTypes::class)

package utils.byteops

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import utils.codec.fromAscii

class PaddingTest : FunSpec({
    val testCases = listOf(
        Pair(
            15,
            "YELLOW SUBMARINE\u000e\u000e\u000e\u000e\u000e\u000e\u000e\u000e\u000e\u000e\u000e\u000e\u000e\u000e"
        ),
        Pair(16, "YELLOW SUBMARINE"),
        Pair(17, "YELLOW SUBMARINE\u0001"),
        Pair(20, "YELLOW SUBMARINE\u0004\u0004\u0004\u0004"),
    )

    context("Given test cases pass for PKCS#7 padding") {
        withData(testCases) { (blockSize, expected) ->
            val input = "YELLOW SUBMARINE".fromAscii()
            val padded = input.pkcs7Pad(blockSize)

            padded shouldBe expected.fromAscii()
        }
    }
})
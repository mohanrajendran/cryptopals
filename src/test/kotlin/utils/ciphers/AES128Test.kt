@file:OptIn(ExperimentalUnsignedTypes::class)

package utils.ciphers

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import utils.codec.fromHex

class AES128Test : FunSpec({
    val aes: AES128 = AES128("2b7e151628aed2a6abf7158809cf4f3c".fromHex())
    val testCases = listOf(
        Pair("3243f6a8885a308d313198a2e0370734", "3925841d02dc09fbdc118597196a0b32"),
        Pair(
            "3243f6a8885a308d313198a2e03707343243f6a8885a308d313198a2e0370734",
            "3925841d02dc09fbdc118597196a0b323925841d02dc09fbdc118597196a0b32"
        )
    )

    context("Given test cases pass") {
        withData(testCases) { (plainText, cipherText) ->
            val plain = plainText.fromHex()
            val cipher = cipherText.fromHex()

            aes.encrypt(plain) shouldBe cipher
            aes.decrypt(cipher) shouldBe plain
        }
    }
})

@file:OptIn(ExperimentalUnsignedTypes::class)

package utils.ciphers

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import utils.codec.Hex
import utils.codec.toBytes
import utils.codec.toHex

class AES128Test : FunSpec({
    val aes = AES128(Hex("2b7e151628aed2a6abf7158809cf4f3c").toBytes())
    val iv = UByteArray(16) { 0u }
    val ecbTestCases = listOf(
        Pair("3243f6a8885a308d313198a2e0370734", "3925841d02dc09fbdc118597196a0b32"),
        Pair(
            "3243f6a8885a308d313198a2e03707343243f6a8885a308d313198a2e0370734",
            "3925841d02dc09fbdc118597196a0b323925841d02dc09fbdc118597196a0b32"
        )
    )
    val cbcTestCases = listOf(
        Pair("3243f6a8885a308d313198a2e0370734", "3925841d02dc09fbdc118597196a0b32"),
        Pair(
            "3243f6a8885a308d313198a2e03707343243f6a8885a308d313198a2e0370734",
            "3925841d02dc09fbdc118597196a0b32ebbb47f679290b492c9154ce9cd97f83"
        )
    )

    context("Given test cases pass") {
        context("ECB mode") {
            withData(ecbTestCases) { (plainText, cipherText) ->
                val plain = Hex(plainText).toBytes()
                val cipher = Hex(cipherText).toBytes()

                assertSoftly {
                    aes.encrypt(plain) shouldBe cipher
                    aes.decrypt(cipher) shouldBe plain
                }
            }
        }

        context("CBC mode") {
            withData(cbcTestCases) { (plainText, cipherText) ->
                val plain = Hex(plainText).toBytes()
                val cipher = Hex(cipherText).toBytes()

                assertSoftly {
                    aes.encrypt(plain, mode = BlockMode.CBC, iv = iv) shouldBe cipher
                    aes.decrypt(cipher, mode = BlockMode.CBC, iv = iv).toHex() shouldBe plain.toHex()
                }
            }
        }
    }
})

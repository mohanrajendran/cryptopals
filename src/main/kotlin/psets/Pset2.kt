@file:OptIn(ExperimentalUnsignedTypes::class)

package psets

import utils.byteops.pkcs7Pad
import utils.ciphers.AES128
import utils.ciphers.BlockMode
import utils.codec.Ascii
import utils.codec.Base64
import utils.codec.toAscii
import utils.codec.toBytes
import java.io.File

object Pset2 {
    fun PrintAnswers() {
        println("Set 2 Answers")
        println("-------------")
        PrintChallenge9()
        PrintChallenge10()
    }

    private fun PrintChallenge9() {
        println("Challenge 9")
        val answer =
            Ascii("YELLOW SUBMARINE").toBytes().pkcs7Pad(20).toAscii()
        println("Answer:- $answer")
    }

    private fun PrintChallenge10() {
        println("Challenge 10")
        File("resources/s2c10.in").useLines { lines ->
            val cipherText = Base64(lines.joinToString(separator = "")).toBytes()
            val key = Ascii("YELLOW SUBMARINE").toBytes()
            val aes = AES128(key)
            val iv = UByteArray(16) { 0u }
            val plainText = aes.decrypt(cipherText, mode = BlockMode.CBC, iv = iv)
            println("Answer:- ${plainText.toAscii()}")
        }
    }
}
@file:OptIn(ExperimentalUnsignedTypes::class)

package psets

import utils.byteops.hammingDistance
import utils.byteops.pkcs7Pad
import utils.ciphers.*
import utils.codec.*
import java.io.File

object Pset2 {
    fun PrintAnswers() {
        println("Set 2 Answers")
        println("-------------")
        PrintChallenge9()
        PrintChallenge10()
        PrintChallenge11()
        PrintChallenge12()
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

    private fun PrintChallenge11() {
        println("Challenge 11")
        val plainText = UByteArray(128) { 0u }

        // Predict ECB mode
        val cipherTextEcb = encryptionOracle(plainText, mode = BlockMode.ECB)
        require(cipherTextEcb.repeatedBlocks() > 0) { "Unable to detect ECB" }
        println("Successfully detected ECB mode")

        // Predict CBC mode
        val cipherTextCbc = encryptionOracle(plainText, mode = BlockMode.CBC)
        require(cipherTextCbc.repeatedBlocks() == 0) { "Unable to detect CBC" }
        println("Successfully detected CBC mode")
    }

    private fun PrintChallenge12() {
        println("Challenge 12")
        val suffix =
            Base64(
                "Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkg" +
                        "aGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBq" +
                        "dXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUg" +
                        "YnkK"
            ).toBytes()

        // Find block size
        val initialSize = fixedKeyEncryptionOracleWithSuffix(ubyteArrayOf(), suffix).size
        var nextSize = initialSize
        var length = 1
        while (nextSize == initialSize) {
            val plainText = UByteArray(length++) { 0u }
            nextSize = fixedKeyEncryptionOracleWithSuffix(plainText, suffix).size
        }
        val blockSize = nextSize - initialSize
        println("Block-size is $blockSize bytes")

        // Verify ECB
        val ecbPlainText = UByteArray(blockSize * 2) { 0u }
        val ecbCipherText = fixedKeyEncryptionOracleWithSuffix(ecbPlainText, suffix)
        require(
            ecbCipherText.copyOfRange(0, blockSize) hammingDistance ecbCipherText.copyOfRange(
                blockSize,
                2 * blockSize
            ) == 0
        ) { "First two blocks should be equal." }
        println("Verified ECB mode")

        // Re-construct the suffix
        val reconstructed = UByteArray(initialSize)
        val prev = UByteArray(blockSize) { 0u }
        for (i in reconstructed.indices) {
            prev.copyInto(prev, 0, 1, blockSize)
            val targetBlockIndex = i / blockSize
            val targetBlock =
                fixedKeyEncryptionOracleWithSuffix(
                    prev.copyOfRange(0, blockSize - 1 - i % blockSize),
                    suffix
                ).copyOfRange(
                    targetBlockIndex * blockSize,
                    (targetBlockIndex + 1) * blockSize
                )
            for (j in 0u until 255u) {
                prev[blockSize - 1] = j.toUByte()
                val obtainedBlock = fixedKeyEncryptionOracleWithSuffix(prev, suffix).copyOfRange(0, blockSize)
                if (targetBlock hammingDistance obtainedBlock == 0) {
                    reconstructed[i] = j.toUByte()
                    break
                }
            }
        }
        println("Answer:- ${reconstructed.toAscii()}")
    }
}
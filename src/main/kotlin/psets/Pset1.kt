package psets

import utils.byteops.*
import utils.ciphers.AES128
import utils.codec.*
import java.io.File

@ExperimentalUnsignedTypes
object Pset1 {
    fun PrintAnswers() {
        println("Set 1 Answers")
        println("-------------")
        PrintChallenge1()
        PrintChallenge2()
        PrintChallenge3()
        PrintChallenge4()
        PrintChallenge5()
        PrintChallenge6()
        PrintChallenge7()
        PrintChallenge8()
    }

    private fun PrintChallenge1() {
        println("Challenge 1")
        val answer =
            "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"
                .fromHex().toBase64()
        println("Answer:- $answer")
    }

    private fun PrintChallenge2() {
        println("Challenge 2")
        val hex1 = "1c0111001f010100061a024b53535009181c".fromHex()
        val hex2 = "686974207468652062756c6c277320657965".fromHex()
        val answer = (hex1 xor hex2).toHex()
        println("Answer:- $answer")
    }

    private fun PrintChallenge3() {
        println("Challenge 3")
        val cipherText = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736".fromHex()
        val maxString = mostProbablePlaintext(cipherText).toAscii()
        println("Answer:- $maxString")
    }

    private fun PrintChallenge4() {
        println("Challenge 4")
        File("resources/s1c4.in").useLines { lines ->
            val maxBytes = lines.map { mostProbablePlaintext(it.fromHex()) }
                .maxByOrNull { it.englishScore() }!!

            println("Answer:- ${maxBytes.toAscii()}")
        }
    }

    private fun PrintChallenge5() {
        println("Challenge 5")
        val plainText =
            """
            Burning 'em, if you ain't quick and nimble
            I go crazy when I hear a cymbal
            """.trimIndent().fromAscii()
        val key = "ICE".fromAscii()
        val cipherText = plainText xorRepeat key
        println("Answer:- ${cipherText.toHex()}")
    }

    private fun PrintChallenge6() {
        println("Challenge 6")
        File("resources/s1c6.in").useLines { lines ->
            val cipherText = lines.joinToString(separator = "").fromBase64()
            val blockSize = (2..41).minByOrNull { normalizedEditDistance(cipherText, it) }!!

            val plainText = UByteArray(cipherText.size)
            for (i in 0 until blockSize) {
                val plainInterleave = mostProbablePlaintext(transpose(cipherText, i, blockSize))
                var idx = 0

                for (j in i until cipherText.size step blockSize) {
                    plainText[j] = plainInterleave[idx++]
                }
            }
            println("Answer:- ${plainText.toAscii()}")
        }
    }

    private fun PrintChallenge7() {
        println("Challenge 7")
        File("resources/s1c7.in").useLines { lines ->
            val cipherText = (lines.joinToString(separator = "")).fromBase64()
            val key = "YELLOW SUBMARINE".fromAscii()
            val aes = AES128(key)
            val plainText = aes.decrypt(cipherText)
            println("Answer:- ${plainText.toAscii()}")
        }
    }

    private fun PrintChallenge8() {
        println("challenge 8")
        File("resources/s1c8.in").useLines { lines ->
            val maxRepeated = lines
                .maxByOrNull { repeatedBlocks(it.fromHex()) }
            println("Answer:- ${maxRepeated!!}")
        }
    }

    private fun mostProbablePlaintext(cipherText: UByteArray): UByteArray {
        return (0..256)
            .map { cipherText xorByte it.toUByte() }
            .maxByOrNull { it.englishScore() }!!
    }

    private fun normalizedEditDistance(cipherText: UByteArray, blockSize: Int): Float {
        val comparisons = cipherText.size / blockSize
        val totalDistance = (0 until comparisons - 1).sumOf {
            val b1 = cipherText.copyOfRange(it * blockSize, (it + 1) * blockSize)
            val b2 = cipherText.copyOfRange((it + 1) * blockSize, (it + 2) * blockSize)
            b1 hammingDistance b2
        }
        return totalDistance / (comparisons * blockSize.toFloat())
    }

    private fun transpose(cipherText: UByteArray, offset: Int, period: Int): UByteArray {
        return cipherText.filterIndexed { index, _ -> index % period == offset }.toUByteArray()
    }

    private fun repeatedBlocks(cipherText: UByteArray): Int {
        val counts = mutableMapOf<String, Int>()

        cipherText.chunked(16)
            .forEach { chunk ->
                val key = chunk.toUByteArray().toHex()
                counts[key] = counts.getOrDefault(key, 0) + 1
            }

        return counts.values.filter { it > 1 }.sum()
    }
}

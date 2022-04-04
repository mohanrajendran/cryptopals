package psets

import utils.English
import utils.bytes.Hamming
import utils.bytes.Xor
import utils.ciphers.AES128
import utils.codec.Ascii
import utils.codec.Base64
import utils.codec.Hex
import java.io.File

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
    }

    private fun PrintChallenge1() {
        println("Challenge 1")
        println(
            "Answer:- " + Base64.toBase64(
                Hex.fromHex(
                    "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"
                )
            )
        )
    }

    private fun PrintChallenge2() {
        println("Challenge 2")
        val hex1 = Hex.fromHex("1c0111001f010100061a024b53535009181c")
        val hex2 = Hex.fromHex("686974207468652062756c6c277320657965")
        println("Answer:- ${Hex.toHex(Xor.sameLength(hex1, hex2))}")
    }

    private fun PrintChallenge3() {
        println("Challenge 3")
        val cipherText = Hex.fromHex("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736")
        val maxString = Ascii.toAscii(mostProbablePlaintext(cipherText))
        println("Answer:- $maxString")
    }

    private fun PrintChallenge4() {
        println("Challenge 4")
        File("resources/s1c4.in").useLines { lines ->
            val maxBytes = lines.map { mostProbablePlaintext(Hex.fromHex(it)) }
                .maxByOrNull { English.englishScore(it) }

            println("Answer:- ${Ascii.toAscii(maxBytes!!)}")
        }
    }

    private fun PrintChallenge5() {
        println("Challenge 5")
        val plainText = Ascii.fromAscii(
            """
            Burning 'em, if you ain't quick and nimble
            I go crazy when I hear a cymbal
            """.trimIndent()
        )
        val key = Ascii.fromAscii("ICE")
        val cipherText = Xor.repeated(plainText, key)
        println("Answer:- ${Hex.toHex(cipherText)}")
    }

    private fun PrintChallenge6() {
        println("Challenge 6")
        File("resources/s1c6.in").useLines { lines ->
            val cipherText = Base64.fromBase64(lines.joinToString(separator = ""))
            val blockSize = (2..41).minByOrNull { normalizedEditDistance(cipherText, it) }!!

            val plainText = UByteArray(cipherText.size)
            for (i in 0 until blockSize) {
                val plainInterleave = mostProbablePlaintext(transpose(cipherText, i, blockSize))
                var idx = 0

                for (j in i until cipherText.size step blockSize) {
                    plainText[j] = plainInterleave[idx++]
                }
            }
            println("Answer:- ${Ascii.toAscii(plainText)}")
        }
    }

    private fun PrintChallenge7() {
        println("Challenge 7")
        File("resources/s1c7.in").useLines { lines ->
            val cipherText = Base64.fromBase64(lines.joinToString(separator = ""))
            val key = Ascii.fromAscii("YELLOW SUBMARINE")
            val aes = AES128(key)
            val plainText = aes.decrypt(cipherText)
            println("Answer:- ${Ascii.toAscii(plainText)}")
        }
    }

    private fun mostProbablePlaintext(cipherText: UByteArray): UByteArray {
        return (0..256)
            .map { Xor.repeated(cipherText, it.toUByte()) }
            .maxByOrNull { English.englishScore(it) }!!
    }

    private fun normalizedEditDistance(cipherText: UByteArray, blockSize: Int): Float {
        val comparisons = cipherText.size / blockSize
        val totalDistance = (0 until comparisons - 1).sumOf {
            val b1 = cipherText.copyOfRange(it * blockSize, (it + 1) * blockSize)
            val b2 = cipherText.copyOfRange((it + 1) * blockSize, (it + 2) * blockSize)
            Hamming.distance(b1, b2)
        }
        return totalDistance / (comparisons * blockSize.toFloat())
    }

    private fun transpose(cipherText: UByteArray, offset: Int, period: Int): UByteArray {
        return cipherText.filterIndexed { index, _ -> index % period == offset }.toUByteArray()
    }
}

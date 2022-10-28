@file:OptIn(ExperimentalUnsignedTypes::class)

package psets

import utils.byteops.pkcs7Pad
import utils.codec.fromAscii
import utils.codec.toAscii

object Pset2 {
    fun PrintAnswers() {
        println("Set 2 Answers")
        println("-------------")
        PrintChallenge9()
    }

    private fun PrintChallenge9() {
        println("Challenge 9")
        val answer =
            "YELLOW SUBMARINE".fromAscii().pkcs7Pad(20).toAscii()
        println("Answer:- $answer")
    }
}
package utils

import java.util.HashMap

@ExperimentalUnsignedTypes
object English {
    fun englishScore(ascii: UByteArray): Double {
        return ascii.sumOf { letterScore(it) }
    }

    private fun letterScore(c: UByte): Double {
        val ch = Character.toLowerCase(c.toInt().toChar())
        return frequencies[ch] ?: 0.0
    }

    private val frequencies: HashMap<Char, Double> = hashMapOf(
        'a' to 0.0651738,
        'b' to 0.0124248,
        'c' to 0.0217339,
        'd' to 0.0349835,
        'e' to 0.1041442,
        'f' to 0.0197881,
        'g' to 0.0158610,
        'h' to 0.0492888,
        'i' to 0.0558094,
        'j' to 0.0009033,
        'k' to 0.0050529,
        'l' to 0.0331490,
        'm' to 0.0202124,
        'n' to 0.0564513,
        'o' to 0.0596302,
        'p' to 0.0137645,
        'q' to 0.0008606,
        'r' to 0.0497563,
        's' to 0.0515760,
        't' to 0.0729357,
        'u' to 0.0225134,
        'v' to 0.0082903,
        'w' to 0.0171272,
        'x' to 0.0013692,
        'y' to 0.0145984,
        'z' to 0.0007836,
        ' ' to 0.1918182,
    )
}

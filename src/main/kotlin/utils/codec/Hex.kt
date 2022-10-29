@file:OptIn(ExperimentalUnsignedTypes::class)

package utils.codec

@JvmInline
value class Hex(val string: String)

fun Hex.toBytes(): UByteArray {
    val hex = this.string
    require(hex.length % 2 == 0) { "Input string must be of even length" }

    val result = UByteArray(hex.length / 2)
    for (i in 0 until hex.length / 2) {
        val fst = Character.digit(hex[i * 2], 16) shl 4
        val snd = Character.digit(hex[i * 2 + 1], 16)
        result[i] = (fst + snd).toUByte()
    }
    return result
}

fun UByteArray.toHex(): Hex {
    val bytes = this
    val sb = StringBuilder()
    for (b in bytes) {
        sb.append(String.format("%02x", b.toByte()))
    }
    return Hex(sb.toString())
}

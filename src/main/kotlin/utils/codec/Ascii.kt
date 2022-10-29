@file:OptIn(ExperimentalUnsignedTypes::class)

package utils.codec

import java.nio.charset.StandardCharsets

@JvmInline
value class Ascii(val string: String)

fun Ascii.toBytes(): UByteArray {
    val ascii = this
    return ascii.string.toByteArray().toUByteArray()
}

fun UByteArray.toAscii(): Ascii {
    return Ascii(String(this.toByteArray(), StandardCharsets.US_ASCII))
}

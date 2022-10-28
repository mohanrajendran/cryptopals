@file:OptIn(ExperimentalUnsignedTypes::class)

package utils.codec

import java.nio.charset.StandardCharsets

fun String.fromAscii(): UByteArray {
    val ascii = this
    return ascii.toByteArray().toUByteArray()
}

fun UByteArray.toAscii(): String {
    return String(this.toByteArray(), StandardCharsets.US_ASCII)
}

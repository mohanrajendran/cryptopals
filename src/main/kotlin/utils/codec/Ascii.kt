package utils.codec

import java.nio.charset.StandardCharsets

@ExperimentalUnsignedTypes
object Ascii {
    fun fromAscii(ascii: String): UByteArray {
        return ascii.toByteArray().toUByteArray()
    }

    fun toAscii(bytes: UByteArray): String {
        return String(bytes.toByteArray(), StandardCharsets.US_ASCII)
    }
}

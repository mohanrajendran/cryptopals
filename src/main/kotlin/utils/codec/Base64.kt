@file:OptIn(ExperimentalUnsignedTypes::class)

package utils.codec

private const val base64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

@JvmInline
value class Base64(val string: String)

fun Base64.toBytes(): UByteArray {
    val base64 = this.string
    require(base64.length % 4 == 0) { "Input string must be of a length divisible by 4, got length ${base64.length}" }

    if (base64.length == 0)
        return ubyteArrayOf()

    var resultLength = 3 * base64.length / 4
    if (base64[base64.length - 1] == '=') resultLength--
    if (base64[base64.length - 2] == '=') resultLength--
    val result = UByteArray(resultLength)
    var carry = 0
    var rIdx = 0
    for (i in base64.indices) {
        val current = base64[i]
        if (current == '=') break
        val value = base64Chars.indexOf(current)
        when (i % 4) {
            0 -> {
                carry = value
            }
            1 -> {
                result[rIdx++] = ((carry shl 2) + (value shr 4)).toUByte()
                carry = value and (1 shl 4) - 1
            }
            2 -> {
                result[rIdx++] = ((carry shl 4) + (value shr 2)).toUByte()
                carry = value and (1 shl 2) - 1
            }
            else -> {
                result[rIdx++] = ((carry shl 6) + value).toUByte()
            }
        }
    }
    return result
}

fun UByteArray.toBase64(): Base64 {
    val bytes = this
    val sb = StringBuilder()
    var carry = 0
    for (i in bytes.indices) {
        val current = bytes[i].toInt()
        when {
            i % 3 == 0 -> {
                val idx: Int = current shr 2
                sb.append(base64Chars[idx])
                carry = current and (1 shl 2) - 1
            }
            i % 3 == 1 -> {
                val idx: Int = (carry shl 4) + (current shr 4)
                sb.append(base64Chars[idx])
                carry = current and (1 shl 4) - 1
            }
            else -> {
                var idx: Int = (carry shl 2) + (current shr 6)
                sb.append(base64Chars[idx])
                idx = current and (1 shl 6) - 1
                sb.append(base64Chars[idx])
            }
        }
    }
    if (bytes.size % 3 == 1) {
        val idx = carry shl 4
        sb.append(base64Chars[idx])
        sb.append("==")
    } else if (bytes.size % 3 == 2) {
        val idx = carry shl 2
        sb.append(base64Chars[idx])
        sb.append('=')
    }
    return Base64(sb.toString())
}

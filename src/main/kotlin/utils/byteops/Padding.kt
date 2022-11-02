@file:OptIn(ExperimentalUnsignedTypes::class)

package utils.byteops

fun UByteArray.pkcs7Pad(blockSize: Int): UByteArray {
    val excess = blockSize - (this.size % blockSize)
    val result = UByteArray(this.size + excess)
    this.copyInto(result)
    for (i in 0 until excess) {
        result[this.size + i] = excess.toUByte()
    }

    return result
}
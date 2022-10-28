@file:OptIn(ExperimentalUnsignedTypes::class)

package utils.byteops

infix fun UByteArray.xor(other: UByteArray): UByteArray {
    require(this.size == other.size) { "Input byte arrays must be of the same length" }
    return this.zip(other).map { it.first xor it.second }.toUByteArray()
}

infix fun UByteArray.xorByte(byte: UByte): UByteArray {
    return this xorRepeat ubyteArrayOf(byte)
}

infix fun UByteArray.xorRepeat(repeatableArray: UByteArray): UByteArray {
    val result = UByteArray(this.size)
    for (i in result.indices) {
        result[i] = (this[i] xor repeatableArray[i % repeatableArray.size])
    }

    return result
}
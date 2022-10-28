@file:OptIn(ExperimentalUnsignedTypes::class)

package utils.byteops

infix fun UByteArray.hammingDistance(other: UByteArray): Int {
    require(this.size == other.size) { "Two byte arrays must be of same length" }
    return this.zip(other).sumOf { it.first distance it.second }
}

private infix fun UByte.distance(other: UByte): Int {
    return (this xor other).countOneBits()
}
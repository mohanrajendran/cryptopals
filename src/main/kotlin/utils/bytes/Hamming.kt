package utils.bytes

@ExperimentalUnsignedTypes
object Hamming {
    fun distance(fst: UByteArray, snd: UByteArray): Int {
        require(fst.size == snd.size) { "Two byte arrays must be of same length" }
        return fst.zip(snd).sumOf { distance(it.first, it.second) }
    }

    private fun distance(fst: UByte, snd: UByte): Int {
        return (fst xor snd).countOneBits()
    }
}

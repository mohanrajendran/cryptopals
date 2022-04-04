package utils.bytes

@ExperimentalUnsignedTypes
object Xor {
    fun sameLength(fst: UByteArray, snd: UByteArray): UByteArray {
        require(fst.size == snd.size) { "Input byte arrays must be of the same length" }
        return fst.zip(snd).map { it.first xor it.second }.toUByteArray()
    }

    fun repeated(notRepeat: UByteArray, repeat: UByte): UByteArray {
        return repeated(notRepeat, ubyteArrayOf(repeat))
    }

    fun repeated(notRepeat: UByteArray, repeat: UByteArray): UByteArray {
        val result = UByteArray(notRepeat.size)
        for (i in result.indices) {
            result[i] = (notRepeat[i] xor repeat[i % repeat.size])
        }
        return result
    }
}

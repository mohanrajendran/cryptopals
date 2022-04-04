package utils.ciphers

@ExperimentalUnsignedTypes
class AES128 constructor(key: UByteArray) {
    private val expandedKey: UByteArray

    companion object {
        private const val KEY_BYTES = 16
        private const val NUM_ROUNDS = 10

        // @formatter:off
        private val rCon = ubyteArrayOf(
            0x8du, 0x01u, 0x02u, 0x04u, 0x08u, 0x10u, 0x20u, 0x40u, 0x80u, 0x1bu, 0x36u, 0x6cu, 0xd8u, 0xabu, 0x4du, 0x9au,
            0x2fu, 0x5eu, 0xbcu, 0x63u, 0xc6u, 0x97u, 0x35u, 0x6au, 0xd4u, 0xb3u, 0x7du, 0xfau, 0xefu, 0xc5u, 0x91u, 0x39u,
            0x72u, 0xe4u, 0xd3u, 0xbdu, 0x61u, 0xc2u, 0x9fu, 0x25u, 0x4au, 0x94u, 0x33u, 0x66u, 0xccu, 0x83u, 0x1du, 0x3au,
            0x74u, 0xe8u, 0xcbu, 0x8du, 0x01u, 0x02u, 0x04u, 0x08u, 0x10u, 0x20u, 0x40u, 0x80u, 0x1bu, 0x36u, 0x6cu, 0xd8u,
            0xabu, 0x4du, 0x9au, 0x2fu, 0x5eu, 0xbcu, 0x63u, 0xc6u, 0x97u, 0x35u, 0x6au, 0xd4u, 0xb3u, 0x7du, 0xfau, 0xefu,
            0xc5u, 0x91u, 0x39u, 0x72u, 0xe4u, 0xd3u, 0xbdu, 0x61u, 0xc2u, 0x9fu, 0x25u, 0x4au, 0x94u, 0x33u, 0x66u, 0xccu,
            0x83u, 0x1du, 0x3au, 0x74u, 0xe8u, 0xcbu, 0x8du, 0x01u, 0x02u, 0x04u, 0x08u, 0x10u, 0x20u, 0x40u, 0x80u, 0x1bu,
            0x36u, 0x6cu, 0xd8u, 0xabu, 0x4du, 0x9au, 0x2fu, 0x5eu, 0xbcu, 0x63u, 0xc6u, 0x97u, 0x35u, 0x6au, 0xd4u, 0xb3u,
            0x7du, 0xfau, 0xefu, 0xc5u, 0x91u, 0x39u, 0x72u, 0xe4u, 0xd3u, 0xbdu, 0x61u, 0xc2u, 0x9fu, 0x25u, 0x4au, 0x94u,
            0x33u, 0x66u, 0xccu, 0x83u, 0x1du, 0x3au, 0x74u, 0xe8u, 0xcbu, 0x8du, 0x01u, 0x02u, 0x04u, 0x08u, 0x10u, 0x20u,
            0x40u, 0x80u, 0x1bu, 0x36u, 0x6cu, 0xd8u, 0xabu, 0x4du, 0x9au, 0x2fu, 0x5eu, 0xbcu, 0x63u, 0xc6u, 0x97u, 0x35u,
            0x6au, 0xd4u, 0xb3u, 0x7du, 0xfau, 0xefu, 0xc5u, 0x91u, 0x39u, 0x72u, 0xe4u, 0xd3u, 0xbdu, 0x61u, 0xc2u, 0x9fu,
            0x25u, 0x4au, 0x94u, 0x33u, 0x66u, 0xccu, 0x83u, 0x1du, 0x3au, 0x74u, 0xe8u, 0xcbu, 0x8du, 0x01u, 0x02u, 0x04u,
            0x08u, 0x10u, 0x20u, 0x40u, 0x80u, 0x1bu, 0x36u, 0x6cu, 0xd8u, 0xabu, 0x4du, 0x9au, 0x2fu, 0x5eu, 0xbcu, 0x63u,
            0xc6u, 0x97u, 0x35u, 0x6au, 0xd4u, 0xb3u, 0x7du, 0xfau, 0xefu, 0xc5u, 0x91u, 0x39u, 0x72u, 0xe4u, 0xd3u, 0xbdu,
            0x61u, 0xc2u, 0x9fu, 0x25u, 0x4au, 0x94u, 0x33u, 0x66u, 0xccu, 0x83u, 0x1du, 0x3au, 0x74u, 0xe8u, 0xcbu, 0x8du
        )
        var sBox = ubyteArrayOf(
            0x63u, 0x7Cu, 0x77u, 0x7Bu, 0xF2u, 0x6Bu, 0x6Fu, 0xC5u, 0x30u, 0x01u, 0x67u, 0x2Bu, 0xFEu, 0xD7u, 0xABu, 0x76u,
            0xCAu, 0x82u, 0xC9u, 0x7Du, 0xFAu, 0x59u, 0x47u, 0xF0u, 0xADu, 0xD4u, 0xA2u, 0xAFu, 0x9Cu, 0xA4u, 0x72u, 0xC0u,
            0xB7u, 0xFDu, 0x93u, 0x26u, 0x36u, 0x3Fu, 0xF7u, 0xCCu, 0x34u, 0xA5u, 0xE5u, 0xF1u, 0x71u, 0xD8u, 0x31u, 0x15u,
            0x04u, 0xC7u, 0x23u, 0xC3u, 0x18u, 0x96u, 0x05u, 0x9Au, 0x07u, 0x12u, 0x80u, 0xE2u, 0xEBu, 0x27u, 0xB2u, 0x75u,
            0x09u, 0x83u, 0x2Cu, 0x1Au, 0x1Bu, 0x6Eu, 0x5Au, 0xA0u, 0x52u, 0x3Bu, 0xD6u, 0xB3u, 0x29u, 0xE3u, 0x2Fu, 0x84u,
            0x53u, 0xD1u, 0x00u, 0xEDu, 0x20u, 0xFCu, 0xB1u, 0x5Bu, 0x6Au, 0xCBu, 0xBEu, 0x39u, 0x4Au, 0x4Cu, 0x58u, 0xCFu,
            0xD0u, 0xEFu, 0xAAu, 0xFBu, 0x43u, 0x4Du, 0x33u, 0x85u, 0x45u, 0xF9u, 0x02u, 0x7Fu, 0x50u, 0x3Cu, 0x9Fu, 0xA8u,
            0x51u, 0xA3u, 0x40u, 0x8Fu, 0x92u, 0x9Du, 0x38u, 0xF5u, 0xBCu, 0xB6u, 0xDAu, 0x21u, 0x10u, 0xFFu, 0xF3u, 0xD2u,
            0xCDu, 0x0Cu, 0x13u, 0xECu, 0x5Fu, 0x97u, 0x44u, 0x17u, 0xC4u, 0xA7u, 0x7Eu, 0x3Du, 0x64u, 0x5Du, 0x19u, 0x73u,
            0x60u, 0x81u, 0x4Fu, 0xDCu, 0x22u, 0x2Au, 0x90u, 0x88u, 0x46u, 0xEEu, 0xB8u, 0x14u, 0xDEu, 0x5Eu, 0x0Bu, 0xDBu,
            0xE0u, 0x32u, 0x3Au, 0x0Au, 0x49u, 0x06u, 0x24u, 0x5Cu, 0xC2u, 0xD3u, 0xACu, 0x62u, 0x91u, 0x95u, 0xE4u, 0x79u,
            0xE7u, 0xC8u, 0x37u, 0x6Du, 0x8Du, 0xD5u, 0x4Eu, 0xA9u, 0x6Cu, 0x56u, 0xF4u, 0xEAu, 0x65u, 0x7Au, 0xAEu, 0x08u,
            0xBAu, 0x78u, 0x25u, 0x2Eu, 0x1Cu, 0xA6u, 0xB4u, 0xC6u, 0xE8u, 0xDDu, 0x74u, 0x1Fu, 0x4Bu, 0xBDu, 0x8Bu, 0x8Au,
            0x70u, 0x3Eu, 0xB5u, 0x66u, 0x48u, 0x03u, 0xF6u, 0x0Eu, 0x61u, 0x35u, 0x57u, 0xB9u, 0x86u, 0xC1u, 0x1Du, 0x9Eu,
            0xE1u, 0xF8u, 0x98u, 0x11u, 0x69u, 0xD9u, 0x8Eu, 0x94u, 0x9Bu, 0x1Eu, 0x87u, 0xE9u, 0xCEu, 0x55u, 0x28u, 0xDFu,
            0x8Cu, 0xA1u, 0x89u, 0x0Du, 0xBFu, 0xE6u, 0x42u, 0x68u, 0x41u, 0x99u, 0x2Du, 0x0Fu, 0xB0u, 0x54u, 0xBBu, 0x16u
        )
        var sBoxInv = ubyteArrayOf(
            0x52u, 0x09u, 0x6Au, 0xD5u, 0x30u, 0x36u, 0xA5u, 0x38u, 0xBFu, 0x40u, 0xA3u, 0x9Eu, 0x81u, 0xF3u, 0xD7u, 0xFBu,
            0x7Cu, 0xE3u, 0x39u, 0x82u, 0x9Bu, 0x2Fu, 0xFFu, 0x87u, 0x34u, 0x8Eu, 0x43u, 0x44u, 0xC4u, 0xDEu, 0xE9u, 0xCBu,
            0x54u, 0x7Bu, 0x94u, 0x32u, 0xA6u, 0xC2u, 0x23u, 0x3Du, 0xEEu, 0x4Cu, 0x95u, 0x0Bu, 0x42u, 0xFAu, 0xC3u, 0x4Eu,
            0x08u, 0x2Eu, 0xA1u, 0x66u, 0x28u, 0xD9u, 0x24u, 0xB2u, 0x76u, 0x5Bu, 0xA2u, 0x49u, 0x6Du, 0x8Bu, 0xD1u, 0x25u,
            0x72u, 0xF8u, 0xF6u, 0x64u, 0x86u, 0x68u, 0x98u, 0x16u, 0xD4u, 0xA4u, 0x5Cu, 0xCCu, 0x5Du, 0x65u, 0xB6u, 0x92u,
            0x6Cu, 0x70u, 0x48u, 0x50u, 0xFDu, 0xEDu, 0xB9u, 0xDAu, 0x5Eu, 0x15u, 0x46u, 0x57u, 0xA7u, 0x8Du, 0x9Du, 0x84u,
            0x90u, 0xD8u, 0xABu, 0x00u, 0x8Cu, 0xBCu, 0xD3u, 0x0Au, 0xF7u, 0xE4u, 0x58u, 0x05u, 0xB8u, 0xB3u, 0x45u, 0x06u,
            0xD0u, 0x2Cu, 0x1Eu, 0x8Fu, 0xCAu, 0x3Fu, 0x0Fu, 0x02u, 0xC1u, 0xAFu, 0xBDu, 0x03u, 0x01u, 0x13u, 0x8Au, 0x6Bu,
            0x3Au, 0x91u, 0x11u, 0x41u, 0x4Fu, 0x67u, 0xDCu, 0xEAu, 0x97u, 0xF2u, 0xCFu, 0xCEu, 0xF0u, 0xB4u, 0xE6u, 0x73u,
            0x96u, 0xACu, 0x74u, 0x22u, 0xE7u, 0xADu, 0x35u, 0x85u, 0xE2u, 0xF9u, 0x37u, 0xE8u, 0x1Cu, 0x75u, 0xDFu, 0x6Eu,
            0x47u, 0xF1u, 0x1Au, 0x71u, 0x1Du, 0x29u, 0xC5u, 0x89u, 0x6Fu, 0xB7u, 0x62u, 0x0Eu, 0xAAu, 0x18u, 0xBEu, 0x1Bu,
            0xFCu, 0x56u, 0x3Eu, 0x4Bu, 0xC6u, 0xD2u, 0x79u, 0x20u, 0x9Au, 0xDBu, 0xC0u, 0xFEu, 0x78u, 0xCDu, 0x5Au, 0xF4u,
            0x1Fu, 0xDDu, 0xA8u, 0x33u, 0x88u, 0x07u, 0xC7u, 0x31u, 0xB1u, 0x12u, 0x10u, 0x59u, 0x27u, 0x80u, 0xECu, 0x5Fu,
            0x60u, 0x51u, 0x7Fu, 0xA9u, 0x19u, 0xB5u, 0x4Au, 0x0Du, 0x2Du, 0xE5u, 0x7Au, 0x9Fu, 0x93u, 0xC9u, 0x9Cu, 0xEFu,
            0xA0u, 0xE0u, 0x3Bu, 0x4Du, 0xAEu, 0x2Au, 0xF5u, 0xB0u, 0xC8u, 0xEBu, 0xBBu, 0x3Cu, 0x83u, 0x53u, 0x99u, 0x61u,
            0x17u, 0x2Bu, 0x04u, 0x7Eu, 0xBAu, 0x77u, 0xD6u, 0x26u, 0xE1u, 0x69u, 0x14u, 0x63u, 0x55u, 0x21u, 0x0Cu, 0x7Du
        )
        // @formatter:on
    }

    init {
        require(key.size == KEY_BYTES) { "Key length should be 128-bits" }
        expandedKey = expandKey(key)
    }

    private fun expandKey(key: UByteArray): UByteArray {
        val expandedKey = UByteArray(KEY_BYTES * (NUM_ROUNDS + 1))
        (0 until KEY_BYTES).forEach { i ->
            expandedKey[i] = key[i]
        }
        for (i in 4..43) {
            val word = ubyteArrayOf(
                expandedKey[(i - 1) * 4], expandedKey[(i - 1) * 4 + 1], expandedKey[(i - 1) * 4 + 2],
                expandedKey[(i - 1) * 4 + 3]
            )
            if (i % 4 == 0) {
                val temp = word[0]
                word[0] = word[1]
                word[1] = word[2]
                word[2] = word[3]
                word[3] = temp
                for (j in 0..3) word[j] = sBox[0xFF and word[j].toInt()]
                word[0] = word[0] xor rCon[i / 4]
            }
            for (j in 0..3) {
                expandedKey[i * 4 + j] = (word[j] xor expandedKey[(i - 4) * 4 + j])
            }
        }
        return expandedKey
    }

    fun decrypt(cipherText: UByteArray): UByteArray {
        val plainText = UByteArray(cipherText.size)
        var i = 0
        while (i < cipherText.size) {
            val block = cipherText.copyOfRange(i, i + KEY_BYTES)
            val plainBlock = decryptBlock(block)
            plainBlock.copyInto(plainText, i, 0, KEY_BYTES)
            i += KEY_BYTES
        }
        return plainText
    }

    private fun decryptBlock(block: UByteArray): UByteArray {
        addRoundKey(block, NUM_ROUNDS * block.size)
        for (round in NUM_ROUNDS - 1 downTo 1) {
            invShiftRows(block)
            invSubBytes(block)
            addRoundKey(block, round * block.size)
            invMixColumns(block)
        }
        invShiftRows(block)
        invSubBytes(block)
        addRoundKey(block, 0)
        return block
    }

    private fun invSubBytes(state: UByteArray) {
        for (i in state.indices) {
            state[i] = sBoxInv[0xFF and state[i].toInt()]
        }
    }

    private fun invShiftRows(state: UByteArray) {
        // shift row 1
        var temp: UByte = state[1]
        state[1] = state[13]
        state[13] = state[9]
        state[9] = state[5]
        state[5] = temp

        // shift row 2
        temp = state[2]
        state[2] = state[10]
        state[10] = temp
        temp = state[6]
        state[6] = state[14]
        state[14] = temp

        // shift row 3
        temp = state[7]
        state[7] = state[11]
        state[11] = state[15]
        state[15] = state[3]
        state[3] = temp
    }

    private fun invMixColumns(state: UByteArray) {
        var i = 0
        while (i < 16) {
            val t = ubyteArrayOf(state[i], state[i + 1], state[i + 2], state[i + 3])
            state[i] = (gmul(t[0], 0x0e) xor gmul(t[1], 0x0b) xor gmul(t[2], 0x0d) xor gmul(t[3], 0x09))
            state[i + 1] = (gmul(t[0], 0x09) xor gmul(t[1], 0x0e) xor gmul(t[2], 0x0b) xor gmul(t[3], 0x0d))
            state[i + 2] = (gmul(t[0], 0x0d) xor gmul(t[1], 0x09) xor gmul(t[2], 0x0e) xor gmul(t[3], 0x0b))
            state[i + 3] = (gmul(t[0], 0x0b) xor gmul(t[1], 0x0d) xor gmul(t[2], 0x09) xor gmul(t[3], 0x0e))
            i += 4
        }
    }

    fun encrypt(plainText: UByteArray): UByteArray {
        val cipherText = UByteArray(plainText.size)
        var i = 0
        while (i < plainText.size) {
            val block = plainText.copyOfRange(i, i + KEY_BYTES)
            val cipherBlock = encryptBlock(block)
            cipherBlock.copyInto(cipherText, i, 0, KEY_BYTES)
            i += KEY_BYTES
        }
        return cipherText
    }

    private fun encryptBlock(block: UByteArray): UByteArray {
        addRoundKey(block, 0)
        for (round in 1 until NUM_ROUNDS) {
            subBytes(block)
            shiftRows(block)
            mixColumns(block)
            addRoundKey(block, round * block.size)
        }
        subBytes(block)
        shiftRows(block)
        addRoundKey(block, NUM_ROUNDS * block.size)
        return block
    }

    private fun addRoundKey(state: UByteArray, offset: Int) {
        for (i in state.indices) {
            state[i] = state[i] xor expandedKey[offset + i]
        }
    }

    private fun subBytes(state: UByteArray) {
        for (i in state.indices) {
            state[i] = sBox[0xFF and state[i].toInt()]
        }
    }

    private fun shiftRows(state: UByteArray) {
        // shift row 1
        var temp: UByte = state[1]
        state[1] = state[5]
        state[5] = state[9]
        state[9] = state[13]
        state[13] = temp

        // shift row 2
        temp = state[2]
        state[2] = state[10]
        state[10] = temp
        temp = state[6]
        state[6] = state[14]
        state[14] = temp

        // shift row 3
        temp = state[15]
        state[15] = state[11]
        state[11] = state[7]
        state[7] = state[3]
        state[3] = temp
    }

    private fun mixColumns(state: UByteArray) {
        var i = 0
        while (i < 16) {
            val t = ubyteArrayOf(state[i], state[i + 1], state[i + 2], state[i + 3])
            state[i] = (gmul(t[0], 0x02) xor gmul(t[1], 0x03) xor t[2] xor t[3])
            state[i + 1] = (t[0] xor gmul(t[1], 0x02) xor gmul(t[2], 0x03) xor t[3])
            state[i + 2] = (t[0] xor t[1] xor gmul(t[2], 0x02) xor gmul(t[3], 0x03))
            state[i + 3] = (gmul(t[0], 0x03) xor t[1] xor t[2] xor gmul(t[3], 0x02))
            i += 4
        }
    }

    private fun gmul(aIn: UByte, bIn: Int): UByte {
        var a = aIn
        var b = bIn
        var p: UByte = 0.toUByte()
        while (a.toInt() != 0 && b != 0) {
            if (b and 0x01 == 0x01) {
                p = p xor a
            }
            b = (b and 0xff ushr 1)
            a = xtime(a)
        }
        return p
    }

    private fun xtime(vIn: UByte): UByte {
        val v = vIn.toInt()
        return (v shl 1 xor (v and 0xff ushr 7) * 0x1b).toUByte()
    }
}

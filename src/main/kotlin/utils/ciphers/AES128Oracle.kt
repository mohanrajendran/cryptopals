@file:OptIn(ExperimentalUnsignedTypes::class)

package utils.ciphers

import utils.byteops.pkcs7Pad
import utils.codec.toHex
import kotlin.random.Random
import kotlin.random.nextUBytes

fun UByteArray.repeatedBlocks(): Int {
    return this.chunked(16)
        .map { it.toUByteArray().toHex().string }
        .groupingBy { it }
        .eachCount()
        .values.filter { it > 1 }.sum()
}

fun fixedKeyEncryptionOracleWithSuffix(plainText: UByteArray, suffix: UByteArray): UByteArray {
    val random = Random(42)
    val key = random.nextUBytes(AES128.KEY_BYTES)
    return encryptionOracle(plainText, key, BlockMode.ECB, suffix = suffix)
}

fun encryptionOracle(plainText: UByteArray, mode: BlockMode? = null): UByteArray {
    val modeValue = mode ?: if (Random.nextBoolean()) BlockMode.ECB else BlockMode.CBC
    val key = Random.nextUBytes(AES128.KEY_BYTES)

    return encryptionOracle(
        plainText, key, modeValue,
        prefix = Random.nextUBytes(Random.nextInt(5, 11)),
        suffix = Random.nextUBytes(Random.nextInt(5, 11))
    )
}

private fun encryptionOracle(
    plainText: UByteArray,
    key: UByteArray,
    mode: BlockMode,
    prefix: UByteArray = ubyteArrayOf(),
    suffix: UByteArray = ubyteArrayOf()
): UByteArray {
    val aes = AES128(key)
    val padded = (prefix + plainText + suffix).pkcs7Pad(AES128.KEY_BYTES)

    return when (mode) {
        BlockMode.ECB -> aes.encrypt(padded, mode = BlockMode.ECB)
        BlockMode.CBC -> aes.encrypt(padded, mode = BlockMode.CBC, iv = Random.nextUBytes(AES128.KEY_BYTES))
    }
}
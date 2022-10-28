package utils.codec

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.uByte
import io.kotest.property.arbitrary.uByteArray
import io.kotest.property.checkAll


@ExperimentalUnsignedTypes
class Base64Test : FunSpec({
    val testCases = listOf(
        Pair("TWFu", ubyteArrayOf(77u, 97u, 110u)),
        Pair("TWE=", ubyteArrayOf(77u, 97u)),
        Pair("TQ==", ubyteArrayOf(77u))
    )

    context("Given test cases pass") {
        withData(testCases) { (base64, bytes) ->
            val uBytes = bytes.toUByteArray()
            base64.fromBase64() shouldBe uBytes
            uBytes.toBase64() shouldBe base64
        }
    }

    context("Property test") {
        test("From bytes to base64 to bytes") {
            checkAll(Arb.uByteArray(Arb.int(1..20), Arb.uByte())) {
                it.toBase64().fromBase64() shouldBe it
            }
        }
    }
})

@file:OptIn(ExperimentalUnsignedTypes::class)

package utils.codec

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll

class HexTest : FunSpec({
    test("Given test cases pass") {
        val result = "4d616e".fromHex()
        result shouldBe ubyteArrayOf(77u, 97u, 110u)
    }

    context("Property test") {
        test("From bytes to hex to bytes") {
            checkAll(Arb.uByteArray(Arb.int(0..20), Arb.uByte())) {
                it.toHex().fromHex() shouldBe it
            }
        }

        test("From hex to bytes to hex") {
            val hexArb: Arb<String> = arbitrary {
                val length = Arb.int(0..20).map { it * 2 }.bind()
                val hex = Arb.string(length, Codepoint.hex()).bind()
                hex
            }

            checkAll(hexArb) {
                it.fromHex().toHex() shouldBe it
            }
        }
    }
})
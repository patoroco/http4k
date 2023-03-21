package org.http4k.format

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger

class MoshiJsonTest : JsonContract<MoshiNode>(Moshi) {
    override val prettyString = """{
    "hello": "world"
}"""

    @Test
    override fun `serializes object to json`() {
        j {
            val input = obj(
                "string" to string("value"),
                "double" to number(1.5),
                "long" to number(10L),
                "boolean" to boolean(true),
                "bigDec" to number(BigDecimal(1.2)),
                "bigInt" to number(BigInteger("12344")),
                "null" to nullNode(),
                "int" to number(2),
                "empty" to obj(),
                "array" to array(
                    listOf(
                        string(""),
                        number(123)
                    )
                ),
                "singletonArray" to array(obj("number" to number(123)))
            )
            val expected =
                """{"string":"value","double":1.5,"long":10,"boolean":true,"bigDec":1.2,"bigInt":12344,"int":2,"empty":{},"array":["",123],"singletonArray":[{"number":123}]}"""
            assertThat(compact(input), equalTo(expected))
        }
    }
}

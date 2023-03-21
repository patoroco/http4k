package org.http4k.format

import ConfigurableDslJson
import com.dslplatform.json.runtime.Settings
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class DslJsonAutoTest : AutoMarshallingJsonContract(DslJson) {

    override val expectedAutoMarshallingResult =
        """{"string":"hello","child":{"string":"world","child":null,"numbers":[1],"bool":true},"numbers":[],"bool":false}"""

    override val expectedArbitraryMap =
        """{"str":"val1","num":123.1,"array":[1.1,"stuff"],"map":{"foo":"bar"},"bool":true}"""

    override val expectedAbitraryArray = """["foo", 123.1, {"foo":"bar"}, [1.1, 2.1], true]"""

    @Disabled("no strict mode")
    override fun `fails decoding when a extra key found`() {
        super.`fails decoding when a extra key found`()
    }

    @Test
    override fun `roundtrip arbitrary map`() {
        val wrapper = mapOf(
            "str" to "val1",
            "num" to BigDecimal("123.1"),
            "array" to listOf(BigDecimal("1.1"), "stuff"),
            "map" to mapOf("foo" to "bar"),
            "bool" to true
        )
        val asString = marshaller.asFormatString(wrapper)
        assertThat(asString.normaliseJson(), equalTo(expectedArbitraryMap))
        assertThat(marshaller.asA(asString), equalTo(wrapper))
    }

    @Test
    override fun `roundtrip arbitrary array`() {
        val wrapper = listOf(
            "foo",
            BigDecimal("123.1"),
            mapOf("foo" to "bar"),
            listOf(BigDecimal("1.1"), BigDecimal("2.1")),
            true
        )
        val asString = marshaller.asFormatString(wrapper)
        assertThat(asString.normaliseJson(), equalTo(expectedAbitraryArray.normaliseJson()))
        assertThat(marshaller.asA(asString), equalTo(wrapper))
    }

    @Test
    @Disabled("Currently doesn't work because of lazy parsing")
    fun `roundtrip list of arbitrary objects to and from body`() {
    }

    @Test
    fun `roundtrip array of arbitrary objects to and from JSON`() {
        val expected = arrayOf(obj)
        val asJsonString = DslJson.asFormatString(expected)
        val actual: Array<ArbObject> = DslJson.asA(asJsonString)
        assertThat(actual.toList(), equalTo(expected.toList()))
    }

    @Disabled("not supported")
    override fun `exception is marshalled`() {
    }

    @Test
    @Disabled("not supported")
    override fun `handles unit`() {
        marshaller.asA<Unit>("{}")
    }

    override fun customMarshaller() = ConfigurableDslJson(DDslJson(Settings.withRuntime()).asConfigurable().customise())

    override fun customMarshallerProhibitStrings() = ConfigurableDslJson(
        DDslJson(Settings.withRuntime()).asConfigurable().prohibitStrings()
            .customise()
    )
}

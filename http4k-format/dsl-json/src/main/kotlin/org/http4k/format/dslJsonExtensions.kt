package org.http4k.format

import com.dslplatform.json.BoolConverter
import com.dslplatform.json.JsonReader
import com.dslplatform.json.JsonWriter
import com.dslplatform.json.NumberConverter
import com.dslplatform.json.StringConverter
import org.http4k.lens.BiDiMapping
import java.math.BigDecimal
import java.math.BigInteger

fun DDslJson.asConfigurable(): AutoMappingConfiguration<DDslJson> =
    object : AutoMappingConfiguration<DDslJson> {

        override fun <OUT> boolean(mapping: BiDiMapping<Boolean, OUT>) = apply {
            register(mapping, BoolConverter::deserialize) { w, v ->
                BoolConverter.serialize(mapping.asIn(v), w)
            }
        }


        override fun <OUT> int(mapping: BiDiMapping<Int, OUT>) = apply {
            register(mapping, NumberConverter::deserializeInt) { w, v ->
                NumberConverter.serialize(mapping.asIn(v), w)
            }
        }

        override fun <OUT> long(mapping: BiDiMapping<Long, OUT>) = apply {
            register(mapping, NumberConverter::deserializeLong) { w, v ->
                NumberConverter.serialize(mapping.asIn(v), w)
            }
        }

        override fun <OUT> double(mapping: BiDiMapping<Double, OUT>) = apply {
            register(mapping, NumberConverter::deserializeDouble) { w, v ->
                NumberConverter.serialize(mapping.asIn(v), w)
            }
        }

        override fun <OUT> bigInteger(mapping: BiDiMapping<BigInteger, OUT>) = apply {
            register(mapping, { NumberConverter.deserializeLong(it).toBigInteger() }) { w, v ->
                NumberConverter.serialize(mapping.asIn(v).toLong(), w)
            }
        }

        override fun <OUT> bigDecimal(mapping: BiDiMapping<BigDecimal, OUT>) = apply {
            register(mapping, NumberConverter::deserializeDecimal) { w, v ->
                NumberConverter.serialize(mapping.asIn(v), w)
            }
        }

        override fun <OUT> text(mapping: BiDiMapping<String, OUT>) = apply {
            register(mapping, StringConverter::deserialize) { w, v ->
                StringConverter.serialize(mapping.asIn(v), w)
            }
        }

        override fun done() = this@asConfigurable
    }

private fun <IN, OUT> DDslJson.register(
    mapping: BiDiMapping<IN, OUT>,
    r: (JsonReader<*>) -> IN,
    w: (JsonWriter, OUT) -> Unit
) {
    registerWriter(mapping.clazz) { writer: JsonWriter, value: OUT? ->
        when (value) {
            null -> writer.writeNull()
            else -> w(writer, value)
        }
    }
    registerReader(mapping.clazz) { it: JsonReader<*> ->
        if (it.wasNull()) null else mapping.asOut(r(it))
    }
}

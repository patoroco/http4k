import com.dslplatform.json.DslJson
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.ContentType.Companion.APPLICATION_JSON
import org.http4k.format.AutoMarshalling
import org.http4k.lens.BiDiBodyLensSpec
import org.http4k.lens.BiDiWsMessageLensSpec
import org.http4k.lens.ContentNegotiation
import org.http4k.lens.string
import org.http4k.websocket.WsMessage
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.charset.Charset.defaultCharset
import kotlin.reflect.KClass

open class ConfigurableDslJson(
    private val dslJson: DslJson<Any>, val defaultContentType: ContentType = APPLICATION_JSON
) : AutoMarshalling() {
    override fun <T : Any> asA(input: String, target: KClass<T>): T = asA(input.byteInputStream(), target)

    override fun <T : Any> asA(input: InputStream, target: KClass<T>): T =
        dslJson.deserialize(target.java, input) ?: error("null")

    override fun asFormatString(input: Any) = ByteArrayOutputStream().run {
        dslJson.serialize(input, this)
        toString(defaultCharset())
    }

    inline fun <reified T : Any> Body.Companion.auto(
        description: String? = null,
        contentNegotiation: ContentNegotiation = ContentNegotiation.None,
        contentType: ContentType = defaultContentType
    ): BiDiBodyLensSpec<T> = autoBody(description, contentNegotiation, contentType)

    inline fun <reified T : Any> autoBody(
        description: String? = null,
        contentNegotiation: ContentNegotiation = ContentNegotiation.None,
        contentType: ContentType = defaultContentType
    ): BiDiBodyLensSpec<T> =
        Body.string(contentType, description, contentNegotiation).map({ asA(it, T::class) }, { asFormatString(it) })

    inline fun <reified T : Any> WsMessage.Companion.auto(): BiDiWsMessageLensSpec<T> =
        WsMessage.string().map({ it.asA(T::class) }, { asFormatString(it) })
}

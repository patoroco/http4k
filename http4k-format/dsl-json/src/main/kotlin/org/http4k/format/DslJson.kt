package org.http4k.format

import ConfigurableDslJson
import com.dslplatform.json.runtime.Settings

/**
 * To implement custom JSON configuration, create your own object singleton. Extra mappings can be added before done() is called.
 */
object DslJson : ConfigurableDslJson(
    DDslJson(Settings.withRuntime())
        .asConfigurable()
        .withStandardMappings()
        .done()
)

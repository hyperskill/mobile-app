package org.hyperskill.app.android.core.extensions

import co.touchlab.kermit.Logger
import org.hyperskill.app.android.HyperskillApp

/**
 * Provides logger with specified [tag],
 * taking it from [HyperskillApp.graph]
 */
fun logger(tag: String): Lazy<Logger> =
    LazyLoggerProvider(tag)

private class LazyLoggerProvider(private val tag: String) : Lazy<Logger> {

    private var logger: Logger? = null
    override val value: Logger
        get() {
            if (logger == null) {
                logger = HyperskillApp.graph().loggerComponent.logger.withTag(tag)
            }
            return logger!!
        }

    override fun isInitialized(): Boolean =
        logger != null
}
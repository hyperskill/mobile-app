package org.hyperskill.app.logging.inject

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter
import org.hyperskill.app.logging.SentryLogWriter
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor

object LoggerBuilder {
    fun build(sentryInteractor: SentryInteractor): Logger =
        Logger(
            config = StaticConfig(
                logWriterList = listOf(
                    platformLogWriter(),
                    SentryLogWriter(sentryInteractor)
                )
            )
        )
}
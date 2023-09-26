package org.hyperskill.app.logging.inject

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.logging.SentryLogWriter
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor

object LoggerBuilder {
    private const val LOG_TAG = "HYPERSKILL_APP"
    fun build(
        buildVariant: BuildVariant,
        sentryInteractor: SentryInteractor
    ): Logger =
        Logger(
            config = StaticConfig(
                logWriterList = listOf(
                    platformLogWriter(),
                    SentryLogWriter(buildVariant, sentryInteractor)
                ),
                minSeverity = if (buildVariant == BuildVariant.RELEASE) {
                    Severity.Info
                } else {
                    Severity.Debug
                }
            ),
            tag = LOG_TAG
        )
}
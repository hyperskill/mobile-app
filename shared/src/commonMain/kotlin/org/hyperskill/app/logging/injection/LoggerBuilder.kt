package org.hyperskill.app.logging.injection

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.logging.sentry.SentryLogWriter
import org.hyperskill.app.logging.sentry.SentryMessageStringFormatterImpl
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor

internal object LoggerBuilder {
    private const val LOG_TAG = "HYPERSKILL_APP"

    fun build(
        buildVariant: BuildVariant,
        sentryInteractor: SentryInteractor
    ): Logger =
        Logger(
            config = StaticConfig(
                logWriterList = listOfNotNull(
                    if (buildVariant == BuildVariant.RELEASE) null else platformLogWriter(),
                    SentryLogWriter(
                        sentryInteractor = sentryInteractor,
                        sentryMessageStringFormatter = SentryMessageStringFormatterImpl
                    )
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
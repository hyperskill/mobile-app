package org.hyperskill.app.logging.sentry

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Message
import co.touchlab.kermit.Severity
import co.touchlab.kermit.Tag
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumb
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumbCategory
import org.hyperskill.app.sentry.domain.model.level.HyperskillSentryLevel
import ru.nobird.app.core.model.mapOfNotNull

internal class SentryLogWriter(
    private val sentryInteractor: SentryInteractor,
    private val sentryMessageStringFormatter: SentryMessageStringFormatter
) : LogWriter() {

    companion object {
        private const val PARAM_DATA_ERROR = "error"
    }

    override fun isLoggable(tag: String, severity: Severity): Boolean =
        when (severity) {
            Severity.Verbose,
            Severity.Debug,
            Severity.Assert -> false
            Severity.Info,
            Severity.Warn,
            Severity.Error -> true
        }

    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        if (throwable != null) {
            addExceptionBreadcrumb(severity, throwable)
        }

        val formattedMessage = sentryMessageStringFormatter.formatMessage(null, Tag(tag), Message(message), throwable)
        val data = getData(throwable)

        when (severity) {
            Severity.Info -> sentryInteractor.captureMessage(formattedMessage, HyperskillSentryLevel.INFO, data)
            Severity.Warn -> sentryInteractor.captureMessage(formattedMessage, HyperskillSentryLevel.WARNING, data)
            Severity.Error -> sentryInteractor.captureMessage(formattedMessage, HyperskillSentryLevel.ERROR, data)
            Severity.Debug, Severity.Verbose, Severity.Assert -> {
                // no op
            }
        }
    }

    private fun addExceptionBreadcrumb(severity: Severity, throwable: Throwable) {
        sentryInteractor.addBreadcrumb(
            HyperskillSentryBreadcrumb(
                category = HyperskillSentryBreadcrumbCategory.EXCEPTION,
                message = throwable.toString(),
                level = when (severity) {
                    Severity.Verbose,
                    Severity.Debug,
                    Severity.Assert -> HyperskillSentryLevel.DEBUG
                    Severity.Info -> HyperskillSentryLevel.INFO
                    Severity.Warn -> HyperskillSentryLevel.WARNING
                    Severity.Error -> HyperskillSentryLevel.ERROR
                }
            )
        )
    }

    private fun getData(throwable: Throwable?): Map<String, Any> =
        mapOfNotNull(PARAM_DATA_ERROR to throwable.toString())
}
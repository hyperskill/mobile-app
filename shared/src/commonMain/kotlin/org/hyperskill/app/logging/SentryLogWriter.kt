package org.hyperskill.app.logging

import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Message
import co.touchlab.kermit.MessageStringFormatter
import co.touchlab.kermit.Severity
import co.touchlab.kermit.Tag
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.level.HyperskillSentryLevel

class SentryLogWriter(
    private val sentryInteractor: SentryInteractor,
    private val messageStringFormatter: MessageStringFormatter = DefaultFormatter
) : LogWriter() {

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
        val formattedMessage = messageStringFormatter.formatMessage(null, Tag(tag), Message(message))
        when (severity) {
            Severity.Info -> sentryInteractor.captureMessage(formattedMessage, HyperskillSentryLevel.INFO)
            Severity.Warn -> sentryInteractor.captureMessage(formattedMessage, HyperskillSentryLevel.WARNING)
            Severity.Error -> sentryInteractor.captureMessage(formattedMessage, HyperskillSentryLevel.ERROR)
            Severity.Debug -> sentryInteractor.captureMessage(formattedMessage, HyperskillSentryLevel.DEBUG)
            Severity.Verbose, Severity.Assert -> {
                // no op
            }
        }
        if (throwable != null) {
            sentryInteractor.captureException(throwable)
        }
    }
}
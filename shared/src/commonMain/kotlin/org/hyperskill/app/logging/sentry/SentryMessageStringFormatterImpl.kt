package org.hyperskill.app.logging.sentry

import co.touchlab.kermit.Message
import co.touchlab.kermit.Severity
import co.touchlab.kermit.Tag

internal object SentryMessageStringFormatterImpl : SentryMessageStringFormatter {
    override fun formatMessage(severity: Severity?, tag: Tag?, message: Message, throwable: Throwable?): String {
        val formattedMessage = formatMessage(severity, tag, message)
        return if (throwable != null) {
            "$formattedMessage $throwable"
        } else {
            formattedMessage
        }
    }
}
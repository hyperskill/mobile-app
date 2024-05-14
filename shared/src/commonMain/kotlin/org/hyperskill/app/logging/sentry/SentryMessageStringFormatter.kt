package org.hyperskill.app.logging.sentry

import co.touchlab.kermit.Message
import co.touchlab.kermit.MessageStringFormatter
import co.touchlab.kermit.Severity
import co.touchlab.kermit.Tag

internal interface SentryMessageStringFormatter : MessageStringFormatter {
    fun formatMessage(severity: Severity?, tag: Tag?, message: Message, throwable: Throwable?): String
}
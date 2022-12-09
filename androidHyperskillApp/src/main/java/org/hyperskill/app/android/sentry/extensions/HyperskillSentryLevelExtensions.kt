package org.hyperskill.app.android.sentry.extensions

import io.sentry.SentryLevel
import org.hyperskill.app.sentry.domain.model.level.HyperskillSentryLevel

fun HyperskillSentryLevel.toSentryLevel(): SentryLevel =
    when (this) {
        HyperskillSentryLevel.DEBUG -> SentryLevel.DEBUG
        HyperskillSentryLevel.INFO -> SentryLevel.INFO
        HyperskillSentryLevel.WARNING -> SentryLevel.WARNING
        HyperskillSentryLevel.ERROR -> SentryLevel.ERROR
        HyperskillSentryLevel.FATAL -> SentryLevel.FATAL
    }
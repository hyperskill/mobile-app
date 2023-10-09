package org.hyperskill.app.sentry.domain.model.level

import org.hyperskill.app.core.domain.BuildVariant

/**
 * Represents the severity level of an event.
 *
 * The level is one of five values, which are — in order of severity — fatal, error, warning, info, and debug.
 *
 */
enum class HyperskillSentryLevel {
    DEBUG,
    INFO,
    WARNING,
    ERROR,
    FATAL;

    companion object {
        fun min(buildVariant: BuildVariant): HyperskillSentryLevel =
            if (buildVariant == BuildVariant.RELEASE) {
                INFO
            } else {
                DEBUG
            }
    }
}
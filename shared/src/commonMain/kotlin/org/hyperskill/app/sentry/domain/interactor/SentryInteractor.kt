package org.hyperskill.app.sentry.domain.interactor

import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumb
import org.hyperskill.app.sentry.domain.model.level.HyperskillSentryLevel
import org.hyperskill.app.sentry.domain.model.manager.SentryManager

/**
 * Interactor that capable of performing business-logic with Sentry.
 *
 * @property sentryManager The platform specific implementation of the Sentry SDK.
 *
 * @see SentryManager
 */
class SentryInteractor(
    private val sentryManager: SentryManager
) {
    fun addBreadcrumb(breadcrumb: HyperskillSentryBreadcrumb) =
        sentryManager.addBreadcrumb(breadcrumb)

    fun captureMessage(message: String, level: HyperskillSentryLevel) =
        sentryManager.captureMessage(message, level)

    fun captureErrorMessage(message: String) =
        sentryManager.captureErrorMessage(message)
}
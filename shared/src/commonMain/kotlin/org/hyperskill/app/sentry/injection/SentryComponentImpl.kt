package org.hyperskill.app.sentry.injection

import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.manager.SentryManager

class SentryComponentImpl(
    sentryManager: SentryManager
) : SentryComponent {
    override val sentryInteractor: SentryInteractor =
        SentryInteractor(sentryManager)
}
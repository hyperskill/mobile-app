package org.hyperskill.app.sentry.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor

class SentryComponentImpl(
    appGraph: AppGraph
) : SentryComponent {
    override val sentryInteractor: SentryInteractor =
        SentryInteractor(appGraph.commonComponent.sentryManager)
}
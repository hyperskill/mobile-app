package org.hyperskill.app.sentry.injection

import org.hyperskill.app.sentry.domain.interactor.SentryInteractor

interface SentryComponent {
    val sentryInteractor: SentryInteractor
}
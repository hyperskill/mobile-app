package org.hyperskill.app.android.play_services.injection

import android.content.Context
import org.hyperskill.app.android.play_services.domain.model.PlayServicesCheckerImpl
import org.hyperskill.app.play_services.domain.PlayServicesChecker
import org.hyperskill.app.play_services.injection.PlayServicesCheckerComponent
import org.hyperskill.app.sentry.injection.SentryComponent

class PlayServicesCheckerComponentImpl(
    private val context: Context,
    private val sentryComponent: SentryComponent
) : PlayServicesCheckerComponent {
    override val playServicesChecker: PlayServicesChecker
        get() = PlayServicesCheckerImpl(
            context,
            sentryComponent.sentryInteractor
        )
}
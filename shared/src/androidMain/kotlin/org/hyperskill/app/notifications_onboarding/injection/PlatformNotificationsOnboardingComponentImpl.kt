package org.hyperskill.app.notifications_onboarding.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.notifications_onboarding.presentation.NotificationOnboardingViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformNotificationsOnboardingComponentImpl(
    private val notificationsOnboardingComponent: NotificationsOnboardingComponent
) : PlatformNotificationsOnboardingComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                NotificationOnboardingViewModel::class.java to {
                    NotificationOnboardingViewModel(
                        notificationsOnboardingComponent.notificationsOnboardingFeature.wrapWithViewContainer()
                    )
                }
            )
        )
}
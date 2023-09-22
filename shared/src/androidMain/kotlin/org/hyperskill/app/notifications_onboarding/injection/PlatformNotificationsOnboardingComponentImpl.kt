package org.hyperskill.app.notifications_onboarding.injection

import org.hyperskill.app.core.flowredux.presentation.wrapWithFlowView
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingViewModel

class PlatformNotificationsOnboardingComponentImpl(
    private val notificationsOnboardingComponent: NotificationsOnboardingComponent
) : PlatformNotificationsOnboardingComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                NotificationsOnboardingViewModel::class.java to {
                    NotificationsOnboardingViewModel(
                        notificationsOnboardingComponent.notificationsOnboardingFeature.wrapWithFlowView()
                    )
                }
            )
        )
}
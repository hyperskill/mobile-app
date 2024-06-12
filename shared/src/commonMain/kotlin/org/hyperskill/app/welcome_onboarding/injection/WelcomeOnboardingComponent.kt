package org.hyperskill.app.welcome_onboarding.injection

import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.State
import ru.nobird.app.presentation.redux.feature.Feature

interface WelcomeOnboardingComponent {
    val welcomeOnboardingFeature: Feature<State, Message, Action>
}
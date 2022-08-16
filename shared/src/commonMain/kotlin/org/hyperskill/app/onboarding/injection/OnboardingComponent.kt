package org.hyperskill.app.onboarding.injection

import org.hyperskill.app.onboarding.presentation.OnboardingFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface OnboardingComponent {
    val onboardingFeature: Feature<OnboardingFeature.State, OnboardingFeature.Message, OnboardingFeature.Action>
}
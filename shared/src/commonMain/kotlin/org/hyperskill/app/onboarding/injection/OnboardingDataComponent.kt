package org.hyperskill.app.onboarding.injection

import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor

interface OnboardingDataComponent {
    val onboardingInteractor: OnboardingInteractor
}
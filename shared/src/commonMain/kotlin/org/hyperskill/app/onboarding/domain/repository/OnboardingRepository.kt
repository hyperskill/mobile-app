package org.hyperskill.app.onboarding.domain.repository

import org.hyperskill.app.onboarding.domain.model.ProblemsOnboardingFlags

interface OnboardingRepository {

    fun isParsonsOnboardingShown(): Boolean
    fun setParsonsOnboardingShown(isShown: Boolean)

    fun isFillBlanksInputModeOnboardingShown(): Boolean
    fun setFillBlanksInputModeOnboardingShown(isShown: Boolean)
    fun isFillBlanksSelectModeOnboardingShown(): Boolean
    fun setFillBlanksSelectModeOnboardingShown(isShown: Boolean)

    fun wasNotificationOnboardingShown(): Boolean
    fun setNotificationOnboardingWasShown(wasShown: Boolean)

    fun wasFirstProblemOnboardingShown(): Boolean
    fun setFirstProblemOnboardingWasShown(wasShown: Boolean)
}

fun OnboardingRepository.getProblemsOnboardingFlags(): ProblemsOnboardingFlags =
    ProblemsOnboardingFlags(
        isParsonsOnboardingShown = isParsonsOnboardingShown(),
        isFillBlanksInputModeOnboardingShown = isFillBlanksInputModeOnboardingShown(),
        isFillBlanksSelectModeOnboardingShown = isFillBlanksSelectModeOnboardingShown()
    )
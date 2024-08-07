package org.hyperskill.onboarding.domain.model

import org.hyperskill.app.onboarding.domain.model.ProblemsOnboardingFlags

fun ProblemsOnboardingFlags.Companion.stub(
    isParsonsOnboardingShown: Boolean = false
): ProblemsOnboardingFlags =
    ProblemsOnboardingFlags(
        isParsonsOnboardingShown = isParsonsOnboardingShown
    )
package org.hyperskill.app.first_problem_onboarding.injection

import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Action
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Message
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface FirstProblemOnboardingComponent {
    fun firstProblemOnboardingFeature(isNewUserMode: Boolean): Feature<ViewState, Message, Action>
}
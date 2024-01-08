package org.hyperskill.app.interview_preparation_onboarding.injection

import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.Action
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.Message
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.State
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.presentation.redux.feature.Feature

interface InterviewPreparationOnboardingComponent {
    fun interviewPreparationOnboardingFeature(stepRoute: StepRoute): Feature<State, Message, Action>
}
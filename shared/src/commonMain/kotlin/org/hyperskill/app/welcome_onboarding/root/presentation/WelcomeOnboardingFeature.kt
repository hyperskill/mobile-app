package org.hyperskill.app.welcome_onboarding.root.presentation

import kotlin.time.Duration
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.model.isNewUser
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingFeatureParams
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeOnboardingProgrammingLanguage
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeOnboardingStartScreen
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeQuestionnaireItemType
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeQuestionnaireType

object WelcomeOnboardingFeature {

    data class State(
        val initialStep: WelcomeOnboardingStartScreen,
        val nextLearningActivityState: NextLearningActivityState,
        val isNextLearningActivityLoadingShowed: Boolean
    )

    sealed interface NextLearningActivityState {
        object Idle : NextLearningActivityState
        object Loading : NextLearningActivityState
        object Error : NextLearningActivityState
        data class Success(val nextLearningActivity: LearningActivity?) : NextLearningActivityState
    }

    fun shouldLaunchFeature(profile: Profile, isNotificationPermissionGranted: Boolean): Boolean =
        profile.isNewUser || !isNotificationPermissionGranted

    internal fun initialState(params: WelcomeOnboardingFeatureParams) =
        State(
            initialStep = when {
                params.profile.isNewUser -> WelcomeOnboardingStartScreen.START_SCREEN
                !params.isNotificationPermissionGranted -> WelcomeOnboardingStartScreen.NOTIFICATION_ONBOARDING
                else -> error("Welcome onboarding should not be shown")
            },
            nextLearningActivityState = NextLearningActivityState.Idle,
            isNextLearningActivityLoadingShowed = false
        )

    sealed interface Message {
        object Initialize : Message
        object StartJourneyClicked : Message
        data class QuestionnaireItemClicked(
            val questionnaireType: WelcomeQuestionnaireType,
            val itemType: WelcomeQuestionnaireItemType
        ) : Message
        data class ProgrammingLanguageSelected(val language: WelcomeOnboardingProgrammingLanguage) : Message
        object TrackSelected : Message
        object NotificationPermissionOnboardingCompleted : Message
        object FinishOnboardingShowed : Message
    }

    internal sealed interface InternalMessage : Message {
        object FinishOnboardingTimerFired : InternalMessage
        data class FetchNextLearningActivitySuccess(val nextLearningActivity: LearningActivity?) : InternalMessage
        object FetchNextLearningActivityError : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                object StartScreen : NavigateTo
                data class WelcomeOnboardingQuestionnaire(val type: WelcomeQuestionnaireType) : NavigateTo
                object ChooseProgrammingLanguage : NavigateTo
                data class TrackDetails(val track: WelcomeOnboardingTrack) : NavigateTo
                object NotificationOnboarding : NavigateTo
                object OnboardingFinish : NavigateTo
            }
            data class CompleteWelcomeOnboarding(val stepRoute: StepRoute?) : NavigateTo
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val event: AnalyticEvent) : InternalAction
        object FetchNextLearningActivity : InternalAction
        data class LaunchFinishOnboardingTimer(val duration: Duration) : InternalAction
    }
}
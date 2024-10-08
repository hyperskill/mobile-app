package org.hyperskill.app.welcome_onboarding.root.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.model.isNewUser
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingFeatureParams
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.questionnaire.model.WelcomeQuestionnaireItemType
import org.hyperskill.app.welcome_onboarding.questionnaire.model.WelcomeQuestionnaireType
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeOnboardingProgrammingLanguage
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeOnboardingStartScreen

object WelcomeOnboardingFeature {

    data class State(
        internal val initialStep: WelcomeOnboardingStartScreen,
        internal val selectedTrack: WelcomeOnboardingTrack?,
        internal val nextLearningActivityState: NextLearningActivityState,
        val isNextLearningActivityLoadingShown: Boolean
    )

    sealed interface NextLearningActivityState {
        data object Idle : NextLearningActivityState
        data object Loading : NextLearningActivityState
        data object Error : NextLearningActivityState
        data class Success(val nextLearningActivity: LearningActivity?) : NextLearningActivityState
    }

    fun shouldBeLaunchedAfterAuthorization(profile: Profile, isNotificationPermissionGranted: Boolean): Boolean =
        profile.isNewUser || !isNotificationPermissionGranted

    fun shouldBeLaunchedOnStartup(profile: Profile): Boolean =
        profile.isNewUser

    internal fun initialState(params: WelcomeOnboardingFeatureParams) =
        State(
            initialStep = when {
                params.profile.isNewUser -> WelcomeOnboardingStartScreen.START_SCREEN
                !params.isNotificationPermissionGranted -> WelcomeOnboardingStartScreen.NOTIFICATION_ONBOARDING
                else -> error("Welcome onboarding should not be shown")
            },
            selectedTrack = null,
            nextLearningActivityState = NextLearningActivityState.Idle,
            isNextLearningActivityLoadingShown = false
        )

    sealed interface Message {
        data object Initialize : Message
        data object StartJourneyClicked : Message
        data class QuestionnaireItemClicked(
            val questionnaireType: WelcomeQuestionnaireType,
            val itemType: WelcomeQuestionnaireItemType
        ) : Message
        data class ProgrammingLanguageSelected(val language: WelcomeOnboardingProgrammingLanguage) : Message
        data class TrackSelected(
            val selectedTrack: WelcomeOnboardingTrack,
            val isNotificationPermissionGranted: Boolean
        ) : Message
        data object NotificationPermissionOnboardingCompleted : Message
        data object FinishClicked : Message

        data object StartOnboardingViewed : Message
        class UserQuestionnaireViewed(val questionnaireType: WelcomeQuestionnaireType) : Message
        data object SelectProgrammingLanguageViewed : Message
        data object FinishOnboardingViewed : Message
    }

    internal sealed interface InternalMessage : Message {
        data class FetchNextLearningActivitySuccess(val nextLearningActivity: LearningActivity?) : InternalMessage
        data object FetchNextLearningActivityError : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                data object StartScreen : NavigateTo
                data class WelcomeOnboardingQuestionnaire(val type: WelcomeQuestionnaireType) : NavigateTo
                data object ChooseProgrammingLanguage : NavigateTo
                data class TrackDetails(val track: WelcomeOnboardingTrack) : NavigateTo
                data object NotificationOnboarding : NavigateTo
                data class OnboardingFinish(val selectedTrack: WelcomeOnboardingTrack) : NavigateTo
            }

            data class CompleteWelcomeOnboarding(val stepRoute: StepRoute?) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val event: AnalyticEvent) : InternalAction
        data object FetchNextLearningActivity : InternalAction
    }
}
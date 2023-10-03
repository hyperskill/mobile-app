package org.hyperskill.app.first_problem_onboarding.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.step.domain.model.StepRoute

object FirstProblemOnboardingFeature {
    internal data class State(
        val profileState: ProfileState,
        val nextLearningActivityState: NextLearningActivityState,
        /**
         * Field to differ new and existing users to change screen texts/icons
         */
        val isNewUserMode: Boolean,
        /**
         * Flag for displaying loading animation (after click on CTA button)
         */
        val isLearningActivityLoading: Boolean
    )

    internal fun initialState(isNewUserMode: Boolean): State =
        State(
            profileState = ProfileState.Idle,
            nextLearningActivityState = NextLearningActivityState.Idle,
            isNewUserMode = isNewUserMode,
            isLearningActivityLoading = false
        )

    internal sealed interface ProfileState {
        object Idle : ProfileState
        object Loading : ProfileState
        object Error : ProfileState
        data class Content(val profile: Profile) : ProfileState
    }

    internal sealed interface NextLearningActivityState {
        object Idle : NextLearningActivityState
        object Loading : NextLearningActivityState
        object Error : NextLearningActivityState
        data class Content(val nextLearningActivity: LearningActivity?) : NextLearningActivityState
    }

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object Error : ViewState
        data class Content(
            val subtitle: String,
            val isNewUserMode: Boolean,
            val isLearningActivityLoading: Boolean
        ) : ViewState
    }

    sealed interface Message {
        object Initialize : Message
        object RetryContentLoading : Message
        object LearningActionButtonClicked : Message

        /**
         * Analytic
         * */
        object ViewedEventMessage : Message
    }

    internal sealed interface FetchProfileResult : Message {
        object Error : FetchProfileResult
        data class Success(val profile: Profile) : FetchProfileResult
    }

    internal sealed interface FetchNextLearningActivityResult : Message {
        object Error : FetchNextLearningActivityResult
        data class Success(val nextLearningActivity: LearningActivity?) : FetchNextLearningActivityResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            object ShowNetworkError : ViewAction

            sealed interface NavigateTo : ViewAction {
                data class StepScreen(val stepRoute: StepRoute) : NavigateTo
                object StudyPlanScreen : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticsEvent(val event: HyperskillAnalyticEvent) : InternalAction
        object FetchProfile : InternalAction
        object FetchNextLearningActivity : InternalAction
    }
}
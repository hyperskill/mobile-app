package org.hyperskill.app.topic_completed_modal.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.topic_completed_modal.domain.model.TopicCompletedModalFeatureParams
import org.hyperskill.app.topic_completed_modal.domain.model.TopicCompletedModalFeatureParams.ContinueBehaviour
import org.hyperskill.app.topics.domain.model.Topic

object TopicCompletedModalFeature {
    internal data class State(
        val topic: Topic,
        val passedTopicsCount: Int,
        val continueBehaviour: ContinueBehaviour
    )

    internal fun initialState(params: TopicCompletedModalFeatureParams) =
        State(
            topic = params.topic,
            passedTopicsCount = params.passedTopicsCount,
            continueBehaviour = params.continueBehaviour
        )

    data class ViewState(
        val title: String,
        val description: String,
        val earnedGemsText: String,
        val callToActionButtonTitle: String,
        val spacebotAvatarVariantIndex: Int,
        val backgroundAnimationStyle: BackgroundAnimationStyle
    ) {
        enum class BackgroundAnimationStyle {
            FIRST, SECOND
        }
    }

    sealed interface Message {
        object CloseButtonClicked : Message
        object CallToActionButtonClicked : Message

        object ShownEventMessage : Message
        object HiddenEventMessage : Message
        object UserDidTakeScreenshotEventMessage : Message
    }

    internal sealed interface InternalMessage : Message

    sealed interface Action {
        sealed interface ViewAction : Action {
            object Dismiss : ViewAction

            sealed interface NavigateTo : ViewAction {
                object NextTopic : NavigateTo
                object StudyPlan : NavigateTo
                data class Paywall(val paywallTransitionSource: PaywallTransitionSource) : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}
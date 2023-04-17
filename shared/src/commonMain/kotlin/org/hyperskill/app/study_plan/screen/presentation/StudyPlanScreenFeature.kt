package org.hyperskill.app.study_plan.screen.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature

object StudyPlanScreenFeature {

    fun isAvailable(buildKonfig: BuildKonfig): Boolean =
        BuildKonfig.IS_INTERNAL_TESTING ?: (buildKonfig.buildVariant == BuildVariant.DEBUG)

    internal data class State(
        val toolbarState: GamificationToolbarFeature.State,
        val studyPlanWidgetState: StudyPlanWidgetFeature.State
    ) {
        val isRefreshing: Boolean
            get() = toolbarState is GamificationToolbarFeature.State.Content && toolbarState.isRefreshing ||
                studyPlanWidgetState.isRefreshing
    }

    sealed interface Message {

        object Initialize : Message

        object PullToRefresh : Message

        object ScreenBecomesActive : Message

        object ViewedEventMessage : Message

        data class GamificationToolbarMessage(
            val message: GamificationToolbarFeature.Message
        ) : Message

        data class StudyPlanWidgetMessage(
            val message: StudyPlanWidgetFeature.Message
        ) : Message
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data class GamificationToolbarViewAction(
                val viewAction: GamificationToolbarFeature.Action.ViewAction
            ) : ViewAction
            data class StudyPlanWidgetViewAction(
                val viewAction: StudyPlanWidgetFeature.Action.ViewAction
            ) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction

        data class GamificationToolbarAction(
            val action: GamificationToolbarFeature.Action
        ) : InternalAction

        data class StudyPlanWidgetAction(
            val action: StudyPlanWidgetFeature.Action
        ) : InternalAction
    }
}
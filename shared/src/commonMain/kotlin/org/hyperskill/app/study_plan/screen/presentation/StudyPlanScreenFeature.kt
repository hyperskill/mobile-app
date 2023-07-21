package org.hyperskill.app.study_plan.screen.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.isRefreshing
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.isRefreshing
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.view.model.StudyPlanWidgetViewState

object StudyPlanScreenFeature {

    internal data class State(
        val toolbarState: GamificationToolbarFeature.State,
        val problemsLimitState: ProblemsLimitFeature.State,
        val studyPlanWidgetState: StudyPlanWidgetFeature.State
    ) {
        val isRefreshing: Boolean
            get() = toolbarState.isRefreshing ||
                problemsLimitState.isRefreshing ||
                studyPlanWidgetState.isRefreshing
    }

    data class ViewState(
        val trackTitle: String?,
        val toolbarState: GamificationToolbarFeature.State,
        val problemsLimitViewState: ProblemsLimitFeature.ViewState,
        val studyPlanWidgetViewState: StudyPlanWidgetViewState,
        val isRefreshing: Boolean
    )

    sealed interface Message {

        object Initialize : Message

        object RetryContentLoading : Message

        object PullToRefresh : Message

        object ScreenBecomesActive : Message

        object ViewedEventMessage : Message

        data class GamificationToolbarMessage(
            val message: GamificationToolbarFeature.Message
        ) : Message

        data class ProblemsLimitMessage(
            val message: ProblemsLimitFeature.Message
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
            data class ProblemsLimitViewAction(
                val viewAction: ProblemsLimitFeature.Action.ViewAction
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

        data class ProblemsLimitAction(
            val action: ProblemsLimitFeature.Action
        ) : InternalAction
    }
}
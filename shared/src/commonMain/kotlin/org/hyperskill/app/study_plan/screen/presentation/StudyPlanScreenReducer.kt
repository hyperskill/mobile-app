package org.hyperskill.app.study_plan.screen.presentation

import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.study_plan.analytics.StudyPlanClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.analytics.StudyPlanViewedHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal typealias StudyPlanScreenReducerResult = Pair<StudyPlanScreenFeature.State, Set<StudyPlanScreenFeature.Action>>

internal class StudyPlanScreenReducer(
    private val toolbarReducer: GamificationToolbarReducer,
    private val studyPlanWidgetReducer: StudyPlanWidgetReducer
) : StateReducer<StudyPlanScreenFeature.State, StudyPlanScreenFeature.Message, StudyPlanScreenFeature.Action> {
    override fun reduce(
        state: StudyPlanScreenFeature.State,
        message: StudyPlanScreenFeature.Message
    ): StudyPlanScreenReducerResult =
        when (message) {
            is StudyPlanScreenFeature.Message.Initialize -> {
                handleInitializeMessage(state, message)
            }
            is StudyPlanScreenFeature.Message.PullToRefresh -> {
                val (widgetState, widgetActions)  = if (!state.studyPlanWidgetState.isRefreshing) {
                    state.studyPlanWidgetState.copy(isRefreshing = true) to setOf(
                        StudyPlanScreenFeature.InternalAction.StudyPlanWidgetAction(
                            StudyPlanWidgetFeature.InternalAction.FetchStudyPlan(showLoadingIndicators = false)
                        )
                    )
                } else {
                    state.studyPlanWidgetState to emptySet()
                }

                val (toolbarState, toolbarActions) = reduceToolbarMessage(
                    state.toolbarState,
                    GamificationToolbarFeature.Message.PullToRefresh(GamificationToolbarScreen.STUDY_PLAN)
                )

                state.copy(
                    studyPlanWidgetState = widgetState,
                    toolbarState = toolbarState,
                ) to widgetActions + toolbarActions + setOf(
                    StudyPlanScreenFeature.InternalAction.LogAnalyticEvent(
                        StudyPlanClickedPullToRefreshHyperskillAnalyticEvent()
                    )
                )
            }
            is StudyPlanScreenFeature.Message.GamificationToolbarMessage -> {
                val (toolbarState, toolbarActions) =
                    reduceToolbarMessage(state.toolbarState, message.message)
                state.copy(toolbarState = toolbarState) to toolbarActions
            }
            is StudyPlanScreenFeature.Message.StudyPlanWidgetMessage -> {
                val (widgetState, widgetActions) =
                    reduceStudyPlanWidgetMessage(state.studyPlanWidgetState, message.message)
                state.copy(studyPlanWidgetState = widgetState) to widgetActions
            }
            StudyPlanScreenFeature.Message.ViewedEventMessage -> {
                state to setOf(
                    StudyPlanScreenFeature.InternalAction.LogAnalyticEvent(
                        StudyPlanViewedHyperskillAnalyticEvent()
                    )
                )
            }
        }

    private fun handleInitializeMessage(
        state: StudyPlanScreenFeature.State,
        message: StudyPlanScreenFeature.Message.Initialize
    ): StudyPlanScreenReducerResult {
        val (toolbarState, toolbarActions) =
            reduceToolbarMessage(
                state.toolbarState,
                GamificationToolbarFeature.Message.Initialize(
                    screen = GamificationToolbarScreen.STUDY_PLAN,
                    forceUpdate = message.forceUpdate
                )
            )
        val (studyPlanState, studyPlanActions) =
            reduceStudyPlanWidgetMessage(
                state.studyPlanWidgetState,
                StudyPlanWidgetFeature.Message.Initialize
            )
        return state.copy(
            toolbarState = toolbarState,
            studyPlanWidgetState = studyPlanState
        ) to (toolbarActions + studyPlanActions)
    }

    private fun reduceToolbarMessage(
        state: GamificationToolbarFeature.State,
        message: GamificationToolbarFeature.Message
    ): Pair<GamificationToolbarFeature.State, Set<StudyPlanScreenFeature.Action>> {
        val (toolbarState, toolbarActions) =
            toolbarReducer.reduce(state, message)
        val actions = toolbarActions
            .map {
                if (it is GamificationToolbarFeature.Action.ViewAction) {
                    StudyPlanScreenFeature.Action.ViewAction.GamificationToolbarViewAction(it)
                } else {
                    StudyPlanScreenFeature.InternalAction.GamificationToolbarAction(it)
                }
            }
            .toSet()
        return toolbarState to actions
    }

    private fun reduceStudyPlanWidgetMessage(
        state: StudyPlanWidgetFeature.State,
        message: StudyPlanWidgetFeature.Message
    ): Pair<StudyPlanWidgetFeature.State, Set<StudyPlanScreenFeature.Action>> {
        val (widgetState, widgetActions) =
            studyPlanWidgetReducer.reduce(state, message)
        val actions = widgetActions
            .map {
                if (it is StudyPlanWidgetFeature.Action.ViewAction) {
                    StudyPlanScreenFeature.Action.ViewAction.StudyPlanWidgetViewAction(it)
                } else {
                    StudyPlanScreenFeature.InternalAction.StudyPlanWidgetAction(it)
                }
            }
            .toSet()
        return widgetState to actions
    }
}
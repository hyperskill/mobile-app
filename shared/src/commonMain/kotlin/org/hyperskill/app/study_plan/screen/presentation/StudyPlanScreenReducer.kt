package org.hyperskill.app.study_plan.screen.presentation

import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanViewedHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal typealias StudyPlanScreenReducerResult = Pair<StudyPlanScreenFeature.State, Set<StudyPlanScreenFeature.Action>>

internal class StudyPlanScreenReducer(
    private val toolbarReducer: GamificationToolbarReducer,
    private val problemsLimitReducer: ProblemsLimitReducer,
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
                val (widgetState, widgetActions) = reduceStudyPlanWidgetMessage(
                    state.studyPlanWidgetState,
                    StudyPlanWidgetFeature.Message.PullToRefresh
                )

                val (toolbarState, toolbarActions) = reduceToolbarMessage(
                    state.toolbarState,
                    GamificationToolbarFeature.Message.PullToRefresh
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
            is StudyPlanScreenFeature.Message.ScreenBecomesActive -> {
                val (widgetState, widgetActions) = reduceStudyPlanWidgetMessage(
                    state.studyPlanWidgetState,
                    StudyPlanWidgetFeature.Message.ReloadContentInBackground
                )

                state.copy(
                    studyPlanWidgetState = widgetState,
                ) to widgetActions
            }
            is StudyPlanScreenFeature.Message.GamificationToolbarMessage -> {
                val (toolbarState, toolbarActions) =
                    reduceToolbarMessage(state.toolbarState, message.message)
                state.copy(toolbarState = toolbarState) to toolbarActions
            }
            is StudyPlanScreenFeature.Message.ProblemsLimitMessage -> {
                val (problemsLimitState, problemsLimitActions) =
                    reduceProblemsLimitMessage(state.problemsLimitState, message.message)
                state.copy(problemsLimitState = problemsLimitState) to problemsLimitActions
            }
            is StudyPlanScreenFeature.Message.StudyPlanWidgetMessage -> {
                val (widgetState, widgetActions) =
                    reduceStudyPlanWidgetMessage(state.studyPlanWidgetState, message.message)
                state.copy(studyPlanWidgetState = widgetState) to widgetActions
            }
            is StudyPlanScreenFeature.Message.ViewedEventMessage -> {
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
                GamificationToolbarFeature.Message.Initialize()
            )
        val (problemsLimitState, problemsLimitActions) =
            reduceProblemsLimitMessage(
                state.problemsLimitState,
                ProblemsLimitFeature.Message.Initialize()
            )
        val (studyPlanState, studyPlanActions) =
            reduceStudyPlanWidgetMessage(
                state.studyPlanWidgetState,
                StudyPlanWidgetFeature.Message.Initialize
            )
        return state.copy(
            toolbarState = toolbarState,
            problemsLimitState = problemsLimitState,
            studyPlanWidgetState = studyPlanState
        ) to (toolbarActions + problemsLimitActions + studyPlanActions)
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

    private fun reduceProblemsLimitMessage(
        state: ProblemsLimitFeature.State,
        message: ProblemsLimitFeature.Message
    ): Pair<ProblemsLimitFeature.State, Set<StudyPlanScreenFeature.Action>> {
        val (problemsLimitState, problemsLimitActions) =
            problemsLimitReducer.reduce(state, message)

        val actions = problemsLimitActions
            .map {
                if (it is ProblemsLimitFeature.Action.ViewAction) {
                    StudyPlanScreenFeature.Action.ViewAction.ProblemsLimitViewAction(it)
                } else {
                    StudyPlanScreenFeature.InternalAction.ProblemsLimitAction(it)
                }
            }
            .toSet()

        return problemsLimitState to actions
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
package org.hyperskill.app.study_plan.screen.presentation

import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanViewedHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetFeature
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetReducer
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal typealias StudyPlanScreenReducerResult = Pair<StudyPlanScreenFeature.State, Set<StudyPlanScreenFeature.Action>>

internal class StudyPlanScreenReducer(
    private val toolbarReducer: GamificationToolbarReducer,
    private val problemsLimitReducer: ProblemsLimitReducer,
    private val usersQuestionnaireWidgetReducer: UsersQuestionnaireWidgetReducer,
    private val studyPlanWidgetReducer: StudyPlanWidgetReducer
) : StateReducer<StudyPlanScreenFeature.State, StudyPlanScreenFeature.Message, StudyPlanScreenFeature.Action> {
    override fun reduce(
        state: StudyPlanScreenFeature.State,
        message: StudyPlanScreenFeature.Message
    ): StudyPlanScreenReducerResult =
        when (message) {
            is StudyPlanScreenFeature.Message.Initialize ->
                initializeFeatures(state)
            is StudyPlanScreenFeature.Message.RetryContentLoading ->
                initializeFeatures(state, retryContentLoadingClicked = true)
            is StudyPlanScreenFeature.Message.PullToRefresh ->
                handlePullToRefreshMessage(state)
            is StudyPlanScreenFeature.Message.ScreenBecomesActive -> {
                val (widgetState, widgetActions) = reduceStudyPlanWidgetMessage(
                    state.studyPlanWidgetState,
                    StudyPlanWidgetFeature.InternalMessage.ReloadContentInBackground
                )
                state.copy(studyPlanWidgetState = widgetState) to widgetActions
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
            is StudyPlanScreenFeature.Message.UsersQuestionnaireWidgetMessage -> {
                val (usersQuestionnaireWidgetState, usersQuestionnaireWidgetActions) =
                    reduceUsersQuestionnaireWidgetMessage(state.usersQuestionnaireWidgetState, message.message)
                state.copy(
                    usersQuestionnaireWidgetState = usersQuestionnaireWidgetState
                ) to usersQuestionnaireWidgetActions
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

    private fun initializeFeatures(
        state: StudyPlanScreenFeature.State,
        retryContentLoadingClicked: Boolean = false
    ): StudyPlanScreenReducerResult {
        val (toolbarState, toolbarActions) =
            reduceToolbarMessage(
                state.toolbarState,
                GamificationToolbarFeature.InternalMessage.Initialize(forceUpdate = retryContentLoadingClicked)
            )
        val (problemsLimitState, problemsLimitActions) =
            reduceProblemsLimitMessage(
                state.problemsLimitState,
                ProblemsLimitFeature.InternalMessage.Initialize(forceUpdate = retryContentLoadingClicked)
            )
        val (usersQuestionnaireWidgetState, usersQuestionnaireWidgetActions) =
            reduceUsersQuestionnaireWidgetMessage(
                state.usersQuestionnaireWidgetState,
                UsersQuestionnaireWidgetFeature.InternalMessage.Initialize
            )
        val (studyPlanState, studyPlanActions) =
            reduceStudyPlanWidgetMessage(
                state.studyPlanWidgetState,
                StudyPlanWidgetFeature.InternalMessage.Initialize(forceUpdate = retryContentLoadingClicked)
            )

        val analyticActions = if (retryContentLoadingClicked) {
            setOf(
                StudyPlanScreenFeature.InternalAction.LogAnalyticEvent(
                    StudyPlanClickedRetryContentLoadingHyperskillAnalyticEvent()
                )
            )
        } else {
            emptySet()
        }

        val actions = toolbarActions +
            problemsLimitActions +
            usersQuestionnaireWidgetActions +
            studyPlanActions +
            analyticActions

        return state.copy(
            toolbarState = toolbarState,
            problemsLimitState = problemsLimitState,
            usersQuestionnaireWidgetState = usersQuestionnaireWidgetState,
            studyPlanWidgetState = studyPlanState
        ) to actions
    }

    private fun handlePullToRefreshMessage(
        state: StudyPlanScreenFeature.State
    ): StudyPlanScreenReducerResult {
        val (toolbarState, toolbarActions) = reduceToolbarMessage(
            state.toolbarState,
            GamificationToolbarFeature.InternalMessage.PullToRefresh
        )
        val (problemsLimitState, problemsLimitActions) =
            reduceProblemsLimitMessage(
                state.problemsLimitState,
                ProblemsLimitFeature.InternalMessage.PullToRefresh
            )
        val (studyPlanWidgetState, studyPlanWidgetActions) = reduceStudyPlanWidgetMessage(
            state.studyPlanWidgetState,
            StudyPlanWidgetFeature.Message.PullToRefresh
        )

        return state.copy(
            toolbarState = toolbarState,
            problemsLimitState = problemsLimitState,
            studyPlanWidgetState = studyPlanWidgetState
        ) to toolbarActions + studyPlanWidgetActions + problemsLimitActions + setOf(
            StudyPlanScreenFeature.InternalAction.LogAnalyticEvent(
                StudyPlanClickedPullToRefreshHyperskillAnalyticEvent()
            )
        )
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

    private fun reduceUsersQuestionnaireWidgetMessage(
        state: UsersQuestionnaireWidgetFeature.State,
        message: UsersQuestionnaireWidgetFeature.Message
    ): Pair<UsersQuestionnaireWidgetFeature.State, Set<StudyPlanScreenFeature.Action>> {
        val (usersQuestionnaireWidgetState, usersQuestionnaireWidgetActions) =
            usersQuestionnaireWidgetReducer.reduce(state, message)

        val actions = usersQuestionnaireWidgetActions
            .map {
                if (it is UsersQuestionnaireWidgetFeature.Action.ViewAction) {
                    StudyPlanScreenFeature.Action.ViewAction.UsersQuestionnaireWidgetViewAction(it)
                } else {
                    StudyPlanScreenFeature.InternalAction.UsersQuestionnaireWidgetAction(it)
                }
            }
            .toSet()

        return usersQuestionnaireWidgetState to actions
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
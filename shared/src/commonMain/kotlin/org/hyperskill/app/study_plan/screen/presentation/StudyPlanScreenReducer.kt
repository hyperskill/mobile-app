package org.hyperskill.app.study_plan.screen.presentation

import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanViewedHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetReducer
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal typealias StudyPlanScreenReducerResult = Pair<StudyPlanScreenFeature.State, Set<StudyPlanScreenFeature.Action>>

internal class StudyPlanScreenReducer(
    private val toolbarReducer: GamificationToolbarReducer,
    private val usersInterviewWidgetReducer: UsersInterviewWidgetReducer,
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
            is StudyPlanScreenFeature.Message.UsersInterviewWidgetMessage -> {
                val (usersInterviewWidgetState, usersInterviewWidgetActions) =
                    reduceUsersInterviewWidgetMessage(state.usersInterviewWidgetState, message.message)
                state.copy(usersInterviewWidgetState = usersInterviewWidgetState) to usersInterviewWidgetActions
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
        val (usersInterviewWidgetState, usersInterviewWidgetActions) =
            reduceUsersInterviewWidgetMessage(
                state.usersInterviewWidgetState,
                UsersInterviewWidgetFeature.InternalMessage.Initialize
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
            usersInterviewWidgetActions +
            studyPlanActions +
            analyticActions

        return state.copy(
            toolbarState = toolbarState,
            usersInterviewWidgetState = usersInterviewWidgetState,
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
        val (studyPlanWidgetState, studyPlanWidgetActions) = reduceStudyPlanWidgetMessage(
            state.studyPlanWidgetState,
            StudyPlanWidgetFeature.Message.PullToRefresh
        )

        return state.copy(
            toolbarState = toolbarState,
            studyPlanWidgetState = studyPlanWidgetState
        ) to toolbarActions + studyPlanWidgetActions + setOf(
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

    private fun reduceUsersInterviewWidgetMessage(
        state: UsersInterviewWidgetFeature.State,
        message: UsersInterviewWidgetFeature.Message
    ): Pair<UsersInterviewWidgetFeature.State, Set<StudyPlanScreenFeature.Action>> {
        val (usersInterviewWidgetState, usersInterviewWidgetActions) =
            usersInterviewWidgetReducer.reduce(state, message)

        val actions = usersInterviewWidgetActions
            .map {
                if (it is UsersInterviewWidgetFeature.Action.ViewAction) {
                    StudyPlanScreenFeature.Action.ViewAction.UsersInterviewWidgetViewAction(it)
                } else {
                    StudyPlanScreenFeature.InternalAction.UsersInterviewWidgetAction(it)
                }
            }
            .toSet()

        return usersInterviewWidgetState to actions
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
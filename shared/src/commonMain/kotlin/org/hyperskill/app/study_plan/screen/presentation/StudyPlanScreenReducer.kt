package org.hyperskill.app.study_plan.screen.presentation

import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
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
            is StudyPlanScreenFeature.Message.Initialize ->
                handleInitializeMessage(state, message)
            is StudyPlanScreenFeature.Message.GamificationToolbarMessage ->
                reduceToolbarMessage(state, message)
            is StudyPlanScreenFeature.Message.StudyPlanWidgetMessage ->
                reduceStudyPlanWidgetMessage(state, message)
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
                GamificationToolbarFeature.Message.Initialize(GamificationToolbarScreen.STUDY_PLAN)
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

    private fun reduceToolbarMessage(
        state: StudyPlanScreenFeature.State,
        message: StudyPlanScreenFeature.Message.GamificationToolbarMessage
    ): StudyPlanScreenReducerResult {
        val (toolbarState, toolbarActions) =
            reduceToolbarMessage(state.toolbarState, message.message)
        return state.copy(toolbarState = toolbarState) to toolbarActions
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

    private fun reduceStudyPlanWidgetMessage(
        state: StudyPlanScreenFeature.State,
        message: StudyPlanScreenFeature.Message.StudyPlanWidgetMessage
    ): StudyPlanScreenReducerResult {
        val (widgetState, widgetActions) =
            reduceStudyPlanWidgetMessage(state.studyPlanWidgetState, message.message)
        return state.copy(studyPlanWidgetState = widgetState) to widgetActions
    }
}
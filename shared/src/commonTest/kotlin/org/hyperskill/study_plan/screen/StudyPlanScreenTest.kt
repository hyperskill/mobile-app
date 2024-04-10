package org.hyperskill.study_plan.screen

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanViewedHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenReducer
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetReducer

class StudyPlanScreenTest {
    private val reducer = StudyPlanScreenReducer(
        GamificationToolbarReducer(GamificationToolbarScreen.STUDY_PLAN),
        ProblemsLimitReducer(ProblemsLimitScreen.STUDY_PLAN),
        UsersInterviewWidgetReducer(),
        StudyPlanWidgetReducer()
    )

    @Test
    fun `Viewed event message should trigger logging view analytic event`() {
        val (state, actions) = reducer.reduce(stubState(), StudyPlanScreenFeature.Message.ViewedEventMessage)
        assertEquals(state, stubState())

        assertEquals(actions.size, 1)
        val targetAction = actions.first() as StudyPlanScreenFeature.InternalAction.LogAnalyticEvent
        if (targetAction.analyticEvent is StudyPlanViewedHyperskillAnalyticEvent) {
            // pass
        } else {
            fail("Unexpected action: $targetAction")
        }
    }

    @Test
    fun `Pull-to-refresh message should trigger logging pull-to-refresh analytic event`() {
        val (_, actions) = reducer.reduce(stubState(), StudyPlanScreenFeature.Message.PullToRefresh)
        assertTrue {
            val targetAction = actions
                .filterIsInstance<StudyPlanScreenFeature.InternalAction.LogAnalyticEvent>()
                .first()
            targetAction.analyticEvent is StudyPlanClickedPullToRefreshHyperskillAnalyticEvent
        }
    }

    @Test
    fun `Retry content loading message should trigger logging analytic event`() {
        val (actualState, actions) = reducer.reduce(
            stubState(),
            StudyPlanScreenFeature.Message.RetryContentLoading
        )

        val expectedState = stubState(
            toolbarState = GamificationToolbarFeature.State.Loading,
            problemsLimitState = ProblemsLimitFeature.State.Loading,
            questionnaireWidgetState = UsersInterviewWidgetFeature.State.Loading,
            studyPlanWidgetState = StudyPlanWidgetFeature.State(
                sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADING
            )
        )

        assertEquals(expectedState, actualState)

        actions.firstOrNull {
            it is StudyPlanScreenFeature.InternalAction.LogAnalyticEvent &&
                it.analyticEvent is StudyPlanClickedRetryContentLoadingHyperskillAnalyticEvent
        } ?: fail("Analytic action not found")
    }

    private fun stubState(
        toolbarState: GamificationToolbarFeature.State = GamificationToolbarFeature.State.Idle,
        problemsLimitState: ProblemsLimitFeature.State = ProblemsLimitFeature.State.Idle,
        questionnaireWidgetState: UsersInterviewWidgetFeature.State = UsersInterviewWidgetFeature.State.Idle,
        studyPlanWidgetState: StudyPlanWidgetFeature.State = StudyPlanWidgetFeature.State()
    ): StudyPlanScreenFeature.State =
        StudyPlanScreenFeature.State(
            toolbarState,
            problemsLimitState,
            questionnaireWidgetState,
            studyPlanWidgetState
        )
}
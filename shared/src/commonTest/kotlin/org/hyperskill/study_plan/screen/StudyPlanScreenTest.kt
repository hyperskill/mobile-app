package org.hyperskill.study_plan.screen

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetReducer
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedChangeTrackHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanViewedHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenReducer
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.ContentStatus
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetReducer

class StudyPlanScreenTest {
    private val reducer = StudyPlanScreenReducer(
        GamificationToolbarReducer(GamificationToolbarScreen.STUDY_PLAN),
        UsersInterviewWidgetReducer(),
        NotificationDailyStudyReminderWidgetReducer(),
        StudyPlanWidgetReducer()
    )

    @Test
    fun `Viewed event message should trigger logging view analytic event`() {
        val (state, actions) = reducer.reduce(
            StudyPlanScreenFeature.State.stub(),
            StudyPlanScreenFeature.Message.ViewedEventMessage
        )
        assertEquals(state, StudyPlanScreenFeature.State.stub())

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
        val (_, actions) = reducer.reduce(
            StudyPlanScreenFeature.State.stub(),
            StudyPlanScreenFeature.Message.PullToRefresh
        )
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
            StudyPlanScreenFeature.State.stub(),
            StudyPlanScreenFeature.Message.RetryContentLoading
        )

        val expectedState = StudyPlanScreenFeature.State.stub(
            toolbarState = GamificationToolbarFeature.State.Loading,
            usersInterviewWidgetState = UsersInterviewWidgetFeature.State.Loading,
            studyPlanWidgetState = StudyPlanWidgetFeature.State(
                sectionsStatus = ContentStatus.LOADING
            )
        )

        assertEquals(expectedState, actualState)

        actions.firstOrNull {
            it is StudyPlanScreenFeature.InternalAction.LogAnalyticEvent &&
                it.analyticEvent is StudyPlanClickedRetryContentLoadingHyperskillAnalyticEvent
        } ?: fail("Analytic action not found")
    }

    @Test
    fun `ChangeTrackButtonClicked message should navigate to track selection screen`() {
        val (state, actions) = reducer.reduce(
            StudyPlanScreenFeature.State.stub(),
            StudyPlanScreenFeature.Message.ChangeTrackButtonClicked
        )

        assertEquals(state, StudyPlanScreenFeature.State.stub())
        assertTrue {
            actions.any {
                it is StudyPlanScreenFeature.Action.ViewAction.NavigateTo.TrackSelectionScreen
            }
        }
        assertTrue {
            actions.any {
                it is StudyPlanScreenFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StudyPlanClickedChangeTrackHyperskillAnalyticEvent
            }
        }
    }
}
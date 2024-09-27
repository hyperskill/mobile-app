package org.hyperskill.study_plan.screen

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.screen.view.StudyPlanScreenViewStateMapper
import org.hyperskill.app.study_plan.widget.view.mapper.StudyPlanWidgetViewStateMapper
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature

class StudyPlanScreenViewStateMapperTest {
    private val viewStateMapper = StudyPlanScreenViewStateMapper(
        studyPlanWidgetViewStateMapper = StudyPlanWidgetViewStateMapper(
            dateFormatter = SharedDateFormatter(
                ResourceProviderStub()
            )
        ),
        resourceProvider = ResourceProviderStub()
    )

    @Test
    fun `NotificationDailyStudyReminderWidgetViewState should be Hidden when UsersInterviewWidget is Visible`() {
        val state = StudyPlanScreenFeature.State.stub(
            usersInterviewWidgetState = UsersInterviewWidgetFeature.State.Visible,
            notificationDailyStudyReminderWidgetState = NotificationDailyStudyReminderWidgetFeature.State.Data(
                passedTopicsCount = 5
            )
        )

        val viewState = viewStateMapper.map(state)

        assertEquals(
            NotificationDailyStudyReminderWidgetFeature.ViewState.Hidden,
            viewState.notificationDailyStudyReminderWidgetViewState
        )
    }

    /* ktlint-disable */
    @Test
    fun `NotificationDailyStudyReminderWidgetViewState should be Visible when Data state has passedTopicsCount greater than 0 and UsersInterviewWidget is not Visible`() {
        val state = StudyPlanScreenFeature.State.stub(
            usersInterviewWidgetState = UsersInterviewWidgetFeature.State.Idle,
            notificationDailyStudyReminderWidgetState = NotificationDailyStudyReminderWidgetFeature.State.Data(
                passedTopicsCount = 5
            )
        )

        val viewState = viewStateMapper.map(state)

        assertEquals(
            NotificationDailyStudyReminderWidgetFeature.ViewState.Visible,
            viewState.notificationDailyStudyReminderWidgetViewState
        )
    }

    /* ktlint-disable */
    @Test
    fun `NotificationDailyStudyReminderWidgetViewState should be Hidden when Data state has passedTopicsCount equals 0 and UsersInterviewWidget is not Visible`() {
        val state = StudyPlanScreenFeature.State.stub(
            usersInterviewWidgetState = UsersInterviewWidgetFeature.State.Idle,
            notificationDailyStudyReminderWidgetState = NotificationDailyStudyReminderWidgetFeature.State.Data(
                passedTopicsCount = 0
            )
        )

        val viewState = viewStateMapper.map(state)

        assertEquals(
            NotificationDailyStudyReminderWidgetFeature.ViewState.Hidden,
            viewState.notificationDailyStudyReminderWidgetViewState
        )
    }

    /* ktlint-disable */
    @Test
    fun `NotificationDailyStudyReminderWidgetViewState should be Hidden when State is Idle and UsersInterviewWidget is not Visible`() {
        val state = StudyPlanScreenFeature.State.stub(
            usersInterviewWidgetState = UsersInterviewWidgetFeature.State.Idle,
            notificationDailyStudyReminderWidgetState = NotificationDailyStudyReminderWidgetFeature.State.Idle
        )

        val viewState = viewStateMapper.map(state)

        assertEquals(
            NotificationDailyStudyReminderWidgetFeature.ViewState.Hidden,
            viewState.notificationDailyStudyReminderWidgetViewState
        )
    }

    /* ktlint-disable */
    @Test
    fun `NotificationDailyStudyReminderWidgetViewState should be Hidden when State is Loading and UsersInterviewWidget is not Visible`() {
        val state = StudyPlanScreenFeature.State.stub(
            usersInterviewWidgetState = UsersInterviewWidgetFeature.State.Idle,
            notificationDailyStudyReminderWidgetState = NotificationDailyStudyReminderWidgetFeature.State.Loading
        )

        val viewState = viewStateMapper.map(state)

        assertEquals(
            NotificationDailyStudyReminderWidgetFeature.ViewState.Hidden,
            viewState.notificationDailyStudyReminderWidgetViewState
        )
    }
}
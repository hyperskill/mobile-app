package org.hyperskill.study_plan.screen

import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature

internal fun StudyPlanScreenFeature.State.Companion.stub(
    toolbarState: GamificationToolbarFeature.State = GamificationToolbarFeature.State.Idle,
    usersInterviewWidgetState: UsersInterviewWidgetFeature.State = UsersInterviewWidgetFeature.State.Idle,
    notificationDailyStudyReminderWidgetState: NotificationDailyStudyReminderWidgetFeature.State =
        NotificationDailyStudyReminderWidgetFeature.State.Idle,
    studyPlanWidgetState: StudyPlanWidgetFeature.State = StudyPlanWidgetFeature.State()
): StudyPlanScreenFeature.State =
    StudyPlanScreenFeature.State(
        toolbarState = toolbarState,
        usersInterviewWidgetState = usersInterviewWidgetState,
        notificationDailyStudyReminderWidgetState = notificationDailyStudyReminderWidgetState,
        studyPlanWidgetState = studyPlanWidgetState
    )
package org.hyperskill.app.study_plan.screen.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.notification_daily_study_reminder_widget.injection.NotificationDailyStudyReminderWidgetComponent
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.widget.injection.StudyPlanWidgetComponent
import org.hyperskill.app.users_interview_widget.injection.UsersInterviewWidgetComponent
import ru.nobird.app.presentation.redux.feature.Feature

internal class StudyPlanScreenComponentImpl(private val appGraph: AppGraph) : StudyPlanScreenComponent {

    private val toolbarComponent: GamificationToolbarComponent =
        appGraph.buildGamificationToolbarComponent(GamificationToolbarScreen.STUDY_PLAN)

    private val usersInterviewWidgetComponent: UsersInterviewWidgetComponent =
        appGraph.buildUsersInterviewWidgetComponent()

    private val notificationDailyStudyReminderWidgetComponent: NotificationDailyStudyReminderWidgetComponent =
        appGraph.buildNotificationDailyStudyReminderWidgetComponent()

    private val studyPlanWidgetComponent: StudyPlanWidgetComponent =
        appGraph.buildStudyPlanWidgetComponent()

    override val studyPlanScreenFeature: Feature<
        StudyPlanScreenFeature.ViewState, StudyPlanScreenFeature.Message, StudyPlanScreenFeature.Action>
        get() = StudyPlanScreenFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            toolbarReducer = toolbarComponent.gamificationToolbarReducer,
            toolbarActionDispatcher = toolbarComponent.gamificationToolbarActionDispatcher,
            usersInterviewWidgetReducer = usersInterviewWidgetComponent.usersInterviewWidgetReducer,
            usersInterviewWidgetActionDispatcher = usersInterviewWidgetComponent
                .usersInterviewWidgetActionDispatcher,
            notificationDailyStudyReminderWidgetReducer = notificationDailyStudyReminderWidgetComponent
                .notificationDailyStudyReminderWidgetReducer,
            notificationDailyStudyReminderWidgetActionDispatcher = notificationDailyStudyReminderWidgetComponent
                .notificationDailyStudyReminderWidgetActionDispatcher,
            studyPlanWidgetReducer = studyPlanWidgetComponent.studyPlanWidgetReducer,
            studyPlanWidgetDispatcher = studyPlanWidgetComponent.studyPlanWidgetDispatcher,
            studyPlanWidgetViewStateMapper = studyPlanWidgetComponent.studyPlanWidgetViewStateMapper,
            resourceProvider = appGraph.commonComponent.resourceProvider,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}
package org.hyperskill.app.study_plan.screen.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.wrapWithAnalyticLogger
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetActionDispatcher
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetReducer
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature.InternalAction
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenReducer
import org.hyperskill.app.study_plan.screen.view.StudyPlanScreenViewStateMapper
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetActionDispatcher
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import org.hyperskill.app.study_plan.widget.view.mapper.StudyPlanWidgetViewStateMapper
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetActionDispatcher
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetReducer
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object StudyPlanScreenFeatureBuilder {
    private const val LOG_TAG = "StudyPlanScreenFeature"

    fun build(
        analyticInteractor: AnalyticInteractor,
        toolbarReducer: GamificationToolbarReducer,
        toolbarActionDispatcher: GamificationToolbarActionDispatcher,
        usersInterviewWidgetReducer: UsersInterviewWidgetReducer,
        usersInterviewWidgetActionDispatcher: UsersInterviewWidgetActionDispatcher,
        notificationDailyStudyReminderWidgetReducer: NotificationDailyStudyReminderWidgetReducer,
        notificationDailyStudyReminderWidgetActionDispatcher: NotificationDailyStudyReminderWidgetActionDispatcher,
        studyPlanWidgetReducer: StudyPlanWidgetReducer,
        studyPlanWidgetDispatcher: StudyPlanWidgetActionDispatcher,
        studyPlanWidgetViewStateMapper: StudyPlanWidgetViewStateMapper,
        resourceProvider: ResourceProvider,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<StudyPlanScreenFeature.ViewState, StudyPlanScreenFeature.Message, StudyPlanScreenFeature.Action> {
        val studyPlanScreenReducer = StudyPlanScreenReducer(
            toolbarReducer = toolbarReducer,
            usersInterviewWidgetReducer = usersInterviewWidgetReducer,
            notificationDailyStudyReminderWidgetReducer = notificationDailyStudyReminderWidgetReducer,
            studyPlanWidgetReducer = studyPlanWidgetReducer
        ).wrapWithLogger(buildVariant, logger, LOG_TAG)

        val studyPlanScreenViewStateMapper =
            StudyPlanScreenViewStateMapper(
                studyPlanWidgetViewStateMapper = studyPlanWidgetViewStateMapper,
                resourceProvider = resourceProvider
            )

        return ReduxFeature(
            StudyPlanScreenFeature.State(
                toolbarState = GamificationToolbarFeature.State.Idle,
                usersInterviewWidgetState = UsersInterviewWidgetFeature.State.Idle,
                notificationDailyStudyReminderWidgetState = NotificationDailyStudyReminderWidgetFeature.State.Idle,
                studyPlanWidgetState = StudyPlanWidgetFeature.State()
            ),
            reducer = studyPlanScreenReducer
        )
            .transformState(studyPlanScreenViewStateMapper::map)
            .wrapWithActionDispatcher(
                toolbarActionDispatcher.transform(
                    transformAction = {
                        it.safeCast<InternalAction.GamificationToolbarAction>()?.action
                    },
                    transformMessage = StudyPlanScreenFeature.Message::GamificationToolbarMessage
                )
            )
            .wrapWithActionDispatcher(
                usersInterviewWidgetActionDispatcher.transform(
                    transformAction = {
                        it.safeCast<InternalAction.UsersInterviewWidgetAction>()?.action
                    },
                    transformMessage = StudyPlanScreenFeature.Message::UsersInterviewWidgetMessage
                )
            )
            .wrapWithActionDispatcher(
                notificationDailyStudyReminderWidgetActionDispatcher.transform(
                    transformAction = {
                        it.safeCast<InternalAction.NotificationDailyStudyReminderWidgetAction>()?.action
                    },
                    transformMessage = StudyPlanScreenFeature.Message::NotificationDailyStudyReminderWidgetMessage
                )
            )
            .wrapWithActionDispatcher(
                studyPlanWidgetDispatcher.transform(
                    transformAction = {
                        it.safeCast<InternalAction.StudyPlanWidgetAction>()?.action
                    },
                    transformMessage = StudyPlanScreenFeature.Message::StudyPlanWidgetMessage
                )
            )
            .wrapWithAnalyticLogger(analyticInteractor) {
                (it as? InternalAction.LogAnalyticEvent)?.analyticEvent
            }
    }
}
package org.hyperskill.app.study_plan.widget.presentation

import kotlinx.coroutines.delay
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Action
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.InternalAction
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Message
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StudyPlanWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val studyPlanInteractor: StudyPlanInteractor,
    private val trackInteractor: TrackInteractor,
    private val sentryInteractor: SentryInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchStudyPlan -> {
                if (action.delayBeforeFetching != null) {
                    delay(action.delayBeforeFetching)
                }

                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStudyPlanWidgetFetchCurrentStudyPlan()
                sentryInteractor.startTransaction(sentryTransaction)

                studyPlanInteractor.getCurrentStudyPlan(forceLoadFromRemote = true)
                    .onSuccess { studyPlan ->
                        sentryInteractor.finishTransaction(sentryTransaction)
                        onNewMessage(
                            StudyPlanWidgetFeature.StudyPlanFetchResult.Success(
                                studyPlan = studyPlan,
                                attemptNumber = action.attemptNumber,
                                showLoadingIndicators = action.showLoadingIndicators
                            )
                        )
                    }
                    .onFailure {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        onNewMessage(StudyPlanWidgetFeature.StudyPlanFetchResult.Failed)
                    }
            }
            is InternalAction.FetchSections -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStudyPlanWidgetFetchStudyPlanSections()
                sentryInteractor.startTransaction(sentryTransaction)

                studyPlanInteractor.getStudyPlanSections(action.sectionsIds)
                    .onSuccess { sections ->
                        sentryInteractor.finishTransaction(sentryTransaction)
                        onNewMessage(StudyPlanWidgetFeature.SectionsFetchResult.Success(sections))
                    }
                    .onFailure {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        onNewMessage(StudyPlanWidgetFeature.SectionsFetchResult.Failed)
                    }
            }
            is InternalAction.FetchActivities -> {
                sentryInteractor.startTransaction(action.sentryTransaction)

                studyPlanInteractor.getLearningActivities(action.activitiesIds, action.types, action.states)
                    .onSuccess { learningActivities ->
                        sentryInteractor.finishTransaction(action.sentryTransaction)
                        onNewMessage(
                            StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(
                                action.sectionId,
                                learningActivities
                            )
                        )
                    }
                    .onFailure {
                        sentryInteractor.finishTransaction(action.sentryTransaction, throwable = it)
                        onNewMessage(StudyPlanWidgetFeature.LearningActivitiesFetchResult.Failed(action.sectionId))
                    }
            }
            is InternalAction.FetchTrack -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStudyPlanWidgetFetchTrack()
                sentryInteractor.startTransaction(sentryTransaction)

                trackInteractor.getTrack(action.trackId, true)
                    .onSuccess {
                        sentryInteractor.finishTransaction(sentryTransaction)
                        onNewMessage(
                            StudyPlanWidgetFeature.TrackFetchResult.Success(it)
                        )
                    }
                    .onFailure {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        onNewMessage(StudyPlanWidgetFeature.TrackFetchResult.Failed)
                    }
            }
            is InternalAction.CaptureSentryErrorMessage -> {
                sentryInteractor.captureErrorMessage(action.message)
            }
            is InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
            else -> {
                // no op
            }
        }
    }
}